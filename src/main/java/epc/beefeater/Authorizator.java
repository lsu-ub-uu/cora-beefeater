package epc.beefeater;

public class Authorizator implements AuthorizationInputBoundary {

	@Override
	public boolean isAuthorized(String userId, String permissionKey) {
		
		return true;
	}

}
