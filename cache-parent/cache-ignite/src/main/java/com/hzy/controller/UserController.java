package com.hzy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hzy.pojo.TbUser;
import com.hzy.service.UserService;
import com.hzy.utils.ResultUtil;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/")
	public ResultUtil<List<TbUser>> list(){
		ResultUtil<List<TbUser>> result = userService.list();
		return result;
	}
	
	@GetMapping("/{id}")
	public ResultUtil<TbUser> get(@PathVariable(name="id")String id){
		ResultUtil<TbUser> result = userService.userById(id);
		return result;
	}
	
	@PostMapping("/")
	public ResultUtil<TbUser> add(TbUser user){
		ResultUtil<TbUser> result = userService.add(user);
		return result;
	}
}
