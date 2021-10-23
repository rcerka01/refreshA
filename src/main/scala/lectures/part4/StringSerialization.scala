package lectures.part4

import java.util.Date

case class NetworkUser(name: String, age: Int, email: String)
case class Post(content: String, CreatedSt: Date)
case class Feed(user: NetworkUser, posts: List[Post])

sealed trait JSONvalue {
  def stringify: String
}

final case class JSONobject(values: Map[String, JSONvalue]) extends JSONvalue {
  override def stringify: String = values.map {
    case (key, value) => "\"" + key + "\":" + value.stringify
  }.mkString("{", "," , "}")

}

final case class JSONstring(value: String) extends JSONvalue {
  override def stringify: String = "\"" + value + "\""
}

final case class JSONnumber(value: Int) extends JSONvalue {
  override def stringify: String = value.toString
}

final case class JSONarray(values: List[JSONvalue]) extends JSONvalue {
  override def stringify: String = values.map( _.stringify ).mkString("[", ",", "]")
}

//////////////////////////////////////

trait JSONconverter[T] {
  def convert(v: T): JSONvalue
}

//implicit class JSONenricher[T](v: T) {
//  def toJson(implicit converter: JSONconverter[T]) =
//    converter.convert(v)
//}

implicit object StringConverter extends JSONconverter[String] {
  override def convert(v: String): JSONvalue = JSONstring(v)
}

implicit object IntConverter extends JSONconverter[Int] {
  override def convert(v: Int): JSONvalue = JSONnumber(v)
}

implicit object UserConverter extends JSONconverter[NetworkUser] {
  override def convert(user: NetworkUser): JSONvalue = JSONobject(Map(
    "user" -> JSONstring(user.name),
    "age" -> JSONnumber(user.age),
    "email" -> JSONstring(user.email)
  ))
}

implicit object PostConverter extends JSONconverter[Post] {
  override def convert(post: Post): JSONvalue = JSONobject(Map(
    "content" -> JSONstring(post.content),
    "created" -> JSONstring(post.CreatedSt.toString)
  ))
}

implicit object FeedConverter extends JSONconverter[Feed] {
  override def convert(feed: Feed): JSONvalue = JSONobject(Map(
    "user" -> UserConverter.convert(feed.user),
    "posts" -> JSONarray(feed.posts.map(PostConverter.convert))
  ))
}

// if declared before, converter dependency can be removed
//implicit object FeedConverter extends JSONconverter[Feed] {
//  override def convert(feed: Feed): JSONvalue = JSONobject(Map(
//    "user" -> feed.user.toJson,
//    "posts" -> JSONarray(feed.posts.map(_.toJson))
//  ))
//}

/////////////////////////////////////////////

implicit class JSONenricher[T](v: T) {
  def toJson(implicit converter: JSONconverter[T]) =
    converter.convert(v)
}

object StringSerialization extends App {

  // pre test
  val data = JSONobject(Map(
    "user" -> JSONstring("Ray"),
    "posts" -> JSONarray(List(
      JSONstring("some string"),
      JSONnumber(32)
    ))
  ))
  println(data.stringify)

  // test
  val now = new Date(System.currentTimeMillis())
  val ray = NetworkUser("Ray", 42, "email")
  val feed = Feed(ray, List(
    Post("Hallo", now),
    Post("How do you do", now)
  ))
  println(feed.toJson.stringify)
}
