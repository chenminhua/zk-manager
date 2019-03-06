package com.chenminhua.zkmanager

import org.apache.zookeeper.CreateMode
import org.apache.zookeeper.ZooDefs
import org.apache.zookeeper.ZooKeeper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ZKManagerImpl : ZKManager {

    @Autowired lateinit var zkClient: ZooKeeper

    override fun getZNodeData(path: String, watchFlag: Boolean): String {
        return String(zkClient.getData(path, null, null));
    }

    override fun update(path: String, data: String) {
        val version = zkClient.exists(path, true).version
        zkClient.setData(path, data.toByteArray(), version)
    }

    override fun create(path: String, data: String) {
        zkClient.create(path, data.toByteArray(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT)
    }

    override fun getChildern(path: String): List<String> {
        return zkClient.getChildren(path, false)
    }
}