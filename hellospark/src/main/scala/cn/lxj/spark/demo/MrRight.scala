package cn.lxj.spark.demo

import cn.lxj.spark.model.Boy

class MrRight[T] {
  def choose[T <: Comparable[T]](first: T, second: T): T = {
    if (first.compareTo(second) > 0) first else second
  }
}

object MrRight {
  def main(args: Array[String]): Unit = {
    val mr = new MrRight[Boy]
    val b1 = new Boy("zhangsan", 99)
    val b2 = new Boy("lisi", 22)
    val b = mr.choose(b1, b2)
    println(b.name)
  }
}
