@file:OptIn(ExperimentalJsExport::class)

import org.khronos.webgl.Float64Array
import org.khronos.webgl.set
import org.w3c.dom.MessageEvent
import org.w3c.dom.MessagePort
import kotlin.math.PI
import kotlin.math.sin

@ExperimentalJsExport
@JsExport
object WorkletProcessor {
  var port: MessagePort? = null
  var offset: Double = 0.0
  var counter: Int = 0
  var note = Note.C0
  var directionUp = true

  @JsName("setPort")
  fun setPort(port: MessagePort) {
    WorkletProcessor.port = port
    WorkletProcessor.port?.onmessage = WorkletProcessor::onMessage
  }

  @JsName("onMessage")
  fun onMessage(message: MessageEvent) {
    console.log("WorkletProcessor: Received message", message)

    when (message.data) {
      "start" -> {
        println("Start worklet!")
      }
      "stop" -> {

      }
      else ->
        console.error("Don't kow how to handle message", message)
    }
  }

  @JsName("process")
  fun process(samples: Int, left: Float64Array, right: Float64Array) {
    for (sample in 0 until samples) {
      val noteProgress = counter % 5000
      if (noteProgress == 0) {
        note = note.transpose(
            if (directionUp) {
              1
            } else {
              -1
            }
        )
        if (note == Note.B8) {
          directionUp = false
        }
        if (note == Note.C0) {
          directionUp = true
        }
      }
      offset = (offset + note.sampleDelta) % 1.0

      var value = sin(offset * 2 * PI) +
                  sin(offset * 4 * PI) * 0.6 +
                  sin(offset * 6 * PI) * 0.35 +
                  sin(offset * 8 * PI) * 0.2
      // simple amplitude
      value *= if (noteProgress < 0.1) {
        (noteProgress / 500.0)
      } else {
        (1.0 - (noteProgress - 0.1) / 4500.0)
      }
      //val value = if (offset < 0.5) { 1.0 } else { -1.0 }

      left[sample] = value
      right[sample] = value

      counter++
    }
  }

}
