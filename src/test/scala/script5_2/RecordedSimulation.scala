package script5_2

import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

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

	val headers_15 = Map(
		"Content-Type" -> "application/json;charset=utf-8",
		"Origin" -> "http://192.168.56.102",
		"x-blog-instance" -> "96d5b379-7e1d-4dac-a6ba-1e50db561b04")

	val slugRegex = regex("""Blog\/(.*SLUG_\d*)""").findRandom.saveAs("postSlug")

	val slug = regex(""""Slug":"(SLUG_\d*)"""").find.saveAs("slug")

	val date = regex(""""DateCreated":"([\d\-\s:]*)"""").find.saveAs("date")

	val relativeLink = regex(""""RelativeLink":"\/Blog(\/.*SLUG_\d*)"""").find.saveAs("relativeLink")

	val postId = regex("""<div class="post-adminlinks"><a href="http:\/\/192\.168\.56\.102\/Blog\/admin\/app\/editor\/editpost\.cshtml\?id=([0-9a-zA-Z\-]*)">""").findRandom.saveAs("postId")

	val regexViewState = regex("""id=\"__VIEWSTATE\" value=\"(.*)\"""").find.saveAs("viewState")

	val regexViewStateGen = regex("""id=\"__VIEWSTATEGENERATOR\" value=\"(.*)\"""").find.saveAs("viewStateGenerator")

	val regexEventValidation = regex("""id=\"__EVENTVALIDATION\" value=\"(.*)\"""").find.saveAs("eventValidation")

	val pauseMin = 4

	val pauseMax = 5

	val defDate = "2020/08/13"

	def generateInt(max: Int, min: Int): Int = {
		val r = new Random
		r.nextInt((max - min) + 1) + min
	}

	val HomePage: ChainBuilder = exec(http("HomePage")
		.get("/Blog")
		.headers(headers_0))
		.pause(generateInt(pauseMax, pauseMin))


	val LoginPage: ChainBuilder = exec(http("request_0")
		.get("/Blog/Account/login.aspx?ReturnURL=/Blog/admin/")
		.check(regexViewState)
		.check(regexViewStateGen)
		.check(regexEventValidation))
		.pause(generateInt(pauseMax, pauseMin))

	val Login: ChainBuilder = exec(http("request_3")
		.post("/Blog/Account/login.aspx?ReturnURL=%2fBlog%2fadmin%2f")
		.headers(headers_3)
		.formParam("__VIEWSTATE", "${viewState}")
		.formParam("__VIEWSTATEGENERATOR", "${viewStateGenerator}")
		.formParam("__EVENTVALIDATION", "${eventValidation}")
		.formParam("ctl00$MainContent$LoginUser$UserName", "admin")
		.formParam("ctl00$MainContent$LoginUser$Password", "admin")
		.formParam("ctl00$MainContent$LoginUser$LoginButton", "Log in"))
		.pause(generateInt(pauseMax, pauseMin))

	val ViewMyBlog: ChainBuilder = exec(http("request_6")
		.get("/Blog/")
		.headers(headers_2))
		.pause(generateInt(pauseMax, pauseMin))

	val printSlug = exec {s: Session =>
		println(s("slug").as[StringContext])
		s
	}

	val OpenDate: ChainBuilder = exec(http("request_7")
		.get(s"/Blog/${defDate}/default")
		.headers(headers_2)
		.check(slugRegex))
		.pause(generateInt(pauseMax, pauseMin))

	val Post: ChainBuilder = exec(http("Post")
		.get("/Blog/${postSlug}")
		.check(postId))
		.pause(generateInt(pauseMax, pauseMin))

	val EditPostPage: ChainBuilder = exec(http("request_0")
		.get("/Blog/admin/app/editor/editpost.cshtml?id=${postId}")
		.headers(headers_0)
		.resources(http("request_1")
			.get("/Blog/api/lookups")
			.headers(headers_1),
			http("request_2")
				.get("/Blog/api/tags?skip=0&take=0")
				.headers(headers_1),
			http("request_3")
				.get("/Blog/api/posts/${postId}")
				.headers(headers_1)
				.check(slug)
				.check(date)
				.check(relativeLink)))
		.exec(printSlug)
	http("request_4")
		.get("/Blog/api/customfields?filter=CustomType+%3D%3D+%22PROFILE%22")
		.headers(headers_1)
	http("request_5")
		.get("/Blog/api/customfields?filter=CustomType+%3D%3D+%22POST%22+%26%26+ObjectId+%3D%3D+%22f384e5fc-b0c8-429d-9178-458feeb19891%22")
		.headers(headers_1)

	val EditPost: ChainBuilder = exec(http("request_15")
		.put("/Blog/api/posts/update/foo")
		.headers(headers_15)
		.body(ElFileBody("script5_2/recordedsimulation/0015_request.json"))
		.resources(http("request_16")
			.put("/Blog/api/customfields")
			.headers(headers_15)
			.body(StringBody("[]")),
			http("request_17")
				.get("/Blog/api/posts/${postId}")
				.headers(headers_4)))
		.pause(generateInt(pauseMax, pauseMin))

	val Logout: ChainBuilder = exec(http("request_18")
		.get("/Blog/Account/login.aspx?logoff")
		.headers(headers_2))
		.pause(generateInt(pauseMax, pauseMin))

	val scn = scenario("Script5")
		.exec(HomePage)
		.exec(LoginPage)
		.exec(Login)
		.exec(ViewMyBlog)
		.repeat(50) {
			exec(OpenDate)
				.exec(Post)
				.exec(EditPostPage)
				.exec(EditPost)
		}
		.exec(Logout)

	setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}