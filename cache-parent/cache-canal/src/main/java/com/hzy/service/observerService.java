package com.hzy.service;

import com.hzy.pojo.SynchData;

public interface observerService {
	boolean remove(SynchData<Object> synchData);
	boolean update(SynchData<Object> synchData);
	boolean add(SynchData< Object> synchData);
}
