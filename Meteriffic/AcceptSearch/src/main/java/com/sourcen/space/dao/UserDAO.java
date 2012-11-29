package com.sourcen.space.dao;

import java.util.List;

import com.sourcen.space.model.Role;
import com.sourcen.space.model.User;

public interface UserDAO {

	public User getUser(User template);

	public User getUserByUsername(String username);

	public int getNumberOfUser();

	public List<User> getAllUser();

	public User saveUser(User user);

	public void updateUser(User user);

	public List<Role> getRoles(String username);

	public List<User> listUser(int start, int count);

	
}
