/*
 * Copyright 2015 Uppsala University Library
 *
 * This file is part of Cora.
 *
 *     Cora is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Cora is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Cora.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.uu.ub.cora.beefeater;

import java.util.HashSet;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.Test;

import se.uu.ub.cora.beefeater.authentication.User;

public class AuthorizationInputBoundaryTest {
	@Test
	public void testInit() {
		Authorizator authorizator = new AuthorizatorImp();
		Set<String> permissionKeys = new HashSet<>();
		permissionKeys.add("READ:PLACE:SYSTEM:UU:UUB");

		User user = new User("122222");
		user.loginId = "someAuthorizedUserId";
		boolean authorized = authorizator.isAuthorized(user, permissionKeys);

		Assert.assertTrue(authorized, "userId should be authorized to use permissionKey");
	}

	@Test
	public void testUnauthorizedUser() {
		Authorizator authorizator = new AuthorizatorImp();
		Set<String> permissionKeys = new HashSet<>();
		permissionKeys.add("READ:PLACE:SYSTEM:UU:UUB");

		User user = new User("122222");
		user.loginId = "unauthorizedUserId";
		boolean authorized = authorizator.isAuthorized(user, permissionKeys);

		Assert.assertFalse(authorized, "userId should be authorized to use permissionKey");
	}
}
