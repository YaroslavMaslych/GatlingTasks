package script2

import framework._

import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder

class Script_2 extends Simulation {

  val scn: ScenarioBuilder = scenario("Script_2")
    .exec(requests.HomePage)
    .exec(requests.LoginPage)
    .exec(requests.LoginEditor)
    .exec(requests.Posts)
    .exec(requests.NewPostPage)
    .exec(requests.NewPost)
    .exec(requests.LogOut)

  setUp(scn.inject(constantUsersPerSec(1).during(2000.seconds)).protocols(protocol.httpProtocol))
}