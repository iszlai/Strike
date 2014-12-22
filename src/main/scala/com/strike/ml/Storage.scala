package com.strike.ml

import collection.mutable.{ HashMap, MultiMap, Set }

/**
 * Created by Lehel on 12/21/2014.
 */
class Storage {
   val internalStorage=new HashMap[String, Set[String]] with MultiMap[String, String]

  def keys():Iterable[String]={
    internalStorage.keys
  }

  def set_val(key:String,value:String)={
    if (internalStorage.contains(key))
      internalStorage.remove(key);
    internalStorage.addBinding(key,value)
  }

  def get_val( key:String):Set[String]= {
    return internalStorage(key)
  }
  def append_val( key:String, value:String)={
    internalStorage.addBinding(key,value)
  }

  def get_list(key:String):Set[String]={
    internalStorage(key)
  }

}
