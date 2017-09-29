package minesweeper;

import static org.junit.Assert.*;

import org.junit.Test;

import minesweeper.Square.SquareState;

/**
 * Tests for Square class
 */
public class SquareTest {
    // Testing strategy
    //
    // isBomb():
    //      Square - contains bomb, doesn't contain
    //
    // getState() and setState():
    //      Square - untouched, flagged, dug
    //
    // tpString():
    //      Square - untouched, flagged, dug
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    // Test getState(), setState(), isBomb()
    @Test
    public void testState() {
        Square s = new Square(true);
        assertEquals("untouched state", s.getState(), SquareState.UNTOUCHED);
        assertTrue("contains bomb", s.isBomb());
        
        s.setState(SquareState.FLAGGED);
        assertEquals("flagged state", s.getState(), SquareState.FLAGGED);
        
        s.setState(SquareState.DUG);
        assertEquals("dug state", s.getState(), SquareState.DUG);
        assertFalse("doesn't contain bomb", s.isBomb());
    }
    
    // Test toString()
    @Test
    public void testString() {
        Square s = new Square(false);
        assertEquals("untouched state", s.toString(), "-");
        
        s.setState(SquareState.FLAGGED);
        assertEquals("flagged state", s.toString(), "F");
        
        s.setState(SquareState.DUG);
        assertEquals("dug state", s.toString(), " ");
    }
}
