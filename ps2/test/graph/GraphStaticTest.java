/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import java.util.Collections;

import org.junit.Test;

/**
 * Tests for static methods of Graph.
 * 
 * To facilitate testing multiple implementations of Graph, instance methods are
 * tested in GraphInstanceTest.
 */
public class GraphStaticTest {
    
    // Testing strategy
    //   empty()
    //     no inputs, only output is empty graph
    //     observe with vertices()
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testEmptyVerticesEmpty() {
        assertEquals("expected empty() graph to have no vertices",
                Collections.emptySet(), Graph.empty().vertices());
    }
    
    @Test
    public void testInteger() {
       Graph<Integer> graph = Graph.empty();
       
       assertTrue("expected True, added vertex to the graph", graph.add(1));
       assertTrue("expected graph size 1", graph.vertices().size() == 1);
       assertTrue("expected graph to contain the vertex", graph.vertices().contains(1));
       assertTrue("expected True, vertex in the Graph", graph.remove(1));
       assertTrue("expected empty graph", graph.vertices().size() == 0);
       
    }
    
    @Test
    public void testDouble() {
       Graph<Double> graph = Graph.empty();  
       
       assertTrue("expected True, added vertex to the graph", graph.add(1.0));
       assertTrue("expected graph size 1", graph.vertices().size() == 1);
       assertTrue("expected graph to contain the vertex", graph.vertices().contains(1.0));
       assertTrue("expected True, vertex in the Graph", graph.remove(1.0));
       assertTrue("expected empty graph", graph.vertices().size() == 0);

    }    
}
