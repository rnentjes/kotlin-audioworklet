import org.khronos.webgl.Float32Array
import org.w3c.dom.MessagePort

abstract external class AudioWorkletProcessor {
  /** [MDN Reference](https://developer.mozilla.org/docs/Web/API/AudioWorkletNode/parameters) */
  //val parameters: AudioParamMap;
  /** [MDN Reference](https://developer.mozilla.org/docs/Web/API/AudioWorkletNode/port) */
  @JsName("port")
  val port: MessagePort

  @JsName("process")
  open fun process (
    inputs: Array<Array<Float32Array>>,
    outputs: Array<Array<Float32Array>>,
    parameters: dynamic
  ) : Boolean

}

external fun registerProcessor(name: String, processorCtor: JsClass<*>)
external val sampleRate: Int
