package com.hzy.server;

import java.net.InetSocketAddress;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;

/**
 * 创建canal连接
 * 
 * @author  liuchangsong
 * @version  [版本号, 2018年9月28日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
@Component
public class CreateCanalConnector {
	
	@Value("${canal.server.ip}")
	private  String ip; // 单机版地址
	@Value("${canal.server.port}")
	private  int port; // 单机版端口
	@Value("${canal.server.zookeeper.registry}")
	private  String zookeeper; // 集群注册中心地址
	@Value("${canal.server.destination}")
	private  String destination; // 目的地
	@Value("${canal.server.loginName}")
	private  String name; // 用户名
	@Value("${canal.server.password}")
	private  String password; // 密码
	@Value("${canal.server.cluster}")
	private  String cluster; // 是否为集群
	
	/**
	 * 获取canal单机版连接
	 * @return
	 */
	public  CanalConnector getCanalSingeConnector() {
		return CanalConnectors.newSingleConnector(new InetSocketAddress(ip, port), destination, name, password);
	}

	/**
	 * 获取集群canal链接
	 */
	public  CanalConnector getCanalClusterConnector() {
		return CanalConnectors.newClusterConnector(zookeeper, destination, name, password);
	}
	
	/**
	 * 根据配置获取连接
	 */
	public CanalConnector getCanalConnector() {
		if(cluster.equals("true")) {
			return getCanalClusterConnector();
		}else {
			return getCanalSingeConnector();
		}
	}
}
