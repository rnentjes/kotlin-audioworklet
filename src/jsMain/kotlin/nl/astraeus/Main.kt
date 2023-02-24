package nl.astraeus

import kotlinx.browser.document
import nl.astraeus.handler.AudioWorkletHandler
import org.w3c.dom.HTMLInputElement

fun main() {
  AudioWorkletHandler.loadCode()

  println("Ok")

  document.getElementById("startButton")?.also {
    it.addEventListener("click", {
      if (AudioWorkletHandler.audioContext == null) {
        AudioWorkletHandler.createContext {
          println("Created context")

          AudioWorkletHandler.start()
        }
      } else {
        AudioWorkletHandler.start()
      }
    }, "")
  }

  document.getElementById("stopButton")?.also {
    it.addEventListener("click", {
        AudioWorkletHandler.stop()
    }, "")
  }

  document.getElementById("noteLength")?.also {
    it.addEventListener("change", {
      val target = it.target
      if (target is HTMLInputElement) {
        AudioWorkletHandler.setNoteLength(target.value.toInt())
      }
    }, "")
  }
  document.getElementById("harmonics")?.also {
    it.addEventListener("change", {
      val target = it.target
      if (target is HTMLInputElement) {
        AudioWorkletHandler.setHarmonics(target.value.toInt())
      }
    }, "")
  }

}
