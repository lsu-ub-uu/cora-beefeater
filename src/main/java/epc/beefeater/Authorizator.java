package epc.beefeater;

import java.util.Set;

public class Authorizator implements AuthorizationInputBoundary {

	@Override
	public boolean isAuthorized(String userId, Set<String> permissionKey) {
		
		return true;
	}

}
