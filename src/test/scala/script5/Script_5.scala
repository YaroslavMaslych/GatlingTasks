package script5

import framework._
import io.gatling.core.Predef._

class Script_5 extends Simulation {

	val scn = scenario("Script_5")
		.exec(requests.HomePage)
		.exec(requests.LoginPage)
		.exec(requests.LoginAdmin)
		.exec(requests.ViewMyBlog)
		.repeat(50) {
			exec(requests.OpenDate)
				.exec(requests.Post)
				.exec(requests.EditPostPage)
				.exec(requests.EditPost)
		}
		.exec(requests.LogOut)

	setUp(scn.inject(atOnceUsers(1))).protocols(protocol.httpProtocol)
}