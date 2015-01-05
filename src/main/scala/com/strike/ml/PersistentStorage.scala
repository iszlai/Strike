package com.strike.ml

import com.redis.RedisClient

import scala.collection.mutable.{MultiMap, Set, HashMap}

/**
 * Created by Lehel on 1/5/2015.
 */
class PersistentStorage {
/*  val internalStorage= new RedisClient("localhost", 6379)

  def keys():Iterable[K]={
    internalStorage.keys("*")
  }

  def setValue(key:K,value:V)={
    internalStorage.rpush(key,value)
  }

  def getValue( key:K):Set[V]= {
    return internalStorage.lrange(key,0,-1)
  }
  def appendValue( key:K, value:V)={
    internalStorage.rpush(key,value)
  }

  def getList(key:K):Set[V]={
    return internalStorage.lrange(key,0,-1)
  }
*/
}
