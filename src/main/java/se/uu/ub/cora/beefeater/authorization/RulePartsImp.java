package se.uu.ub.cora.beefeater.authorization;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class RulePartsImp implements RuleParts {
	HashMap<String, RulePartValues> ruleParts = new HashMap<>();

	@Override
	public boolean containsKey(String key) {
		return ruleParts.containsKey(key);
	}

	@Override
	public RulePartValues get(String key) {
		return ruleParts.get(key);
	}

	@Override
	public Set<Entry<String, RulePartValues>> entrySet() {
		return ruleParts.entrySet();
	}

	@Override
	public void put(String key, RulePartValues set) {
		ruleParts.put(key, set);
	}

	@Override
	public Set<String> keySet() {
		return ruleParts.keySet();
	}

	@Override
	public int size() {
		return ruleParts.size();
	}

}
