package epc.beefeater;

public interface AuthorizationInputBoundary {

	boolean isAuthorized(String userId, String permissionKey);

}
