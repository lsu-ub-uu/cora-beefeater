/*
 * Copyright 2015, 2016 Uppsala University Library
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

package se.uu.ub.cora.beefeater;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class AuthorizatorImp implements Authorizator {

	@Override
	public boolean providedRulesSatisfiesRequiredRules(List<Map<String, Set<String>>> providedRules,
			List<Map<String, Set<String>>> requiredRules) {
		if (requiredRules.isEmpty()) {
			return true;
		}
		return providedRulesSatisfiesExistingRequiredRules(providedRules, requiredRules);

	}

	private boolean providedRulesSatisfiesExistingRequiredRules(
			List<Map<String, Set<String>>> providedRules,
			List<Map<String, Set<String>>> requiredRules) {
		return requiredRules.stream().anyMatch(
				requiredRule -> providedRulesSatisfiesRequiredRule(requiredRule, providedRules));
	}

	private boolean providedRulesSatisfiesRequiredRule(Map<String, Set<String>> requiredRule,
			List<Map<String, Set<String>>> providedRules) {
		return providedRules.stream().anyMatch(
				providedRule -> providedRuleSatisfiesRequiredRule(providedRule, requiredRule));
	}

	private boolean providedRuleSatisfiesRequiredRule(Map<String, Set<String>> providedRule,
			Map<String, Set<String>> requiredRule) {
		return requiredRule.entrySet().stream().allMatch(
				requiredPart -> providedRuleSatisfiesRequiredPart(providedRule, requiredPart));
	}

	private boolean providedRuleSatisfiesRequiredPart(Map<String, Set<String>> providedRule,
			Entry<String, Set<String>> requiredPart) {
		String key = requiredPart.getKey();
		if (!providedRule.containsKey(key)) {
			return false;
		}
		Set<String> requiredValues = requiredPart.getValue();
		Set<String> providedValues = providedRule.get(key);
		return providedExistingValuesSatisfiesRequiredValue(requiredValues, providedValues);
	}

	private boolean providedExistingValuesSatisfiesRequiredValue(Set<String> requiredValues,
			Set<String> providedValues) {
		return requiredValues.stream()
				.allMatch(requiredValue -> providedValuesSatisfiesRequiredValue(providedValues,
						requiredValue));
	}

	private boolean providedValuesSatisfiesRequiredValue(Set<String> providedValues,
			String requiredValue) {
		return providedValues.stream().anyMatch(
				providedValue -> providedValueSatisfiesRequiredValue(providedValue, requiredValue));
	}

	private boolean providedValueSatisfiesRequiredValue(String providedValue,
			String requiredValue) {
		return requiredValue.startsWith(providedValue);
	}

}
