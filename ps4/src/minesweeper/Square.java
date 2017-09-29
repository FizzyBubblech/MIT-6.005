package minesweeper;


/**
 * A mutable data-type representing a square on a Minesweeper Board. 
 * It is either flagged, dug, or untouched. And also either contains a bomb, 
 * or does not contain a bomb. 
 */
public class Square {
    
    /**
     * Enumeration of Square states.
     */
    public enum SquareState {UNTOUCHED, DUG, FLAGGED}
    
    private boolean isBomb;
    private SquareState state;
    
    // Abstraction function:
    //      AF(bomb, state) = a square that contains a bomb iff isBomb
    //                        and a state(dug, flagged, untouched).
    //
    // Rep invariant:
    //      Dug square can't have a bomb.
    //
    // Rep exposure:
    //      isBomb and state fields are private of immutable type boolean 
    //      and SquareState.
    
    private void checkRep() {
        if (state.equals(SquareState.DUG)) {
            assert !isBomb;
        }
    }
    
    /**
     * Make an untouched Square
     * 
     * @param bomb to set
     */
    public Square(boolean bomb) {
        isBomb = bomb;
        state = SquareState.UNTOUCHED;
        checkRep();
    }
    
    /**
     * Check whether this Square contains a bomb
     * 
     * @return true iff this Square contains a bomb
     */
    public boolean isBomb() {
        return isBomb;
    }
    
    /**
     * Get state of this Square
     * 
     * @return state of this Square
     */
    public SquareState getState() {
        return state;
    }
    
    /**
     * Set state of this Square
     * 
     * @param state of this Square, must be DUG or FLAGGED,
     *        removes bomb if set to DUG
     */
    public void setState(SquareState state) {
        if (state.equals(SquareState.DUG)) {
            isBomb = false;
            this.state = state;
            checkRep();
        }
        else {
            this.state = state;
            checkRep();
        }
    }
    
    @Override
    public String toString() {
        switch(state) {
            case UNTOUCHED:
                return "-";
            case FLAGGED:
                return "F";
            case DUG:
                return " ";
            default:
                throw new RuntimeException("can't get here");
        }
    }  
}
