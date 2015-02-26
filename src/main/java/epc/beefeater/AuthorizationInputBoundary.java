package epc.beefeater;

import java.util.Set;

public interface AuthorizationInputBoundary {

	boolean isAuthorized(String userId, Set<String> recordCalculateKeys);

}
