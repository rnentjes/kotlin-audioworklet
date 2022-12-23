@file:OptIn(ExperimentalJsExport::class)

import org.khronos.webgl.Float64Array
import org.khronos.webgl.get
import org.khronos.webgl.set
import org.w3c.dom.MessageEvent
import org.w3c.dom.MessagePort
import kotlin.math.PI
import kotlin.math.sin

@ExperimentalJsExport
@JsExport
object WorkletProcessor {
  var port: MessagePort? = null

  var offsets = Array(5) { 0.0 }
  var counter: Int = 0
  var baseNote = Note.C2
  val CARRAY = arrayOf(
    Note.C1,
    Note.C2,
    Note.C3,
    Note.C4,
    Note.C5,
    Note.C6,
    Note.C7,
    Note.C8,
  )

  @JsName("setPort")
  fun setPort(port: MessagePort) {
    WorkletProcessor.port = port
    WorkletProcessor.port?.onmessage = WorkletProcessor::onMessage
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
    var first = true
    for (index in offsets.indices) {
      var offset = offsets[index]
      var tmpCounter = counter
      var offsetNote = baseNote.transpose(index * 12)
      var delta = offsetNote.sampleDelta

      for (sample in 0 until samples) {
        var value = sin(offset * 2 * PI)
        offset += delta

        val noteProgress = tmpCounter % 5000
        if (noteProgress == 0) {
          offsetNote = offsetNote.transpose(1)
          if (offsetNote in CARRAY) {
            offsetNote = offsetNote.transpose(-12)
          }
          delta = offsetNote.sampleDelta
        }
        value *= (1.0 - noteProgress / 5000.0)

        if (first) {
          left[sample] = value
          right[sample] = value
        } else {
          left[sample] = left[sample] + value
          right[sample] = right[sample] + value
        }
        tmpCounter++
      }

      offsets[index] = offset
      first = false
    }

    var tmpCounter = counter
    for (sample in 0 until samples) {
      if (tmpCounter % 5000 == 0) {
        baseNote = baseNote.transpose(1)
        if (baseNote in CARRAY) {
          baseNote = baseNote.transpose(-12)
        }
      }
      tmpCounter++
    }
    counter += samples

    val nrOffsets = offsets.size
    for (sample in 0 until samples) {
      left[sample] = left[sample] / nrOffsets
      right[sample] = right[sample] / nrOffsets
    }

    /*    for (sample in 0 until samples) {
          val noteProgress = counter % 5000
          if (noteProgress == 0) {
            baseNote = baseNote.transpose(1)
            if (baseNote == Note.C3s) {
              baseNote = Note.C2
            }
          }
          var value = 0.0
          var volumeAdjust = (Note.C3.ordinal - baseNote.ordinal) / 12.0
          for (index in 0 until offsets.size) {
            offsets[index] = (offsets[index] + (baseNote.transpose(index * 12).sampleDelta)) % 1.0

            var partValue = sin(offsets[index] * 2 * PI)
            if (index == 0) {
              partValue *= volumeAdjust
            } else if (index == offsets.size - 1) {
              partValue *= (1.0 - volumeAdjust)
            }

            value += partValue

          }

          value /= offsets.size
          // simple amplitude
          value *= if (noteProgress < 0.1) {
            (noteProgress / 500.0)
          } else {
            (1.0 - (noteProgress - 0.1) / 4500.0)
          }

          left[sample] = value
          right[sample] = value

          counter++
        }*/
  }

}
