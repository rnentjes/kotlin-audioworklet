import org.khronos.webgl.Float32Array
import org.khronos.webgl.set
import org.w3c.dom.MessageEvent
import kotlin.math.PI
import kotlin.math.sin

const val PI2 = PI * 2

@ExperimentalJsExport
@JsExport
class AudioProcessor : AudioWorkletProcessor() {
  private var started = true
  private var counter: Int = 0
  private var note = Note.C2
  private var offset = 0.0

  private var note_length = 2500
  private var harmonics = 3
  private var transpose = 0

  init {
    this.port.onmessage = ::handleMessage
  }

  private fun handleMessage(message: MessageEvent) {
    console.log("WorkletProcessor: Received message", message)

    val data = message.data
    if (data is String) {
      val parts = data.split("\n")
      when (parts[0]) {
        "start" -> {
          println("Start worklet!")

          Note.sampleRate = sampleRate
          started = true
        }
        "stop" -> {
          println("Stop worklet!")

          started = false
        }
        "set_note_length" -> {
          note_length = parts[1].toInt()
        }
        "harmonics" -> {
          harmonics = parts[1].toInt()
        }
        "transpose" -> {
          transpose = parts[1].toInt()
        }
        else ->
          console.error("Don't kow how to handle message", message)
      }
    }
  }

  override fun process(
    inputs: Array<Array<Float32Array>>,
    outputs: Array<Array<Float32Array>>,
    parameters: dynamic
  ) : Boolean {
    if (started) {
      //console.log("process called", inputs, outputs, parameters, port)
      //console.log("sample rate", sampleRate)//console.log("WorkletProcessor: process", samples, left, right)

      check(outputs.size == 1) {
        "Expected 1 output got ${outputs.size}"
      }
      check(outputs[0].size == 2) {
        "Expected 2 output channels, got ${outputs.size}"
      }

      var delta = note.sampleDelta
      val samples = outputs[0][0].length
      val left = outputs[0][0]
      val right = outputs[0][1]

      //console.log("left/right", left, right)

      for (sample in 0..<samples) {
        var value = sin(offset * PI2)

        for(index in 0..<harmonics) {
          value += sin(offset * (index + 2) * PI2) * (3.0 / (index+2))
        }
        offset += delta

        // new note every NOTE_LENGTH samples
        val noteProgress = counter % note_length
        if (noteProgress == 0) {
          note = note.transpose(1)
          if (note.ordinal >= Note.C7.transpose(transpose).ordinal) {
            note = Note.C2.transpose(transpose)
          }
          delta = note.sampleDelta
        }
        // simple envelop from max to 0 every note
        value *= (1.0 - noteProgress / note_length.toDouble())

        left[sample] = value.toFloat()
        right[sample] = value.toFloat()

        counter++
      }
    }

    return true
  }

}
