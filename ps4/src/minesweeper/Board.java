/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package minesweeper;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import minesweeper.Square.SquareState;

/**
 * A mutable threadsafe data-type representing a Minesweeper board, which is a grid of squares.
 */
public class Board {
    private final Square[][] grid;
    private final int width;
    private final int height;
    
    // Abstraction function:
    //      AF(grid, width, height) = a Minesweeper board, which is a grid of Squares
    //                                with # of columns width and # of rows height.
    // Rep invariant:
    //      width and height > 0
    //      # of grid columns = width
    //      # of grid rows = height
    //
    // Rep exposure:
    //      Grid, width, height fields are private and final.
    //      Methods use defensive copying to avoid sharing the rep with clients.
    //      
    // Thread safety:
    //    threadsafe by monitor pattern: all accesses to rep are guarded 
    //                                   by this object's lock.
    //    Deadlocks can't occur, because there's only one Board object to work with.  
    
    private void checkRep() {
        assert width > 0;
        assert height > 0;
        assert height == grid.length;
        for (Square[] row : grid) {
            assert row.length == width;
        }
    }
    
    /**
     * Make a Board with random bomb positions
     * 
     * @param width of a Board, must be positive
     * @param height of a Board, must be positive
     * @param bombChance chance of a square to be a bomb
     */
    public Board(int width, int height, Double bombChance) {
        if (width <= 0 || height <= 0) {
            throw new RuntimeException("illegal arguments");
        }
        
        grid = new Square[height][width];
        this.width = width;
        this.height = height;
        
        // fill grid
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (Math.random() > bombChance) {
                    grid[y][x] = new Square(false);
                } else {
                    grid[y][x] = new Square(true);
                }
            }
        }
        checkRep();
    }
    
    /**
     * Make a Board from a file
     * 
     * @param file to make Board from, must be properly formatted
     * @throws IOException if couldn't read a file
     */
    public Board(File file) throws IOException {
        // extract lines
        List<String> lines = Files.readAllLines(file.toPath());
        
        // check formatting
        if (!lines.get(0).matches("[0-9]+ [0-9]+")) {
            throw new RuntimeException("The file is improperly formatted");
        }
        
        // get size
        String[] size = lines.get(0).split(" ");
        width = Integer.parseInt(size[0]);
        height = Integer.parseInt(size[1]);
        grid = new Square[height][width];
        
        lines.remove(0);
        // check formatting
        if (height != lines.size()) {
            throw new RuntimeException("The file is improperly formatted");
        }
        for (String line : lines) {
            if (!line.matches("((0|1) )*(0|1)")) {
                throw new RuntimeException("The file is improperly formatted");
            }
        }
        
        // fill grid
        for (int y = 0; y < lines.size(); y++) {
            String[] line = lines.get(y).split(" ");
            
            // check formatting
            if (line.length != width) {
                throw new RuntimeException("The file is improperly formatted");
            }
            
            for (int x = 0; x < line.length; x++) {
                if (line[x].equals("0")) {
                    grid[y][x] = new Square(false);
                } else {
                    grid[y][x] = new Square(true);
                }
            }
        }
        checkRep();
    }
    
    /**
     * Get width of this Board
     * 
     * @return width of this Board
     */
    public synchronized int getWidth() {
        return width;
    }
    
    /**
     * Get height of this Board
     * 
     * @return height of this Board
     */
    public synchronized int getHeight() {
        return height;
    }
    
    /**
     * Get state of a square
     * 
     * @param x column # of a square, 0 <= x < width 
     * @param y row # of a square, 0 <= y < height
     * @return state of a square
     */
    public synchronized SquareState getSquareState(int x, int y) {
        Square square = grid[y][x];
        return square.getState();
    }
    
    /**
     * Flag an untouched square
     * 
     * @param x column # of a square, 0 <= x < width 
     * @param y row # of a square, 0 <= y < height
     */
    public synchronized void flag(int x, int y) {
        Square square = grid[y][x];
        square.setState(SquareState.FLAGGED);
    }
    
    /**
     * Remove flag from flagged square
     * 
     * @param x column # of a square, 0 <= x < width 
     * @param y row # of a square, 0 <= y < height
     */
    public synchronized void deflag(int x, int y) {
        Square square = grid[y][x];
        square.setState(SquareState.UNTOUCHED);
    }
    
    /**
     * If the square has no neighbor squares with bombs, 
     * then for each untouched neighbor square, 
     * change said square to dug and repeat this step.
     * 
     * @param x column # of a square, 0 <= x < width 
     * @param y row # of a square, 0 <= y < height
     */
    private void digNeighbors(int x, int y) {
        int[][] neighbors = {{y-1, x-1}, {y-1, x}, {y-1, x+1}, {y, x-1}, {y, x+1},
                             {y+1, x-1}, {y+1, x}, {y+1, x+1}};
        
        if (adjacentBombs(x, y) == 0) {
            for (int[] coord : neighbors) {
                if ((0 <= coord[0] && coord[0] < height) && (0 <= coord[1] && coord[1] < width)) {
                    Square square = grid[coord[0]][coord[1]];
                    if (!square.getState().equals(SquareState.DUG)) {
                        square.setState(SquareState.DUG);
                        digNeighbors(coord[1], coord[0]);
                    }
                }
            }
        }
    }
    
    /**
     * Dig an untouched square
     * 
     * @param x column # of a square, 0 <= x < width 
     * @param y row # of a square, 0 <= y < height
     * @return true if square is a bomb, false otherwise
     */
    public synchronized boolean dig(int x, int y) {
        Square square = grid[y][x];
        
        if (square.isBomb()) {
            square.setState(SquareState.DUG);
            digNeighbors(x, y);
            checkRep();
            return true;
        } else {
            square.setState(SquareState.DUG);
            digNeighbors(x, y);
            checkRep();
            return false;
        }
    }
    
    /**
     * Count number of square's adjacent bombs
     * 
     * @param x column # of a square, 0 <= x < width 
     * @param y row # of a square, 0 <= y < height
     * @return number of adjacent bombs
     */
    private int adjacentBombs(int x, int y) {
       int count = 0;
       int[][] neighbors = {{y-1, x-1}, {y-1, x}, {y-1, x+1}, {y, x-1}, {y, x+1},
                            {y+1, x-1}, {y+1, x}, {y+1, x+1}};
       
       for (int[] coord : neighbors) {
           if ((0 <= coord[0] && coord[0] < height) && 
               (0 <= coord[1] && coord[1] < width)) {
               Square square = grid[coord[0]][coord[1]];
               
               if (square.isBomb()) {
                   count += 1;
               }
           }
       }
       return count;
    }
    
    @Override
    public synchronized String toString() {
        String boardString = "";
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Square square = grid[y][x];
                if (square.getState().equals(SquareState.DUG) && 
                    adjacentBombs(x, y) > 0) {
                    boardString += String.valueOf(adjacentBombs(x, y));
                } else {
                    boardString += square.toString();
                }
                boardString += " ";
            }
            boardString = boardString.substring(0, boardString.length()-1);
            boardString += "\n";
        }
        return boardString;
    }
}
