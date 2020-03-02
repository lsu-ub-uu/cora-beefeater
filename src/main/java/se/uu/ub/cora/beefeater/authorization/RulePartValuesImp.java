/*
 * Copyright 2018 Uppsala University Library
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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Stream;

public class RulePartValuesImp implements RulePartValues {

	Set<String> rulePartValues = new HashSet<>();

	@Override
	public Stream<String> stream() {
		return rulePartValues.stream();
	}

	@Override
	public void add(String value) {
		rulePartValues.add(value);
	}

	@Override
	public int size() {
		return rulePartValues.size();
	}

	@Override
	public Iterator<String> iterator() {
		return rulePartValues.iterator();
	}

	@Override
	public boolean contains(String key) {
		return rulePartValues.contains(key);
	}

}
