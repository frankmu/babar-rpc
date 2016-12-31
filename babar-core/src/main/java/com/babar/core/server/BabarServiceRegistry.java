package com.babar.core.server;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher.Event;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@PropertySource("classpath:zookeeper.config")
@Component
public class BabarServiceRegistry {

	@Value("${zookeeper.registry.path}")
	private String zookeeperRegistryPath;

	private CountDownLatch latch = new CountDownLatch(1);
	private ZooKeeper zooKeeper;
	Log log = LogFactory.getLog(this.getClass());

	public BabarServiceRegistry(@Value("${zookeeper.host}")String zooKeeperHost, @Value("${zookeeper.timeout}")int zooKeeperTimeout){
		try {
			zooKeeper = new ZooKeeper(zooKeeperHost, zooKeeperTimeout, (event) -> {
				if(event.getState() == Event.KeeperState.SyncConnected){
					latch.countDown();
				}
			});
			latch.await();
			log.info("Connected to zookeeper at " + zooKeeperHost);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void close(){
		try {
			zooKeeper.close();
			log.info("zookeeper client has been closed.");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void registerService(String path, String data){
		try {
			zooKeeper.create(path, data == null ? null : data.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
			log.info("Created zookeeper node [" + path + "] -> [" + data + "]");
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void unRegisterService(String path){
		try {
			if(zooKeeper != null && zooKeeper.exists(path, false) != null){
				zooKeeper.delete(path, -1);
			}
		} catch (InterruptedException | KeeperException e) {
			e.printStackTrace();
		}
	}

	public byte[] get(String path){
		try {
			return zooKeeper.getData(path, false, null);
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void initRegistryPath(){
		try {
			if(zooKeeper != null && zooKeeper.exists(zookeeperRegistryPath, false) == null){
				zooKeeper.create(zookeeperRegistryPath, null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
				log.info("Created zookeeper persistent node [" + zookeeperRegistryPath + "] -> [NULL]");
			}
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void destroyRegistryPath(){
		try {
			if(zooKeeper != null && zooKeeper.exists(zookeeperRegistryPath, false) != null){
				unRegisterService(zookeeperRegistryPath);
				log.info("Destroy zookeeper persistent node [" + zookeeperRegistryPath + "] -> [NULL]");
			}
		} catch (KeeperException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}