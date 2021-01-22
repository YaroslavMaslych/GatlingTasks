package script6

import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.core.check.Check.check
import io.gatling.core.check.CheckBuilder
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import io.gatling.jdbc.Predef._

import java.util.Random

class Script6 extends Simulation {

  val httpProtocol: HttpProtocolBuilder = http
    .baseUrl("http://192.168.56.102")
    .inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*""", """.*css\?family=Roboto:400,500,700"""), WhiteList())
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:84.0) Gecko/20100101 Firefox/84.0")

  val headers_0 = Map(
    "Cache-Control" -> "no-cache",
    "Pragma" -> "no-cache",
    "Upgrade-Insecure-Requests" -> "1")

  val headers_1 = Map("Upgrade-Insecure-Requests" -> "1")

  val countSlugRegex = regex("""Blog\/(.*SLUG_\d*)""").count.saveAs("postSlugCount")

  val firstSlugRegex = regex("""Blog\/(.*SLUG_\d*)""").findAll.saveAs("postSlugAll")

  val randomPageRegex = regex("""<a href="\/Blog\/([^<]*\?page=\d*)\">\d*<\/a>""").findRandom.saveAs("randomPage")

  val regexViewState = regex("""id=\"__VIEWSTATE\" value=\"(.*)\"""").find.saveAs("viewState")

  val regexViewStateGen = regex("""id=\"__VIEWSTATEGENERATOR\" value=\"(.*)\"""").find.saveAs("viewStateGenerator")

  val regexEventValidation = regex("""id=\"__EVENTVALIDATION\" value=\"(.*)\"""").find.saveAs("eventValidation")


  val calculateRegex = exec { s: Session =>
    val a = new Random
    val c = s("postSlugCount").as[Int];
    val i = a.nextInt(2)
    val r = s("postSlugAll").as[List[String]]
    val k = generateInt(c - 1, 0)
    if (i == 0) {
      s.set("postSlug", r(0))
    } else {
      s.set("postSlug", r(k))
    }
  }

  val predefinedPageNumber = 10

  val pauseMin = 10

  val pauseMax = 5

  def generateMonth(min: Int, max: Int): String = {
    val r = new Random
    val i = r.nextInt((max - min) + 1) + min
    if (i / 10 == 0) "0" + i
    else Integer.toString(i)
  }

  def generateInt(max: Int, min: Int): Int = {
    val r = new Random
    r.nextInt((max - min)+1) + min
  }

  val HomePage: ChainBuilder = exec(http("HomePage")
    .get("/Blog")
    .headers(headers_0)
    .check(countSlugRegex)
    .check(firstSlugRegex)
    .check(randomPageRegex))
    .pause(generateInt(pauseMin, pauseMax))

  val RandomDate: ChainBuilder = exec(http("RandomDate")
    .get(s"/Blog/2020/${generateMonth(1, 12)}/default")
    .headers(headers_1)
    .check(countSlugRegex)
    .check(firstSlugRegex)
    .check(randomPageRegex))
    .pause(generateInt(pauseMin, pauseMax))

  val PredefinedDate: ChainBuilder = exec(http("PredefinedDate")
    .get(s"/Blog/2020/${predefinedPageNumber}/default")
    .headers(headers_1)
    .check(countSlugRegex)
    .check(firstSlugRegex)
    .check(randomPageRegex))
    .pause(generateInt(pauseMin, pauseMax))

  val SearchByName: ChainBuilder = exec(http("SearchByName")
    .get("/Blog/search?q=Title")
    .headers(headers_1)
    .check(countSlugRegex)
    .check(firstSlugRegex))
    .pause(generateInt(pauseMin, pauseMax))

  val Archive: ChainBuilder = exec(http("Archive")
    .get("/Blog/archive")
    .headers(headers_1)
    .check(countSlugRegex)
    .check(firstSlugRegex))
    .pause(generateInt(pauseMin, pauseMax))

  val Contacts: ChainBuilder = exec(http("Contacts")
    .get("/Blog/contact")
    .headers(headers_1))
    .pause(generateInt(pauseMin, pauseMax))

  val RandomPage: ChainBuilder = exec(http("RandomPage")
    .get("/Blog/${randomPage}")
    .headers(headers_1))
    .pause(generateInt(pauseMin, pauseMax))

  val Post: ChainBuilder = exec(http("Post")
    .get("/Blog/${postSlug}")
    .check(regexViewState)
    .check(regexViewStateGen)
    .check(regexEventValidation))
    .pause(generateInt(pauseMin, pauseMax))

  val Comment: ChainBuilder = exec(http("request_1")
    .post("/Blog/${postSlug}")
    .headers(headers_1)
    .formParam("__EVENTTARGET", "")
    .formParam("__EVENTARGUMENT", "")
    .formParam("__VIEWSTATE", "$viewState")
    .formParam("txtName", "")
    .formParam("", "")
    .formParam("", "")
    .formParam("txtContent", "")
    .formParam("simpleCaptchaValue", "")
    .formParam("ctl00$cphBody$ucCommentList$hiddenReplyTo", "")
    .formParam("simpleCaptchaValue", "")
    .formParam("ctl00$cphBody$ucCommentList$hfCaptcha", "9f294db9-3f4e-41fb-a800-7f4445917b90")
    .formParam("", "")
    .formParam("__VIEWSTATEGENERATOR", "$viewStateGenerator")
    .formParam("__CALLBACKID", "ctl00$cphBody$ucCommentList")
    .formParam("__CALLBACKPARAM", "anon-|-anon@anon.anon-|--|--|-anon-|-false-|-false-|-9f294db9-3f4e-41fb-a800-7f4445917b90-|--|--|--|--|-10")
    .formParam("__EVENTVALIDATION", "$eventValidation"))

  object RCL {
    val randomChain1: ChainBuilder = randomSwitch(
      33.334 -> HomePage,
      33.333 -> RandomDate,
      33.333 -> PredefinedDate
    ).exec(calculateRegex)

    val randomChain2: ChainBuilder = randomSwitch(
      50.0 -> SearchByName,
      50.0 -> Archive
    ).exec(calculateRegex)

    val randomChain3: ChainBuilder = randomSwitch(
      50.0 -> RandomPage
    ).exec(calculateRegex)

    val randomChain4: ChainBuilder = randomSwitch(
      50.0 -> randomChain5
    )
  }

  val randomChain5 = Post.randomSwitch(
    50.0 -> Comment
  )

  val scn6: ScenarioBuilder = scenario("Script6")
    .exec(HomePage)
    .repeat(50) {
      randomSwitch(
        50.0 -> exec(RCL.randomChain1, RCL.randomChain3, RCL.randomChain4),
        33.333 -> exec(RCL.randomChain2, RCL.randomChain4),
        16.667 -> exec(Contacts)
      )
    }

  setUp(scn6.inject(rampUsers(100).during(1000.seconds)).protocols(httpProtocol))
}
