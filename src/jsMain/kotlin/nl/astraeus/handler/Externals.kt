package nl.astraeus.handler

external class AudioContext {
  var sampleRate: Int
}

external class AudioWorkletNode(
  audioContext: dynamic,
  name: String,
  options: dynamic
)

class AudioWorkletNodeParameters(
  @JsName("numberOfInputs")
  val numberOfInputs: Int,
  @JsName("outputChannelCount")
  val outputChannelCount: Array<Int>
)
