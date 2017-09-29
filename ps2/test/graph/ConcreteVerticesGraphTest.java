/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

/**
 * Tests for ConcreteVerticesGraph.
 * 
 * This class runs the GraphInstanceTest tests against ConcreteVerticesGraph, as
 * well as tests for that particular implementation.
 * 
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteVerticesGraphTest extends GraphInstanceTest {
    
    private static final String vertex1 = "v1";
    private static final String vertex2 = "v2";
    private static final String vertex3 = "v3";
    private static final String vertex4 = "v4";
    private static final String vertex5 = "v5";
    
    private static final int weight1 = 1;
    private static final int weight2 = 2;
    private static final int weight3 = 3;
    private static final int weight4 = 4;
    
    /*
     * Provide a ConcreteVerticesGraph for tests in GraphInstanceTest.
     */
    @Override public Graph<String> emptyInstance() {
        return new ConcreteVerticesGraph<String>();
    }
    
    /*
     * Testing ConcreteVerticesGraph...
     */
    
    // Testing strategy for ConcreteVerticesGraph.toString()
    //      num vertices = 0, 1, n      
    //      num edges = 0, 1, n
    
    // tests for ConcreteVerticesGraph.toString()
    
    // covers num vertices = 0
    //        num edges = 0
    @Test
    public void testToStringZeroVerticesZeroEdges() {
        Graph<String> graph = emptyInstance();
        String expected = "";
        
        assertEquals("expected empty string", expected, graph.toString());
    }
    
    // covers num vertices = 1
    @Test
    public void testToStringOneVertex() {
        Graph<String> graph = emptyInstance();
        graph.add(vertex1);
        
        String expected = "v1: inEdges{}\toutEdges{}\n";
        
        assertEquals("expected string", expected, graph.toString());
    }
    
    // covers num vertices = n
    //        num edges = 1
    @Test
    public void testToStringNVerticesOneEdge() {
        Graph<String> graph = emptyInstance();
        graph.add(vertex1);
        graph.add(vertex2);
        graph.set(vertex1, vertex2, weight1);
        
        String expected = "v1: inEdges{}\toutEdges{v2=1}\nv2: inEdges{v1=1}\toutEdges{}\n";
        
        
        assertEquals("expected string", expected, graph.toString());
    }
    
    // covers num edges = n
    @Test
    public void testToStringNEdges() {
        Graph<String> graph = emptyInstance();
        graph.add(vertex1);
        graph.add(vertex2);
        graph.add(vertex3);
        graph.set(vertex1, vertex2, weight1);
        graph.set(vertex1, vertex3, weight2);
        
        String expected = "v1: inEdges{}\toutEdges{v2=1, v3=2}\nv2: inEdges{v1=1}\toutEdges{}\n" 
                          + "v3: inEdges{v1=2}\toutEdges{}\n";
        
        assertEquals("expected string", expected, graph.toString());
    }
    
    
    /*
     * Testing Vertex...
     */
    
    // Testing strategy for Vertex
    // test getLabel()
    //
    // getIncoming() and getOutcoming():
    //      num edges - 0, 1, n
    //
    // addIncoming() and addOutcoming():
    //      edge - present, not present
    //
    // removeIncoming() and removeOutcoming():
    //      edge - present, not present
    //
    // test toString()
    
    // tests for operations of Vertex
    
    // test getLabel()
    @Test
    public void testGetLabel() {
        Vertex<String> v1 = new Vertex<String>(vertex1);
        
        assertEquals("expected to get name 'v1'", v1.getLabel(), vertex1);
    }
    
    // tests getIncoming() and getOutcoming()
    
    // covers num edges - 0
    @Test
    public void testGetNone() {
        Vertex<String> v1 = new Vertex<String>(vertex1);
        
        Map<String, Integer> map1 = v1.getIncoming();
        Map<String, Integer> map2 = v1.getOutcoming();
        
        assertTrue("expected empty map", map1.isEmpty());
        assertTrue("expected empty map", map2.isEmpty());
    }

    // covers num edges - 1
    @Test
    public void testGetOne() {
        Vertex<String> v1 = new Vertex<String>(vertex1);
        Vertex<String> v2 = new Vertex<String>(vertex2);
        Vertex<String> v3 = new Vertex<String>(vertex3);
        v1.addIncoming(v2, weight1);
        v1.addOutcoming(v3, weight2);
        
        Map<String, Integer> map1 = v1.getIncoming();
        Map<String, Integer> map2 = v1.getOutcoming();
        
        assertTrue("expected map size 1", map1.size() == 1);
        assertTrue("expected map to contain key", map1.containsKey(vertex2));
        assertTrue("expected map size 1", map2.size() == 1);
        assertTrue("expected map to contain key", map2.containsKey(vertex3));
    }
    
    // covers num edges - n
    @Test
    public void testGetN() {
        Vertex<String> v1 = new Vertex<String>(vertex1);
        v1.addIncoming(new Vertex<String>(vertex2), weight1);
        v1.addOutcoming(new Vertex<String>(vertex3), weight2);
        v1.addIncoming(new Vertex<String>(vertex4), weight3);
        v1.addOutcoming(new Vertex<String>(vertex5), weight4);
        
        Map<String, Integer> map1 = v1.getIncoming();
        Map<String, Integer> map2 = v1.getOutcoming();
        
        assertTrue("expected map size 2", map1.size() == 2);
        assertTrue("expected map to contain key", map1.containsKey(vertex2));
        assertTrue("expected map to contain key", map1.containsKey(vertex4));
        assertTrue("expected map size 2", map2.size() == 2);
        assertTrue("expected map to contain key", map2.containsKey(vertex3));
        assertTrue("expected map to contain key", map2.containsKey(vertex5));
    }
    
    // tests addIncoming() and addOutcoming()
    
    // covers edge - not present
    @Test
    public void testAddNotPresent() {
        Vertex<String> v1 = new Vertex<String>(vertex1);
        Vertex<String> v2 = new Vertex<String>(vertex2);
        Vertex<String> v3 = new Vertex<String>(vertex3);
        
        Integer oldWeight1 = v1.addIncoming(v2, weight1);
        Integer oldWeight2 = v1.addOutcoming(v3, weight2);
        Map<String, Integer> map1 = v1.getIncoming();
        Map<String, Integer> map2 = v1.getOutcoming();
        Map<String, Integer> map3 = v2.getOutcoming();
        Map<String, Integer> map4 = v3.getIncoming();
        
        assertTrue("expected old weight equals 0", oldWeight1 == 0);
        assertTrue("expected old weight equals 0", oldWeight2 == 0);
        assertTrue("expected map size 1", map1.size() == 1);
        assertTrue("expected map size 1", map2.size() == 1);
        assertTrue("expected map size 1", map3.size() == 1);
        assertTrue("expected map size 1", map4.size() == 1);
        assertTrue("expected map to contain key", map1.containsKey(vertex2));
        assertTrue("expected map to contain key", map2.containsKey(vertex3));
        assertTrue("expected map to contain key", map3.containsKey(vertex1));
        assertTrue("expected map to contain key", map4.containsKey(vertex1));
        assertTrue("expected new weight equals 1", map1.get(vertex2) == weight1);
        assertTrue("expected new weight equals 2", map2.get(vertex3) == weight2);
        assertTrue("expected new weight equals 1", map3.get(vertex1) == weight1);
        assertTrue("expected new weight equals 2", map4.get(vertex1) == weight2);
    }
    
    // covers edge - present
    @Test
    public void testAddPresent() {
        Vertex<String> v1 = new Vertex<String>(vertex1);
        Vertex<String> v2 = new Vertex<String>(vertex2);
        Vertex<String> v3 = new Vertex<String>(vertex3);
        v1.addIncoming(v2, weight1);
        v1.addOutcoming(v3, weight2);
        
        Integer oldWeight1 = v1.addIncoming(v2, weight3);
        Integer oldWeight2 = v1.addOutcoming(v3, weight4);
        Map<String, Integer> map1 = v1.getIncoming();
        Map<String, Integer> map2 = v1.getOutcoming();
        Map<String, Integer> map3 = v2.getOutcoming();
        Map<String, Integer> map4 = v3.getIncoming();
        
        assertTrue("expected weight equals 1", oldWeight1 == weight1);
        assertTrue("expected weight equals 2", oldWeight2 == weight2);
        assertTrue("expected map size 1", map1.size() == 1);
        assertTrue("expected map size 1", map2.size() == 1);
        assertTrue("expected map size 1", map3.size() == 1);
        assertTrue("expected map size 1", map4.size() == 1);
        assertTrue("expected map to contain key", map1.containsKey(vertex2));
        assertTrue("expected map to contain key", map2.containsKey(vertex3));
        assertTrue("expected map to contain key", map3.containsKey(vertex1));
        assertTrue("expected map to contain key", map4.containsKey(vertex1));
        assertTrue("expected new weight equals 3", map1.get(vertex2) == weight3);
        assertTrue("expected new weight equals 4", map2.get(vertex3) == weight4);
        assertTrue("expected new weight equals 3", map3.get(vertex1) == weight3);
        assertTrue("expected new weight equals 4", map4.get(vertex1) == weight4);
    }
    
    // tests removeIncoming() and removeOutcoming()
    
    // covers edge - not present
    @Test
    public void testRemoveNotPresent() {
       Vertex<String> v1 = new Vertex<String>(vertex1);
       
       Boolean isIn1 = v1.removeIncoming(new Vertex<String>(vertex2));
       Boolean isIn2 = v1.removeOutcoming(new Vertex<String>(vertex3));
       
       assertFalse("expected empty edge", isIn1);
       assertFalse("expected empty edge", isIn2);
    }
    
    // covers edge - not present
    @Test
    public void testRemovePresent() {
       Vertex<String> v1 = new Vertex<String>(vertex1);
       Vertex<String> v2 = new Vertex<String>(vertex2);
       Vertex<String> v3 = new Vertex<String>(vertex3);
       v1.addIncoming(v2, weight1);
       v1.addOutcoming(v3, weight2);
       
       Boolean isIn1 = v1.removeIncoming(v2);
       Boolean isIn2 = v1.removeOutcoming(v3);
       Map<String, Integer> map1 = v1.getIncoming();
       Map<String, Integer> map2 = v1.getOutcoming();
       Map<String, Integer> map3 = v2.getOutcoming();
       Map<String, Integer> map4 = v3.getIncoming();
       
       assertTrue("expected present edge", isIn1);
       assertTrue("expected present edge", isIn2);
       assertTrue("expected map size 0", map1.size() == 0);
       assertTrue("expected map size 0", map2.size() == 0);
       assertTrue("expected map size 0", map3.size() == 0);
       assertTrue("expected map size 0", map4.size() == 0);
    }
    
    //Test toString
    @Test
    public void testVertexToString(){
        Vertex<String> v1 = new Vertex<String>(vertex1);
        
        v1.addOutcoming(new Vertex<String>(vertex2), weight1);
        v1.addIncoming(new Vertex<String>(vertex3), weight2);
        
        String expected = "v1: inEdges{v3=2}\toutEdges{v2=1}\n";
        
        assertEquals("expected string", expected, v1.toString());
    }

}
