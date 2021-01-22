package script6

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class RecordedSimulation extends Simulation {

	val httpProtocol = http
		.baseUrl("http://192.168.56.102")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*"""), WhiteList())
		.acceptHeader("*/*")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("en-US,en;q=0.5")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:84.0) Gecko/20100101 Firefox/84.0")

	val headers_0 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_1 = Map(
		"Content-Type" -> "application/x-www-form-urlencoded; charset=utf-8",
		"Origin" -> "http://192.168.56.102")



	val scn = scenario("RecordedSimulation")
		.exec(http("request_0")
			.get("/Blog/post/2020/12/13/SLUG_1142")
			.headers(headers_0))
		.pause(21)
		.exec(http("request_1")
			.post("/Blog/post/2020/12/13/SLUG_1142")
			.headers(headers_1)
			.formParam("__EVENTTARGET", "")
			.formParam("__EVENTARGUMENT", "")
			.formParam("__VIEWSTATE", "EwHAzXIdHTg07VMHZPUkPMhQarbimX5exScg1v7dMM3rqNpl+KXI2r/o/mgj/i981KYcjyTNKHp7LhgTkZkt0dwpZ+iUE/nj1ek7Myr1KRWbOWNIkX7G7vP2k1UqvSkjsf51gCIOnaZ94Iv/McaPLRijHZfaD+C0osm/oqjN9XNo4UsCa0L/xqBbcOq6FpjMJHwUXg==")
			.formParam("txtName", "")
			.formParam("", "")
			.formParam("", "")
			.formParam("txtContent", "")
			.formParam("simpleCaptchaValue", "")
			.formParam("ctl00$cphBody$ucCommentList$hiddenReplyTo", "")
			.formParam("simpleCaptchaValue", "")
			.formParam("ctl00$cphBody$ucCommentList$hfCaptcha", "9f294db9-3f4e-41fb-a800-7f4445917b90")
			.formParam("", "")
			.formParam("__VIEWSTATEGENERATOR", "0DB23B8C")
			.formParam("__CALLBACKID", "ctl00$cphBody$ucCommentList")
			.formParam("__CALLBACKPARAM", "loh-|-loh@loh.loh-|--|--|-loh-|-false-|-false-|-9f294db9-3f4e-41fb-a800-7f4445917b90-|--|--|--|--|-10")
			.formParam("__EVENTVALIDATION", "ZTLQGXX68JrDud+XIYksva66oQSTKkPlVYzZp/5rWk3weiIfTaE5ehalwmrQZy51GAf+PyiuIkisiPepkTFAdKeliY3nnPTU/06XS833YMszu4DSK6VISgssIsX+LVt4HsViTQ=="))

	setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}