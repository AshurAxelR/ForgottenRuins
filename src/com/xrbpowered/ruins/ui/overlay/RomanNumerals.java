package com.xrbpowered.ruins.ui.overlay;

import java.util.TreeMap;

public class RomanNumerals {

	private final static TreeMap<Integer, String> map = new TreeMap<Integer, String>();
	static {
		map.put(1000, "M");
		map.put(900, "CM");
		map.put(500, "D");
		map.put(400, "CD");
		map.put(100, "C");
		map.put(90, "XC");
		map.put(50, "L");
		map.put(40, "XL");
		map.put(10, "X");
		map.put(9, "IX");
		map.put(5, "V");
		map.put(4, "IV");
		map.put(1, "I");
	}

	private static StringBuilder toRoman(StringBuilder sb, int number) {
		int x = map.floorKey(number);
		sb.append(map.get(x));
		return (number!=x) ? toRoman(sb, number-x) : sb;
	}
	
	public static String toRoman(int number) {
		return toRoman(new StringBuilder(), number).toString();
	}

	public static void main(String[] args) {
		for(int i=1; i<100; i++)
			System.out.println(toRoman(i));
	}

}
