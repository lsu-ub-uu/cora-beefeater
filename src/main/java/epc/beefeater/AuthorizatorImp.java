package epc.beefeater;

import java.util.Set;

public class Authorizator implements AuthorizationInputBoundary {

	@Override
	public boolean isAuthorized(String userId, Set<String> permissionKey) {
		
		//THIS IS A TEMPORARY HACK TO BE ENABLE TESTING OF UNAUTHORIZED ACCESS
		if("unauthorizedUserId".equals(userId)){
			return false;
		}
		
		return true;
	}

}
