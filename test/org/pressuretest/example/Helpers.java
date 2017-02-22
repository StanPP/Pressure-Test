package org.pressuretest.example;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class Helpers {
	//private WebDriver driver;
	//private String baseUrl;

	
	public boolean checkPageIsReady(WebDriver driver) throws InterruptedException {

		// Returns true if the page is ready within the default timeout (in
		// seconds)
		// otherwise returns false if page still not ready when timeout expires
		
		boolean pageLoadedandReady = false;

		Integer defaultTimeout = Const.DEFAULTTIMEOUT; // In Seconds - See Helpers for current value

		JavascriptExecutor js = (JavascriptExecutor) driver;

		// Initially below given if condition will check ready state of page.
		if (js.executeScript("return document.readyState").toString().equals("complete")) {
			System.out.println("Page is loaded.");
			pageLoadedandReady = true;
			return pageLoadedandReady;
		}

		// This loop will check if the page is ready after
		// every 1 second, up to the default timeout.
		for (int i = 0; i < defaultTimeout; i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			// To check page ready state.
			if (js.executeScript("return document.readyState").toString().equals("complete")) {
				System.out.println("Page is loaded after " + i + " secs");
				pageLoadedandReady = true;
				break;
			}
		}
		return pageLoadedandReady;
	}
	
}
