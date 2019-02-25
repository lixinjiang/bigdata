package cn.lxj.spark.Implicit

import cn.lxj.spark.model.Girl

/**
  * 自定义隐式转化
  */
object MyPredef {
  implicit def girlToOrderd(girl: Girl) = new Ordered[Girl] {
    override def compare(that: Girl): Int = {
      if (girl.faceValue == that.faceValue) {
        girl.size - that.size
      } else {
        girl.faceValue - that.faceValue
      }
    }
  }

  implicit object girlOrdering extends Ordering[Girl] {
    override def compare(x: Girl, y: Girl): Int = {
      if (x.faceValue == y.faceValue) {
        x.size - y.size
      } else {
        x.faceValue - y.faceValue
      }
    }
  }
}