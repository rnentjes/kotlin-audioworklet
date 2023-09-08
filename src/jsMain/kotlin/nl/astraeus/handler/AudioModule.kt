package nl.astraeus.handler

import org.w3c.dom.MessageEvent
import org.w3c.dom.MessagePort

private val audioModules = mutableMapOf<String, AudioModule>()

fun loadAudioModule(jsFile: String) {
  val module = audioModules.getOrPut(jsFile) {
    AudioModule(jsFile)
  }
}

enum class ModuleStatus {
  INIT,
  LOADING,
  READY
}

class AudioModule(
  val jsFile: String
) {
  var status = ModuleStatus.INIT
  var audioContext: dynamic = null
  var module: dynamic = null

  // call from user gesture
  fun doAction(action: () -> Unit) {
    if (module == null && status == ModuleStatus.INIT) {
      status = ModuleStatus.LOADING
      if (audioContext == null) {
        audioContext = AudioContext()
      }
      module = audioContext.audioWorklet.addModule(
        jsFile
      )
      module.then {
        status = ModuleStatus.READY
        action()
      }
    } else if (status == ModuleStatus.READY) {
      action()
    } else {
      console.log("Module not yet loaded")
    }
  }
}

abstract class AudioNode(
  val module: AudioModule,
  val processorName: String,
  val numberOfInputs: Int = 0,
  val outputChannelCount: Array<Int> = arrayOf(2),
  val destination: dynamic = null,
  val outputIndex: Int = 0,
  val inputIndex: Int = 0
) {
  var created = false
  var node: dynamic = null
  var port: MessagePort? = null

  abstract fun onMessage(message: MessageEvent)

  // call from user gesture
  fun create(done: (node: dynamic) -> Unit) {
    module.doAction {
      node = AudioWorkletNode(
        module.audioContext,
        processorName,
        AudioWorkletNodeParameters(
          numberOfInputs,
          outputChannelCount
        )
      )

      if (destination == null) {
        node.connect(module.audioContext.destination)
      } else {
        node.connect(destination, outputIndex, inputIndex)
       }

      node.port.onmessage = ::onMessage

      port = node.port as? MessagePort

      created = true

      done(node)
    }
  }

}