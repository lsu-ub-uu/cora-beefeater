package epc.beefeater;

import java.util.Set;

public interface Authorizator {

	boolean isAuthorized(String userId, Set<String> recordCalculateKeys);

}
