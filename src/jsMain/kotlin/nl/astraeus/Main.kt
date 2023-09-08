package nl.astraeus

import kotlinx.browser.document
import nl.astraeus.handler.AudioModule
import org.w3c.dom.HTMLInputElement

fun main() {
  val audioModule = AudioModule("static/audio-worklet.js")
  val mixer = MixerProcessorNode(audioModule)
  var node1: AudioProcessorNode? = null
  var node2: AudioProcessorNode? = null


  document.getElementById("createButton")?.also {
    it.addEventListener("click", {
      mixer.create {
        node1 = AudioProcessorNode(audioModule, mixer.node)
        node2 = AudioProcessorNode(audioModule, mixer.node, 0,1)

        node1?.create {
          println("node 1 created")
        }
        node2?.create {
          println("node 2 created")

          node2?.transpose(7)
        }
      }
    }, "")
  }

  document.getElementById("startButton")?.also {
    it.addEventListener("click", {
      mixer.start()
    }, "")
  }

  document.getElementById("stopButton")?.also {
    it.addEventListener("click", {
      mixer.stop()
    }, "")
  }

  document.getElementById("noteLength")?.also {
    it.addEventListener("change", {
      val target = it.target
      if (target is HTMLInputElement) {
        node1?.length(target.value.toInt())
        node2?.length(target.value.toInt())
      }
    }, "")
  }
  document.getElementById("harmonics")?.also {
    it.addEventListener("change", {
      val target = it.target
      if (target is HTMLInputElement) {
        node1?.harmonic(target.value.toInt())
        node2?.harmonic(target.value.toInt())
      }
    }, "")
  }

}
