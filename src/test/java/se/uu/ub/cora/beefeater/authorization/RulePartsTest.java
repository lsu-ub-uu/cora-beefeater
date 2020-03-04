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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import java.util.Map.Entry;
import java.util.Set;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class RulePartsTest {

	private RulePartsImp ruleParts;
	private RulePartValues values;

	@BeforeMethod
	public void setUp() {
		ruleParts = new RulePartsImp();
		values = new RulePartValuesImp();
		values.add("someValue");
		ruleParts.put("someRulePartId", values);
	}

	@Test
	public void testInit() {
		ruleParts = new RulePartsImp();
		assertEquals(ruleParts.size(), 0);
	}

	@Test
	public void testRuleParts() {
		assertTrue(ruleParts.containsKey("someRulePartId"));
		assertSame(ruleParts.get("someRulePartId"), values);
		assertEquals(ruleParts.size(), 1);
	}

	@Test
	public void testRulePartsKeySet() {
		assertEquals(ruleParts.keySet().size(), 1);
		assertEquals(ruleParts.keySet().iterator().next(), "someRulePartId");
	}

	@Test
	public void testRulePartsEntrySet() {
		assertEquals(ruleParts.entrySet().size(), 1);
		Set<Entry<String, RulePartValues>> entrySet = ruleParts.entrySet();
		Entry<String, RulePartValues> entry = entrySet.iterator().next();
		assertEquals(entry.getKey(), "someRulePartId");
		assertSame(entry.getValue(), values);
	}
}
