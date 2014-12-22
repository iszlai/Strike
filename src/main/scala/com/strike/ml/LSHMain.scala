package com.strike.ml

import java.io.File

import scala.io.Source

/**
 * Created by Lehel on 12/22/2014.
 */
object LSHMain {

  def main (args: Array[String]) {
    for(line <- Source.fromFile(new File("resultData.csv")).getLines()){
      val s=line.split(",")
      println(s.length)
    }

    val l=new LSH(6,8)

    l.index(Array(2,3,4,5,6,7,8,9))
    l.index(Array(1,2,3,4,5,6,7,8))
    l.index(Array(10,12,99,1,5,31,2,3))
    for(i<-l.query(Array(1,2,3,4,5,6,7,7),10)) println(i)
  }
}
