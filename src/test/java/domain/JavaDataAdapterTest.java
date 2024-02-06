package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import domain.javadata.AccessModifier;
import domain.javadata.ClassData;
import domain.javadata.ClassNodeAdapter;
import domain.javadata.FieldData;
import domain.javadata.MethodData;
import domain.javadata.VariableData;

/**
 * Test that the ASM adapters found in domain.javadata can do at least everything demonstrated in the ASM example code.
 */
public class JavaDataAdapterTest {
	private static final String STRING_RESOURCE_PATH = "java/lang/String.class";

	private byte[] javaBytecode;

	@BeforeEach
	public void setup() throws IOException {
		try (InputStream in = this.getClass().getClassLoader().getResourceAsStream(STRING_RESOURCE_PATH)) {
			this.javaBytecode = in.readAllBytes();
		}
	}

	@Test
	public void testClass() {
		ClassData classData = new ClassNodeAdapter(this.javaBytecode);

		assertEquals("java.lang.String", classData.getFullName());
		assertEquals(AccessModifier.PUBLIC, classData.getAccessModifier());
		assertEquals(false, classData.isAbstract());
		assertEquals(false, classData.isStatic());
		assertEquals(true, classData.isFinal());
		// assertEquals(0, classData.getTypeParamFullNames().size());
		assertEquals("java.lang.Object", classData.getSuperFullName());
		assertEquals(Set.of(
			"java.io.Serializable",
			"java.lang.Comparable",
			"java.lang.CharSequence"
		), classData.getInterfaceFullNames());
		assertEquals(null, classData.getContainingClassFullName());
		assertEquals(Set.of(
			"java.util.Spliterator$OfInt",
			"java.lang.String$CaseInsensitiveComparator",
			"java.lang.StringCoding$Result",
			"java.lang.StringUTF16$CodePointsSpliterator",
			"java.lang.StringLatin1$CharsSpliterator",
			"java.lang.StringUTF16$CharsSpliterator"
		), classData.getInnerClassFullNames());
	}

	@Test
	public void testFields() {
		ClassData classData = new ClassNodeAdapter(this.javaBytecode);
		Set<FieldData> fields = classData.getFields();
		FieldData valueField = findField(fields, "value");

		assertEquals("value", valueField.getName());
		assertEquals("byte[]", valueField.getTypeFullName());
		assertEquals(AccessModifier.PRIVATE, valueField.getAccessModifier());
		assertEquals(false, valueField.isStatic());
		assertEquals(true, valueField.isFinal());
	}

	private FieldData findField(Set<FieldData> fields, String name) {
		for (FieldData field : fields) {
			if (field.getName().equals(name)) {
				return field;
			}
		}
		fail("Field not found: " + name);
		return null;
	}

	@Test
	public void testMethods() {
		ClassData classData = new ClassNodeAdapter(this.javaBytecode);
		Set<MethodData> methods = classData.getMethods();
		MethodData rangeCheckMethod = findMethod(methods, "rangeCheck");

		assertEquals("rangeCheck", rangeCheckMethod.getName());
		assertEquals("java.lang.Void", rangeCheckMethod.getReturnTypeFullName());
		assertEquals(AccessModifier.PRIVATE, rangeCheckMethod.getAccessModifier());
		assertEquals(true, rangeCheckMethod.isStatic());
		assertEquals(false, rangeCheckMethod.isFinal());
		assertEquals(false, rangeCheckMethod.isAbstract());
		List<VariableData> paramList = List.of(
			new VariableData("value", "char[]"),
			new VariableData("offset", "int"),
			new VariableData("count", "int")
		);
		assertEquals(paramList, rangeCheckMethod.getParams());
		assertEquals(Set.of(), rangeCheckMethod.getExceptionTypeFullNames());
		assertEquals(Set.copyOf(paramList), rangeCheckMethod.getLocalVariables());
	}

	private MethodData findMethod(Set<MethodData> methods, String name) {
		for (MethodData method : methods) {
			if (method.getName().equals(name)) {
				return method;
			}
		}
		fail("Method not found: " + name);
		return null;
	}

	@Test
	@Disabled("NYI") // TODO: write this test
	public void testInstructions() {
		ClassData classData = new ClassNodeAdapter(this.javaBytecode);
	}
}
