package com.hzy.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.alibaba.otter.canal.client.CanalConnector;
import com.hzy.server.Command;
import com.hzy.server.CreateCanalConnector;

@SpringBootApplication
public class CanalConfig {
	
	@Bean
	public CanalConnector getCanalConnector(CreateCanalConnector create) {
		CanalConnector canalConnector = create.getCanalConnector();
		return canalConnector;
	}
	
	@Bean
	public Map<String,String> CacheTable(){
		Map<String,String> map=new HashMap<>();
		map.put("test.tb_user", "TbUser");
		map.put("test.tb_role", "TbRole");
		map.put("test.tb_user_role", "TbUserRole");
		return map;
	}
	
	@Bean
	public Command startCanal(Command command) {
		command.start();
		return null;
	}
}
