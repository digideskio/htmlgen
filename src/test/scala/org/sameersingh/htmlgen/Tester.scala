package org.sameersingh.htmlgen

/**
 * @author sameer
 * @since 4/24/14.
 */
class Tester(c: Converter) {

  import ConverterUtils._
  import c.Implicits._

  def writeHTML(title: String, filename: String) {
    val body = wrap(title, "h2") + wrap(
      "\n<hr>\n" + wrap("Basic", "h3") + wrap(basic, "div") +
        "\n<hr>\n" + wrap("Iterables", "h3") + wrap(iterables, "div") +
        "\n<hr>\n" + wrap("Products", "h3") + wrap(products, "div") +
        "\n<hr>\n" + wrap("Hybrid", "h3") + wrap(hybrid, "div") +
        "\n<hr>\n" + wrap("Misc", "h3") + wrap(misc, "div") +
        "\n<hr>\n" + wrap("Plots", "h3") + wrap(plots, "div")
      , "div")
    val html = htmlWrap(body)
    writeFile(filename, html)
  }


  def print(a: Any, dataType: String): String = {
    wrap(dataType, "h4") + "\n" +
      ("<code style=\"background-color:#F0FFFF;\">%s</code>" format (a.toString)) + "\n" +
      ("<div>\n%s\n</div>" format a.source) + "\n"
  }

  def basic: String = {
    val sb = new StringBuffer()
    sb append print(10, "Int")
    sb append print(10.0, "Double")
    sb append print("qwerty", "String")
    sb.toString
  }

  def iterables: String = {
    val sb = new StringBuffer()
    sb append print(List(10, 20, 30, 40), "List")
    sb append print(Seq(10, 20, 30, 40), "Seq")
    sb append print(Set(10, 20, 30, 40), "Set")
    sb append print(Map("a" -> 10, "b" -> 20, "c" -> 30, "d" -> 40), "Map")
    sb.toString
  }

  case class Company(name: String, address: String)

  case class Person(first: String, last: String, email: String, age: Int, employedBy: Company)

  def products: String = {
    val sb = new StringBuffer()
    sb append print("a" -> 100, "Pair")
    sb append print(Tuple3("a", 10, 100.0), "Tuple3")
    val c = Company("Battle School", "Seattle, WA")
    val p = Person("Andrew", "Wiggin", "ender@intfleet.com", 65, c)
    sb append print(c, "Case")
    sb append print(p, "Case (Nested)")
    sb.toString
  }

  def hybrid: String = {
    val sb = new StringBuffer()
    sb append print(List(List(10, 20, 30, 40), List(1.0, 2.0, 3.0, 4.0), List("a", "b", "c", "d")), "List of lists")
    sb append print(Set(List(10, 20, 30, 40), List(1.0, 2.0, 3.0, 4.0), List("a", "b", "c", "d")), "Set of lists")
    sb append print(List(Set(10, 20, 30, 40), Set(1.0, 2.0, 3.0, 4.0), Set("a", "b", "c", "d")), "List of sets")
    sb append print(Map(List(100, 200) -> Set("a", "b"), List(10, 20) -> Set("abraca", "dabra")), "Map of lists to sets")
    val c1 = Company("Battle School", "Seattle, WA")
    val c2 = Company("Command School", "Mountain View, CA")
    val c3 = Company("Dept of Xenobiology", "Lusitania")
    val p1 = Person("Andrew", "Wiggin", "ender@intfleet.com", 10, c2)
    val p2 = Person("Hyrum", "Graff", "graff@intfleet.com", 65, c2)
    val p3 = Person("Ivanova", "von Hesse", "novinha@xenbio.com", 25, c3)
    sb append print(List(c1, c2, c3), "List of Cases")
    sb append print(Set(p1, p2, p3), "Set of Nested Cases")
    sb append print(Map(c2 -> List(p1, p2), c3 -> List(p3)), "Map of Cases to list of Nested Cases")
    sb.toString
  }

  def misc: String = {
    val sb = new StringBuffer()
    val company = Company("Battle School", "Seattle, WA")
    val person = Person("Andrew", "Wiggin", "ender@intfleet.com", 65, company)
    sb append (wrap("Override Case", "h4") + "\n" +
      ("<code style=\"background-color:#F0FFFF;\">%s</code>" format (company.toString)) + "\n" +
      ("<div>\n%s\n</div>" format c.convert(company, overriden = {
        case c: Company => c.toString
      }).source) + "\n"
      )
    sb append (wrap("Override Case (Nested)", "h4") + "\n" +
      ("<code style=\"background-color:#F0FFFF;\">%s</code>" format (person.toString)) + "\n" +
      ("<div>\n%s\n</div>" format c.convert(person, overriden = {
        case c: Company => c.toString
      }).source) + "\n"
      )
    sb.toString
  }

  def plots: String = {
    import org.sameersingh.scalaplot.Implicits._
    val x = (1 until 10).map(_.toDouble)
    val y1 = x.map(j => math.pow(j, 1))
    val y2 = x.map(j => math.pow(j, 2))
    val y3 = x.map(j => math.pow(j, 3))
    val p1 = plot(x -> Y(y1, "Linear") :: x -> Y(y2, "Square") :: x -> Y(y3, "Cube") :: List(), title = "Powers!", y = Axis(log = true), showLegend = true)
    val p2 = plot(x -> Y(y1, "x") :: x -> Y(y2, "x^2") :: x -> Y(y3, "x^3") :: List(), title = "Powers!", y = Axis(log = true), showLegend = true)
    val sb = new StringBuffer()
    sb append (wrap("Plots", "h4") + "\n" +
      ("<code style=\"background-color:#F0FFFF;\">%s</code>" format (p1)) + "\n" +
      ("<div>\n%s\n</div>" format c.convert(p1).source) + "\n"
      )
    sb append (wrap("Plots", "h4") + "\n" +
      ("<code style=\"background-color:#F0FFFF;\">%s</code>" format (p2)) + "\n" +
      ("<div>\n%s\n</div>" format c.convert(p2).source) + "\n"
      )
    sb.toString
  }
}
