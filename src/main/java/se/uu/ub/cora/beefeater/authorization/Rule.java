/*
 * Copyright 2016, 2020 Uppsala University Library
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

public interface Rule {

	Set<Entry<String, RulePartValues>> entrySet();

	boolean containsRulePart(String key);

	RulePartValues getRulePartValuesForKey(String key);

	void addRulePart(String key, RulePartValues set);

	void addRulePartIfKeyIsAbsent(String key, RulePartValues rulePartValues);

	Set<String> keySet();

	int getNumberOfRuleParts();

	void addWriteRecordPartPermission(String writePermission);

	List<String> getWriteRecordPartPermissions();

	void addReadRecordPartPermission(String readPermission);

	List<String> getReadRecordPartPermissions();

}
