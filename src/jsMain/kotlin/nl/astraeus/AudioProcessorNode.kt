package nl.astraeus

import nl.astraeus.handler.AudioNode
import org.w3c.dom.MessageEvent

class AudioProcessorNode(
  audioModule: dynamic,
  destination: dynamic,
  outputIndex: Int = 0,
  inputIndex: Int = 0
) : AudioNode(
  audioModule,
  "audio-processor",
  0,
  arrayOf(2),
  destination,
  outputIndex,
  inputIndex
) {
  override fun onMessage(message: MessageEvent) {
    console.log("Got message from audio worklet", message)
  }

  fun start() {
    if (!created) {
      create {
        start()
      }
    } else {
      node?.port.postMessage("start")
    }
  }

  fun stop() {
    if (!created) {
      create {
        stop()
      }
    } else {
      node?.port.postMessage("stop")
    }
  }

  fun harmonic(i: Int) {
    node?.port.postMessage("harmonics\n$i")
  }

  fun transpose(i: Int) {
    node?.port.postMessage("transpose\n$i")
  }

  fun length(i: Int) {
    node?.port.postMessage("set_note_length\n$i")
  }
}