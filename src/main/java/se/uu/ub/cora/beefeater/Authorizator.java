package se.uu.ub.cora.beefeater;

import java.util.Set;

public interface Authorizator {

	boolean isAuthorized(String userId, Set<String> recordCalculateKeys);

}
