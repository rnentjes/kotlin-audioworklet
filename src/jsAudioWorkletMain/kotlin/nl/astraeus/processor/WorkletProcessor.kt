@file:OptIn(ExperimentalJsExport::class)

package nl.astraeus.processor

import org.khronos.webgl.Float64Array
import org.w3c.dom.MessageEvent
import org.w3c.dom.MessagePort

@ExperimentalJsExport
@JsExport
object WorkletProcessor {
  var port: MessagePort? = null

  @JsName("setPort")
  fun setPort(port: MessagePort) {
    this.port = port
    this.port?.onmessage = ::onMessage
  }

  @JsName("onMessage")
  fun onMessage(message: MessageEvent) {
    console.log("WorkletProcessor: Received message", message)

    when (message.data) {
      "start" -> {
        println("Start worklet!")
      }
      "stop" -> {

      }
      else ->
        console.error("Don't kow how to handle message", message)
    }
  }

  @JsName("process")
  fun process(samples: Int, left: Float64Array, right: Float64Array) {
    //console.log("WorkletProcessor.process", samples)
//    val buffer = Float64SampleBuffer(samples, left, right)
//
//    audioGenerator?.also { generator ->
//      generator.fillBuffer(buffer, 0, samples, false)
//    }
  }

}
