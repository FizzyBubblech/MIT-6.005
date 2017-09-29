package expressivo;

import java.util.Map;

/**
 * An immutable data-type representing a variable with name, that is
 * case-sensitive nonempty sequences of letters
 */
public class Variable implements Expression {
    private final String name;
    
    // Abstraction function
    //    AF(name) = a variable with a name
    //
    // Rep invariant
    //    name != ""
    //    cannot have spaces
    //
    // Safety from rep exposure
    //    name field is private, final and immutable
    
    private void checkRep() {
        assert !name.equals("");
        assert name.matches("[a-zA-z]+");
    }
    
    /** 
     * Make a Variable
     * 
     * @param str name of a variable
     */
    public Variable(String str) {
        this.name = str;
        checkRep();
    }
    
    @Override
    public Expression differentiate(String variable) {
        if (name.equals(variable)) {
            return new Number(1.0);
        } else {
            return new Number(0.0);
        }
    }
    
    @Override
    public Expression simplify(Map<String,Double> environment) {
        if (environment.containsKey(name)) {
            return new Number(environment.get(name));
        }
        return new Variable(name);
    }
    
    @Override
    public boolean isNumber() {
        return false;
    }
    
    @Override
    public Double getValue() {
        throw new AssertionError("Not a number");
    }
    
    @Override 
    public String toString() {
        return name;
    }
    
    @Override
    public boolean equals(Object thatObject) {
        if (!(thatObject instanceof Variable)) return false;
        Variable that = (Variable) thatObject;
        checkRep();
        return this.name.equals(that.name);
    }
    
    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
