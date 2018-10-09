package com.hzy.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hzy.client.CanalClient;

@Component
public class Command {
	
	@Autowired
	private CanalClient canalClient;
	protected Thread thread = null;
	
	/**
	 * 启动canal客户端
	 */
	public void start() {
		thread = new Thread(new Runnable() {

			public void run() {
				canalClient.startCanal();
			}
		});
		thread.start();
		
	}
}
