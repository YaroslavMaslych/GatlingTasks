package script4

import framework._
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder

import scala.concurrent.duration._

class Script_4 extends Simulation {

  val scn: ScenarioBuilder = scenario("Script_4")
    .exec(requests.HomePage)
    .exec(requests.LoginPage)
    .exec(requests.LoginAdmin)
    .repeat(10) {
    exec(requests.Users)
    .exec(requests.UsersPanel)
    .doIfOrElse(session => session("userCount").as[Int] < 10) {
      exec(util.printNumberOfUsers)
        .exec(util.incrementUser)
        .exec(requests.CreateUser)
    } {
      doIfOrElse(session => session("userCount").as[Int] > 20) {
        exec(util.decrementUser)
          .exec(requests.DeleteUser)
      } {
        uniformRandomSwitch(
          exec(util.incrementUser)
            .exec(requests.CreateUser),
          exec(util.decrementUser)
            .exec(requests.DeleteUser)
        )
      }

    }
    }
    .exec(requests.LogOut)

  setUp(scn.inject(rampUsers(20).during(300.seconds))).protocols(protocol.httpProtocol)
}