package com.strike.ml

import java.util.Arrays

import org.apache.commons.math3.distribution.NormalDistribution

import scala.collection.mutable.Set

/**
 * Created by Lehel on 12/19/2014.
 */
class LSH(hashSize: Int, inputDimension: Int, numberOfHashTables: Int = 1 ) {

  val uniformPlanes = initUniformPlanes(hashSize, inputDimension, numberOfHashTables);
  val hashTables = initHashTables();

  def index(inputPoint: Array[Int]) = {
    val hashToIndex = hash(uniformPlanes(0), inputPoint);
    val value = Arrays.toString(inputPoint)
    hashTables.appendValue(hashToIndex, value)
  }

  def hash(planes: Array[Array[Double]], inputPoint: Array[Int]): String = {
    val projections = dot(planes, inputPoint)
    val resultArray = for (i <- projections) yield {
      if (i > 0) "1" else "0"
    }
    resultArray.foldLeft("")((first: String, second: String) => first + second)
  }

  def dot(first: Array[Array[Double]], second: Array[Int]): Array[Double] = {
    val result = new Array[Double](first.size)
    for (i <- 0 to (first.size - 1)) {
      result(i) = dotProduct(first(i), second)
    }
    return result
  }

  def dotProduct(first: Array[Double], second: Array[Int]): Double = {
    require(first.size == second.size)
    (for ((a, b) <- first zip second) yield a * b) sum
  }

  def query(searchItem: Array[Int], numberOfResults: Int): Set[String] = {
    val hashOfQuery = hash(uniformPlanes(0), searchItem)
    val candidates = hashTables.getList(hashOfQuery)
    return candidates
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

  def initHashTables(): Storage[String, String] = {
    return new Storage[String, String];
  }
}

