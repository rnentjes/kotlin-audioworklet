import kotlinx.browser.document
import handler.AudioWorkletHandler

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

}
