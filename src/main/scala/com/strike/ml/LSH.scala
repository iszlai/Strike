package com.strike.ml

import org.apache.commons.math3.distribution.NormalDistribution

/**
 * Created by Lehel on 12/19/2014.
 */
class LSH(hash_size: Int, input_dim: Int, num_hashtables: Int = 1 /*,storage_config=None, matrices_filename=None, overwrite=false */) {

  val uniform_planes = init_uniform_planes(hash_size, input_dim, num_hashtables);
  val hashTables = init_hashtables();

  def index(input_point: Array[Int]) = {
    val hashToIndex = hash(uniform_planes(0), input_point);
    val value = arrayToString(input_point)
    hashTables.append_val(hashToIndex, value)
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

  def dotProduct(as: Array[Double], bs: Array[Int]): Double = {
    require(as.size == bs.size)
    (for ((a, b) <- as zip bs) yield a * b) sum
  }

  def arrayToString(array: Array[Int]): String = {
    val s = new StringBuilder
    for (i <- array) {
      s.append(i.toString)
    }
    return s.toString
  }

  def query(searchItem: Array[Int], numberOfResults: Int) = {
    val hashOfQuery = hash(uniform_planes(0), searchItem)
    val candidates = hashTables.get_list(hashOfQuery)
    for (i <- candidates) {
      println(i)
    }

  }

  def init_uniform_planes(hash_size: Int, input_dim: Int, num_hashtables: Int): IndexedSeq[Array[Array[Double]]] = {
    return for (x <- 0 to num_hashtables) yield {
      generate_uniform_planes(hash_size, input_dim)
    };
  }

  //Generate uniformly distributed hyperplanes and return it as a 2D array.
  def generate_uniform_planes(rows: Int, cols: Int): Array[Array[Double]] = {
    val nd = new NormalDistribution()
    Array.fill[Double](rows, cols)(nd.sample)
  }

  def init_hashtables(): Storage = {
    return new Storage;
  }
}

