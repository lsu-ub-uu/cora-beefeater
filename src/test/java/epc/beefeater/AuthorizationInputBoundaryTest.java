package epc.beefeater;

import org.testng.Assert;
import org.testng.annotations.Test;

public class AuthorizationInputBoundaryTest {
	@Test
	public void testInit() {
		AuthorizationInputBoundary authorizator = new Authorizator();
		boolean authorized = authorizator.isAuthorized("userId",
				"permissionKey");

		Assert.assertTrue(authorized,
				"userId should be authorized to use permissionKey");
	}
}
