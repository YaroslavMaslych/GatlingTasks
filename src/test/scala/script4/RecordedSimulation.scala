package script4

import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import org.checkerframework.checker.units.qual.s

import java.util.Random

class RecordedSimulation extends Simulation {

  val httpProtocol = http
    .baseUrl("http://192.168.56.102")
    .inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*"""), WhiteList())
    .acceptHeader("application/json, text/plain, */*")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:84.0) Gecko/20100101 Firefox/84.0")

  val headers_0 = Map(
    "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
    "Cache-Control" -> "no-cache",
    "Pragma" -> "no-cache",
    "Upgrade-Insecure-Requests" -> "1")

  val headers_1 = Map(
    "Accept" -> "application/font-woff2;q=1.0,application/font-woff;q=0.9,*/*;q=0.8",
    "Accept-Encoding" -> "identity",
    "Cache-Control" -> "no-cache",
    "Pragma" -> "no-cache")

  val headers_2 = Map(
    "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
    "Upgrade-Insecure-Requests" -> "1")

  val headers_3 = Map(
    "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
    "Origin" -> "http://192.168.56.102",
    "Upgrade-Insecure-Requests" -> "1")

  val headers_4 = Map("x-blog-instance" -> "96d5b379-7e1d-4dac-a6ba-1e50db561b04")

  val headers_9 = Map(
    "Accept" -> "application/json, text/javascript, */*; q=0.01",
    "Content-Type" -> "application/json; charset=utf-8",
    "X-Requested-With" -> "XMLHttpRequest")

  val headers_13 = Map(
    "Content-Type" -> "application/json;charset=utf-8",
    "Origin" -> "http://192.168.56.102",
    "x-blog-instance" -> "96d5b379-7e1d-4dac-a6ba-1e50db561b04")

  val regexViewState = regex("""id=\"__VIEWSTATE\" value=\"(.*)\"""").find.saveAs("viewState")

  val regexViewStateGen = regex("""id=\"__VIEWSTATEGENERATOR\" value=\"(.*)\"""").find.saveAs("viewStateGenerator")

  val regexEventValidation = regex("""id=\"__EVENTVALIDATION\" value=\"(.*)\"""").find.saveAs("eventValidation")

  val regexUsers = regex("""UserName":"User_0*(\d*)""").count.saveAs("userCount")

  def generateInt(max: Int, min: Int): Int = {
    val r = new Random
    r.nextInt((max - min) + 1) + min
  }

  def increment(text: String): String = {
    val number = text.toInt
    return if ((number + 1) < 10) "0" + Integer.valueOf(number + 1).toString
    else Integer.valueOf(number + 1).toString
  }

  def user(text: String): String = {
    val number = text.toInt
    return if (number < 10) "0" + Integer.valueOf(number).toString
    else Integer.valueOf(number).toString
  }

  var bigger: Boolean = false
  var smaller = false

  val incrementUser = exec { s: Session =>
    val result = increment(s("userCount").as[String])
    s.set("userNumberInJSON", result)
  }

  val decrementUser = exec { s: Session =>
    val result = user(s("userCount").as[String])
    s.set("userNumberInJSONForDelete", result)
  }

  val printNumberOfUsers = exec { s: Session =>
    println(s("userCount").as[String])
    s
  }

  val pauseMin = 4

  val pauseMax = 5

  val HomePage: ChainBuilder = exec(http("HomePage")
    .get("/Blog")
    .headers(headers_0))
    .pause(generateInt(pauseMin, pauseMax))

  val LoginPage: ChainBuilder = exec(http("request_0")
    .get("/Blog/Account/login.aspx?ReturnURL=/Blog/admin/")
    .check(regexViewState)
    .check(regexViewStateGen)
    .check(regexEventValidation))

  val Login: ChainBuilder = exec(http("request_3")
    .post("/Blog/Account/login.aspx?ReturnURL=%2fBlog%2fadmin%2f")
    .headers(headers_3)
    .formParam("__VIEWSTATE", "${viewState}")
    .formParam("__VIEWSTATEGENERATOR", "${viewStateGenerator}")
    .formParam("__EVENTVALIDATION", "${eventValidation}")
    .formParam("ctl00$MainContent$LoginUser$UserName", "admin")
    .formParam("ctl00$MainContent$LoginUser$Password", "admin")
    .formParam("ctl00$MainContent$LoginUser$LoginButton", "Log in"))
    .pause(generateInt(pauseMin, pauseMax))

  val Users: ChainBuilder = exec(http("request_6")
    .get("/Blog/api/users/admin")
    .headers(headers_4))
    .pause(generateInt(pauseMin, pauseMax))

  val UsersPanel: ChainBuilder = exec(http("request_0")
    .get("/Blog/api/users?filter=1+%3D%3D+1&order=UserName&skip=0&take=0")
    .headers(headers_4)
    .check(regexUsers))

  val CreateUser: ChainBuilder = exec(http("request_13")
    .post("/Blog/api/users")
    .headers(headers_13)
    .body(ElFileBody("script4/recordedsimulation/0013_request.json")))
    .pause(generateInt(pauseMin, pauseMax))

  val DeleteUser: ChainBuilder = exec(http("request_111")
    .put("/Blog/api/users/processchecked/delete")
    .headers(headers_13)
    .body(ElFileBody("script4/recordedsimulation/0000_request.json")))
    .pause(generateInt(pauseMin, pauseMax))

  val LogOut: ChainBuilder = exec(http("request_19")
    .get("/Blog/Account/login.aspx?logoff")
    .headers(headers_2))
    .pause(generateInt(pauseMin, pauseMax))

  val scn: ScenarioBuilder = scenario("Script4")
    .exec(HomePage)
    .exec(LoginPage)
    .exec(Login)
    .repeat(10) {
    exec(Users)
    .exec(UsersPanel)
    .doIfOrElse(session => session("userCount").as[Int] < 10) {
      exec(printNumberOfUsers)
        .exec(incrementUser)
        .exec(CreateUser)
    } {
      doIfOrElse(session => session("userCount").as[Int] > 20) {
        exec(decrementUser)
          .exec(DeleteUser)
      } {
        uniformRandomSwitch(
          exec(incrementUser)
            .exec(CreateUser),
          exec(decrementUser)
            .exec(DeleteUser)
        )
      }

    }
    }
    .exec(LogOut)

  setUp(scn.inject(rampUsers(20).during(300.seconds))).protocols(httpProtocol)
}