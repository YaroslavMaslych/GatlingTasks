package script3

import framework.headers
import framework._
import scala.concurrent.duration.DurationInt
import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
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

  val script3: ScenarioBuilder = scenario("Script_3")
    .randomSwitch(
      50.0 -> exec(randomChain1, randomChain3, randomChain4),
      33.333 -> exec(randomChain2, randomChain4),
      16.667 -> exec(requests.Contacts)
    )

  val script3alt: ScenarioBuilder = scenario("Script_3_alt")
    .randomSwitch(
      16.6 -> exec(requests.HomePage,randomSwitch(
        50.0 -> exec(requests.RandomPage).randomSwitch(
          50.0 -> exec(requests.Post)
        ))),
        16.6 -> exec(requests.PredefinedDate,randomSwitch(
          50.0 -> exec(requests.RandomPage).randomSwitch(
            50.0 -> exec(requests.Post)
          ))),
      16.6 -> exec(requests.RandomDate,randomSwitch(
        50.0 -> exec(requests.RandomPage).randomSwitch(
          50.0 -> exec(requests.Post)
        ))),
      16.6 -> exec(requests.SearchByName,randomSwitch(
          50.0 -> exec(requests.Post)
        )),
      16.6 -> exec(requests.Archive,randomSwitch(
        50.0 -> exec(requests.Post)
      )),
      16.7 -> exec(requests.Contacts)
    )

  setUp(script3alt.inject(rampUsers(2000).during(3000.seconds)).protocols(protocol.httpProtocol))
}

