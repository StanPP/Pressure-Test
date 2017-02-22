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
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

/*
 * 
 * 
 *   EDGE  EDGE  EDGE  EDGE  EDGE  EDGE  EDGE  EDGE  EDGE  EDGE  EDGE  EDGE  EDGE  EDGE  EDGE  EDGE  EDGE  EDGE  EDGE 
 * 
 * 
 * 
 */

public class PressureTestForEdge {
	private WebDriver driver;
	private String baseUrl;
	private StringBuffer verificationErrors = new StringBuffer();

	// Helper methods
	Helpers helperMethods = new Helpers();

	@Before
	public void setUp() throws Exception {

		System.setProperty("webdriver.edge.driver", Const.EDGEDRIVERLOCATION);

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
			
			driver.findElement(By.id("locator-form-search")).sendKeys("Reading, Reading");  //Send Keys sometimes has a problem with MS Edge
			
			driver.findElement(By.id("locator-form-search")).sendKeys(Keys.RETURN);

			pageIsReady = helperMethods.checkPageIsReady(driver);
			if (pageIsReady && isElementPresent(By.id("detail-table-view"))) {

				// click on the table view
				driver.findElement(By.id("detail-table-view")).click();

				//
				// extract pressure for 21:00 from the table for today
				//
				HTMLTableRead htmlTableReader = new HTMLTableRead();
				Integer pressureDay1 = new Integer(htmlTableReader.ExtractPressureData(driver));

				//
				// Move to next day
				//
				driver.get((driver.getCurrentUrl() + "?day=1"));

				pageIsReady = helperMethods.checkPageIsReady(driver);
				if (pageIsReady) {
					//
					// extract pressure for 21:00 from the table for tomorrow
					//
					Integer pressureDay2 = new Integer(htmlTableReader.ExtractPressureData(driver));

					//
					// Output the values and the result
					//

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
					errorMessage = "page not ready";
				}

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
