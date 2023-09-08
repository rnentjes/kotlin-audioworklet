import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.Netty
import io.ktor.server.routing.*
import kotlinx.html.*

fun HTML.index() {
  head {
    title("Hello from Ktor!")
    link("static/worklet.css", "stylesheet" ,"text/css")
  }
  body {
    div {
      +"We need a button to start because we can only start audio from a user event:"
    }
    div("button_div") {
      span("button") {
        id = "createButton"

        +"Create"
      }

      span("button") {
        id = "startButton"

        +"Start"
      }

      span("button") {
        id = "stopButton"

        +"Stop"
      }
    }
    div {
      + "An example of how to interact with the audioworklet:"
    }
    div {
      label {
        htmlFor = "noteLength"
        +"Note length (in samples):"
      }
      input {
        id = "noteLength"
        type = InputType.number
        value = "2500"
        min = "1"
        max = "100000"
        step = "100"
      }
    }
    div {
      label {
        htmlFor = "harmonics"
        +"Number of harmonics:"
      }
      input {
        id = "harmonics"
        type = InputType.number
        value = "3"
        min = "1"
        max = "10"
      }
    }
    script(src = "/static/kotlin-audioworklet.js") {}
  }
}

fun main() {
  embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
    routing {
      get("/") {
        call.respondHtml(HttpStatusCode.OK, HTML::index)
      }
      static("/static") {
        resources()
      }
    }
  }.start(wait = true)
}
