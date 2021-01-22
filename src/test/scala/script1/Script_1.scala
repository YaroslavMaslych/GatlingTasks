package script1

import io.gatling.core.Predef._
import framework._
import io.gatling.core.structure.ScenarioBuilder

import scala.concurrent.duration.DurationInt

class Script_1 extends Simulation {

  val scn: ScenarioBuilder = scenario("Script_1")
    .exec(requests.HomePage)
    .exec(requests.Contacts)

  setUp(scn.inject(constantUsersPerSec(1).during(100.seconds)).protocols(protocol.httpProtocol))
}