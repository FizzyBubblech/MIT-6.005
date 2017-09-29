/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package expressivo;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Tests for the static methods of Commands.
 */
public class CommandsTest {

    // Testing strategy
    //
    // Commands.differentiate():
    //      # numbers = 1, > 1
    //      # variables = 1, >1
    //      Differentiation variable - present, not present
    //      add
    //      mult
    //      exception for illegal input
    //
    // Commands.simplify():
    //      # numbers = 1, > 1
    //      # variables = 1, >1
    //      # environment var = 0, 1, >1
    //      variable in env - present, not present
    //      add
    //      mult
    //      exception for illegal input

    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    
    // tests for Commands.differentiate()
    
    // covers exception
    @Test(expected=IllegalArgumentException.class)
    public void testDifferentiateException() {
        Commands.differentiate("(3", "x");
    }
    
    // covers number = 1
    @Test
    public void testDifferentiateNumber() {
        
        assertEquals("Expected string 0.0", Commands.differentiate("2", "x") , "0.0");
    }
    
    // covers variable = 1
    //        diff var - present/not present
    @Test
    public void testDifferentiateVariable() {
        
        assertEquals("Expected string 1.0", Commands.differentiate("y", "y") , "1.0");
        assertEquals("Expected string 0.0", Commands.differentiate("x", "y") , "0.0");
    }

    //covers add
    //       variable >1
    @Test
    public void testDifferentiateAdd() {
        
        assertEquals("Expected String (1.0 + 1.0)", 
                     Commands.differentiate("x+x", "x"), "(1.0 + 1.0)");
    }
    
    //covers mult
    //       number >1
    @Test
    public void testDifferentiateMult() {
        
        assertEquals("Expected String ((1.0 * 0.0) + (2.0 * 0.0))", 
                      Commands.differentiate("1*2", "x"), "((1.0 * 0.0) + (2.0 * 0.0))");
    }
    
    
    // tests for Commands.simplify()
    
    // covers exception
    @Test(expected=IllegalArgumentException.class)
    public void testSimplifyException() {
        Commands.simplify("(3", new HashMap<String, Double>());
    }
    
    // covers number = 1
    //        env = 1
    @Test
    public void testSimplifyNumber() {
        Map<String, Double> env = new HashMap<>();
        env.put("x", 10.0);
        
        assertEquals("Expected string 1.0", Commands.simplify("1", env) , "1.0");
    }
    
    // covers variable = 1
    //        not present in env
    //        env = 0
    @Test
    public void testSimplifyVariable() {
        
        assertEquals("Expected string y", 
                     Commands.simplify("y", new HashMap<String, Double>()) , "y");
    }

    //covers add
    //       number >1
    @Test
    public void testSimplifyAdd() {
        assertEquals("Expected String 3.0", 
                     Commands.simplify("1+2", new HashMap<String, Double>()), "3.0");
    }
    
    //covers mult
    //       variable > 1
    //       env >1
    //       present in env
    @Test
    public void testSimplifyMult() {
        Map<String, Double> env = new HashMap<>();
        env.put("x", 10.0);
        env.put("y", 2.0);
        
        assertEquals("Expected String 20.0", 
                      Commands.simplify("x*y", env), "20.0");
    }

    
}
