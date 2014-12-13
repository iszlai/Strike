package com.strike.etl

import java.io.FileWriter

import akka.actor.Actor
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

/**
 * Created by Lehel on 12/13/2014.
 */
class FileAppendingActor extends Actor {
  def logger = LoggerFactory.getLogger(this.getClass)
  val conf = ConfigFactory.load()
  val RESULTFILE=conf.getString("saveToURL")
  val fw = new FileWriter(RESULTFILE, true)
  logger.info("FileAppenderWorker created")

  def receive = {
    case TextToAppend(message) => {
      logger.trace(s"received:{$message}")
      fw.write(message + System.lineSeparator)
    }
  }

  override def postStop() = {
    super.postStop()
    fw.close()
    logger.info("FileAppenderWorker shutting down, resultData.csv closed")
  }

}
