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

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.beefeater.authorization.Rule;
import se.uu.ub.cora.beefeater.authorization.RulePartValues;

public class AuthorizatorTest {
	private List<Rule> providedRules;
	private List<Rule> requiredRules;
	private Authorizator authorizator;

	@BeforeMethod
	public void setUp() {
		authorizator = new AuthorizatorImp();
		providedRules = new ArrayList<Rule>();
		requiredRules = new ArrayList<Rule>();

	}

	@Test
	public void testExactMatch() {
		Rule providedRule = createProvidedRule();
		createRulePart(providedRule, "action", "system.read");
		createRulePart(providedRule, "organisation", "system.se.uu");
		createRulePart(providedRule, "recordType", "system.book");
		createRulePart(providedRule, "delete", "system.existing");
		createRulePart(providedRule, "publish", "system.notpublished");

		Rule requiredRule = createRequiredRule();
		createRulePart(requiredRule, "action", "system.read");
		createRulePart(requiredRule, "organisation", "system.se.uu");
		createRulePart(requiredRule, "recordType", "system.book");
		createRulePart(requiredRule, "delete", "system.existing");
		createRulePart(requiredRule, "publish", "system.notpublished");

		assertTrue(providedRulesSatisfiesRequiredRules());
	}

	@Test
	public void testNoPartsMatch() {
		Rule providedRule = createProvidedRule();
		createRulePart(providedRule, "action", "system.read");
		createRulePart(providedRule, "organisation", "system.se.uu");
		createRulePart(providedRule, "recordType", "system.book");
		createRulePart(providedRule, "delete", "system.existing");
		createRulePart(providedRule, "publish", "system.notpublished");

		Rule requiredRule = createRequiredRule();
		createRulePart(requiredRule, "action", "system.create");
		createRulePart(requiredRule, "organisation", "system.no");
		createRulePart(requiredRule, "recordType", "system.person");
		createRulePart(requiredRule, "delete", "system.notexisting");
		createRulePart(requiredRule, "publish", "system.published");

		assertFalse(providedRulesSatisfiesRequiredRules());
	}

	@Test
	public void testNoRules() {
		assertTrue(providedRulesSatisfiesRequiredRules());
	}

	@Test
	public void testNoRequiredRule() {
		Rule providedRule = createProvidedRule();
		createRulePart(providedRule, "rulePart1", "system.x");

		assertTrue(providedRulesSatisfiesRequiredRules());
	}

	@Test
	public void testNoProvidedRule() {
		Rule requiredRule = createRequiredRule();
		createRulePart(requiredRule, "rulePart1", "system.x");

		assertFalse(providedRulesSatisfiesRequiredRules());
	}

	@Test
	public void testRequiredRuleHasDifferentKeyThanProvided() {
		Rule providedRule = createProvidedRule();
		createRulePart(providedRule, "rulePart1", "system.x");

		Rule requiredRule = createRequiredRule();
		createRulePart(requiredRule, "rulePart2", "system.x");

		assertFalse(providedRulesSatisfiesRequiredRules());
	}

	@Test
	public void testRequiredRuleHasDifferentValueThanProvided() {
		Rule providedRule = createProvidedRule();
		createRulePart(providedRule, "rulePart1", "system.x");

		Rule requiredRule = createRequiredRule();
		createRulePart(requiredRule, "rulePart1", "systom.x");

		assertFalse(providedRulesSatisfiesRequiredRules());
	}

	@Test
	public void testRequiredRuleHasProvidedWildcard() {
		Rule providedRule = createProvidedRule();
		createRulePart(providedRule, "rulePart1", "system.x*");

		Rule requiredRule = createRequiredRule();
		createRulePart(requiredRule, "rulePart1", "system.xxx.yy");

		assertTrue(providedRulesSatisfiesRequiredRules());
	}

	@Test
	public void testRequiredRuleHasDifferentKeyThanProvidedWildcard() {
		Rule providedRule = createProvidedRule();
		createRulePart(providedRule, "rulePart2", "system.x*");

		Rule requiredRule = createRequiredRule();
		createRulePart(requiredRule, "rulePart1", "system.xxx.yy");

		assertFalse(providedRulesSatisfiesRequiredRules());
	}

	@Test
	public void testRequiredRuleHasDifferentValueThanProvidedWildcard() {
		Rule providedRule = createProvidedRule();
		createRulePart(providedRule, "rulePart1", "system.x*");

		Rule requiredRule = createRequiredRule();
		createRulePart(requiredRule, "rulePart1", "systom.xxx.yy");

		assertFalse(providedRulesSatisfiesRequiredRules());
	}

	@Test
	public void testRequiredRuleHasOneLessPartThanProvided2() {
		Rule providedRule = createProvidedRule();
		createRulePart(providedRule, "rulePart1", "system.x");
		createRulePart(providedRule, "rulePart2", "system.y");

		Rule requiredRule = createRequiredRule();
		createRulePart(requiredRule, "rulePart1", "system.x");

		assertTrue(providedRulesSatisfiesRequiredRules());
	}

	@Test
	public void testRequiredRuleHasOneMorePartThanProvided() {
		Rule providedRule = createProvidedRule();
		createRulePart(providedRule, "rulePart1", "system.x");

		Rule requiredRule = createRequiredRule();
		createRulePart(requiredRule, "rulePart1", "system.x");
		createRulePart(requiredRule, "rulePart2", "system.y");

		assertFalse(providedRulesSatisfiesRequiredRules());
	}

	@Test
	public void testProvidedRuleHasOneMoreValueThanRequired() {
		Rule providedRule = createProvidedRule();
		createRulePart(providedRule, "rulePart1", "system.y", "system.x");

		Rule requiredRule = createRequiredRule();
		createRulePart(requiredRule, "rulePart1", "system.x");

		assertTrue(providedRulesSatisfiesRequiredRules());
	}

	@Test
	public void testRequiredRuleHasOneMoreValueThanProvided() {
		Rule providedRule = createProvidedRule();
		createRulePart(providedRule, "rulePart1", "system.x");

		Rule requiredRule = createRequiredRule();
		createRulePart(requiredRule, "rulePart1", "system.x", "system.y");

		assertFalse(providedRulesSatisfiesRequiredRules());
	}

	@Test
	public void testProvidedHasOneMoreRuleThanRequired() {
		Rule providedRule = createProvidedRule();
		createRulePart(providedRule, "rulePart1", "system.y");
		Rule providedRule2 = createProvidedRule();
		createRulePart(providedRule2, "rulePart1", "system.x");

		Rule requiredRule = createRequiredRule();
		createRulePart(requiredRule, "rulePart1", "system.x");

		assertTrue(providedRulesSatisfiesRequiredRules());
	}

	@Test
	public void testRequiredHasOneMoreRuleThanProvided() {
		Rule providedRule = createProvidedRule();
		createRulePart(providedRule, "rulePart1", "system.y");

		Rule requiredRule = createRequiredRule();
		createRulePart(requiredRule, "rulePart1", "system.x");
		Rule requiredRule2 = createRequiredRule();
		createRulePart(requiredRule2, "rulePart1", "system.y");

		assertTrue(providedRulesSatisfiesRequiredRules());
	}

	@Test
	public void testSeriesExampleNotSatisfied() {
		Rule providedRule = createProvidedRule();
		createRulePart(providedRule, "action", "system.");
		createRulePart(providedRule, "recordType", "system.");
		createRulePart(providedRule, "serie", "system.se.uu.a");

		Rule requiredRule = createRequiredRule();
		createRulePart(requiredRule, "action", "system.update");
		createRulePart(requiredRule, "recordType", "system.book");
		createRulePart(requiredRule, "serie", "system.");

		assertFalse(providedRulesSatisfiesRequiredRules());
	}

	@Test
	public void testSeriesExampleSatisfied() {
		Rule providedRule = createProvidedRule();
		createRulePart(providedRule, "action", "system.*");
		createRulePart(providedRule, "recordType", "system.*");
		createRulePart(providedRule, "serie", "system.se.uu.a");

		Rule requiredRule = createRequiredRule();
		createRulePart(requiredRule, "action", "system.update");
		createRulePart(requiredRule, "recordType", "system.book");
		createRulePart(requiredRule, "serie", "system.se.uu.a");

		assertTrue(providedRulesSatisfiesRequiredRules());
	}

	@Test
	public void testUserIdExampleSatisfiedAdmin() {
		Rule providedRule = createProvidedRule();
		createRulePart(providedRule, "action", "system.*");
		createRulePart(providedRule, "recordType", "system.book");
		createRulePart(providedRule, "userId", "system.*");

		Rule requiredRule = createRequiredRule();
		createRulePart(requiredRule, "action", "system.update");
		createRulePart(requiredRule, "recordType", "system.book");
		createRulePart(requiredRule, "userId", "system.uu.y");

		assertTrue(providedRulesSatisfiesRequiredRules());
	}

	@Test
	public void testUserIdExampleSatisfiedUser() {
		Rule providedRule = createProvidedRule();
		createRulePart(providedRule, "action", "system.*");
		createRulePart(providedRule, "recordType", "system.book");
		createRulePart(providedRule, "userId", "system.uu.x");

		Rule requiredRule = createRequiredRule();
		createRulePart(requiredRule, "action", "system.update");
		createRulePart(requiredRule, "recordType", "system.book");
		createRulePart(requiredRule, "userId", "system.uu.x");

		assertTrue(providedRulesSatisfiesRequiredRules());
	}

	@Test
	public void testUserIdExampleNotSatisfiedUser() {
		Rule providedRule = createProvidedRule();
		createRulePart(providedRule, "action", "system.");
		createRulePart(providedRule, "recordType", "system.book");
		createRulePart(providedRule, "userId", "system.uu.x");

		Rule requiredRule = createRequiredRule();
		createRulePart(requiredRule, "action", "system.update");
		createRulePart(requiredRule, "recordType", "system.book");
		createRulePart(requiredRule, "userId", "system.uu.y");

		assertFalse(providedRulesSatisfiesRequiredRules());
	}

	private boolean providedRulesSatisfiesRequiredRules() {
		return authorizator.providedRulesSatisfiesRequiredRules(providedRules, requiredRules);
	}

	private Rule createRequiredRule() {
		Rule requiredRule = new Rule();
		requiredRules.add(requiredRule);
		return requiredRule;
	}

	private Rule createProvidedRule() {
		Rule providedRule = new Rule();
		providedRules.add(providedRule);
		return providedRule;
	}

	private void createRulePart(Rule providedRule, String key, String... values) {
		RulePartValues set = new RulePartValues();
		for (String value : values) {
			set.add(value);
		}
		providedRule.put(key, set);
	}

}
