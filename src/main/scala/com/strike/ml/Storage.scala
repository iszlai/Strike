package com.strike.ml

import collection.mutable.{ HashMap, MultiMap, Set }

/**
 * Created by Lehel on 12/21/2014.
 */
class Storage[K,V] {
   val internalStorage=new HashMap[K, Set[V]] with MultiMap[K, V]

  def keys():Iterable[K]={
    internalStorage.keys
  }

  def setValue(key:K,value:V)={
    if (internalStorage.contains(key))
      internalStorage.remove(key);
    internalStorage.addBinding(key,value)
  }

  def getValue( key:K):Set[V]= {
    return internalStorage(key)
  }
  def appendValue( key:K, value:V)={
    internalStorage.addBinding(key,value)
  }

  def getList(key:K):Set[V]={
    internalStorage(key)
  }

}
