import com.strike.ml.{Storage, LSH}
import org.scalatest.{Matchers, BeforeAndAfter, FunSuite}


/**
 * Created by Lehel on 12/22/2014.
 */
class TestLSH extends FunSuite with BeforeAndAfter with Matchers{
  var lsh:LSH=_

  before{
    lsh=new LSH(8,8)
  }

  test("An empty Set should have size 0") {

    Set.empty.size should equal(0)
  }

  test("lsh can be create with bitsize #{8} and dimension #{8}"){
    lsh shouldBe an [LSH]
  }

  test("Uniform planse returns correct size planes"){
    val up=lsh.initUniformPlanes(8,8,1)
    up should have size 2
    up(0) should have size 8
    up(0)(0) should have size 8
    up(1) should have size 8
    up(1)(0) should have size 8
  }

  test("initHashTables creates storage"){
    lsh.initHashTables shouldBe a [Storage[String,Array[Int]]]
  }

  test("hash returns the same for near points:(2,3,4,5,8,7,8,9)-(1,2,3,4,5,8,7,8)"){
    val x=Array(2,3,4,5,8,7,8,9)
    val y=Array(1,2,3,4,5,8,7,8)
    val planes=lsh.uniformPlanes(0)
    info (scala.runtime.ScalaRunTime.stringOf (planes))
    LSH.hash(planes,x) should equal(LSH.hash(planes,y))
  }

  test("hash returns the different for far points:(2,3,4,5,8,7,8,9)-(10,12,99,1,5,31,2,3)"){
    val x=Array(2,3,4,5,8,7,8,9)
    val y=Array(10,12,99,1,5,31,2,3)
    val planes=lsh.uniformPlanes(0)
    info(scala.runtime.ScalaRunTime.stringOf(planes))
    LSH.hash(planes,x) should not equal(LSH.hash(planes,y))
  }

  test("dot product for same size (1.0,2.0,3.0) - (1,1,1)"){
    LSH.dotProduct(Array(1.0,2.0,3.0),Array(1,1,1)) shouldBe 6
  }

  test("dot product for different size (1.0,2.0,3.0,4.0) - (1,1,1)"){
    an [IllegalArgumentException] shouldBe thrownBy (LSH.dotProduct(Array(1.0,2.0,3.0,4.0),Array(1,1,1)))
  }

  test("dot should return array of ones"){
    val e1=Array.fill(3)(1.0)
    val e2=Array.fill(3)(1.0)
    val e3=Array.fill(3)(1.0)
    val second=Array.fill(3)(1)
    val first=Array(e1,e2,e3)
    LSH.dot(first,second) should equal(Array.fill(3)(3))
  }

  test("can index (2,3,4,5,8,7,8,9)"){
    val x=Array(2,3,4,5,8,7,8,9)
    lsh.index( x)
    lsh.hashTables.keys().size shouldBe 1
  }

  test("can query sample data "){
    lsh.index(Array(2,3,4,5,8,7,8,9))
    lsh.index(Array(1,2,3,4,5,8,7,8))
    lsh.index(Array(10,12,99,1,5,31,2,3))
    lsh.query(Array(1,2,3,4,5,8,7,7),10) should have size 2
  }



}
