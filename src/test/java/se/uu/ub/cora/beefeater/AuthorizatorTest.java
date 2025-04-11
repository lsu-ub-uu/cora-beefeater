/*
 * Copyright 2015, 2016, 2025 Uppsala University Library
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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.beefeater.authentication.User;
import se.uu.ub.cora.beefeater.authorization.Rule;
import se.uu.ub.cora.beefeater.authorization.RuleImp;
import se.uu.ub.cora.beefeater.authorization.RulePartValues;
import se.uu.ub.cora.beefeater.authorization.RulePartValuesImp;

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
		RuleImp providedRule = createProvidedRule();
		createRulePart(providedRule, "action", "system.read");
		createRulePart(providedRule, "organisation", "system.se.uu");
		createRulePart(providedRule, "recordType", "system.book");
		createRulePart(providedRule, "delete", "system.existing");
		createRulePart(providedRule, "publish", "system.notpublished");

		RuleImp requiredRule = createRequiredRule();
		createRulePart(requiredRule, "action", "system.read");
		createRulePart(requiredRule, "organisation", "system.se.uu");
		createRulePart(requiredRule, "recordType", "system.book");
		createRulePart(requiredRule, "delete", "system.existing");
		createRulePart(requiredRule, "publish", "system.notpublished");

		assertTrue(providedRulesSatisfiesRequiredRules());
		List<Rule> matchedRules = providedRulesMatchRequiredRules();
		assertEquals(matchedRules.size(), 1);
	}

	@Test
	public void testNoPartsMatch() {
		RuleImp providedRule = createProvidedRule();
		createRulePart(providedRule, "action", "system.read");
		createRulePart(providedRule, "organisation", "system.se.uu");
		createRulePart(providedRule, "recordType", "system.book");
		createRulePart(providedRule, "delete", "system.existing");
		createRulePart(providedRule, "publish", "system.notpublished");

		RuleImp requiredRule = createRequiredRule();
		createRulePart(requiredRule, "action", "system.create");
		createRulePart(requiredRule, "organisation", "system.no");
		createRulePart(requiredRule, "recordType", "system.person");
		createRulePart(requiredRule, "delete", "system.notexisting");
		createRulePart(requiredRule, "publish", "system.published");

		assertFalse(providedRulesSatisfiesRequiredRules());
		List<Rule> matchedRules = providedRulesMatchRequiredRules();
		assertEquals(matchedRules.size(), 0);
	}

	@Test
	public void testNoRules() {
		assertTrue(providedRulesSatisfiesRequiredRules());
		List<Rule> matchedRules = providedRulesMatchRequiredRules();
		assertEquals(matchedRules.size(), 0);
	}

	@Test
	public void testNoRequiredRule() {
		RuleImp providedRule = createProvidedRule();
		createRulePart(providedRule, "rulePart1", "system.x");

		assertTrue(providedRulesSatisfiesRequiredRules());
		List<Rule> matchedRules = providedRulesMatchRequiredRules();
		assertEquals(matchedRules.size(), 0);
	}

	@Test
	public void testNoProvidedRule() {
		RuleImp requiredRule = createRequiredRule();
		createRulePart(requiredRule, "rulePart1", "system.x");

		assertFalse(providedRulesSatisfiesRequiredRules());
		List<Rule> matchedRules = providedRulesMatchRequiredRules();
		assertEquals(matchedRules.size(), 0);
	}

	@Test
	public void testRequiredRuleHasDifferentKeyThanProvided() {
		RuleImp providedRule = createProvidedRule();
		createRulePart(providedRule, "rulePart1", "system.x");

		RuleImp requiredRule = createRequiredRule();
		createRulePart(requiredRule, "rulePart2", "system.x");

		assertFalse(providedRulesSatisfiesRequiredRules());
		List<Rule> matchedRules = providedRulesMatchRequiredRules();
		assertEquals(matchedRules.size(), 0);
	}

	@Test
	public void testRequiredRuleHasDifferentValueThanProvided() {
		RuleImp providedRule = createProvidedRule();
		createRulePart(providedRule, "rulePart1", "system.x");

		RuleImp requiredRule = createRequiredRule();
		createRulePart(requiredRule, "rulePart1", "systom.x");

		assertFalse(providedRulesSatisfiesRequiredRules());
		List<Rule> matchedRules = providedRulesMatchRequiredRules();
		assertEquals(matchedRules.size(), 0);
	}

	@Test
	public void testRequiredRuleHasProvidedWildcard() {
		RuleImp providedRule = createProvidedRule();
		createRulePart(providedRule, "rulePart1", "system.x*");

		RuleImp requiredRule = createRequiredRule();
		createRulePart(requiredRule, "rulePart1", "system.xxx.yy");

		assertTrue(providedRulesSatisfiesRequiredRules());
		List<Rule> matchedRules = providedRulesMatchRequiredRules();
		assertEquals(matchedRules.size(), 1);
	}

	@Test
	public void testRequiredRuleHasDifferentKeyThanProvidedWildcard() {
		RuleImp providedRule = createProvidedRule();
		createRulePart(providedRule, "rulePart2", "system.x*");

		RuleImp requiredRule = createRequiredRule();
		createRulePart(requiredRule, "rulePart1", "system.xxx.yy");

		assertFalse(providedRulesSatisfiesRequiredRules());
		List<Rule> matchedRules = providedRulesMatchRequiredRules();
		assertEquals(matchedRules.size(), 0);
	}

	@Test
	public void testRequiredRuleHasDifferentValueThanProvidedWildcard() {
		RuleImp providedRule = createProvidedRule();
		createRulePart(providedRule, "rulePart1", "system.x*");

		RuleImp requiredRule = createRequiredRule();
		createRulePart(requiredRule, "rulePart1", "systom.xxx.yy");

		assertFalse(providedRulesSatisfiesRequiredRules());
		List<Rule> matchedRules = providedRulesMatchRequiredRules();
		assertEquals(matchedRules.size(), 0);
	}

	@Test
	public void testRequiredRuleHasOneLessPartThanProvided2() {
		RuleImp providedRule = createProvidedRule();
		createRulePart(providedRule, "rulePart1", "system.x");
		createRulePart(providedRule, "rulePart2", "system.y");

		RuleImp requiredRule = createRequiredRule();
		createRulePart(requiredRule, "rulePart1", "system.x");

		assertTrue(providedRulesSatisfiesRequiredRules());
		List<Rule> matchedRules = providedRulesMatchRequiredRules();
		assertEquals(matchedRules.size(), 1);
	}

	@Test
	public void testRequiredRuleHasOneMorePartThanProvided() {
		RuleImp providedRule = createProvidedRule();
		createRulePart(providedRule, "rulePart1", "system.x");

		RuleImp requiredRule = createRequiredRule();
		createRulePart(requiredRule, "rulePart1", "system.x");
		createRulePart(requiredRule, "rulePart2", "system.y");

		assertFalse(providedRulesSatisfiesRequiredRules());
		List<Rule> matchedRules = providedRulesMatchRequiredRules();
		assertEquals(matchedRules.size(), 0);
	}

	@Test
	public void testProvidedRuleHasOneMoreValueThanRequired() {
		RuleImp providedRule = createProvidedRule();
		createRulePart(providedRule, "rulePart1", "system.y", "system.x");

		RuleImp requiredRule = createRequiredRule();
		createRulePart(requiredRule, "rulePart1", "system.x");

		assertTrue(providedRulesSatisfiesRequiredRules());
		List<Rule> matchedRules = providedRulesMatchRequiredRules();
		assertEquals(matchedRules.size(), 1);
	}

	@Test
	public void testRequiredRuleHasOneMoreValueThanProvided() {
		RuleImp providedRule = createProvidedRule();
		createRulePart(providedRule, "rulePart1", "system.x");

		RuleImp requiredRule = createRequiredRule();
		createRulePart(requiredRule, "rulePart1", "system.x", "system.y");

		assertFalse(providedRulesSatisfiesRequiredRules());
		List<Rule> matchedRules = providedRulesMatchRequiredRules();
		assertEquals(matchedRules.size(), 0);
	}

	@Test
	public void testProvidedHasOneMoreRuleThanRequired() {
		RuleImp providedRule = createProvidedRule();
		createRulePart(providedRule, "rulePart1", "system.y");
		RuleImp providedRule2 = createProvidedRule();
		createRulePart(providedRule2, "rulePart1", "system.x");

		RuleImp requiredRule = createRequiredRule();
		createRulePart(requiredRule, "rulePart1", "system.x");

		assertTrue(providedRulesSatisfiesRequiredRules());
		List<Rule> matchedRules = providedRulesMatchRequiredRules();
		assertEquals(matchedRules.size(), 1);
	}

	@Test
	public void testRequiredHasOneMoreRuleThanProvided() {
		RuleImp providedRule = createProvidedRule();
		createRulePart(providedRule, "rulePart1", "system.y");

		RuleImp requiredRule = createRequiredRule();
		createRulePart(requiredRule, "rulePart1", "system.x");
		RuleImp requiredRule2 = createRequiredRule();
		createRulePart(requiredRule2, "rulePart1", "system.y");

		assertTrue(providedRulesSatisfiesRequiredRules());
		List<Rule> matchedRules = providedRulesMatchRequiredRules();
		assertEquals(matchedRules.size(), 1);
	}

	@Test
	public void testSeriesExampleNotSatisfied() {
		RuleImp providedRule = createProvidedRule();
		createRulePart(providedRule, "action", "system.");
		createRulePart(providedRule, "recordType", "system.");
		createRulePart(providedRule, "serie", "system.se.uu.a");

		RuleImp requiredRule = createRequiredRule();
		createRulePart(requiredRule, "action", "system.update");
		createRulePart(requiredRule, "recordType", "system.book");
		createRulePart(requiredRule, "serie", "system.");

		assertFalse(providedRulesSatisfiesRequiredRules());
		List<Rule> matchedRules = providedRulesMatchRequiredRules();
		assertEquals(matchedRules.size(), 0);
	}

	@Test
	public void testSeriesExampleSatisfied() {
		RuleImp providedRule = createProvidedRule();
		createRulePart(providedRule, "action", "system.*");
		createRulePart(providedRule, "recordType", "system.*");
		createRulePart(providedRule, "serie", "system.se.uu.a");

		RuleImp requiredRule = createRequiredRule();
		createRulePart(requiredRule, "action", "system.update");
		createRulePart(requiredRule, "recordType", "system.book");
		createRulePart(requiredRule, "serie", "system.se.uu.a");

		assertTrue(providedRulesSatisfiesRequiredRules());
		List<Rule> matchedRules = providedRulesMatchRequiredRules();
		assertEquals(matchedRules.size(), 1);
	}

	@Test
	public void testUserIdExampleSatisfiedAdmin() {
		RuleImp providedRule = createProvidedRule();
		createRulePart(providedRule, "action", "system.*");
		createRulePart(providedRule, "recordType", "system.book");
		createRulePart(providedRule, "userId", "system.*");

		RuleImp requiredRule = createRequiredRule();
		createRulePart(requiredRule, "action", "system.update");
		createRulePart(requiredRule, "recordType", "system.book");
		createRulePart(requiredRule, "userId", "system.uu.y");

		assertTrue(providedRulesSatisfiesRequiredRules());
		List<Rule> matchedRules = providedRulesMatchRequiredRules();
		assertEquals(matchedRules.size(), 1);
	}

	@Test
	public void testUserIdExampleSatisfiedUser() {
		RuleImp providedRule = createProvidedRule();
		createRulePart(providedRule, "action", "system.*");
		createRulePart(providedRule, "recordType", "system.book");
		createRulePart(providedRule, "userId", "system.uu.x");

		RuleImp requiredRule = createRequiredRule();
		createRulePart(requiredRule, "action", "system.update");
		createRulePart(requiredRule, "recordType", "system.book");
		createRulePart(requiredRule, "userId", "system.uu.x");

		assertTrue(providedRulesSatisfiesRequiredRules());
		List<Rule> matchedRules = providedRulesMatchRequiredRules();
		assertEquals(matchedRules.size(), 1);
	}

	@Test
	public void testUserIdExampleNotSatisfiedUser() {
		RuleImp providedRule = createProvidedRule();
		createRulePart(providedRule, "action", "system.");
		createRulePart(providedRule, "recordType", "system.book");
		createRulePart(providedRule, "userId", "system.uu.x");

		RuleImp requiredRule = createRequiredRule();
		createRulePart(requiredRule, "action", "system.update");
		createRulePart(requiredRule, "recordType", "system.book");
		createRulePart(requiredRule, "userId", "system.uu.y");

		assertFalse(providedRulesSatisfiesRequiredRules());
		List<Rule> matchedRules = providedRulesMatchRequiredRules();
		assertEquals(matchedRules.size(), 0);
	}

	@Test
	public void testProvidedHasOneMoreRuleThanRequiredBothMatch() {
		RuleImp providedRule = createProvidedRule();
		createRulePart(providedRule, "rulePart1", "system.x");
		RuleImp providedRule2 = createProvidedRule();
		createRulePart(providedRule2, "rulePart1", "system.x");

		RuleImp requiredRule = createRequiredRule();
		createRulePart(requiredRule, "rulePart1", "system.x");

		assertTrue(providedRulesSatisfiesRequiredRules());
		List<Rule> matchedRules = providedRulesMatchRequiredRules();
		assertEquals(matchedRules.size(), 2);
	}

	@Test
	public void testProvidedHasTwoMoreRuleThanRequiredTwoMatch() {
		RuleImp providedRule = createProvidedRule();
		createRulePart(providedRule, "rulePart1", "system.x");
		RuleImp providedRule2 = createProvidedRule();
		createRulePart(providedRule2, "rulePart1", "system.x");
		RuleImp providedRule3 = createProvidedRule();
		createRulePart(providedRule3, "rulePart1", "system.y");

		RuleImp requiredRule = createRequiredRule();
		createRulePart(requiredRule, "rulePart1", "system.x");

		assertTrue(providedRulesSatisfiesRequiredRules());
		List<Rule> matchedRules = providedRulesMatchRequiredRules();
		assertEquals(matchedRules.size(), 2);
		assertTrue(matchedRules.contains(providedRule));
		assertTrue(matchedRules.contains(providedRule2));
	}

	@Test
	public void testProvidedRuleHasTwoMoreRulesThanRequiredOneMoreValueThanRequiredTwoMatch() {
		RuleImp providedRule = createProvidedRule();
		createRulePart(providedRule, "rulePart1", "system.y", "system.x");
		RuleImp providedRule2 = createProvidedRule();
		createRulePart(providedRule2, "rulePart1", "system.n", "system.n");
		RuleImp providedRule3 = createProvidedRule();
		createRulePart(providedRule3, "rulePart1", "system.z", "system.x");

		RuleImp requiredRule = createRequiredRule();
		createRulePart(requiredRule, "rulePart1", "system.x");

		assertTrue(providedRulesSatisfiesRequiredRules());
		List<Rule> matchedRules = providedRulesMatchRequiredRules();
		assertEquals(matchedRules.size(), 2);
		assertTrue(matchedRules.contains(providedRule));
		assertTrue(matchedRules.contains(providedRule3));
	}

	@Test
	public void testProvidedRuleHasTwoMoreRulesThanRequiredOneMoreValueThanRequiredTwoMatch2() {
		RuleImp providedRule = createProvidedRule();
		createRulePart(providedRule, "rulePart1", "system.y", "system.x");
		RuleImp providedRule2 = createProvidedRule();
		createRulePart(providedRule2, "rulePart1", "system.n", "system.n");
		RuleImp providedRule3 = createProvidedRule();
		createRulePart(providedRule3, "rulePart1", "system.z", "system.x");
		createRulePart(providedRule3, "recordType", "system.z", "system.x");

		RuleImp requiredRule = createRequiredRule();
		createRulePart(requiredRule, "rulePart1", "system.x");
		createRulePart(requiredRule, "recordType", "system.z", "system.x");

		assertTrue(providedRulesSatisfiesRequiredRules());
		List<Rule> matchedRules = providedRulesMatchRequiredRules();
		assertEquals(matchedRules.size(), 1);
		assertTrue(matchedRules.contains(providedRule3));
	}

	@Test
	public void testGetUserIsAuthorizedForPemissionUnit_NotMatchingPermissions() {
		User someUser = createUserWithPermissionUnits("permissionUnit001", "permissionUnit002");

		boolean authorized = authorizator.getUserIsAuthorizedForPemissionUnit(someUser,
				"permissionUnit003");

		assertFalse(authorized);
	}

	@Test
	public void testGetUserIsAuthorizedForPemissionUnit_MatchingPermissions() {
		User someUser = createUserWithPermissionUnits("permissionUnit001", "permissionUnit002");

		boolean authorized = authorizator.getUserIsAuthorizedForPemissionUnit(someUser,
				"permissionUnit002");

		assertTrue(authorized);
	}

	private boolean providedRulesSatisfiesRequiredRules() {
		return authorizator.providedRulesSatisfiesRequiredRules(providedRules, requiredRules);
	}

	private List<Rule> providedRulesMatchRequiredRules() {
		return authorizator.providedRulesMatchRequiredRules(providedRules, requiredRules);
	}

	private RuleImp createRequiredRule() {
		RuleImp requiredRule = new RuleImp();
		requiredRules.add(requiredRule);
		return requiredRule;
	}

	private RuleImp createProvidedRule() {
		RuleImp providedRule = new RuleImp();
		providedRules.add(providedRule);
		return providedRule;
	}

	private void createRulePart(Rule providedRule, String key, String... values) {
		RulePartValues set = new RulePartValuesImp();
		for (String value : values) {
			set.add(value);
		}
		providedRule.addRulePart(key, set);
	}

	private User createUserWithPermissionUnits(String... permissionUnits) {
		User someUser = new User("someId");
		for (String permissionUnit : permissionUnits) {
			someUser.permissionUnitIds.add(permissionUnit);
		}
		return someUser;
	}
}
