package com.qa.opencart.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelUtil {

	private static String TEST_DATA_SHEET_PATH = ".\\src\\test\\resources\\testdata\\testData.xlsx";
	private static Workbook book;
	private static Sheet sheet;

	public static Object[][] getTestData(String sheetName) {

		Object[][] data = null;

		try {
			FileInputStream ip = new FileInputStream(TEST_DATA_SHEET_PATH);
			book = WorkbookFactory.create(ip);
			sheet = book.getSheet(sheetName);
			data = new Object[sheet.getLastRowNum()][sheet.getRow(0).getLastCellNum()];

			for (int i = 0; i < sheet.getLastRowNum(); i++) {
				for (int j = 0; j < sheet.getRow(0).getLastCellNum(); j++) {
					data[i][j] = sheet.getRow(i + 1).getCell(j).toString();
				}
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}

	public static String[] getColumnData(String sheetName, String columnName) {

		FileInputStream ip;
		String[] colValues = null;
		try {
			ip = new FileInputStream(TEST_DATA_SHEET_PATH);
			book = WorkbookFactory.create(ip);
			sheet = book.getSheet(sheetName);
			colValues = new String[sheet.getRow(0).getLastCellNum()-1];
			for (int i = 0; i < sheet.getRow(0).getLastCellNum(); i++) {
				if (sheet.getRow(0).getCell(i).toString().equals(columnName)) {
					for (int j = 0; j < sheet.getLastRowNum(); j++) {
						colValues[j] = sheet.getRow(j + 1).getCell(i).toString();
					}
				}
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return colValues;
	}

	public static Map<String, String> getProductQuantityMap() {

		Object[][] data = getTestData("placeMultipleItems");

		Map<String, String> map = new LinkedHashMap<String, String>();

		for (int i = 0; i < data.length; i++) {
			map.put((String) data[i][1], (String) data[i][2]);
		}
		return map;
	}

}
