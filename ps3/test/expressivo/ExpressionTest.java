/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package expressivo;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Tests for the Expression abstract data type.
 */
public class ExpressionTest {

    // Testing strategy
    // 
    // toString():
    //      # of Number = 1, >1
    //      # of Variable = 1, >1
    //      # of Plus = 1, >1
    //      # of Times = 1, >1
    //      mix of Times and Plus
    //
    // equals() and hashCode():
    //      # of Number = 1, >1
    //      # of Variable = 1, >1 
    //      Variable case-sensitivity
    //      # of Plus = 1, >1
    //      Plus - order, grouping
    //      # of Times = 1, >1 
    //      Times - order, grouping
    //
    // parse():
    //      different space formats
    //      # of numbers: 1, >1
    //      number - integer, fraction
    //      # of variables: 1, >1
    //      variable - case sensitivity
    //      # of add operations: 1, >1
    //      # of mult operations: 1, >1
    //      different grouping
    //      precedence of * and +
    //      Invalid input, throw exception:
    //          empty string
    //          negative number
    //          no operations between variable and number
    //          spaces between variables
    //          incomplete operations
    //          incomplete parenthesis
    //
    // differentiate():
    //      # Number = 1, >1
    //      # Variable = 1, >1
    //      Differentiation variable - present, not present
    //      Plus
    //      Times
    //
    // simplify():
    //      # Number = 1, >1
    //      # Variable = 1, >1
    //      # environment.keys() = 0, 1, >1
    //      variable in environment - present, not present
    //      Plus
    //      Times
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    
    // tests for toString()
    
    // covers Number = 1
    @Test
    public void testToStringNumber() {
        Expression n = new Number(1.0);
        
        assertEquals("Expected String '1.0'", n.toString(), "1.0");
    }
    
    // covers Variable = 1
    @Test
    public void testToStringVariable() {
        Expression v = new Variable("x");
        
        assertEquals("Expected String 'x'", v.toString(), "x");
    }
    
    // covers Plus = 1
    @Test
    public void testToStringPlusOne() {
        Expression p = new Plus(new Number(1.0), new Variable("x"));
        
        assertEquals("Expected String '(1.0 + x)'", p.toString(), "(1.0 + x)");
    }
    
    // covers Plus > 1
    //        Number > 1
    @Test
    public void testToStringPlusN() {
        Expression p = new Plus(new Number(1.0), new Plus(new Number(1.0), new Variable("x")));
        
        assertEquals("Expected String '(1.0 + (1.0 + x))'", p.toString(), "(1.0 + (1.0 + x))");
    }
    
    // covers Times = 1
    @Test
    public void testToStringTimes() {
        Expression t = new Times(new Number(1.0), new Variable("x"));
        
        assertEquals("Expected String '(1.0 * x)'", t.toString(), "(1.0 * x)");
    }
    
    // covers Times > 1
    //        Variable > 1
    @Test
    public void testToStringTimesN() {
        Expression p = new Times(new Number(1.0), new Times(new Variable("x"), new Variable("x")));
        
        assertEquals("Expected String '(1.0 * (x * x))'", p.toString(), "(1.0 * (x * x))");
    }
    
    // covers mix
    @Test
    public void testToStringMix() {
        Expression p = new Plus(new Number(1.0), new Times(new Variable("x"), new Variable("x")));
        
        assertEquals("Expected String '(1.0 + (x * x))'", p.toString(), "(1.0 + (x * x))");
    }
    
    
    // tests for equals() and hashCode()
    
    // covers Number = 1
    @Test
    public void testEqualsNumber() {
        Expression n1 = new Number(1.0);
        Expression n2 = new Number(1.0);
        Expression n3 = new Number(2.0);
        
        assertTrue("expected to be equal", n1.equals(n1));
        assertTrue("expected to be equal", n1.equals(n2));
        assertTrue("expected to be equal", n2.equals(n1));
        assertFalse("expected not to be equal", n1.equals(n3));
        assertFalse("expected not to be equal", n2.equals(n3));
        
        assertTrue("expected HashCodes to be equal", n1.hashCode() == n2.hashCode());
        assertFalse("expected HashCodes not to be equal", n1.hashCode() == n3.hashCode());
    }
    
    // covers Variable = 1
    @Test
    public void testEqualsVariable() {
        Expression n1 = new Variable("x");
        Expression n2 = new Variable("x");
        Expression n3 = new Variable("X");
        
        assertTrue("expected to be equal", n1.equals(n1));
        assertTrue("expected to be equal", n1.equals(n2));
        assertTrue("expected to be equal", n2.equals(n1));
        assertFalse("expected not to be equal", n1.equals(n3));
        assertFalse("expected not to be equal", n2.equals(n3));
        
        assertTrue("expected HashCodes to be equal", n1.hashCode() == n2.hashCode());
        assertFalse("expected HashCodes not to be equal", n1.hashCode() == n3.hashCode());
    }
    
    // covers Plus = 1
    //        order
    @Test
    public void testEqualsPlusOrder() {
        Expression n1 = new Plus(new Variable("x"), new Number(1.0));
        Expression n2 = new Plus(new Variable("x"), new Number(1.0));
        Expression n3 = new Plus(new Number(1.0), new Variable("x"));
        
        assertTrue("expected to be equal", n1.equals(n1));
        assertTrue("expected to be equal", n1.equals(n2));
        assertTrue("expected to be equal", n2.equals(n1));
        assertFalse("expected not to be equal", n1.equals(n3));
        assertFalse("expected not to be equal", n2.equals(n3));
        
        assertTrue("expected HashCodes to be equal", n1.hashCode() == n2.hashCode());
        assertFalse("expected HashCodes not to be equal", n1.hashCode() == n3.hashCode());
    }
    
    // covers Plus > 1
    //        Number > 1
    //        grouping
    @Test
    public void testEqualsPlusGrouping() {
        Expression n1 = new Plus(new Plus(new Variable("x"), new Number(1.0)), new Number(1.0));
        Expression n2 = new Plus(new Variable("x"), new Plus(new Number(1.0), new Number(1.0)));
        
        assertFalse("expected not to be equal", n1.equals(n2));
        assertFalse("expected HashCodes not to be equal", n1.hashCode() == n2.hashCode());
    }
    
    // covers Times = 1
    //        order
    @Test
    public void testEqualsTimesOrder() {
        Expression n1 = new Times(new Variable("x"), new Number(1.0));
        Expression n2 = new Times(new Variable("x"), new Number(1.0));
        Expression n3 = new Times(new Number(1.0), new Variable("x"));
        
        assertTrue("expected to be equal", n1.equals(n1));
        assertTrue("expected to be equal", n1.equals(n2));
        assertTrue("expected to be equal", n2.equals(n1));
        assertFalse("expected not to be equal", n1.equals(n3));
        assertFalse("expected not to be equal", n2.equals(n3));
        
        assertTrue("expected HashCodes to be equal", n1.hashCode() == n2.hashCode());
        assertFalse("expected HashCodes not to be equal", n1.hashCode() == n3.hashCode());
    }
    
    // covers Times > 1
    //        Variable > 1
    //        grouping
    @Test
    public void testEqualsTimesGrouping() {
        Expression n1 = new Times(new Times(new Variable("x"), new Variable("x")), new Number(1.0));
        Expression n2 = new Times(new Variable("x"), new Times(new Variable("x"), new Number(1.0)));
        
        assertFalse("expected not to be equal", n1.equals(n2));
        assertFalse("expected HashCodes not to be equal", n1.hashCode() == n2.hashCode());
    }
    
    
    // tests for parse()
    
    // covers spaces formats
    @Test
    public void testParseSpaces() {
        Expression p1 = Expression.parse("1+1");
        Expression p2 = Expression.parse(" 1 + 1 ");
        Expression p3 = Expression.parse(" 1+ 1");
        Expression p4 = Expression.parse("1 +1 ");
        
        assertTrue("Expected to be equal", p1.equals(p2));
        assertTrue("Expected to be equal", p1.equals(p3));
        assertTrue("Expected to be equal", p1.equals(p4));
    }
    
    // covers number - integer, fraction
    //        # number = 1
    @Test
    public void testParseNumber() {
        Expression p1 = Expression.parse("1");
        Expression p2 = Expression.parse("1.0");
        Expression p3 = Expression.parse(".1");
        
        Expression e1 = new Number(1.0);
        Expression e2 = new Number(0.1);
        
        assertTrue("Expected Expression '1'", p1.equals(e1));
        assertTrue("Expected Expression '1'", p2.equals(e1));
        assertTrue("Expected Expression '0.1'", p3.equals(e2));
    }
    
    // covers variable case sensitivity
    //        # variable = 1
    //        # number = 0
    @Test
    public void testParseVariable() {
        Expression p1 = Expression.parse("x");
        Expression p2 = Expression.parse("X");
        
        Expression e1 = new Variable("x");
        Expression e2 = new Variable("X");
        
        assertTrue("Expected Expression 'x'", p1.equals(e1));
        assertTrue("Expected Expression 'X'", p2.equals(e2));
        assertFalse("Expected not equal", p1.equals(p2));
    }
    
    // covers # add, mult = 1
    //        # number, variable > 1
    @Test
    public void testParseOneX() {
        Expression p1 = Expression.parse("1 + 1");
        Expression p2 = Expression.parse("x * x");
        Expression e1 = new Plus(new Number(1.0), new Number(1.0));
        Expression e2 = new Times(new Variable("x"), new Variable("x"));
        
        assertTrue("Expected Expression '1+1'", p1.equals(e1));
        assertTrue("Expected Expression 'x*x'", p2.equals(e2));
    }
    
    // covers # add, mult > 1
    //        grouping
    @Test
    public void testParseGrouping() {
        Expression p1 = Expression.parse("1 + (1 + 1)");
        Expression p2 = Expression.parse("(1 + 1) + 1");
        Expression e1 = new Plus(new Number(1.0), new Plus(new Number(1.0), new Number(1.0)));
        Expression e2 = new Plus(new Plus(new Number(1.0), new Number(1.0)), new Number(1.0));
        
        Expression p3 = Expression.parse("1 * (1 * 1)");
        Expression p4 = Expression.parse("(1 * 1) * 1");
        Expression e3 = new Times(new Number(1.0), new Times(new Number(1.0), new Number(1.0)));
        Expression e4 = new Times(new Times(new Number(1.0), new Number(1.0)), new Number(1.0));
        
        assertTrue("Expected grouping '1 + (1 + 1)'", p1.equals(e1));
        assertTrue("Expected grouping '(1 + 1) + 1'", p2.equals(e2));
        assertTrue("Expected grouping '1 * (1 * 1)'", p3.equals(e3));
        assertTrue("Expected grouping '(1 * 1) * 1'", p4.equals(e4));
    }
    
    // covers precedence
    @Test
    public void testParsePrecedence() {
        Expression p1 = Expression.parse("1+1*x");
        Expression e1 = new Plus(new Number(1.0), new Times(new Number(1.0), 
                                                            new Variable("x")));
        
        assertTrue("Expected grouping '1 + (1 * 1)'", p1.equals(e1));
    }
    
    // covers invalid input - empty string
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidEmpty() {
        Expression.parse("");
    }
    
    // covers invalid input - negative number
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidNegative() {
        Expression.parse("-1");
    }
    
    // covers invalid input - space between variables 
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidSpace() {
        Expression.parse("a b");
    }
    
    // covers invalid input - no operation
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidNoOperation() {
        Expression.parse("3x");
    }
    
    // covers invalid input - incomplete operation
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidIncompleteOperation() {
        Expression.parse("3 *");
    }
    
    // covers invalid input - incomplete parenthesis
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidIncompleteParanthesis() {
        Expression.parse("(3");
    }
    
    
    // tests for differentiate()
    
    // covers Number = 1
    @Test
    public void testDifferentiateNumber() {
        Expression e = new Number(1.0);
        Expression d = new Number(0.0);
        
        assertEquals("Expected derivative 0", e.differentiate("x"), d);
    }
    
    // covers Variable = 1
    //        diff var = present/not present
    @Test
    public void testDifferentiateVariable() {
        Expression e1 = new Variable("x");
        Expression e2 = new Variable("y");
        Expression d1 = new Number(1.0);
        Expression d2 = new Number(0.0);
        
        assertEquals("Expected derivative 0", e1.differentiate("x"), d1);
        assertEquals("Expected derivative 1", e2.differentiate("x"), d2);
    }
    
    // covers Plus
    //        Number >1
    @Test
    public void testDifferentiatePlus() {
        Expression e = new Plus(new Number(1.0), new Number(1.0));
        Expression d = new Plus(new Number(0.0), new Number(0.0));
        
        assertEquals("Expected derivative 0.0+0.0", e.differentiate("x"), d);
    }
    
    // covers Times
    //        Variable >1
    @Test
    public void testDifferentiateTimes() {
        Expression e = new Times(new Variable("x"), new Variable("x"));
        Expression d = new Plus(new Times(new Variable("x"), new Number(1.0)),
                                new Times(new Variable("x"), new Number(1.0)));
        
        assertEquals("Expected derivative x*1+x*1", e.differentiate("x"), d);
    }
    
    
    // tests for simplify()
    
    // covers Number = 1
    //        env = 1
    @Test
    public void testSimplifyNumber() {
        Map<String, Double> env = new HashMap<>();
        env.put("x", 10.0);
        Expression e = new Number(1.0);
        Expression s = new Number(1.0);
        
        assertEquals("Expected expression 1.0", e.simplify(env), s);
    }
    
    // covers Variable = 1
    //        not present in env
    //        env = 0
    @Test
    public void testSimplifyVariable() {
        Map<String, Double> env = new HashMap<>();
        Expression e = new Variable("x");
        Expression s = new Variable("x");
        
        assertEquals("Expected expression x", e.simplify(env), s);
    }
    
    // covers Plus
    //        Number > 1
    @Test
    public void testSimplifyPlus() {
        Map<String, Double> env = new HashMap<>();
        Expression e = new Plus(new Number(1.0), new Number(2.0));
        Expression s = new Number(3.0);
        
        assertEquals("Expected expression 3.0", e.simplify(env), s);
    }
    
    // covers Times
    //        Variable > 1
    //        env >1
    //        present in env
    @Test
    public void testSimplifyTimes() {
        Map<String, Double> env = new HashMap<>();
        env.put("x", 10.0);
        env.put("y", 2.0);
        Expression e = new Times(new Variable("x"), new Variable("y"));
        Expression s = new Number(20.0);
        
        assertEquals("Expected expression 20.0", e.simplify(env), s);
    }

}
