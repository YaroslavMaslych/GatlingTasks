package script6

import framework._
import io.gatling.core.Predef._
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}

import scala.concurrent.duration._

class Script_6 extends Simulation {

  val randomChain1: ChainBuilder = randomSwitch(
    27.27 -> exec(requests.HomePage),
    18.18 -> exec(requests.RandomDate),
    54.55 -> exec(requests.PredefinedDate)
  ).exec(util.calculateRegex)

  val randomChain2: ChainBuilder = randomSwitch(
    75.0 -> exec(requests.SearchByName),
    25.0 -> exec(requests.Archive)
  ).exec(util.calculateRegex)

  val randomChain3: ChainBuilder = randomSwitch(
    50.0 -> exec(requests.RandomPage)
  ).exec(util.calculateRegex)

  val randomChain4: ChainBuilder = randomSwitch(
    50.0 -> exec(requests.Post).randomSwitch(
      50.0 -> requests.Comment
    )
  )

  val scn6: ScenarioBuilder = scenario("Script6")
    .exec(requests.HomePage)
    .repeat(50) {
      randomSwitch(
        55.0 -> exec(randomChain1, randomChain3, randomChain4),
        40.0 -> exec(randomChain2, randomChain4),
        10.0 -> exec(requests.Contacts)
      )
    }

  setUp(scn6.inject(rampUsers(5).during(1000.seconds)).protocols(protocol.httpProtocol))
}
