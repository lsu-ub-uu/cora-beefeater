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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AuthorizatorTest {
	private List<Map<String, Set<String>>> providedRules;
	private List<Map<String, Set<String>>> requiredRules;
	private Authorizator authorizator;

	@BeforeMethod
	public void setUp() {
		authorizator = new AuthorizatorImp();
		providedRules = new ArrayList<Map<String, Set<String>>>();
		requiredRules = new ArrayList<Map<String, Set<String>>>();

	}

	@Test
	public void testExactMatch() {
		Map<String, Set<String>> providedRule = createProvidedRule();
		createRulePart(providedRule, "action", "system.read");
		createRulePart(providedRule, "organisation", "system.se.uu");
		createRulePart(providedRule, "recordType", "system.book");
		createRulePart(providedRule, "delete", "system.existing");
		createRulePart(providedRule, "publish", "system.notpublished");

		Map<String, Set<String>> requiredRule = createRequiredRule();
		createRulePart(requiredRule, "action", "system.read");
		createRulePart(requiredRule, "organisation", "system.se.uu");
		createRulePart(requiredRule, "recordType", "system.book");
		createRulePart(requiredRule, "delete", "system.existing");
		createRulePart(requiredRule, "publish", "system.notpublished");

		assertTrue(providedRulesSatisfiesRequiredRules());
	}

	@Test
	public void testNoPartsMatch() {
		Map<String, Set<String>> providedRule = createProvidedRule();
		createRulePart(providedRule, "action", "system.read");
		createRulePart(providedRule, "organisation", "system.se.uu");
		createRulePart(providedRule, "recordType", "system.book");
		createRulePart(providedRule, "delete", "system.existing");
		createRulePart(providedRule, "publish", "system.notpublished");

		Map<String, Set<String>> requiredRule = createRequiredRule();
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
		Map<String, Set<String>> providedRule = createProvidedRule();
		createRulePart(providedRule, "rulePart1", "system.x");

		assertTrue(providedRulesSatisfiesRequiredRules());
	}

	@Test
	public void testNoProvidedRule() {
		Map<String, Set<String>> requiredRule = createRequiredRule();
		createRulePart(requiredRule, "rulePart1", "system.x");

		assertFalse(providedRulesSatisfiesRequiredRules());
	}

	@Test
	public void testRequiredRuleHasDifferentKeyThanProvided() {
		Map<String, Set<String>> providedRule = createProvidedRule();
		createRulePart(providedRule, "rulePart1", "system.x");

		Map<String, Set<String>> requiredRule = createRequiredRule();
		createRulePart(requiredRule, "rulePart2", "system.x");

		assertFalse(providedRulesSatisfiesRequiredRules());
	}

	@Test
	public void testRequiredRuleHasOneLessPartThanProvided2() {
		Map<String, Set<String>> providedRule = createProvidedRule();
		createRulePart(providedRule, "rulePart1", "system.x");
		createRulePart(providedRule, "rulePart2", "system.y");

		Map<String, Set<String>> requiredRule = createRequiredRule();
		createRulePart(requiredRule, "rulePart1", "system.x");

		assertTrue(providedRulesSatisfiesRequiredRules());
	}

	@Test
	public void testRequiredRuleHasOneMorePartThanProvided() {
		Map<String, Set<String>> providedRule = createProvidedRule();
		createRulePart(providedRule, "rulePart1", "system.x");

		Map<String, Set<String>> requiredRule = createRequiredRule();
		createRulePart(requiredRule, "rulePart1", "system.x");
		createRulePart(requiredRule, "rulePart2", "system.y");

		assertFalse(providedRulesSatisfiesRequiredRules());
	}

	@Test
	public void testProvidedRuleHasOneMoreValueThanRequired() {
		Map<String, Set<String>> providedRule = createProvidedRule();
		createRulePart(providedRule, "rulePart1", "system.y", "system.x");

		Map<String, Set<String>> requiredRule = createRequiredRule();
		createRulePart(requiredRule, "rulePart1", "system.x");

		assertTrue(providedRulesSatisfiesRequiredRules());
	}

	@Test
	public void testRequiredRuleHasOneMoreValueThanProvided() {
		Map<String, Set<String>> providedRule = createProvidedRule();
		createRulePart(providedRule, "rulePart1", "system.x");

		Map<String, Set<String>> requiredRule = createRequiredRule();
		createRulePart(requiredRule, "rulePart1", "system.x", "system.y");

		assertFalse(providedRulesSatisfiesRequiredRules());
	}

	@Test
	public void testProvidedHasOneMoreRuleThanRequired() {
		Map<String, Set<String>> providedRule = createProvidedRule();
		createRulePart(providedRule, "rulePart1", "system.y");
		Map<String, Set<String>> providedRule2 = createProvidedRule();
		createRulePart(providedRule2, "rulePart1", "system.x");

		Map<String, Set<String>> requiredRule = createRequiredRule();
		createRulePart(requiredRule, "rulePart1", "system.x");

		assertTrue(providedRulesSatisfiesRequiredRules());
	}

	@Test
	public void testRequiredHasOneMoreRuleThanProvided() {
		Map<String, Set<String>> providedRule = createProvidedRule();
		createRulePart(providedRule, "rulePart1", "system.y");

		Map<String, Set<String>> requiredRule = createRequiredRule();
		createRulePart(requiredRule, "rulePart1", "system.x");
		Map<String, Set<String>> requiredRule2 = createRequiredRule();
		createRulePart(requiredRule2, "rulePart1", "system.y");

		assertTrue(providedRulesSatisfiesRequiredRules());
	}

	@Test
	public void testSeriesExampleNotSatisfied() {
		Map<String, Set<String>> providedRule = createProvidedRule();
		createRulePart(providedRule, "action", "system.");
		createRulePart(providedRule, "recordType", "system.");
		createRulePart(providedRule, "serie", "system.se.uu.a");

		Map<String, Set<String>> requiredRule = createRequiredRule();
		createRulePart(requiredRule, "action", "system.update");
		createRulePart(requiredRule, "recordType", "system.book");
		createRulePart(requiredRule, "serie", "system.");

		assertFalse(providedRulesSatisfiesRequiredRules());
	}

	@Test
	public void testSeriesExampleSatisfied() {
		Map<String, Set<String>> providedRule = createProvidedRule();
		createRulePart(providedRule, "action", "system.");
		createRulePart(providedRule, "recordType", "system.");
		createRulePart(providedRule, "serie", "system.se.uu.a");

		Map<String, Set<String>> requiredRule = createRequiredRule();
		createRulePart(requiredRule, "action", "system.update");
		createRulePart(requiredRule, "recordType", "system.book");
		createRulePart(requiredRule, "serie", "system.se.uu.a");

		assertTrue(providedRulesSatisfiesRequiredRules());
	}

	@Test
	public void testUserIdExampleSatisfiedAdmin() {
		Map<String, Set<String>> providedRule = createProvidedRule();
		createRulePart(providedRule, "action", "system.");
		createRulePart(providedRule, "recordType", "system.book");
		createRulePart(providedRule, "userId", "system.");

		Map<String, Set<String>> requiredRule = createRequiredRule();
		createRulePart(requiredRule, "action", "system.update");
		createRulePart(requiredRule, "recordType", "system.book");
		createRulePart(requiredRule, "userId", "system.uu.y");

		assertTrue(providedRulesSatisfiesRequiredRules());
	}

	@Test
	public void testUserIdExampleSatisfiedUser() {
		Map<String, Set<String>> providedRule = createProvidedRule();
		createRulePart(providedRule, "action", "system.");
		createRulePart(providedRule, "recordType", "system.book");
		createRulePart(providedRule, "userId", "system.uu.x");

		Map<String, Set<String>> requiredRule = createRequiredRule();
		createRulePart(requiredRule, "action", "system.update");
		createRulePart(requiredRule, "recordType", "system.book");
		createRulePart(requiredRule, "userId", "system.uu.x");

		assertTrue(providedRulesSatisfiesRequiredRules());
	}

	@Test
	public void testUserIdExampleNotSatisfiedUser() {
		Map<String, Set<String>> providedRule = createProvidedRule();
		createRulePart(providedRule, "action", "system.");
		createRulePart(providedRule, "recordType", "system.book");
		createRulePart(providedRule, "userId", "system.uu.x");

		Map<String, Set<String>> requiredRule = createRequiredRule();
		createRulePart(requiredRule, "action", "system.update");
		createRulePart(requiredRule, "recordType", "system.book");
		createRulePart(requiredRule, "userId", "system.uu.y");

		assertFalse(providedRulesSatisfiesRequiredRules());
	}

	private boolean providedRulesSatisfiesRequiredRules() {
		return authorizator.providedRulesSatisfiesRequiredRules(providedRules, requiredRules);
	}

	private Map<String, Set<String>> createRequiredRule() {
		Map<String, Set<String>> requiredRule = new TreeMap<>();
		requiredRules.add(requiredRule);
		return requiredRule;
	}

	private Map<String, Set<String>> createProvidedRule() {
		Map<String, Set<String>> providedRule = new TreeMap<>();
		providedRules.add(providedRule);
		return providedRule;
	}

	private void createRulePart(Map<String, Set<String>> providedRule, String key,
			String... values) {
		Set<String> set = new HashSet<String>();
		for (String value : values) {
			set.add(value);
		}
		providedRule.put(key, set);
	}

}
