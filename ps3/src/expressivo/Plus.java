package expressivo;

import java.util.Map;

/**
 * An immutable recursive data type representing the sum of two polynomial expressions
 */
public class Plus implements Expression {
    private final Expression left, right;
        
    // Abstraction function
    //    AF(left, right) = the expression left+right
    //
    // Rep invariant
    //    true
    //
    // Safety from rep exposure
    //    all fields are private, final and immutable
    
    private void checkRep() {
        assert left != null;
        assert right != null;
    }
    
    /**
     * Make a Plus which is the sum of left and right. 
     * 
     * @param left left Expression
     * @param right right Expression
     */
    public Plus(Expression left, Expression right) {
        this.left = left;
        this.right = right;
        checkRep();
    }
    
    @Override
    public Expression differentiate(String variable) {
        return new Plus(left.differentiate(variable), right.differentiate(variable));
    }
    
    @Override
    public Expression simplify(Map<String,Double> environment) {
        Expression leftSimpl = left.simplify(environment);
        Expression rightSimpl = right.simplify(environment);
        
        if (leftSimpl.isNumber() && rightSimpl.isNumber()) {
            return new Number(leftSimpl.getValue() + rightSimpl.getValue());
        } else {
            return new Plus(leftSimpl, rightSimpl);
        }
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
        return "(" + left + " + " + right + ")";
    }
    
    @Override
    public boolean equals(Object thatObject) {
        if (!(thatObject instanceof Plus)) return false;
        Plus that = (Plus) thatObject;
        checkRep();
        return this.left.equals(that.left) && this.right.equals(that.right);
    }
    
    @Override
    public int hashCode() {
        return (left.hashCode()*2) + right.hashCode();
    }
}
