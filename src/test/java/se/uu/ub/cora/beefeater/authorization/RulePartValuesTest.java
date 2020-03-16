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
import static org.testng.Assert.assertTrue;

import java.util.stream.Stream;

import org.testng.annotations.Test;

public class RulePartValuesTest {

	@Test
	public void testInit() {
		RulePartValues rulePartValues = new RulePartValuesImp();
		assertEquals(rulePartValues.size(), 0);
	}

	@Test
	public void testRulePartValues() {
		RulePartValues rulePartValues = new RulePartValuesImp();
		rulePartValues.add("someRulePartValue");

		assertEquals(rulePartValues.size(), 1);
		assertTrue(rulePartValues.contains("someRulePartValue"));
		assertEquals(rulePartValues.iterator().next(), "someRulePartValue");

		Stream<String> stream = rulePartValues.stream();
		assertEquals(stream.count(), 1);

	}

}
