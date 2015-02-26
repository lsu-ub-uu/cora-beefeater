package epc.beefeater;

import java.util.HashSet;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.Test;

public class AuthorizationInputBoundaryTest {
	@Test
	public void testInit() {
		AuthorizationInputBoundary authorizator = new Authorizator();
		Set<String> permissionKeys = new HashSet<>();
		permissionKeys.add("READ:PLACE:SYSTEM:UU:UUB");
		boolean authorized = authorizator
				.isAuthorized("userId", permissionKeys);

		Assert.assertTrue(authorized,
				"userId should be authorized to use permissionKey");
	}
	@Test
	public void testUnauthorizedUser() {
		AuthorizationInputBoundary authorizator = new Authorizator();
		Set<String> permissionKeys = new HashSet<>();
		permissionKeys.add("READ:PLACE:SYSTEM:UU:UUB");
		boolean authorized = authorizator
				.isAuthorized("unauthorizedUserId", permissionKeys);
		
		Assert.assertFalse(authorized,
				"userId should be authorized to use permissionKey");
	}
}
