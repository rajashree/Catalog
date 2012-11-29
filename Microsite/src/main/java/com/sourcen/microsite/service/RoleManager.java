package com.sourcen.microsite.service;

import com.sourcen.microsite.model.Role;

public interface RoleManager {

	public void getUserRole(long uid);

	public void addUserRole(long uid, Role role);

	public void removeUserRole(long uid, long rid);
}
