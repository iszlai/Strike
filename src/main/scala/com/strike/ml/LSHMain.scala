package com.strike.ml

import java.awt.{FlowLayout, Image, Toolkit}
import java.awt.image.MemoryImageSource
import java.io.File
import javax.swing.{ImageIcon, JLabel, WindowConstants, JFrame}

import scala.collection.mutable
import scala.collection.mutable.Set
import scala.io.Source

/**
 * Created by Lehel on 12/22/2014.
 */
object LSHMain {

  def main(args: Array[String]): Unit = {

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

    val lsh=new LSH(200,2809,1)
    print("Indexing ")
    time{indexFile(lsh)}
    print("Query ")
    time{queryFile(lsh)}
    print("Display ")
    val s=queryFile(lsh)
    time{displayResults(s)}

  }
  def time[R](block: => R): R = {
    val t0 = System.nanoTime()
    val result = block    // call-by-name
    val t1 = System.nanoTime()
    println("Elapsed time: " + (t1 - t0)/1000000 + "ms")
    result
  }
  def testNearPointsRatio(hashSize:Int,repeatTime:Int=1000)={
    var good8 = 0;
    for (i <- 0 to repeatTime) {
      var lsh = new LSH(hashSize , 8)
      val x = Array(2, 3, 4, 5, 8, 7, 8, 9)
      val y = Array(1, 2, 3, 4, 5, 8, 7, 8)
      val planes = lsh.uniformPlanes(0)
      if (lsh.hash(planes, x) == lsh.hash(planes, y)) good8 += 1;
    }
    println ("near "+hashSize+" bit "+((good8*100)/repeatTime)+"%")
  }

  def testFarPointsRatio(hashSize:Int,repeatTime:Int=1000)={
    var good8 = 0;
    for (i <- 0 to repeatTime) {
      var lsh = new LSH(hashSize , 8)
      val x = Array(2, 3, 4, 5, 8, 7, 8, 9)
      val y = Array(10,12,99,1,5,31,2,3)
      val planes = lsh.uniformPlanes(0)
      if (lsh.hash(planes, x) != lsh.hash(planes, y)) good8 += 1;
    }
    println ("far "+hashSize+" bit "+((good8*100)/repeatTime)+"%")
  }

  def createIntArrayFromString(line:String):Array[Int]={
    line.substring(1,line.length-1).split(",").map(_.trim.toInt)
  }

  def indexGivenFile(file:File,lsh:LSH) = {
    val lines=Source.fromFile(file).getLines()
    var i=1;
    for (line <- lines) {
      val arr = createIntArrayFromString(line)
      lsh.index(arr)
      i+=1;
    }
    print(s" $i nr of files ")
  }

  def indexFile(lsh:LSH)={
    val file=new File("resultData.csv")
    indexGivenFile(file,lsh)

  }

  def queryFile(lsh: LSH):Set[String]={
    val tfile=new File("testData.csv")
    val s=Source.fromFile(tfile).getLines()
    val q=createIntArrayFromString(s.next())
    return lsh.query(q,10)
  }


  def displayResults(results: Set[String]) {
    print("Result size:"+results.size+" ")
    if (results.size < 10) {
      for (i <- results) {
        displayImage(createIntArrayFromString(i))
      }
    }
  }

  def displayImage(img:Array[Int]){
    val i=new MemoryImageSource(53,53,img,0,53);
    val im = Toolkit.getDefaultToolkit().createImage(i);
    val frame = new JFrame();
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.getContentPane().setLayout(new FlowLayout());
    frame.getContentPane().add(new JLabel(new ImageIcon(im)));
    frame.pack();
    frame.setVisible(true);
  }


}