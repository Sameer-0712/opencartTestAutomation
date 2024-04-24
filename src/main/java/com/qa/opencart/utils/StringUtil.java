package com.qa.opencart.utils;

public class StringUtil {

	public static double removeSpecialCharacters(String s) {
		String s1 = s.replaceAll("[^0-9.]", "");
		return Double.parseDouble(s1);
	}

	public static boolean isElementPresentInArray(String s, String[] arr) {

		boolean flag = false;

		for (String s1 : arr) {
			if (s1.equals(s)) {
				flag = true;
				break;
			}
		}
		return flag;
	}
	
	public static int getSumFromStringArray(String[] arr) {		
		int sum = 0;
		for(String s:arr) {
			sum = sum + Integer.parseInt(s);
		}
		return sum;
	}
}
