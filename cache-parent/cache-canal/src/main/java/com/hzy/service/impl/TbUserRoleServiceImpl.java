package com.hzy.service.impl;

import java.util.List;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hzy.mapper.TbUserMapper;
import com.hzy.pojo.SynchData;
import com.hzy.pojo.TbUser;
import com.hzy.pojo.TbUserRole;
import com.hzy.service.observerService;

@Service(value="TbUserRoleServiceImpl")
public class TbUserRoleServiceImpl implements observerService{
	
	@Autowired
	private Ignite ignite;
	@Autowired
	private TbUserMapper userMapper;
	
	@Override
	public boolean remove(SynchData<Object> synchData) {
		IgniteCache<String, Object> cache = ignite.cache(synchData.getSchemaName());
		if(cache==null)
			return false;
		TbUserRole tbUserRole=(TbUserRole) synchData.getBeforeData();
		String key="tb_user."+tbUserRole.getUserId();
		boolean result = cache.remove(key);
		return result;
	}

	@Override
	public boolean update(SynchData<Object> synchData) {
		IgniteCache<String, Object> cache = ignite.cache(synchData.getSchemaName());
		if(cache==null)
			return false;
		TbUserRole tbUserRole=(TbUserRole) synchData.getBeforeData();
		String userId = tbUserRole.getUserId();
		String key="tb_user."+tbUserRole.getUserId();
		TbUser user=(TbUser) cache.get(key);
		if(user==null)
			return false;
		List<String> list = userMapper.getRoleByUserId(userId);
		String roles=null;
		for (String str : list) {
			if(roles==null) {
				roles=str;
			}else {
				roles=roles+","+str;
			}
		}
		user.setRoles(roles);
		boolean result = cache.replace(key, user);
		return result;
	}

	@Override
	public boolean add(SynchData<Object> synchData) {
		
		return update(synchData);
	}
	
}
