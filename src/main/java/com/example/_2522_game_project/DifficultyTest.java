package com.example._2522_game_project;

import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;

/**
 * The Difficulty JUnit test suite.
 *
 * @author EunjeongHur
 * @version 230408
 */
public class DifficultyTest {

    @Test
    public void values() {
        Difficulty[] expectedDifficulties = {Difficulty.EASY, Difficulty.MEDIUM, Difficulty.HARD};
        Difficulty[] actualDifficulties = Difficulty.values();
        for (int i = 0; i < actualDifficulties.length; i++) {
            assertEquals(expectedDifficulties[i], actualDifficulties[i]);
        }
    }

    @Test
    public void valueOf() {
        assertEquals(Difficulty.EASY, Difficulty.valueOf("EASY"));
        assertEquals(Difficulty.MEDIUM, Difficulty.valueOf("MEDIUM"));
        assertEquals(Difficulty.HARD, Difficulty.valueOf("HARD"));
    }
}