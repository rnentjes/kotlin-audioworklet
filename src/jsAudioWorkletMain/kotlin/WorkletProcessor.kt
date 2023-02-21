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

  var counter: Int = 0
  var note = Note.C2
  var offset = 0.0

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
    var tmpCounter = counter
    var delta = note.sampleDelta

    for (sample in 0 until samples) {
      var value = sin(offset * 2 * PI)
      offset += delta

      val noteProgress = tmpCounter % 5000
      if (noteProgress == 0) {
        note = note.transpose(1)
        if (note == Note.C7) {
          note = Note.C2
        }
        delta = note.sampleDelta
      }
      value *= (1.0 - noteProgress / 5000.0)

      left[sample] = value
      right[sample] = value
      tmpCounter++
    }

    counter += samples
  }

}
