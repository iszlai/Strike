
import _root_.com.strike.ml.LSH

val l=new LSH(6,8)
l.index(Array(2,3,4,5,6,7,8,9))
l.index(Array(1,2,3,4,5,6,7,8))
l.index(Array(10,12,99,1,5,31,2,3))
l.query(Array(1,2,3,4,5,6,7,7),10)

def dotProduct(as: Array[Double], bs: Array[Int]):Double = {
  require(as.size == bs.size)
  (for ((a, b) <- as zip bs) yield a * b) sum
}

def dot(first:Array[Array[Double]],second:Array[Int]):Array[Double]={
  val result=new Array[Double](second.size)
  for(i<-0.to(second.size)){
    result(i)=dotProduct(first(i),second)
  }
  return result
}

dotProduct(Array(1,2,3),Array(1,1,1))
