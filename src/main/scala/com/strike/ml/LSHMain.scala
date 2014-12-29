package com.strike.ml

import java.awt.image.MemoryImageSource
import java.awt.{FlowLayout, Toolkit}
import java.io.File
import javax.swing.{ImageIcon, JFrame, JLabel, WindowConstants}

import scala.io.Source
import scala.util.Random

/**
 * Created by Lehel on 12/22/2014.
 */
object LSHMain {

  def main(args: Array[String]): Unit = {
    val lsh = new LSH(50, 2809, 1)
    //testNearFarPoints
    //timeIndexQueryDisplay(lsh)
    //resultStats(lsh, 1000)

  }

  def timeIndexQueryDisplay(lsh: LSH) {
    print("Indexing ")
    time {
      indexFile(lsh)
    }
    print("Query ")
    time {
      queryFile(lsh)
    }
    print("Display ")
    val s = randomQuery(lsh, true)
    time {
      displayResults(s)
    }
  }

  def resultStats(lsh: LSH, numberOfSamples: Int) = {
    var noResults = 0;
    var exactMatch = 0;
    var noExactMatch = 0;
    for (i <- 0 until numberOfSamples) {
      var results = randomQuery(lsh, false);
      if (results.size == 0) {
        noResults += 1
      } else {
        if (results.filter(a => a._2 == 0).size == 1) {
          exactMatch += 1
        } else {
          noExactMatch += 1
        }
      }
    }
    println(s"$numberOfSamples samples")
    println("exact matches: %"+(exactMatch.toDouble/numberOfSamples)*100)
    println(s"no exact matches: %"+ (noExactMatch.toDouble/numberOfSamples)*100)
    println(s"no results: %"+(noResults.toDouble/numberOfSamples)*100)
  }

  def randomQuery(lsh: LSH, display: Boolean = false, file: String = "testData.csv"): List[(Array[Int], Double)] = {
    val rand = new Random()
    val lines = io.Source.fromFile(file).getLines
    val queryLine = lines drop (rand.nextInt(299)) next
    val q = createIntArrayFromString(queryLine)
    if (display) {
      displayImage("Querying this", q)
    }
    return lsh.query(q, 10)
  }

  def time[R](block: => R): R = {
    val t0 = System.nanoTime()
    val result = block // call-by-name
    val t1 = System.nanoTime()
    println("Elapsed time: " + (t1 - t0) / 1000000 + "ms")
    result
  }

  def indexFile(lsh: LSH) = {
    val file = new File("resultData.csv")
    indexGivenFile(file, lsh)

  }

  def indexGivenFile(file: File, lsh: LSH) = {
    val lines = Source.fromFile(file).getLines()
    var i = 1;
    for (line <- lines) {
      val arr = createIntArrayFromString(line)
      lsh.index(arr)
      i += 1;
    }
    print(s" $i nr of files ")
  }

  def queryFile(lsh: LSH, file: String = "testData2.csv"): List[(Array[Int], Double)] = {
    val tfile = new File(file)
    val s = Source.fromFile(tfile).getLines()
    val q = createIntArrayFromString(s.next())
    time {
      lsh.query(q, 10)
    }
  }

  def createIntArrayFromString(line: String): Array[Int] = {
    line.substring(1, line.length - 1).split(",").map(_.trim.toInt)
  }

  def displayResults(results: List[(Array[Int], Double)]) {
    print("Result size:" + results.size + " ")
    if (results.size < 10) {
      for (i <- results) {
        displayImage(i._2.toString, i._1)
      }
    }
  }

  def displayImage(title: String, img: Array[Int]) {
    val i = new MemoryImageSource(53, 53, img, 0, 53);
    val im = Toolkit.getDefaultToolkit().createImage(i);
    val frame = new JFrame();
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.getContentPane().setLayout(new FlowLayout());
    frame.getContentPane().add(new JLabel(new ImageIcon(im)));
    frame.getContentPane().add(new JLabel(title));
    frame.pack();
    frame.setVisible(true);
  }

  def testNearFarPoints {
    testNearPointsRatio(5)
    testFarPointsRatio(5)
    println("-----")
    testNearPointsRatio(6)
    testFarPointsRatio(6)
    println("-----")
    testNearPointsRatio(8)
    testFarPointsRatio(8)
    println("-----")
    testNearPointsRatio(16)
    testFarPointsRatio(16)
    println("-----")
    testNearPointsRatio(32)
    testFarPointsRatio(32)
  }

  def testNearPointsRatio(hashSize: Int, repeatTime: Int = 1000) = {
    var good8 = 0;
    for (i <- 0 to repeatTime) {
      var lsh = new LSH(hashSize, 8)
      val x = Array(2, 3, 4, 5, 8, 7, 8, 9)
      val y = Array(1, 2, 3, 4, 5, 8, 7, 8)
      val planes = lsh.uniformPlanes(0)
      if (lsh.hash(planes, x) == lsh.hash(planes, y)) good8 += 1;
    }
    println("near " + hashSize + " bit " + ((good8 * 100) / repeatTime) + "%")
  }

  def testFarPointsRatio(hashSize: Int, repeatTime: Int = 1000) = {
    var good8 = 0;
    for (i <- 0 to repeatTime) {
      var lsh = new LSH(hashSize, 8)
      val x = Array(2, 3, 4, 5, 8, 7, 8, 9)
      val y = Array(10, 12, 99, 1, 5, 31, 2, 3)
      val planes = lsh.uniformPlanes(0)
      if (lsh.hash(planes, x) != lsh.hash(planes, y)) good8 += 1;
    }
    println("far " + hashSize + " bit " + ((good8 * 100) / repeatTime) + "%")
  }

  def testQuery(point: Array[Int], lsh: LSH): List[(Array[Int], Double)] = {
    return lsh.query(point, 10)
  }


}