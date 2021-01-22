package script3

import framework.headers
import framework._

import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}

class Script_3 extends Simulation {

    val randomChain1: ChainBuilder = randomSwitch(
      33.334 -> requests.HomePage,
      33.333 -> requests.RandomDate,
      33.333 -> requests.PredefinedDate
    )

    val randomChain2: ChainBuilder = randomSwitch(
      50.0 -> requests.SearchByName,
      50.0 -> requests.Archive
    )

    val randomChain3: ChainBuilder = randomSwitch(
      50.0 -> requests.RandomPage
    )

    val randomChain4: ChainBuilder = randomSwitch(
      50.0 -> requests.Post
    )

  val scn4: ScenarioBuilder = scenario("Script_3")
    .randomSwitch(
      50.0 -> exec(randomChain1, randomChain3, randomChain4),
      33.333 -> exec(randomChain2, randomChain4),
      16.667 -> exec(requests.Contacts)
    )

  setUp(scn4.inject(rampUsers(1000).during(1000.seconds)).protocols(protocol.httpProtocol))
}

