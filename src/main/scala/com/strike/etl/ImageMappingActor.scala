package com.strike.etl

import java.awt.image.BufferedImage
import java.util.Arrays

import akka.actor.{ActorRef, Actor}
import org.slf4j.LoggerFactory

/**
 * Created by Lehel on 12/13/2014.
 */

case class ImageToProcess(img: BufferedImage)

case class TextToAppend(line: String)


class ImageMappingActor(fileAppender: ActorRef) extends Actor {

  def logger = LoggerFactory.getLogger(this.getClass)

  def extractString(img: BufferedImage): String = {
    val height = img.getHeight;
    val width = img.getWidth;
    val arr = img.getRGB(0, 0, width, height, null, 0, width);
    Arrays.toString(arr)
  }

  def receive = {
    case ImageToProcess(message) => {
      val result = extractString(message)
      fileAppender ! new TextToAppend(result)
      logger.trace("msg extracted: " + result)
    }
  }


}
