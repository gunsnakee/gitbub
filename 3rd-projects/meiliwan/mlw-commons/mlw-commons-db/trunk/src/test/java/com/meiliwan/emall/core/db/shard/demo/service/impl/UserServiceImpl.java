package com.meiliwan.emall.core.db.shard.demo.service.impl;


import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.core.db.shard.bean.UsersExample;
import com.meiliwan.emall.core.db.shard.demo.dao.UserDao;
import com.meiliwan.emall.core.db.shard.demo.dao.entity.UserEntity;
import com.meiliwan.emall.core.db.shard.demo.service.UserService;

/**
 演示用户service
 */
public class UserServiceImpl implements UserService {

	private UserDao userDao;
	
	public void createUsers() throws Exception {
		UserEntity user1 = new UserEntity();
		user1.setId(1);
		user1.setName("1");
		//
		userDao.insertUser(user1);
		
		
		PageInfo pageInfo = new PageInfo();
		UsersExample users = new UsersExample();
		users.setId(200);
		System.out.println(userDao.findUser(users , pageInfo));
		System.out.println(userDao.findUser(users, pageInfo));
		
		//
		UserEntity user2 = new UserEntity();
		user2.setId(2);
		user2.setName("2");
		//
		userDao.insertUser(user2);
//		
		
		
		//
		UserEntity user3 = new UserEntity();
		user3.setId(200);
		user3.setName("200");
		//
		userDao.insertUser(user3);

		//
		UserEntity user4 = new UserEntity();
		user4.setId(201);
		user4.setName("201");
		//
		userDao.insertUser(user4);
		
		//
		UserEntity user5 = new UserEntity();
		user5.setId(20);
		user5.setName("20");
		//
		userDao.insertUser(user5);

		//
		UserEntity user6 = new UserEntity();
		user6.setId(21);
		user6.setName("21");
		//
		userDao.insertUser(user6);
		
//		PageInfo pageInfo = new PageInfo();
//		UsersExample users = new UsersExample();
		users.setId(1);
		System.out.println(userDao.findUser(users , pageInfo));
		System.out.println(userDao.findUser(users, pageInfo));
		
		//模拟抛出异常，事务回滚
		throw new ServiceException("Error"); 
				
//		System.out.println(userDao.findUser(200));
//		System.out.println(userDao.findUser(100));
//		System.out.println(userDao.findUser(200));
//		System.out.println(userDao.findUser(200));
//		System.out.println(userDao.findUser(100));
//		System.out.println(userDao.findUser(200));
//		System.out.println(userDao.findUser(100));
//		System.out.println(userDao.findUser(200));
//		System.out.println(userDao.findUser(100));
//		System.out.println(userDao.findUser(200));
//		System.out.println(userDao.findUser(100));
//		System.out.println(userDao.findUser(200));
//		System.out.println(userDao.findUser(100));
//		System.out.println(userDao.findUser(200));
//		System.out.println(userDao.findUser(100));
//		System.out.println(userDao.findUser(200));
//		System.out.println(userDao.findUser(100));
//		System.out.println(userDao.findUser(200));
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
}
