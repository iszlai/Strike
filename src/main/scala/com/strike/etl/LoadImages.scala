package com.strike.etl

import java.awt.FlowLayout
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import javax.swing.{JLabel, JFrame, ImageIcon}

import akka.actor.{Inbox, Props, ActorSystem}
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

/**
 * Created by Lehel on 12/12/2014.
 */
object LoadImages {
  def main(args: Array[String]) {

    def logger = LoggerFactory.getLogger(this.getClass)
    val conf = ConfigFactory.load()
    val loadURL=conf.getString("loadFromURL")

    logger.info("Startup")

    // Create the 'helloakka' actor system
    val system = ActorSystem("actorSystem")
    val fileAppendingActor = system.actorOf(Props[FileAppendingActor], "fileAppendingActor")
    val imageMapperWorker = system.actorOf(Props(new ImageMappingActor(fileAppendingActor)))

    // Create an "actor-in-a-box"
    val inbox = Inbox.create(system)
    var nrOfFile = 0;
    for (file <- new File(loadURL).listFiles) {
      logger.info("read in:" + file.getName)
      val img = ImageIO.read(file)
      inbox.send(imageMapperWorker, new ImageToProcess(img))
      nrOfFile += 1
    }


    logger.info(s"Processed $nrOfFile nr.files")
    system.shutdown()
    //display(img)

  }


  def display(img: BufferedImage): Unit = {
    val frame = new JFrame();
    frame.getContentPane().setLayout(new FlowLayout());
    frame.getContentPane().add(new JLabel(new ImageIcon(img)));
    frame.pack();
    frame.setVisible(true);
  }
}
