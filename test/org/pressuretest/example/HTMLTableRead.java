package org.pressuretest.example;

import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.concurrent.TimeUnit;

//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.openqa.selenium.By;
//import org.openqa.selenium.JavascriptExecutor;
//import org.openqa.selenium.Keys;
//import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.edge.EdgeDriver;
//import org.openqa.selenium.firefox.FirefoxDriver;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.*;

//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.internal.TextListener;
//import org.junit.runner.JUnitCore;
//import org.junit.runner.Result;

public class HTMLTableRead {

	public String ExtractPressureData(WebDriver driver) {

		Integer timeColumnIndex = 0;
		String pressureAtGivenTime = "Not Set";

		// Using JSoup HTMLParser, get the page and locate the weather
		// table
		Document doc;

		try {
			doc = Jsoup.connect(driver.getCurrentUrl() + "?day=0").get();

			Elements tableElements = doc.select("table.weather");

			//String timeToFind = Const.TIMETOFIND; // 21:00 - 9pm (Gives
													// flexibility
													// on
													// the required time)

			// Loop through the header to find the desired time
			// and record its column position
			Elements tableHeaderElements = tableElements.select("thead tr th");

			for (int i = 0; i < tableHeaderElements.size(); i++) {
				if (tableHeaderElements.get(i).text().startsWith(Const.TIMETOFIND)) {
					timeColumnIndex = i - 1; // Take of 1 as the header text
												// is included in the count
				}
			}

			// Loop through the rest of the table rows until the Pressure
			// row is found,
			// then loop through the pressure data to find the value in the
			// same
			// column as the desired time.
			Elements tableRowElements = tableElements.select(":not(thead) tr");

			// row loop
			for (int i = 0; i < tableRowElements.size(); i++) {
				Element row = tableRowElements.get(i);

				if (row.text().startsWith("Pressure")) {
					// Loop through the pressure data
					// Need to take the value from the same column as the
					// desired time
					Elements rowItems = row.select("td");
					for (int j = 0; j < rowItems.size(); j++) {
						if (j == timeColumnIndex) {
							// Record this pressure
							pressureAtGivenTime = rowItems.get(j).text();
						}
					}
				}
			}
			
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return pressureAtGivenTime;
	}

}
