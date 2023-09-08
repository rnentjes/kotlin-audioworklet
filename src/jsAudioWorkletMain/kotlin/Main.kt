

@OptIn(ExperimentalJsExport::class)
fun main() {
  registerProcessor("audio-processor", AudioProcessor::class.js)
  registerProcessor("mixer-processor", MixerProcessor::class.js)
}
