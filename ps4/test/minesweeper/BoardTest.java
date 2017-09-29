/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package minesweeper;

import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import org.junit.Test;

import minesweeper.Square.SquareState;

/**
 * Tests for Board class
 */
public class BoardTest {
    
    // Testing strategy
    // 
    // Constructor: 
    //      Board - square, not square shape
    //      with file - valid, invalid path 
    //      with size
    //               
    // test getters:
    //      getWidth()
    //      getHeight()
    //      getSquareState()
    //
    // flag(), deflag()
    //
    // dig():
    //      Square - bomb, not bomb
    //      bomb neighbors - 0, 1, >1
    //      non-bomb neighbors - 0, 1, >1
    //      recursive call
    //
    // toString():
    //      Board - untouched squares, dug square, flagged square, 
    //              bomb neighbors count
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    // covers constructor with size
    //        square shape
    //        getWeight(), getHeight()
    //        toString() - untouched
    @Test
    public void testSizeSquare() {
        Board board = new Board(2, 2, 0.25);
        String str = "- -\n"
                   + "- -\n";
        
        assertEquals("expected height 2", board.getHeight(), 2);
        assertEquals("expected width 2", board.getWidth(), 2);
        assertEquals("expected string", board.toString(), str);
    }
    
    // covers constructor with file - invalid path
    @Test(expected = IOException.class)
    public void testFileIvalid() throws IOException {
        new Board(new File("./test/minesweeper/board0"));
    }
    
    // covers constructor with file - valid path
    //        non-square shape
    //        flag(), deflag()
    //        toString() - flagged
    @Test
    public void testFileValidNonSquare() throws IOException {
        Board board = new Board(new File("./test/minesweeper/board1"));
        String str = "- - - - -\n"
                   + "- - - - -\n"
                   + "- - - - -\n"
                   + "- - - - -\n";
        String str1 = "F - - - -\n"
                    + "- - - - -\n"
                    + "- - - - -\n"
                    + "- - - - -\n";
        
        board.flag(0, 0);
        assertEquals("expected string", board.toString(), str1);
        
        board.deflag(0, 0);
        assertEquals("expected string", board.toString(), str);
    }
    
    // covers dig():
    //          square - bomb
    //          neighbors bombs - 0
    //          neighbors non-bombs > 1
    //        toString():
    //          dug square, bomb count
    @Test
    public void testDig1() throws IOException {
        Board board = new Board(new File("./test/minesweeper/board1"));
        String str = "  1 - - -\n"
                   + "1 2 - - -\n"
                   + "- - - - -\n"
                   + "- - - - -\n";
        
        board.dig(0, 0);
        assertEquals("expected string", board.toString(), str);
    }
    
    // covers dig():
    //          square - not bomb
    //          neighbors bombs - >1
    //          neighboring non-bombs - 0
    @Test
    public void testDig2() throws IOException {
        Board board = new Board(new File("./test/minesweeper/board1"));
        String str = "- - - - -\n"
                   + "- - - - -\n"
                   + "- - - - -\n"
                   + "- - - - 3\n";
        
        board.dig(4, 3);
        assertEquals("expected string", board.toString(), str);
    }
    
    // covers dig():
    //          neighbors bombs - 1
    //        getSquareState()
    @Test
    public void testDig3() throws IOException {
        Board board = new Board(new File("./test/minesweeper/board1"));
        String str = "- - - - -\n"
                   + "- - 1 - -\n"
                   + "- - - - -\n"
                   + "- - - - -\n";
        
        assertEquals("expected untouched state", board.getSquareState(2, 1), 
                                                 SquareState.UNTOUCHED);
        board.dig(2, 1);
        assertEquals("expected untouched state", board.getSquareState(2, 1), 
                                                 SquareState.DUG);
        assertEquals("expected string", board.toString(), str);
    }
    
    // covers dig():
    //          neighbors non-bombs - 1
    @Test
    public void testDig4() throws IOException {
        Board board = new Board(new File("./test/minesweeper/board1"));
        String str = "- - - - -\n"
                   + "- - - - -\n"
                   + "- - - - -\n"
                   + "2 - - - -\n";
        
        board.dig(0, 3);
        assertEquals("expected string", board.toString(), str);
    }
    
    // covers dig():
    //      recursive call
    @Test
    public void testDig5() throws IOException {
        Board board = new Board(new File("./test/minesweeper/board5"));
        String str = "             \n"
                   + "             \n"
                   + "             \n"
                   + "             \n"
                   + "             \n"
                   + "1 1          \n"
                   + "- 1          \n";
        
        board.dig(4, 1);
        assertEquals("expected string", board.toString(), str);
    }
}
