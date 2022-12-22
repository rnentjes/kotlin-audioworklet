package nl.astraeus.handler

import kotlinx.browser.window
import org.w3c.dom.MessageEvent
import org.w3c.dom.MessagePort
import org.w3c.dom.url.URL
import org.w3c.files.Blob
import org.w3c.files.FilePropertyBag

enum class WorkletState {
  INIT,
  LOADING,
  LOADED,
  READY
}

abstract class AudioWorklet(
  val jsCodeFile: String,
  val kotlinCodeFile: String
) {
  var audioContext: dynamic = null
  var state = WorkletState.INIT
  var processingCode: Blob? = null
  var sampleRate: Int = 44100
  var audioWorkletMessagePort: MessagePort? = null

  abstract fun createNode(audioContext: dynamic): dynamic

  abstract fun onAudioWorkletMessage(message: MessageEvent)

  abstract fun onCodeLoaded()

  fun loadCode() {
    // hack
    // concat kotlin js and note-processor.js because
    // audio worklet es6 is not supported in kotlin yet
    state = WorkletState.LOADING

    window.fetch(jsCodeFile).then { daResponse ->
      if (daResponse.ok) {
        daResponse.text().then { daText ->
          window.fetch(kotlinCodeFile).then { npResponse ->
            if (npResponse.ok) {
              npResponse.text().then { npText ->
                processingCode = Blob(
                  arrayOf(daText, npText),
                  FilePropertyBag(type = "application/javascript")
                )

                state = WorkletState.LOADED

                println("Loaded $this code")

                onCodeLoaded()
              }
            }
          }
        }
      }
    }
  }

  fun createContext(callback: () -> Unit) {
    js("window.AudioContext = window.AudioContext || window.webkitAudioContext")

    audioContext = js("new window.AudioContext()")
    sampleRate = audioContext.sampleRate as Int

    check(state == WorkletState.LOADED) {
      "Can not createContext when code is not yet loaded, call loadCode first"
    }

    val module = audioContext.audioWorklet.addModule(
      URL.createObjectURL(processingCode!!)
    )

    module.then {
      val node: dynamic = createNode(audioContext)

      node.connect(audioContext.destination)

      node.port.onmessage = ::onAudioWorkletMessage

      audioWorkletMessagePort = node.port as? MessagePort

      state = WorkletState.READY

      //postBatchedRequests()

      callback()

      "dynamic"
    }
  }

  fun isResumed(): Boolean = audioContext?.state == "running"

  fun resume() {
    check(state == WorkletState.READY) {
      "Unable to resume, state is not READY [$state]"
    }

    audioContext?.resume()
  }

/*  fun postRequest(request: WorkerRequest) {
    batchedRequests.add(request)

    postBatchedRequests()
  }

  private fun postBatchedRequests() {
    val port = audioWorkletMessagePort

    if (port != null) {
      for (request in batchedRequests) {
        val message = when (serializer) {
          is StringFormat -> {
            serializer.encodeToString(WorkerRequest.serializer(), request)
          }

          is BinaryFormat -> {
            serializer.encodeToByteArray(WorkerRequest.serializer(), request)
          }

          else -> {
            error("Unknown serializer format ${serializer::class.simpleName}")
          }
        }

        port.postMessage(message)
      }

      batchedRequests.clear()
    }
  }*/
}

object AudioWorkletHandler : AudioWorklet(
  "static/worklet-processor.js",
  "static/audio-worklet.js"
) {

  override fun createNode(audioContext: dynamic): dynamic = js(
    // worklet-processor as defined in de javascript:
    // registerProcessor('worklet-processor', WorkletProcessor);
  "new AudioWorkletNode(audioContext, 'worklet-processor', { numberOfInputs: 0, outputChannelCount: [2] })"
  )

  override fun onAudioWorkletMessage(message: MessageEvent) {
    console.log("Received message from audio worklet: ", message)
  }

  override fun onCodeLoaded() {
    println("Audio worklet code is loaded.")
  }

  fun start() {
    audioWorkletMessagePort?.postMessage("start")
  }

}
