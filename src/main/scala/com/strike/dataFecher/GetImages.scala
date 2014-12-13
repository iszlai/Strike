package com.strike.dataFecher

import java.io.File
import java.net.URL
import com.typesafe.config.ConfigFactory

import sys.process._
import org.slf4j.LoggerFactory
import scala.xml.XML
import scalaj.http.Http

/**
 * Created by Lehel on 12/13/2014.
 */
object GetImages {
  def logger = LoggerFactory.getLogger(this.getClass)

  def getFilename(item: String): String = {
    val name=item.substring(item.lastIndexOf("/") + 1)
    val s=if(name.contains("?")) name.substring(0,name.indexOf("?")) else name
    return s
  }

  def getImagesFromList(url:String,size:String):Seq[String]={
    val page=Http(url).asString
    val entrys= XML.loadString(page.body) \"entry"\ "image"

    val urlList = for {
    item <- entrys
    if (item \ "@height").text == size
    } yield item.text
    return urlList
  }

  def saveFiles(fileURL:String):Unit={
        val list=getImagesFromList(fileURL,"53")
        for(item <- list) {
          logger.info(s"downloading ....$item")
          val fileName=getFilename(item)
          new URL(item) #> new File("img\\"+fileName) !!
        }
  }


  def main(args: Array[String]) {
    val conf = ConfigFactory.load()
    val free=conf.getString("free")
    saveFiles(free)
    val paid=conf.getString("paid")
    saveFiles(paid)
  }
}
