package cn.lxj.spark.practice

import java.net.URL

import org.apache.spark.{SparkConf, SparkContext}

/**
  * 取出学科点击中的前三
  */
object UrlCount {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("UrlCount").setMaster("local[2]")
    val sc = new SparkContext(conf)

    //rdd1将数据切分，元组中放的是（URL， 1）
    val rdd1 = sc.textFile("E:\\IdeaProjects\\bigdata\\hellospark\\src\\main\\files\\usercount\\itcast.log").map(line => {
      val f = line.split(",")
      (f(1), 1)
    })
    val rdd2 = rdd1.reduceByKey(_ + _)

    val rdd3 = rdd2.map(t => {
      val url = t._1
      val host = new URL(url).getHost
      (host, url, t._2)
    })

    val rdd4 = rdd3.groupBy(_._1).mapValues(it => {
      it.toList.sortBy(_._3).reverse.take(3)
    })

    println(rdd2.collect().toBuffer)
    sc.stop()
  }
}
