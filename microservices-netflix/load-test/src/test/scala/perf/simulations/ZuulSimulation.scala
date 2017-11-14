package perf.simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class ZuulSimulation extends Simulation {
  val httpConf = http
    .baseURL("https://nomadpoc.appdirectondemand.com")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")

  val scn = scenario("BasicSimulation")
    .repeat(10) {
      exec(http("Get both").get("/api/both/toto"))
        .pause(5 seconds)

        .exec(http("Get ping").get("/api/ping/somevalue"))
        .pause(10 seconds)

        .exec(http("Get gnip").get("/api/gnip/something"))
        .pause(8 seconds)

        .exec(http("Get gnip").get("/api/gnop/other"))
        .pause(8 seconds)

        .exec(http("Get gnip").get("/api/pong/other"))
        .pause(8 seconds)
    }

  setUp(
    scn.inject(
      rampUsersPerSec(1) to 10 during(60 second),
      constantUsersPerSec(10) during(5 minutes)
    )
  ).protocols(httpConf)
}