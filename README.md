# Pressure-Test
Selenium Webdriver test

This is an excercise using Selenium WebDriver to:

  * Open the BBC Weather website (http://www.bbc.co.uk/weather)
  * Use the 'Find a Forecast' search for get the weather in 'Reading'
  * Expand the weather results to display Pressure (using the 'Table' option)
  * Obtain the pressure value for 21:00 for today
  * Obtain the pressure value for 21:00 for tomorrow
  * Subtract the two values and then 'echo' the result in Selenium


The code has been built and tested on Windows 10 Home edition.

It is written in Java and tested using JUnit4 using Eclipse v4.6.2

The v1.0.0 code is specifically written for just the Mozilla Firefox browser.

The Release directory contains a runnable .jar

            To run the code requires Java runtime v1.8.0_121 or above (Currently the latest available from Oracle).

            I have exported the code to a runnable .jar and for simplicity just copy the supplied W10Webdrivers folder to the root of C: and run from there.
            The code expects to find the Firefox Gecko driver in c:\W10Webdrivers.

            Open a command prompt and to execute type the following:

            C:\W10Webdrivers> java -jar ptFirefox.jar

            or run the batch file provided.


Improvements, observations & future investigations
--------------------------------------------------

Parameterise the input to support other browsers.  Currently Firefox only, have tried it with Edge and on investigation, webDriver .sendKeys is having an issue intermittently..

Put the table extraction code into a common method, rather than repeat it as I've done.

Consider using the URL for Reading (http://www.bbc.co.uk/weather/2639577) rather than executing a search each time.

After 21:00 in the evening, this value disappears from the page for the current day, so I've added flexibility to allow this to be changed.  This could be an issue if this test were being run on an automated system late in the evening.

Have tried to avoid just putting in sleep timers, whilst pages load, but still have seen some issues where the objects are available and can be used, but the page is still catching up and not ready.


The AddABrowser stream has been created, this is to have a test that works with Mozilla FireFox and MS Edge as well as reducing the amount of code by consolidating common code.

