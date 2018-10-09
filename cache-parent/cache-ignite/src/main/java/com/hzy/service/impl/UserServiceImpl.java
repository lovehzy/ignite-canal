package com.hzy.service.impl;

import java.util.List;
import java.util.UUID;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hzy.mapper.TbUserMapper;
import com.hzy.pojo.TbUser;
import com.hzy.pojo.TbUserExample;
import com.hzy.service.UserService;
import com.hzy.utils.ResultUtil;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private TbUserMapper userMapper;
	@Autowired
	private Ignite ignite;
	
	@Override
	public ResultUtil<List<TbUser>> list() {
		TbUserExample example = new TbUserExample();
		List<TbUser> list = userMapper.selectByExample(example);
		
		return new ResultUtil<List<TbUser>>("200",list,"从数据库查询！");
	}

	@Override
	public ResultUtil<TbUser> userById(String id) {
		IgniteCache<String, TbUser> cache = ignite.getOrCreateCache("test");
		TbUser tbUser = cache.get("tb_user."+id);
		if(tbUser!=null)
			return new ResultUtil<TbUser>("200",tbUser, "从缓存查询！");
		
		TbUser user = userMapper.selectByPrimaryKey(id);
		if(user==null)
			return new ResultUtil<TbUser>("500",tbUser, "没有对应的数据！");
		List<String> list = userMapper.getRoleByUserId(id);
		String role=null;
		if(!list.isEmpty()) {
			for (String str : list) {
				if(role==null) {
					role=str;
				}else{					
					role=role+","+str;
				}
			}
		}
		user.setRoles(role);
		cache.put("tb_user."+id, user);
		return new ResultUtil<TbUser>("200",user, "从数据库查询！");
	}

	@Override
	public ResultUtil<TbUser> add(TbUser user) {
		user.setUserId(UUID.randomUUID().toString());
		int result = userMapper.insert(user);
		return new ResultUtil<TbUser>(result==1?"200":"500",null, "新增用户！");
	}

	@Override
	public ResultUtil<TbUser> removeById(String id) {
		
		return null;
	}

	@Override
	public ResultUtil<TbUser> edit(TbUser user) {
		int result = userMapper.updateByPrimaryKey(user);
		return new ResultUtil<TbUser>(result==1?"200":"500",null, "编辑用户！");
	}

}
