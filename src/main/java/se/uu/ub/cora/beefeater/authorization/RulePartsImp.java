package se.uu.ub.cora.beefeater.authorization;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class RulePartsImp implements RuleParts {
	HashMap<String, RulePartValues> ruleParts = new HashMap<>();

	@Override
	public boolean containsPermissionKey(String permissionKey) {
		return ruleParts.containsKey(permissionKey);
	}

	@Override
	public RulePartValues getValuesForPermissionKey(String permissionKey) {
		return ruleParts.get(permissionKey);
	}

	@Override
	public Set<Entry<String, RulePartValues>> entrySet() {
		return ruleParts.entrySet();
	}

	@Override
	public void add(String permissionKey, RulePartValues values) {
		ruleParts.put(permissionKey, values);
	}

	@Override
	public Set<String> getAllPermissionKeys() {
		return ruleParts.keySet();
	}

	@Override
	public int numberOfRuleParts() {
		return ruleParts.size();
	}

}
