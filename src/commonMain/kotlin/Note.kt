import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

/**
 * User: rnentjes
 * Date: 14-11-15
 * Time: 11:50
 */


enum class Note(
  val description: String
) {
  C0("C-0"),
  C0s("C#0"),
  D0("D-0"),
  D0s("D#0"),
  E0("E-0"),
  F0("F-0"),
  F0s("F#0"),
  G0("G-0"),
  G0s("G#0"),
  A0("A-0"),
  A0s("A#0"),
  B0("B-0"),
  C1("C-1"),
  C1s("C#1"),
  D1("D-1"),
  D1s("D#1"),
  E1("E-1"),
  F1("F-1"),
  F1s("F#1"),
  G1("G-1"),
  G1s("G#1"),
  A1("A-1"),
  A1s("A#1"),
  B1("B-1"),
  C2("C-2"),
  C2s("C#2"),
  D2("D-2"),
  D2s("D#2"),
  E2("E-2"),
  F2("F-2"),
  F2s("F#2"),
  G2("G-2"),
  G2s("G#2"),
  A2("A-2"),
  A2s("A#2"),
  B2("B-2"),
  C3("C-3"),
  C3s("C#3"),
  D3("D-3"),
  D3s("D#3"),
  E3("E-3"),
  F3("F-3"),
  F3s("F#3"),
  G3("G-3"),
  G3s("G#3"),
  A3("A-3"),
  A3s("A#3"),
  B3("B-3"),
  C4("C-4"),
  C4s("C#4"),
  D4("D-4"),
  D4s("D#4"),
  E4("E-4"),
  F4("F-4"),
  F4s("F#4"),
  G4("G-4"),
  G4s("G#4"),
  A4("A-4"),
  A4s("A#4"),
  B4("B-4"),
  C5("C-5"),
  C5s("C#5"),
  D5("D-5"),
  D5s("D#5"),
  E5("E-5"),
  F5("F-5"),
  F5s("F#5"),
  G5("G-5"),
  G5s("G#5"),
  A5("A-5"),
  A5s("A#5"),
  B5("B-5"),
  C6("C-6"),
  C6s("C#6"),
  D6("D-6"),
  D6s("D#6"),
  E6("E-6"),
  F6("F-6"),
  F6s("F#6"),
  G6("G-6"),
  G6s("G#6"),
  A6("A-6"),
  A6s("A#6"),
  B6("B-6"),
  C7("C-7"),
  C7s("C#7"),
  D7("D-7"),
  D7s("D#7"),
  E7("E-7"),
  F7("F-7"),
  F7s("F#7"),
  G7("G-7"),
  G7s("G#7"),
  A7("A-7"),
  A7s("A#7"),
  B7("B-7"),
  C8("C-8"),
  C8s("C#8"),
  D8("D-8"),
  D8s("D#8"),
  E8("E-8"),
  F8("F-8"),
  F8s("F#8"),
  G8("G-8"),
  G8s("G#8"),
  A8("A-8"),
  A8s("A#8"),
  B8("B-8"),
  NONE("---"),
  END("XXX"),
  UP("^^^"),
  ;

  val freq: Double by lazy {
     val ordinal = ordinal
     val relNote = ordinal - A4.ordinal

     440.0 * 2.0.pow(relNote/12.0)
   }
  val cycleLength: Double by lazy { 1.0 / freq }
  val sampleDelta: Double by lazy { (1.0 / sampleRate.toDouble()) / cycleLength }

  fun transpose(semiNotes: Int): Note = if (ordinal >= C0.ordinal && ordinal <= B8.ordinal) {
    var result = this.ordinal + semiNotes

    result = min(result, B8.ordinal)
    result = max(result, C0.ordinal)

    values().firstOrNull { it.ordinal == result } ?: this
  } else {
    this
  }

  companion object {
    var sampleRate: Int = 44100
  }

  /*
   * Amount of one cycle to advance per sample
   */
/*
  fun sampleDelta(): Double {
    // 44100
    val sampleRate = sampleRate
    val time = 1f / sampleRate.toDouble()

    return time / cycleLength
  }
*/

}
