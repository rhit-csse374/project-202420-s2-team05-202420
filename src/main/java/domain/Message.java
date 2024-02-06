package domain;

import java.util.Set;

/**
 * An error, warning, or info message generated by a check.
 */
public final class Message {
	public final MessageLevel level;
	public final String text;
	private final Set<String> classFullNames;

	public Message(MessageLevel level, String text, Set<String> classFullNames) {
		this.level = level;
		this.text = text;
		this.classFullNames = classFullNames;
	}

	public Message(MessageLevel level, String text, String classFullName) {
		this(level, text, Set.of(classFullName));
	}

	public Set<String> getClassFullNames() {
		return Set.copyOf(this.classFullNames);
	}
}
