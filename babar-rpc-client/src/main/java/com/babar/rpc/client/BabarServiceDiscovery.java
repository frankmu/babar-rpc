package com.babar.rpc.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher.Event;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@PropertySource("classpath:zookeeper.config")
@Component
public class BabarServiceDiscovery {

	private String zookeeperRegistryPath;
	private CountDownLatch latch = new CountDownLatch(1);
	private ZooKeeper zooKeeper;
	private List<String> serviceRegistryList = new ArrayList<String>();

	Log log = LogFactory.getLog(this.getClass());

	public BabarServiceDiscovery(@Value("${zookeeper.host}")String zooKeeperHost, @Value("${zookeeper.timeout}")int zooKeeperTimeout, 
			@Value("${zookeeper.registry.path}")String zookeeperRegistryPath){
		try {
			zooKeeper = new ZooKeeper(zooKeeperHost, zooKeeperTimeout, (event) -> {
				if(event.getState() == Event.KeeperState.SyncConnected){
					latch.countDown();
				}
			});
			latch.await();
			log.info("Connected to zookeeper at " + zooKeeperHost);
			this.zookeeperRegistryPath = zookeeperRegistryPath;
			watchNodeChange();
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

	public String getServiceRegistry(){
		if(serviceRegistryList.size() == 1){
			return serviceRegistryList.get(0);
		}else if(serviceRegistryList.size() > 1){
			return serviceRegistryList.get(new Random().nextInt(serviceRegistryList.size()));
		}else{
			return null;
		}
	}

	private void watchNodeChange(){
		try {
			List<String> nodeList = zooKeeper.getChildren(zookeeperRegistryPath, (event) -> {
				if(event.getType() == EventType.NodeChildrenChanged){
					watchNodeChange();
				}
			});
			List<String> serviceList = new ArrayList<String>();
			for(String node : nodeList){
				String data = new String(zooKeeper.getData(zookeeperRegistryPath + "/" + node, false, null));
				serviceList.add(data);
				log.info("zookeeper client discover service node [" + zookeeperRegistryPath + "/" + node + "] -> [" + data + "]");
			}
			serviceRegistryList = serviceList;
			log.info("Child nodes changed to: " + Arrays.toString(serviceRegistryList.toArray()));
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
}