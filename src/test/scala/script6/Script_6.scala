package script6

import framework._
import io.gatling.core.Predef._
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}

import scala.concurrent.duration._

class Script_6 extends Simulation {

  val randomChain1: ChainBuilder = randomSwitch(
    33.334 -> exec(requests.HomePage),
    33.333 -> exec(requests.RandomDate),
    33.333 -> exec(requests.PredefinedDate)
  ).exec(util.calculateRegex)

  val randomChain2: ChainBuilder = randomSwitch(
    50.0 -> exec(requests.SearchByName),
    50.0 -> exec(requests.Archive)
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
        50.0 -> exec(randomChain1, randomChain3, randomChain4),
        33.333 -> exec(randomChain2, randomChain4),
        16.667 -> exec(requests.Contacts)
      )
    }

  setUp(scn6.inject(rampUsers(5).during(1000.seconds)).protocols(protocol.httpProtocol))
}
