package framework

import io.gatling.core.Predef._
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}
import io.gatling.http.Predef._

object requests {
  /*
  ---------------------------------MAIN SCRIPT-----------------------------------
   */

  val HomePage: ChainBuilder = exec(http("HomePage")
    .get("/Blog")
    .headers(headers.headers_3)
    .check(extractors.slugRegex)
    .check(extractors.pageRegex))
    .pause(util.generateInt(util.pauseMin, util.pauseMax))

  val RandomDate: ChainBuilder = exec(http("RandomDate")
    .get(s"/Blog/2020/${util.generateMonth(1, 12)}/default")
    .headers(headers.headers_2)
    .check(extractors.slugRegex)
    .check(extractors.pageRegex))
    .pause(util.generateInt(util.pauseMin, util.pauseMax))

  val PredefinedDate: ChainBuilder = exec(http("PredefinedDate")
    .get(s"/Blog/2020/${util.predefinedPageNumber}/default")
    .headers(headers.headers_2)
    .check(extractors.slugRegex)
    .check(extractors.pageRegex))
    .pause(util.generateInt(util.pauseMin, util.pauseMax))

  val SearchByName: ChainBuilder = exec(http("SearchByName")
    .get("/Blog/search?q=Title")
    .headers(headers.headers_2)
    .check(extractors.slugRegex))
    .pause(util.generateInt(util.pauseMin, util.pauseMax))

  val Archive: ChainBuilder = exec(http("Archive")
    .get("/Blog/archive")
    .headers(headers.headers_2)
    .check(extractors.slugRegex))
    .pause(util.generateInt(util.pauseMin, util.pauseMax))

  val Contacts: ChainBuilder = exec(http("Contacts")
    .get("/Blog/contact")
    .headers(headers.headers_2))
    .pause(util.generateInt(util.pauseMin, util.pauseMax))

  val RandomPage: ChainBuilder = exec(http("RandomPage")
    .get("/Blog/${randomPage}")
    .headers(headers.headers_2)
    .check(extractors.slugRegex))
    .pause(util.generateInt(util.pauseMin, util.pauseMax))

  val Post: ChainBuilder = exec(http("Post")
    .get("/Blog/${postSlug}"))
    .pause(util.generateInt(util.pauseMin, util.pauseMax))


  /*
  ---------------------------------POST GENERATING SCRIPT-----------------------------------
   */

  val LoginPage: ChainBuilder = exec(http("LoginPage")
    .get("/Blog/Account/login.aspx?ReturnURL=/Blog/admin/")
    .check(extractors.regexViewState)
    .check(extractors.regexViewStateGen)
    .check(extractors.regexEventValidation)
    .headers(headers.headers_5))
    .pause(util.generateInt(util.pauseMin, util.pauseMax))

  val Login: ChainBuilder = exec(http("Login")
    .post("/Blog/Account/login.aspx?ReturnURL=%2fBlog%2fadmin%2f")
    .headers(headers.headers_6)
    .formParam("__VIEWSTATE", "${viewState}")
    .formParam("__VIEWSTATEGENERATOR", "${viewStateGenerator}")
    .formParam("__EVENTVALIDATION", "${eventValidation}")
    .formParam("ctl00$MainContent$LoginUser$UserName", "Editor")
    .formParam("ctl00$MainContent$LoginUser$Password", "editor")
    .formParam("ctl00$MainContent$LoginUser$LoginButton", "Log in")
    .resources(http("DashBoard")
      .get("/Blog/api/dashboard")
      .headers(headers.headers_7),
      http("NewsFeed")
        .get("/Blog/api/newsfeed?skip=0&take=5")
        .headers(headers.headers_7)))
    .pause(util.generateInt(util.pauseMin, util.pauseMax))

  val Posts: ChainBuilder = exec(http("Posts")
    .get("/Blog/api/posts?skip=0&take=0")
    .headers(headers.headers_7))
    .pause(util.generateInt(util.pauseMin, util.pauseMax))

  val NewPostPage: ChainBuilder = exec(http("NewPostPage")
    .get("/Blog/admin/app/editor/editpost.cshtml")
    .headers(headers.headers_5)
    .resources(http("request_7")
      .get("/Blog/api/lookups")
      .headers(headers.headers_7),
      http("request_8")
        .get("/Blog/api/tags?skip=0&take=0")
        .headers(headers.headers_7)))
    .pause(util.generateInt(util.pauseMin, util.pauseMax))

  val NewPost: ChainBuilder = exec(http("NewPost")
    .post("/Blog/api/posts")
    .headers(headers.headers_8)
    .body(StringBody(session =>
      s"""
   								|{
	 								|  "Id":"",
	 								|  "Title":"TITLE",
	 								|  "Author":"Editor",
	 								|  "Content":"<p>${util.generateString(100)}</p>",
	 								|  "DateCreated":"2020-${util.generateMonth(1, 12)}-13 11:53",
	 								|  "Slug":"SLUG_${util.getSecCounter}",
	 								|  "Categories":[
	 								|
	 								|  ],
	 								|  "Tags":[
	 								|
	 								|  ],
	 								|  "Comments":"",
	 								|  "HasCommentsEnabled":true,
	 								|  "IsPublished":true
	 								|}
	 							""".stripMargin))
    .resources(http("request_10")
      .put("/Blog/api/customfields")
      .headers(headers.headers_8)
      .body(RawFileBody("script2/recordedsimulation/0010_request.json"))))
    .pause(util.generateInt(util.pauseMin, util.pauseMax))

  val LogOut: ChainBuilder = exec(http("LogOut")
    .get("/Blog/Account/login.aspx?logoff")
    .headers(headers.headers_5))
    .pause(util.generateInt(util.pauseMin, util.pauseMax))

}
