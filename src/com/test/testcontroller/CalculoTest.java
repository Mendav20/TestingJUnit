package com.test.testcontroller;

import static org.junit.Assert.*;

import org.junit.Test;

import com.test.model.calculo;

public class CalculoTest {

	@Test
	public final void testFindMax() {
	assertEquals(4, calculo.findMax(new int[] {1,3,4,2})); // assertEquals sirve como comparador, en donde compara el numero 4 con el arreglo definido
	assertEquals(-1, calculo.findMax(new int[] {-12,-1,-3,-4,-2}));
	}

}
