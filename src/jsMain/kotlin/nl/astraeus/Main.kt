package nl.astraeus

import kotlinx.browser.document
import nl.astraeus.handler.AudioWorkletHandler
import org.w3c.dom.HTMLInputElement

fun main() {
  AudioWorkletHandler.loadCode()

  println("Ok")

  document.getElementById("clicker")?.also {
    it.addEventListener("click", {
      AudioWorkletHandler.createContext {
        println("Created context")

        AudioWorkletHandler.start()
      }
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

}
