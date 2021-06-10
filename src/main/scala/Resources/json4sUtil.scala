package Resources
import org.json4s.{DefaultFormats, Formats, JValue}
import org.json4s.native.Serialization.{writePretty, read => jRead, write => jWrite}
import org.json4s.native.JsonMethods.{parse => jParser}
import org.json4s.native.Serialization

import java.io.{BufferedWriter, File, FileWriter}

trait json4sUtil{
  implicit val serialization = org.json4s.native.Serialization
  implicit val formats:Formats = DefaultFormats

  def write[T <: AnyRef](value: T): String = jWrite(value)
 // def read[T <: AnyRef](value: String): T = jRead(value)
  protected def parse(value: String): JValue = jParser(value)
  //val x = """List("5","6","7")"""
 // val y= parse(x).extract[List[Int]]
  def getEntity[T](path: String)(implicit m: Manifest[T]): T = {
    val entitySrc = scala.io.Source.fromFile(path)
    val entityStr = try entitySrc.mkString finally entitySrc.close()
    parse(entityStr).extract[T]
  }

  def writeJSONToFile[T](entity:T,filePath: String): Unit =
  {
    val bw = new BufferedWriter(new FileWriter(new File(filePath), false))
    bw.write(writePretty(entity))
    bw.close()
  }


}
