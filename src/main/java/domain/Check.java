package domain;

import java.util.Map;
import java.util.Set;

import datasource.Configuration;
import domain.javadata.ClassData;

/**
 * A linter check (be it a cursory style check, principle check, or pattern detector).
 */
public interface Check {
	Set<Message> check(Map<String, ClassData> classes, Configuration config);
}
