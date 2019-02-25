package cn.lxj.spark.demo

import cn.lxj.spark.model.Girl

class MissRight[T] {
  def choose(first: T, second: T)(implicit ord: T => Ordered[T]): T = {
    if (first > second) first else second
  }

  def select(first: T, second: T)(implicit ord: Ordering[T]): T = {
    import Ordered.orderingToOrdered
    if (first > second) first else second
  }
}

object MissRight {
  def main(args: Array[String]): Unit = {
    val mr = new MissRight[Girl]
    var g1 = new Girl("maxinyi", 100, 25)
    var g2 = new Girl("sari", 92, 26)

    import cn.lxj.spark.Implicit.MyPredef.girlOrdering
    val g = mr.select(g1, g2)
    println(g.name)
  }
}
