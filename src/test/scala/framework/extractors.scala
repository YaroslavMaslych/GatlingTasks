package framework

import io.gatling.core.Predef._
import io.gatling.core.check.CheckBuilder
import io.gatling.core.check.regex.RegexCheckType

object extractors{

  val slugRegex: CheckBuilder[RegexCheckType, String, String] = regex("""Blog\/(.*SLUG_\d*)""").findRandom.saveAs("postSlug")

  val pageRegex: CheckBuilder[RegexCheckType, String, String] = regex("""<a href="\/Blog\/([^<]*\?page=\d*)\">\d*<\/a>""").findRandom.saveAs("randomPage")

  val regexViewState: CheckBuilder[RegexCheckType, String, String] = regex("""id=\"__VIEWSTATE\" value=\"(.*)\"""").find.saveAs("viewState")

  val regexViewStateGen: CheckBuilder[RegexCheckType, String, String] = regex("""id=\"__VIEWSTATEGENERATOR\" value=\"(.*)\"""").find.saveAs("viewStateGenerator")

  val regexEventValidation: CheckBuilder[RegexCheckType, String, String] = regex("""id=\"__EVENTVALIDATION\" value=\"(.*)\"""").find.saveAs("eventValidation")

  val regexUsers: CheckBuilder[RegexCheckType, String, Int] = regex("""UserName":"User_0*(\d*)""").count.saveAs("userCount")

  val slug: CheckBuilder[RegexCheckType, String, String] = regex(""""Slug":"(SLUG_\d*)"""").find.saveAs("slug")

  val date: CheckBuilder[RegexCheckType, String, String] = regex(""""DateCreated":"([\d\-\s:]*)"""").find.saveAs("date")

  val relativeLink: CheckBuilder[RegexCheckType, String, String] = regex(""""RelativeLink":"\/Blog(\/.*SLUG_\d*)"""").find.saveAs("relativeLink")

  val postId: CheckBuilder[RegexCheckType, String, String] = regex("""cshtml\?id=([\w\d-]*)""").find.optional.saveAs("postId")

  val countSlugRegex: CheckBuilder[RegexCheckType, String, Int] = regex("""Blog\/(.*SLUG_\d*)""").count.saveAs("postSlugCount")

  val firstSlugRegex: CheckBuilder[RegexCheckType, String, Seq[String]] = regex("""Blog\/(.*SLUG_\d*)""").findAll.saveAs("postSlugAll")

  val randomPageRegex: CheckBuilder[RegexCheckType, String, String] = regex("""<a href="\/Blog\/([^<]*\?page=\d*)\">\d*<\/a>""").findRandom.saveAs("randomPage")

}
