package com.qa.opencart.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

public class CSVUtils {

    private static final String CSV_PATH = "./src/test/resources/testdata/";

    private static List<String[]> rows;

    private static List<String[]> dataList;

    private static List<String[]> csvData(String csvName) {
        String csvFile = CSV_PATH + csvName + ".csv";

        CSVReader reader;
        try {
            reader = new CSVReader(new FileReader(csvFile));
            rows = reader.readAll();
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvException e) {
            e.printStackTrace();
        }

        return rows;
    }

    public static String[] getBillingAddress(){
        csvData("Address");
        return rows.get(0);
    }

    public static String[] getDeliveryAddress(){
        csvData("Address");
        return rows.get(1);
    }

    public static String[] getCountryRegionPinData(String country){
        dataList = csvData("CountryRegionPin");
        String[] countryData = null;
        for(String[] data:dataList){
            if(data[0].equals(country)){
                countryData = data;
                break;
            }
        }
        return countryData;
    }

}
