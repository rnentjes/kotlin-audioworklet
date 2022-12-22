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

  print("get noteLength")
  document.getElementById("noteLength")?.also {
    println("Set noteLength event")
    it.addEventListener("change", {
      println("Set noteLength 1")
      val target = it.target
      if (target is HTMLInputElement) {
        println("Set noteLength 2")
        AudioWorkletHandler.setNoteLength(target.value.toInt())
      }
    }, "")
  }

}
