package org.pressuretest.example;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

public class PressureTestForEdge {
	private WebDriver driver;
	private String baseUrl;
	private StringBuffer verificationErrors = new StringBuffer();

	// Helper methods
	Helpers helperMethods = new Helpers();

	@Before
	public void setUp() throws Exception {

		System.setProperty("webdriver.edge.driver",	Const.EDGEDRIVERLOCATION);

		driver = new EdgeDriver();

		baseUrl = "http://www.bbc.co.uk/weather/";
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
	}

	@Test
	public void testWeatherPageForEdge() throws Exception {
		String errorMessage = "No error";
		boolean pageIsReady = false;

		// Load the page
		driver.get(baseUrl);

		// wait for the page to be ready or timeout
		pageIsReady = helperMethods.checkPageIsReady(driver);

		if (pageIsReady && isElementPresent(By.id("locator-form-search"))) {

			driver.findElement(By.id("locator-form-search")).clear();
			driver.findElement(By.id("locator-form-search")).sendKeys("Reading, Reading");
			driver.findElement(By.id("locator-form-search")).sendKeys(Keys.RETURN);

			pageIsReady = helperMethods.checkPageIsReady(driver);
			if (pageIsReady && isElementPresent(By.id("detail-table-view"))) {

				// click on the table view
				driver.findElement(By.id("detail-table-view")).click();

				//
				// extract pressure for 21:00 from the table for today
				// and then for 21:00 tomorrow
				//

				// Using JSoup HTMLParser, get the page and locate the weather
				// table
				Document doc = Jsoup.connect(driver.getCurrentUrl() + "?day=0").get();

				Elements tableElements = doc.select("table.weather");

				String timeToFind = Const.TIMETOFIND; 	// 21:00 - 9pm (Gives flexibility on
														// the required time)
				Integer timeColumnIndex = 0;
				String pressureAtGivenTime = "Not Set";

				// Loop through the header to find the desired time
				// and record its column position
				Elements tableHeaderElements = tableElements.select("thead tr th");

				for (int i = 0; i < tableHeaderElements.size(); i++) {
					if (tableHeaderElements.get(i).text().startsWith(timeToFind)) {
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

				//
				// Move to next day
				//
				driver.get((driver.getCurrentUrl() + "?day=1"));

				pageIsReady = helperMethods.checkPageIsReady(driver);

				// Using JSoup HTMLParser, get the page and locate the weather
				// table
				doc = Jsoup.connect(driver.getCurrentUrl()).get();

				tableElements = doc.select("table.weather");

				timeColumnIndex = 0;
				String pressureAtGivenTimeDay2 = "Not Set";

				// Loop through the header to find the desired time
				// and record its column position
				tableHeaderElements = tableElements.select("thead tr th");

				for (int i = 0; i < tableHeaderElements.size(); i++) {
					if (tableHeaderElements.get(i).text().startsWith(timeToFind)) {
						timeColumnIndex = i - 1; // Take of 1 as the header text
													// is included in the count
					}
				}

				// Loop through the rest of the table rows until the Pressure
				// row is found,
				// then loop through the pressure data to find the value in the
				// same column as the desired time.
				tableRowElements = tableElements.select(":not(thead) tr");

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
								pressureAtGivenTimeDay2 = rowItems.get(j).text();
							}
						}
					}
				}

				//
				// Output the values and the result
				//
				Integer pressureDay1 = new Integer(pressureAtGivenTime);
				Integer pressureDay2 = new Integer(pressureAtGivenTimeDay2);

				System.out.println("\n\nTest Complete\n");

				if (pressureDay1.equals(pressureDay2)) {
					System.out.println("Pressure has not changed, remaining at " + pressureDay1);
				} else {
					if (pressureDay1 > pressureDay2) {
						System.out.println("Pressure decreased by " + (pressureDay1 - pressureDay2) + " from "
								+ pressureDay1 + " to " + pressureDay2);
					} else {
						System.out.println("Pressure increased by " + (pressureDay2 - pressureDay1) + " from "
								+ pressureDay1 + " to " + pressureDay2);
					}
				}

				System.out.println("\n\n\n");

			} else {
				errorMessage = "detail-table-view not present";
			}

		} else {
			errorMessage = "locator-form-search not present";
		}

		if (errorMessage != "No error") {
			System.out.println(errorMessage);
			fail(errorMessage);
		}

	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

}
