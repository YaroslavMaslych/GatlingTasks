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
    .check(extractors.countSlugRegex)
    .check(extractors.firstSlugRegex)
    .check(extractors.randomPageRegex))
    .pause(util.generateInt(util.pauseMin, util.pauseMax))

  val RandomDate: ChainBuilder = exec(http("RandomDate")
    .get(s"/Blog/2020/${util.generateMonth(1, 12)}/default")
    .headers(headers.headers_2)
    .check(extractors.slugRegex)
    .check(extractors.countSlugRegex)
    .check(extractors.firstSlugRegex)
    .check(extractors.randomPageRegex))
    .pause(util.generateInt(util.pauseMin, util.pauseMax))

  val PredefinedDate: ChainBuilder = exec(http("PredefinedDate")
    .get(s"/Blog/2020/${util.predefinedPageNumber}/default")
    .headers(headers.headers_2)
    .check(extractors.slugRegex)
    .check(extractors.countSlugRegex)
    .check(extractors.firstSlugRegex)
    .check(extractors.randomPageRegex))
    .pause(util.generateInt(util.pauseMin, util.pauseMax))

  val SearchByName: ChainBuilder = exec(http("SearchByName")
    .get("/Blog/search?q=Title")
    .headers(headers.headers_2)
    .check(extractors.slugRegex)
    .check(extractors.countSlugRegex)
    .check(extractors.firstSlugRegex))
    .pause(util.generateInt(util.pauseMin, util.pauseMax))

  val Archive: ChainBuilder = exec(http("Archive")
    .get("/Blog/archive")
    .headers(headers.headers_2)
    .check(extractors.slugRegex)
    .check(extractors.countSlugRegex)
    .check(extractors.firstSlugRegex))
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
    .get("/Blog/${postSlug}")
    .check(extractors.postId)
    .check(extractors.regexViewState)
    .check(extractors.regexViewStateGen)
    .check(extractors.regexEventValidation))
    .pause(util.generateInt(util.pauseMin, util.pauseMax))

  val Comment: ChainBuilder = exec(http("Comment")
    .post("/Blog/${postSlug}")
    .headers(headers.headers_2)
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

  val LoginEditor: ChainBuilder = exec(http("Login")
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


  /*
  ---------------------------------USERS GENERATING SCRIPT-----------------------------------
   */

  val LoginAdmin: ChainBuilder = exec(http("Login")
    .post("/Blog/Account/login.aspx?ReturnURL=%2fBlog%2fadmin%2f")
    .headers(headers.headers_6)
    .formParam("__VIEWSTATE", "${viewState}")
    .formParam("__VIEWSTATEGENERATOR", "${viewStateGenerator}")
    .formParam("__EVENTVALIDATION", "${eventValidation}")
    .formParam("ctl00$MainContent$LoginUser$UserName", "admin")
    .formParam("ctl00$MainContent$LoginUser$Password", "admin")
    .formParam("ctl00$MainContent$LoginUser$LoginButton", "Log in")
    .resources(http("DashBoard")
      .get("/Blog/api/dashboard")
      .headers(headers.headers_7),
      http("NewsFeed")
        .get("/Blog/api/newsfeed?skip=0&take=5")
        .headers(headers.headers_7)))
    .pause(util.generateInt(util.pauseMin, util.pauseMax))

  val Users: ChainBuilder = exec(http("Users")
    .get("/Blog/api/users/admin")
    .headers(headers.headers_7))
    .pause(util.generateInt(util.pauseMin, util.pauseMax))

  val UsersPanel: ChainBuilder = exec(http("UsersPanel")
    .get("/Blog/api/users?filter=1+%3D%3D+1&order=UserName&skip=0&take=0")
    .headers(headers.headers_7)
    .check(extractors.regexUsers))
    .pause(util.generateInt(util.pauseMin, util.pauseMax))

  val CreateUser: ChainBuilder = exec(http("CreateUser")
    .post("/Blog/api/users")
    .headers(headers.headers_8)
    .body(ElFileBody("script4/recordedsimulation/0013_request.json")))
    .pause(util.generateInt(util.pauseMin, util.pauseMax))

  val DeleteUser: ChainBuilder = exec(http("DeleteUser")
    .put("/Blog/api/users/processchecked/delete")
    .headers(headers.headers_8)
    .body(ElFileBody("script4/recordedsimulation/0000_request.json")))
    .pause(util.generateInt(util.pauseMin, util.pauseMax))

    /*
    ---------------------------------EDITS GENERATING SCRIPT-----------------------------------
     */

  val ViewMyBlog: ChainBuilder = exec(http("ViewMyBlog")
    .get("/Blog/")
    .headers(headers.headers_5))
    .pause(util.generateInt(util.pauseMin, util.pauseMax))

  val OpenDate: ChainBuilder = exec(http("OpenDate")
    .get(s"/Blog/${util.defDate}/default")
    .headers(headers.headers_5)
    .check(extractors.slugRegex))
    .pause(util.generateInt(util.pauseMin, util.pauseMax))

  val EditPostPage: ChainBuilder = exec(http("EditPostPage")
    .get("/Blog/admin/app/editor/editpost.cshtml?id=${postId}")
    .headers(headers.headers_3)
    .resources(http("request_1")
      .get("/Blog/api/lookups")
      .headers(headers.headers_4),
      http("request_2")
        .get("/Blog/api/tags?skip=0&take=0")
        .headers(headers.headers_4),
      http("request_3")
        .get("/Blog/api/posts/${postId}")
        .headers(headers.headers_4)
        .check(extractors.slug)
        .check(extractors.date)
        .check(extractors.relativeLink)))
    .exec(util.printSlug)
  http("CustomFields")
    .get("/Blog/api/customfields?filter=CustomType+%3D%3D+%22PROFILE%22")
    .headers(headers.headers_4)
  http("CustomFields2")
    .get("/Blog/api/customfields?filter=CustomType+%3D%3D+%22POST%22+%26%26+ObjectId+%3D%3D+%22f384e5fc-b0c8-429d-9178-458feeb19891%22")
    .headers(headers.headers_4)

  val EditPost: ChainBuilder = exec(http("EditPost")
    .put("/Blog/api/posts/update/foo")
    .headers(headers.headers_8)
    .body(ElFileBody("script5_2/recordedsimulation/0015_request.json"))
    .resources(http("request_16")
      .put("/Blog/api/CustomFields")
      .headers(headers.headers_8)
      .body(StringBody("[]")),
      http("EditedPost")
        .get("/Blog/api/posts/${postId}")
        .headers(headers.headers_7)))
    .pause(util.generateInt(util.pauseMin, util.pauseMax))

}
