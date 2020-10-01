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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import java.util.Map.Entry;
import java.util.Set;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class RuleTest {

	private Rule rule;

	@BeforeMethod
	public void beforeMethod() {
		rule = new RuleImp();
	}

	@Test
	public void testInit() {
		assertTrue(rule.getReadRecordPartPermissions().isEmpty());
		assertTrue(rule.getWriteRecordPartPermissions().isEmpty());
		assertEquals(rule.getNumberOfRuleParts(), 0);
	}

	@Test
	public void testRulePart() {
		RulePartValuesImp rulePartValue = new RulePartValuesImp();
		rulePartValue.add("someRulePartValue");
		rule.addRulePart("someRulePart", rulePartValue);

		assertEquals(rule.getNumberOfRuleParts(), 1);
		assertTrue(rule.containsRulePart("someRulePart"));
		assertFalse(rule.containsRulePart("someNOTEXISTINGRulePart"));
		assertSame(rule.getRulePartValuesForKey("someRulePart"), rulePartValue);
		assertTrue(rule.keySet().contains("someRulePart"));
		assertEquals(rule.keySet().size(), 1);
		assertCorrectEntrySet(rule, rulePartValue);
	}

	private void assertCorrectEntrySet(Rule rule, RulePartValuesImp rulePartValue) {
		Set<Entry<String, RulePartValues>> entrySet = rule.entrySet();
		assertEquals(entrySet.size(), 1);
		Entry<String, RulePartValues> firstEntry = entrySet.iterator().next();
		assertEquals(firstEntry.getKey(), "someRulePart");
		assertSame(firstEntry.getValue(), rulePartValue);
	}

	@Test
	public void testAddIfKeyIsAbsent() throws Exception {
		RulePartValues rulePartValues = new RulePartValuesImp();
		String key = "someKey";
		rule.addRulePartIfKeyIsAbsent(key, rulePartValues);

		RulePartValues otherRulePartValues = new RulePartValuesImp();
		rule.addRulePartIfKeyIsAbsent(key, otherRulePartValues);

		assertRuleHasSameRulePartValuesForKey(rulePartValues, key);
	}

	private void assertRuleHasSameRulePartValuesForKey(RulePartValues rulePartValues, String key) {
		assertEquals(rule.getNumberOfRuleParts(), 1);
		assertTrue(rule.containsRulePart(key));
		assertSame(rule.getRulePartValuesForKey(key), rulePartValues);
	}

	@Test
	public void testAddIfKeyIsAbsentAfterAdd() throws Exception {
		RulePartValues rulePartValues = new RulePartValuesImp();
		String key = "someKey";

		rule.addRulePart(key, rulePartValues);

		RulePartValues otherRulePartValues = new RulePartValuesImp();
		rule.addRulePartIfKeyIsAbsent(key, otherRulePartValues);

		assertRuleHasSameRulePartValuesForKey(rulePartValues, key);
	}

	@Test
	public void testReadRecordPartPermissions() {
		rule.addReadRecordPartPermission("someReadPermission");
		rule.addReadRecordPartPermission("someSecondReadPermission");
		assertEquals(rule.getReadRecordPartPermissions().size(), 2);
		assertEquals(rule.getReadRecordPartPermissions().get(0), "someReadPermission");
		assertEquals(rule.getReadRecordPartPermissions().get(1), "someSecondReadPermission");
	}

	@Test
	public void testWriteRecordPartPermissions() {
		rule.addWriteRecordPartPermission("someWritePermission");
		rule.addWriteRecordPartPermission("someSecondWritePermission");
		assertEquals(rule.getWriteRecordPartPermissions().size(), 2);
		assertEquals(rule.getWriteRecordPartPermissions().get(0), "someWritePermission");
		assertEquals(rule.getWriteRecordPartPermissions().get(1), "someSecondWritePermission");
	}

}
