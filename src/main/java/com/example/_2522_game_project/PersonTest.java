package com.example._2522_game_project;

import static org.testng.AssertJUnit.assertEquals;

/**
 * The Person JUnit test suite.
 *
 * @author EunjeongHur
 * @version 230408
 */
public class PersonTest {
    @org.junit.Test
    public void testName() {
        Person person1  = new Person("Alice", 15);
        assertEquals("Alice", person1.name());
    }

    @org.junit.Test
    public void testScore() {
        Person person1 = new Person("Alice", 15);
        assertEquals(15, person1.score());
    }
}