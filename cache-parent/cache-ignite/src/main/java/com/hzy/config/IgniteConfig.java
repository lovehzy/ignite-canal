package com.hzy.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.DiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IgniteConfig {
	
	@Bean
	public CacheConfiguration<String, Deque<Serializable>> getCacheConfigruation(){
		CacheConfiguration<String, Deque<Serializable>> cfg = new CacheConfiguration<>();
		cfg.setName("hzy");  //缓存的名字
		cfg.setCacheMode(CacheMode.REPLICATED);  //分区模式
		cfg.setBackups(1);// 设置备份的数量
		cfg.setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, 5)));// 过期时间为三天
		return cfg;
	}
	
	@Bean
	public DiscoverySpi discoverySpi() {
		TcpDiscoverySpi discoverySpi = new TcpDiscoverySpi();
		discoverySpi.setLocalPort(47500);   //端口
		discoverySpi.setLocalPortRange(5);   //节点个数
		TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();
		List<String> addresses = new ArrayList<>();    
		addresses.add("127.0.0.1:47500..47504");     //地址
		ipFinder.setAddresses(addresses);
		discoverySpi.setIpFinder(ipFinder);
		//discoverySpi.setForceServerMode(true);
		return discoverySpi;
	}
	
	@Bean
	public IgniteConfiguration getIgniteConfiguration(CacheConfiguration<String, Deque<Serializable>> cacheCfg,DiscoverySpi aisp) {
		IgniteConfiguration cfg = new IgniteConfiguration();
		cfg.setClientMode(true);  //这里用ignite客户端连接服务端节点
		//cfg.setPeerClassLoadingEnabled(true);    // 启用Peer类加载器
		cfg.setCacheConfiguration(cacheCfg);   //添加缓存配置
		cfg.setDiscoverySpi(aisp);     //添加发现者信息
		return cfg;
	}
	
	@Bean
	public Ignite getIgnite(IgniteConfiguration cfg) {
		//通过自定义配置启动ignite
		return Ignition.start(cfg);
	}
}
