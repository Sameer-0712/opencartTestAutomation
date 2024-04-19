package com.qa.opencart.utils;

public class StringUtil {

	public static double removeSpecialCharacters(String s) {
		String s1 = s.replaceAll("[^0-9.]", "");
		return Double.parseDouble(s1);
	}

}
