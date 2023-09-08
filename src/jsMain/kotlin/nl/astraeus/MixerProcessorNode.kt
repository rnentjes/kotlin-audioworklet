package nl.astraeus

import nl.astraeus.handler.AudioNode
import org.w3c.dom.MessageEvent

class MixerProcessorNode(
  audioModule: dynamic
) : AudioNode(
  audioModule,
  "mixer-processor",
  2,
  arrayOf(2)
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
}
