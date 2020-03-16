package se.uu.ub.cora.beefeater.authorization;

import java.util.Iterator;
import java.util.stream.Stream;

public interface RulePartValues {

	Stream<String> stream();

	void add(String value);

	int size();

	Iterator<String> iterator();

	boolean contains(String key);

}
