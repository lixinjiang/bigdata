package cn.lxj.spark.practice

import java.net.URL

import org.apache.spark.{SparkConf, SparkContext}

/**
  * 根据指定的学科，取出前三的
  */
object AdvUrlCount {
  def main(args: Array[String]): Unit = {
    // 从数据库中加载规则
    val arr = Array("java.lxj.cn", "php.lxj.cn", "net.lxj.cn")

    val conf = new SparkConf().setAppName("AdvUrlCount").setMaster("local")
    val sc = new SparkContext(conf)

    // rdd将数据切分，元组中放的是（URL，1）
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

//    val rddjava = rdd3.filter(_._1 == "java.lxj.cn")
//    val sortdjava = rddjava.sortBy(_._3, ascending = false).take(3)
//    val rddphp = rdd3.filter(_._1 == "php.lxj.cn")

    for (ins <- arr) {
      val rdd = rdd3.filter(_._1 == ins)
      val result= rdd.sortBy(_._3, ascending = false).take(3)
      //通过JDBC向数据库中存储数据
      //id，学院，URL，次数， 访问日期
      println(result.toBuffer)
    }

    sc.stop()
  }
}
