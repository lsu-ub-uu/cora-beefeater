package se.uu.ub.cora.beefeater.authorization;

import java.util.Map.Entry;
import java.util.Set;

public interface RuleParts {

	boolean containsKey(String key);

	RulePartValues get(String key);

	Set<Entry<String, RulePartValues>> entrySet();

	void put(String key, RulePartValues set);

	Set<String> keySet();

	int size();

}
