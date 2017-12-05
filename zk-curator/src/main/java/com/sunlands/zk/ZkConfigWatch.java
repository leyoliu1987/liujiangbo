package com.sunlands.zk;

import javax.annotation.PostConstruct;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.springframework.stereotype.Component;
@Component
public class ZkConfigWatch {
	/** 父节点path */
	static final String PARENT_PATH = "/test";
	/** zookeeper服务器地址 */
	public static final String CONNECT_ADDR = "192.168.58.169:2181";
	public static final int SESSION_TIMEOUT = 1000;
    @PostConstruct
	private void init() throws Exception {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 10);
		CuratorFramework cf = CuratorFrameworkFactory.builder().connectString(CONNECT_ADDR)
				.sessionTimeoutMs(SESSION_TIMEOUT).retryPolicy(retryPolicy).build();

		// 3 建立连接
		cf.start();
		
		
		if (cf.checkExists().forPath(PARENT_PATH) == null) {
			cf.create().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath(PARENT_PATH, "这是一个根节点".getBytes());
		}
		
		PathChildrenCache cache = new PathChildrenCache(cf, PARENT_PATH, true);
		cache.start(StartMode.POST_INITIALIZED_EVENT);
		cache.getListenable().addListener(new PathChildrenCacheListener() {
			
			@Override
			public void childEvent(CuratorFramework cf, PathChildrenCacheEvent event) throws Exception {
				switch (event.getType()) {
				
				case CHILD_ADDED:
					System.out.println("CHILD_ADDED :" + event.getData().getPath());
					System.out.println("CHILD_ADDED :" + new String(event.getData().getData()));
					break;
				case CHILD_UPDATED:
					System.out.println("CHILD_UPDATED :" + event.getData().getPath());
					System.out.println("CHILD_UPDATED :" + new String(event.getData().getData()));
					break;
				case CHILD_REMOVED:
					System.out.println("CHILD_REMOVED :" + event.getData().getPath());
					System.out.println("CHILD_REMOVED :" + new String(event.getData().getData()));
					break;
				default:
					break;
				}
				
			}
		});
		
	}
	
	


}
