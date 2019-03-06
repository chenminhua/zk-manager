package com.chenminhua.zkmanager

import org.apache.zookeeper.Watcher
import org.apache.zookeeper.ZooKeeper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.CountDownLatch
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

@SpringBootApplication
class ZkmanagerApplication: CommandLineRunner {

	@Autowired lateinit var zkManager: ZKManager

	override fun run(vararg args: String?) {
		println("zknode /path : ${zkManager.getZNodeData("/path", false)}")

	}
}

fun main(args: Array<String>) {
	runApplication<ZkmanagerApplication>(*args)
}

@Configuration
class ZKConnection {

	private val connectionLatch: CountDownLatch = CountDownLatch(1);

	@Bean
	fun getZKClient(): ZooKeeper {
		val zoo = ZooKeeper("localhost:2181", 2000, Watcher {
			if (it.state == Watcher.Event.KeeperState.SyncConnected) {
				connectionLatch.countDown()
			}
		})

		connectionLatch.await()
		return zoo
	}
}

interface ZKManager {
	fun create(path: String, data: String)

	fun getZNodeData(path: String, watchFlag: Boolean): String

	fun update(path: String, data: String)

	fun getChildern(path: String): List<String>
}