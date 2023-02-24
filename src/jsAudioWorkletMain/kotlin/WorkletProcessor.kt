@file:OptIn(ExperimentalJsExport::class)

import org.khronos.webgl.Float64Array
import org.khronos.webgl.set
import org.w3c.dom.MessageEvent
import org.w3c.dom.MessagePort
import kotlin.math.PI
import kotlin.math.sin

const val PI2 = PI * 2

@ExperimentalJsExport
@JsExport
object WorkletProcessor {
  var port: MessagePort? = null

  var counter: Int = 0
  var note = Note.C2
  var offset = 0.0

  var note_length = 2500

  @JsName("setPort")
  fun setPort(port: MessagePort) {
    WorkletProcessor.port = port
    WorkletProcessor.port?.onmessage = WorkletProcessor::onMessage
  }

  @JsName("onMessage")
  fun onMessage(message: MessageEvent) {
    console.log("WorkletProcessor: Received message", message)

    val data = message.data
    if (data is String) {
      val parts = data.split("\n")
      when (parts[0]) {
        "start" -> {
          println("Start worklet!")
        }

        "stop" -> {

        }

        "set_note_length" -> {
          note_length = parts[1].toInt()
        }

        else ->
          console.error("Don't kow how to handle message", message)
      }
    }
  }

  @JsName("process")
  fun process(samples: Int, left: Float64Array, right: Float64Array) {
    var tmpCounter = counter
    var delta = note.sampleDelta

    for (sample in 0 until samples) {
      var value = sin(offset * PI2) +
          sin(offset * 2 * PI2) * 0.6 +
          sin(offset * 3 * PI2) * 0.4 +
          sin(offset * 4 * PI2) * 0.3
      offset += delta

      // new note every NOTE_LENGTH samples
      val noteProgress = tmpCounter % note_length
      if (noteProgress == 0) {
        note = note.transpose(1)
        if (note == Note.C7) {
          note = Note.C2
        }
        delta = note.sampleDelta
      }
      // simple envelop from max to 0 every note
      value *= (1.0 - noteProgress / note_length.toDouble())

      left[sample] = value
      right[sample] = value
      tmpCounter++
    }

    counter += samples
  }

}
