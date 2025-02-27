/*
 * Copyright 2016, 2025 Uppsala University Library
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

package se.uu.ub.cora.beefeater.authentication;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Set;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class UserTest {

	private User user;

	@BeforeMethod
	private void beforeMethod() {
		user = new User("someUserId");

	}

	@Test
	public void init() {
		assertEquals(user.id, "someUserId");
	}

	@Test
	public void testLoginInfo() {
		user.loginId = "someLoginId";
		user.loginDomain = "someDomain";

		assertEquals(user.id, "someUserId");
		assertEquals(user.loginId, "someLoginId");
		assertEquals(user.loginDomain, "someDomain");
	}

	@Test
	public void testRoleSet() {
		user.roles.add("admin");
		user.roles.add("guest");

		Set<String> roles = user.roles;

		assertEquals(roles.size(), 2);
		assertTrue(roles.contains("guest"));
		assertTrue(roles.contains("admin"));
	}

	@Test
	public void testUserActivation() {
		user.active = true;
		assertTrue(user.active);

		user.active = false;
		assertFalse(user.active);
	}

	@Test
	public void testPermissionUnitIds() {
		user.permissionUnitIds.add("permissionUnit1");
		user.permissionUnitIds.add("permissionUnit2");

		assertTrue(user.permissionUnitIds.contains("permissionUnit1"));
		assertTrue(user.permissionUnitIds.contains("permissionUnit2"));

		user.permissionUnitIds.remove("permissionUnit1");

		assertEquals(user.permissionUnitIds.size(), 1);
		assertFalse(user.permissionUnitIds.contains("permissionUnit1"));
		assertTrue(user.permissionUnitIds.contains("permissionUnit2"));
	}
}
