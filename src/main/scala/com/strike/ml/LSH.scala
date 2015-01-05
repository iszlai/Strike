package com.strike.ml

import org.apache.commons.math3.distribution.NormalDistribution

/**
 * Created by Lehel on 12/19/2014.
 */
class LSH(hashSize: Int, inputDimension: Int, numberOfHashTables: Int = 1) {

  val uniformPlanes = initUniformPlanes(hashSize, inputDimension, numberOfHashTables);
  val hashTables = initHashTables();

  def index(inputPoint: Array[Int]) = {
    val hashToIndex = LSH.hash(uniformPlanes(0), inputPoint);
    hashTables.appendValue(hashToIndex, inputPoint)
  }

  def query(searchItem: Array[Int], numberOfResults: Int): List[(Array[Int], Double)] = {
    val hashOfQuery = LSH.hash(uniformPlanes(0), searchItem)
    val list = hashTables.getList(hashOfQuery).toList
    val distanceList = list.zip(LSH.getDistanceList(list, searchItem))
    distanceList.sortBy(_._2).take(numberOfResults)

  }

  def initUniformPlanes(hashSize: Int, inputDimension: Int, numberOfHashTables: Int): IndexedSeq[Array[Array[Double]]] = {
    return for (x <- 0 to numberOfHashTables) yield {
      generateUniformPlanes(hashSize, inputDimension)
    };
  }

  //Generate uniformly distributed hyperplanes and return it as a 2D array.
  def generateUniformPlanes(rows: Int, cols: Int): Array[Array[Double]] = {
    val nd = new NormalDistribution()
    Array.fill[Double](rows, cols)(nd.sample)
  }

  def initHashTables(): Storage[String, Array[Int]] = new Storage[String, Array[Int]];

}

object LSH {

  def getDistanceList(list: List[Array[Int]], point: Array[Int]): List[Double] = {
    for (item <- list) yield LSH.euclideanDistance(item, point)
  }

  def euclideanDistance(point1: Array[Int], point2: Array[Int]): Double = {
    Math.sqrt(point1.zip(point2).foldLeft(0.0) { case (sum, (v1, v2)) => sum + Math.pow(v1 - v2, 2)})
  }

  def hash(planes: Array[Array[Double]], inputPoint: Array[Int]): String = {
    val projections = LSH.dot(planes, inputPoint)
    val resultArray = for (i <- projections) yield {
      if (i > 0) "1" else "0"
    }
    resultArray.mkString
  }

  def dot(first: Array[Array[Double]], second: Array[Int]): Array[Double] = {
    val result = new Array[Double](first.size)
    for (i <- 0 to first.size - 1) {
      result(i) = dotProduct(first(i), second)
    }
    return result
  }

  def dotProduct(first: Array[Double], second: Array[Int]): Double = {
    require(first.size == second.size)
    var sum: Double = 0;
    for (i <- 0 until first.size) {
      sum += first(i) * second(i)
    }
    sum
  }
}

