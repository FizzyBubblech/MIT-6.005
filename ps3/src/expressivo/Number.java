package expressivo;

import java.util.Map;

/**
 * An immutable data-type representing a nonnegative number in decimal representation
 */
public class Number implements Expression {
    private final Double value;
    
    // Abstraction function
    //    AF(value) = a nonnegative number with a value
    //
    // Rep invariant
    //    value >= 0
    //
    // Safety from rep exposure
    //    value field is private, final and immutable
    
    private void checkRep() {
        assert value >= 0;
    }
    
    /** 
     * Make a Number
     * 
     * @param n value of a Number
     */
    public Number(Double n) {
        this.value = n;
        checkRep();
    }
    
    @Override
    public Expression differentiate(String variable) {
        return new Number(0.0);
    }
    
    @Override
    public Expression simplify(Map<String,Double> environment) {
        return new Number(value);
    }
    
    @Override
    public boolean isNumber() {
        return true;
    }
    
    @Override
    public Double getValue() {
        return value;
    }
    
    @Override 
    public String toString() {
        return String.valueOf(value);
    }
    
    @Override
    public boolean equals(Object thatObject) {
        if (!(thatObject instanceof Number)) return false;
        Number that = (Number) thatObject;
        checkRep();
        return this.value.equals(that.value);
    }
    
    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
