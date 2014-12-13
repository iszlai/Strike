import java.net.URL

import com.typesafe.config.ConfigFactory
import scala.xml.XML
import scalaj.http.Http
import java.io.File

def getImagesFromList(url:String,size:String):Seq[String]={
  val page=Http(url).asString
  val entrys= XML.loadString(page.body) \"entry"\ "image"

  val urlList = for {
    item <- entrys
    if (item \ "@height").text == size
  } yield item.text
  return urlList
}

val page=getImagesFromList("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/toppaidapplications/limit=10/xml","53")
val u="http://a1569.phobos.apple.com/us/r30/Purple2/v4/4c/d0/b6/4cd0b65a-77ed-57a9-4617-07ce170df990/mzl.ypvnwazh.53x53-50.png?mu"
val name=u.substring(u.lastIndexOf("/")+1)
val s=if(name.contains("?")) name.substring(0,name.indexOf("?"))
val conf = ConfigFactory.load()
conf.getString("free")