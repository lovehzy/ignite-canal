package com.hzy.service;

import java.util.List;

import com.hzy.pojo.TbUser;
import com.hzy.utils.ResultUtil;

public interface UserService {
	ResultUtil<List<TbUser>> list();
	ResultUtil<TbUser> userById(String id);
	ResultUtil<TbUser> add(TbUser user);
	ResultUtil< TbUser> removeById(String id);
	ResultUtil<TbUser> edit(TbUser user);
}
