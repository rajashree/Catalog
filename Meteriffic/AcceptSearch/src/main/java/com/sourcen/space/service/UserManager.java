package com.sourcen.space.service;

import java.util.List;

import javassist.NotFoundException;

import com.sourcen.space.model.Role;
import com.sourcen.space.model.User;
import com.sourcen.space.service.ServiceManager;

public interface UserManager extends ServiceManager {

	User createUser(User userTemplate) throws UserAlreadyExistsException,
			UnsupportedOperationException;

	User updateUser(User user) throws NotFoundException;

	int getTotalUserCount();

	void deleteUser(User user) throws UnsupportedOperationException,
			NotFoundException;

	User getUser(long userId) throws NotFoundException;

	List<Role> getRoles(String username) ;

	User getUser(String userName) throws NotFoundException;

	List<User> getAllUser();

	void changePassword(String username, String newPassword)
			throws  NotFoundException;

}
