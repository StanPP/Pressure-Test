package org.pressuretest.example;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ PressureTest.class, PressureTestForEdge.class })
public class AllTests {

}
