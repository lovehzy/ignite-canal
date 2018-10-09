package com.hzy.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.Message;
import com.hzy.pojo.SynchData;
import com.hzy.server.DataCache;
import com.hzy.utils.CanalUtil;
import com.hzy.utils.ResultUtil;

@Component
public class CanalClient {
	
	@Value("${canal.data.batch.size}")
	private String size;                  //获取指定数据的大小
	@Value("${canal.server.subscribe}")
	private String subscribe;
	@Autowired
	private CanalConnector connector;     //客户端与服务端连接
	@Autowired
	private CanalUtil CanalUtil;
	@Autowired
	private DataCache dataCache;          //数据同步的处理类
	public boolean strobe=true;           //循环判断
	
	public void startCanal() {

		try {
			while (strobe) {
				connector.connect();                  //连接上服务器
				connector.subscribe(subscribe);
				connector.rollback();
				Message message = connector.getWithoutAck(Integer.parseInt(size)); // 获取指定数量的数据
				long batchId = message.getId();             //获取检测到的数据id
				int size = message.getEntries().size();     //获取检测到的数据大小
				if (batchId == -1 || size == 0) {
					//缓加载，每阁一秒加载一次（可自定义）
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					List<Entry> entries = message.getEntries();
					List<SynchData<Object>> synchDatas = CanalUtil.entryToSynchData(entries);
					for (final SynchData<Object> synchData : synchDatas) {
						new Thread(new Runnable() {	
							@Override
							public void run() {
								ResultUtil<Object> result = dataCache.dispose(synchData);
								System.out.println(result);
							}
						}).start();
					}
					
				}

				connector.ack(batchId); // 提交确认
			}
		} finally {
			connector.disconnect();
		}

	}
}
