package com.polaris.manage.service.mysql.auth.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polaris.common.exception.ApiException;
import com.polaris.common.exception.PolarisException;
import com.polaris.manage.model.mysql.auth.User;
import com.polaris.manage.persist.mysql.auth.pub.UserDao;
import com.polaris.manage.service.mysql.auth.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	@Override
	public User save(User user) {
		if (user != null) {
			return this.userDao.save(user);
		}
		return null;
	}

	@Override
	public void delete(User user) {
		if (user != null) {
			this.userDao.delete(user);
		}
	}

	@Override
	public User modify(User user) throws ApiException {
		if (user == null) {
			return user;
		}
		if (StringUtils.isBlank(user.getId())) {
			throw new PolarisException("目标对象的ID字段为空，无法执行数据库修改操作！[" + user.toString() + "]");
		}
		return this.userDao.save(user);
	}

	@Override
	public User find(String userId) {
		return this.userDao.findOne(userId);
	}

	@Override
	public List<User> list() {
		return this.userDao.findAll();
	}

	@Override
	public User findByUsernameAndEnableTrueAndLockedFalse(String username) {
		return this.userDao.findByUsernameAndEnableTrueAndLockedFalse(username);
	}

	@Override
	public User finaByUsername(String username) {
		return this.userDao.findByUsername(username);
	}

}
