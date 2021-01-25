package framework

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder

import java.nio.charset.StandardCharsets
import scala.util.Random


object util {
  val calculateRegex: ChainBuilder = exec { s: Session =>
    val a = new Random
    val c = s("postSlugCount").as[Int];
    val i = a.nextInt(2)
    val r = s("postSlugAll").as[List[String]]
    val k = generateInt(0, c - 1)
    if (i == 0) {
      s.set("postSlug", r(0))
    } else {
      s.set("postSlug", r(k))
    }
  }

  val printSlug = exec {s: Session =>
    println(s("slug").as[StringContext])
    s
  }

  val predefinedPageNumber = 10

  val pauseMin = 5

  val pauseMax = 10

  def generateInt(min: Int, max: Int): Int = {
    val r = new Random
    r.nextInt((max - min)+1) + min
  }

  val defDate = "2020/08/13"

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

  private var counter = 0
  private var secCounter = 0

  def getCounter: Int = {
    counter += 1
    counter
  }


  def getSecCounter: String = {
    secCounter += 1
    secCounter.toString
  }

  val rand = new Random()
  val Alphanumeric = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".getBytes

  def mkStr(chars: Array[Byte], length: Int): String = {
    val bytes = new Array[Byte](length)
    for (i <- 0 until length) bytes(i) = chars(rand.nextInt(chars.length))
    new String(bytes, StandardCharsets.US_ASCII)
  }

  def generateString(length: Int): String = mkStr(Alphanumeric, length)

  def generateMonth(min: Int, max: Int): String = {
    val r = new Random
    val i = r.nextInt((max - min) + 1) + min
    if (i / 10 == 0) "0" + i
    else Integer.toString(i)
  }
}
