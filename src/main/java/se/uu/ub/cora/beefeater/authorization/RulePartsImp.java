/*
 * Copyright 2020 Uppsala University Library
 *
 * This file is part of Cora.
 *
 *     Cora is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Cora is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Cora.  If not, see <http://www.gnu.org/licenses/>.
 */
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
