package cn.lxj.spark.demo

import cn.lxj.spark.model.Girl
import cn.lxj.spark.Implicit.MyPredef

class MissLeft[T: Ordering] {
  def choose(first: T, second: T): T = {
    val ord = implicitly[Ordering[T]]
    if (ord.gt(first, second)) first else second
  }
}

object MissLeft {
  def main(args: Array[String]): Unit = {
    import MyPredef.girlOrdering
    val m1 = new MissLeft[Girl]
    val g1 = new Girl("mayi", 100, 25)
    val g2 = new Girl("sora", 98, 26)
    val g = m1.choose(g1, g2)
    println(g.name)
  }
}