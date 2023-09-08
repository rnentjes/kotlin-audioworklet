import org.khronos.webgl.Float32Array
import org.khronos.webgl.get
import org.khronos.webgl.set
import org.w3c.dom.MessageEvent


@ExperimentalJsExport
@JsExport
class MixerProcessor : AudioWorkletProcessor() {
  var started = false

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

          started = true
        }
        "stop" -> {
          println("Stop worklet!")

          started = false
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
      check(inputs.size == 2) {
        "Expected 2 inputs, got ${inputs.size}"
      }
      check(outputs.size == 1) {
        "Expected 1 output, got ${outputs.size}"
      }
      val left = outputs[0][0]
      val right = outputs[0][1]

      for (input in inputs.indices) {
        for (index in 0 ..< left.length) {
          left[index] = left[index] + inputs[input][0][index]
          right[index] = right[index] + inputs[input][1][index]
        }
      }
    }

    return true
  }

}
