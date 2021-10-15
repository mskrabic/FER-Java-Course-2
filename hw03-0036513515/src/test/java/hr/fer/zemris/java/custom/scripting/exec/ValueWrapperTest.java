package hr.fer.zemris.java.custom.scripting.exec;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ValueWrapperTest {
	
	@Test
	public void test1() {
		ValueWrapper v1 = new ValueWrapper(null);
		ValueWrapper v2 = new ValueWrapper(null);
		v1.add(v2.getValue());
		assertTrue(v1.getValue() instanceof Integer);
		assertEquals(((Integer)v1.getValue()).intValue(), 0);
		assertTrue(v2.getValue() == null);
	}

	
	@Test
	public void test2() {
		ValueWrapper v1 = new ValueWrapper("1.2E1");
		ValueWrapper v2 = new ValueWrapper(Integer.valueOf(1));
		v1.add(v2.getValue()); // v3 now stores Double(13); v4 still stores Integer(1).
		assertTrue(v1.getValue() instanceof Double);
		assertEquals(((Double)v1.getValue()).doubleValue(), (double)13);
		assertTrue(v2.getValue() instanceof Integer);
		assertEquals(((Integer)v2.getValue()).intValue(), 1);
		
	}
	
	@Test
	public void test3() {
		ValueWrapper v1 = new ValueWrapper("12");
		ValueWrapper v2 = new ValueWrapper(Integer.valueOf(1));
		v1.add(v2.getValue()); // v5 now stores Integer(13); v6 still stores Integer(1).

		assertTrue(v1.getValue() instanceof Integer);
		assertEquals(((Integer)v1.getValue()).intValue(), 13);
		assertTrue(v2.getValue() instanceof Integer);
		assertEquals(((Integer)v2.getValue()).intValue(), 1);
	}
	
	@Test
	public void test4() {
		ValueWrapper v1 = new ValueWrapper("Ankica");
		ValueWrapper v2 = new ValueWrapper(Integer.valueOf(1));
		assertThrows(RuntimeException.class, () -> v1.add(v2.getValue()));
	}	
}
