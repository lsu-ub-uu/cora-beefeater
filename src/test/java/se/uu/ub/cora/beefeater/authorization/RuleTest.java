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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import java.util.Map.Entry;
import java.util.Set;

import org.testng.annotations.Test;

public class RuleTest {

	@Test
	public void testInit() {
		RuleImp rule = new RuleImp();
		assertTrue(rule.getReadRecordPartPermissions().isEmpty());
		assertTrue(rule.getWriteRecordPartPermissions().isEmpty());
		assertEquals(rule.getNumberOfRuleParts(), 0);
	}

	@Test
	public void testRulePart() {
		RuleImp rule = new RuleImp();
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

	private void assertCorrectEntrySet(RuleImp rule, RulePartValuesImp rulePartValue) {
		Set<Entry<String, RulePartValues>> entrySet = rule.entrySet();
		assertEquals(entrySet.size(), 1);
		Entry<String, RulePartValues> firstEntry = entrySet.iterator().next();
		assertEquals(firstEntry.getKey(), "someRulePart");
		assertSame(firstEntry.getValue(), rulePartValue);
	}

	@Test
	public void testReadRecordPartPermissions() {
		RuleImp rule = new RuleImp();
		rule.addReadRecordPartPermission("someReadPermission");
		rule.addReadRecordPartPermission("someSecondReadPermission");
		assertEquals(rule.getReadRecordPartPermissions().size(), 2);
		assertEquals(rule.getReadRecordPartPermissions().get(0), "someReadPermission");
		assertEquals(rule.getReadRecordPartPermissions().get(1), "someSecondReadPermission");
	}

	@Test
	public void testWriteRecordPartPermissions() {
		RuleImp rule = new RuleImp();
		rule.addWriteRecordPartPermission("someWritePermission");
		rule.addWriteRecordPartPermission("someSecondWritePermission");
		assertEquals(rule.getWriteRecordPartPermissions().size(), 2);
		assertEquals(rule.getWriteRecordPartPermissions().get(0), "someWritePermission");
		assertEquals(rule.getWriteRecordPartPermissions().get(1), "someSecondWritePermission");
	}

}
