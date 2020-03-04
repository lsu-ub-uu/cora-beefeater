package se.uu.ub.cora.beefeater.authorization;

import java.util.Map.Entry;
import java.util.Set;

public interface RuleParts {

	boolean containsPermissionKey(String key);

	RulePartValues getValuesForPermissionKey(String key);

	Set<Entry<String, RulePartValues>> entrySet();

	void add(String key, RulePartValues set);

	Set<String> getAllPermissionKeys();

	int numberOfRuleParts();

}
