package se.uu.ub.cora.beefeater.authorization;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public interface Rule {

	Set<Entry<String, RulePartValues>> entrySet();

	boolean containsRulePart(String key);

	RulePartValues getRulePartValuesForKey(String key);

	void addRulePart(String key, RulePartValues set);

	Set<String> keySet();

	int getNumberOfRuleParts();

	void addWriteRecordPartPermission(String writePermission);

	List<String> getWriteRecordPartPermissions();

	void addReadRecordPartPermission(String readPermission);

	List<String> getReadRecordPartPermissions();

}
