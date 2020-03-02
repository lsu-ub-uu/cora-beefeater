/*
 * Copyright 2016 Uppsala University Library
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

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

//public class RuleImp extends HashMap<String, RulePartValues> implements Rule {
public class RuleImp implements Rule {
	RuleParts ruleParts = new RulePartsImp();
	List<String> readRecordPartPermissions;
	List<String> writeRecordPartPermissions;

	@Override
	public boolean containsRulePart(String key) {
		return ruleParts.containsKey(key);
	}

	@Override
	public RulePartValues getRulePartValuesForKey(String key) {
		return ruleParts.get(key);
	}

	@Override
	public Set<Entry<String, RulePartValues>> entrySet() {
		return ruleParts.entrySet();
	}

	@Override
	public void addRulePart(String key, RulePartValues set) {
		ruleParts.put(key, set);
	}

	@Override
	public Set<String> keySet() {
		// TODO Auto-generated method stub
		return ruleParts.keySet();
	}

	@Override
	public int getNumberOfRuleParts() {
		// TODO Auto-generated method stub
		return ruleParts.size();
	}

}
