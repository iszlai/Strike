package com.strike.ml

import java.io.File

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
}