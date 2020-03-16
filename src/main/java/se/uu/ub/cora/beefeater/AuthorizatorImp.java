/*
 * Copyright 2015, 2016, 2018 Uppsala University Library
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
import java.util.Map.Entry;
import java.util.stream.Collectors;

import se.uu.ub.cora.beefeater.authorization.Rule;
import se.uu.ub.cora.beefeater.authorization.RulePartValues;

public class AuthorizatorImp implements Authorizator {

	private static final String WILDCARD = "*";

	@Override
	public boolean providedRulesSatisfiesRequiredRules(List<Rule> providedRules,
			List<Rule> requiredRules) {
		if (requiredRules.isEmpty()) {
			return true;
		}
		return providedRulesSatisfiesExistingRequiredRules(providedRules, requiredRules);

	}

	private boolean providedRulesSatisfiesExistingRequiredRules(List<Rule> providedRules,
			List<Rule> requiredRules) {
		return requiredRules.stream().anyMatch(
				requiredRule -> providedRulesSatisfiesRequiredRule(requiredRule, providedRules));
	}

	private boolean providedRulesSatisfiesRequiredRule(Rule requiredRule,
			List<Rule> providedRules) {
		return providedRules.stream().anyMatch(
				providedRule -> providedRuleSatisfiesRequiredRule(providedRule, requiredRule));
	}

	private boolean providedRuleSatisfiesRequiredRule(Rule providedRule, Rule requiredRule) {
		return requiredRule.entrySet().stream().allMatch(
				requiredPart -> providedRuleSatisfiesRequiredPart(providedRule, requiredPart));
	}

	private boolean providedRuleSatisfiesRequiredPart(Rule providedRule,
			Entry<String, RulePartValues> requiredPart) {
		String key = requiredPart.getKey();
		if (!providedRule.containsRulePart(key)) {
			return false;
		}
		RulePartValues requiredValues = requiredPart.getValue();
		RulePartValues providedValues = providedRule.getRulePartValuesForKey(key);
		return providedExistingValuesSatisfiesRequiredValue(requiredValues, providedValues);
	}

	private boolean providedExistingValuesSatisfiesRequiredValue(RulePartValues requiredValues,
			RulePartValues providedValues) {
		return requiredValues.stream()
				.allMatch(requiredValue -> providedValuesSatisfiesRequiredValue(providedValues,
						requiredValue));
	}

	private boolean providedValuesSatisfiesRequiredValue(RulePartValues providedValues,
			String requiredValue) {
		return providedValues.stream().anyMatch(
				providedValue -> providedValueSatisfiesRequiredValue(providedValue, requiredValue));
	}

	private boolean providedValueSatisfiesRequiredValue(String providedValue,
			String requiredValue) {
		if (isWildcardValue(providedValue)) {
			return providedValueSatisfiesRequiredValueAsStartOfString(providedValue, requiredValue);
		}
		return requiredValue.equals(providedValue);
	}

	private boolean isWildcardValue(String providedValue) {
		return providedValue.endsWith(WILDCARD);
	}

	private boolean providedValueSatisfiesRequiredValueAsStartOfString(String providedValue,
			String requiredValue) {
		String providedValueWithoutWildcard = providedValue.substring(0,
				providedValue.length() - WILDCARD.length());
		return requiredValue.startsWith(providedValueWithoutWildcard);
	}

	@Override
	public List<Rule> providedRulesMatchRequiredRules(List<Rule> providedRules,
			List<Rule> requiredRules) {
		return providedRulesMatchExistingRequiredRules(providedRules, requiredRules);
	}

	private List<Rule> providedRulesMatchExistingRequiredRules(List<Rule> providedRules,
			List<Rule> requiredRules) {
		return providedRules.stream()
				.filter(providedRule -> providedRulesMatchRequiredRule(providedRule, requiredRules))
				.collect(Collectors.toList());
	}

	private boolean providedRulesMatchRequiredRule(Rule providedRule, List<Rule> requiredRules) {
		return requiredRules.stream().anyMatch(
				requiredRule -> providedRuleMatchRequiredRule(requiredRule, providedRule));
	}

	private boolean providedRuleMatchRequiredRule(Rule requiredRule, Rule providedRule) {
		return requiredRule.entrySet().stream().allMatch(
				requiredPart -> providedRuleSatisfiesRequiredPart(providedRule, requiredPart));
	}
}
