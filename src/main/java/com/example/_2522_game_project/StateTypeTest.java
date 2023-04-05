package com.example._2522_game_project;

import org.junit.Test;

import static org.testng.AssertJUnit.assertEquals;

public class StateTypeTest {

    @Test
    public void values() {
        StateType[] expectedStateTypes = {StateType.OPENED, StateType.UNOPENED, StateType.FLAGGED, StateType.LOCKED};
        StateType[] actualStateTypes = StateType.values();
        for (int i = 0; i < actualStateTypes.length; i++) {
            assertEquals(expectedStateTypes[i], actualStateTypes[i]);
        }
    }

    @Test
    public void valueOf() {
        assertEquals(StateType.OPENED, StateType.valueOf("OPENED"));
        assertEquals(StateType.FLAGGED, StateType.valueOf("FLAGGED"));
        assertEquals(StateType.FLAGGED, StateType.valueOf("FLAGGED"));
        assertEquals(StateType.LOCKED, StateType.valueOf("LOCKED"));
    }
}