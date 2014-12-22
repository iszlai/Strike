package com.strike.ml

import org.apache.commons.math3.distribution.NormalDistribution

import scala.collection.mutable.Set

/**
 * Created by Lehel on 12/19/2014.
 */
class LSH(hash_size: Int, input_dim: Int, num_hashtables: Int = 1 /*,storage_config=None, matrices_filename=None, overwrite=false */) {

  val uniformPlanes = initUniformPlanes(hash_size, input_dim, num_hashtables);
  val hashTables = initHashTables();

  def index(input_point: Array[Int]) = {
    val hashToIndex = hash(uniformPlanes(0), input_point);
    val value = arrayToString(input_point)
    hashTables.appendValue(hashToIndex, value)
  }

  def hash(planes: Array[Array[Double]], input_point: Array[Int]): String = {
    val projections = dot(planes, input_point)
    val resultArray = for (i <- projections) yield {
      if (i > 0) "1" else "0"
    }
    resultArray.foldLeft("")((first: String, second: String) => first + second)
  }

  def dot(first: Array[Array[Double]], second: Array[Int]): Array[Double] = {
    val result = new Array[Double](first.size)
    for (i <- 0.to(first.size - 1)) {
      result(i) = dotProduct(first(i), second)
    }
    return result
  }

  def dotProduct(first: Array[Double], second: Array[Int]): Double = {
    require(first.size == second.size)
    (for ((a, b) <- first zip second) yield a * b) sum
  }

  def arrayToString(array: Array[Int]): String = {
    val s = new StringBuilder
    for (i <- array) {
      s.append(i.toString)
    }
    return s.toString
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

