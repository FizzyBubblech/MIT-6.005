/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Tests for instance methods of Graph.
 * 
 * <p>PS2 instructions: you MUST NOT add constructors, fields, or non-@Test
 * methods to this class, or change the spec of {@link #emptyInstance()}.
 * Your tests MUST only obtain Graph instances by calling emptyInstance().
 * Your tests MUST NOT refer to specific concrete implementations.
 */
public abstract class GraphInstanceTest {
    
    // Testing strategy for each operation of Graph:
    //
    // add():
    //      vertex = in graph, not in graph
    //      graph size = 0, 1, n
    //
    // set():
    //      source,target = in graph, not in graph
    //      edge = new, modified
    //      weight = 0, n
    //      graph size = 0, 1, n
    //
    // remove():
    //      vertex = in graph, not in graph
    //      graph size = 0, 1, n
    //      vertex = has edges coming to, from, both
    //
    // vertices():
    //      graph size = 0, 1, n
    // 
    // sources():
    //      edge num = 0, 1, n
    //      graph size = 1, n
    //
    // targets():
    //      edge num = 0, 1, n
    //      graph size = 1, n
    
    private static final String vertex1 = "v1";
    private static final String vertex2 = "v2";
    private static final String vertex3 = "v3";
    
    private static final int weight0 = 0;
    private static final int weight1 = 1;
    private static final int weight2 = 2;

    
    /**
     * Overridden by implementation-specific test classes.
     * 
     * @return a new empty graph of the particular implementation being tested
     */
    public abstract Graph<String> emptyInstance();
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    // test add()
    
    // covers vertex in graph
    //        graph size = 1
    @Test
    public void testAddInGraphOneSize() {
        Graph<String> graph = emptyInstance();
        graph.add(vertex1);
        
        assertFalse("expected False, graph already contains the vertex", graph.add(vertex1));
        assertTrue("expected graph size 1", graph.vertices().size() == 1);
        assertTrue("expected graph to contain the vertex", graph.vertices().contains(vertex1));
    }
    
    // covers vertex not in graph
    //        graph size = 0
    @Test
    public void testAddNotInGraphZeroSize() {
        Graph<String> graph = emptyInstance();
        
        assertTrue("expected True, added vertex to the graph", graph.add(vertex1));
        assertTrue("expected graph size 1", graph.vertices().size() == 1);
        assertTrue("expected graph to contain the vertex", graph.vertices().contains(vertex1));
    }
    
    // covers graph size = n
    @Test
    public void testAddNSize() {
        Graph<String> graph = emptyInstance();
        graph.add(vertex1);
        graph.add(vertex2);
        
        assertTrue("expected True, added vertex to the graph", graph.add(vertex3));
        assertTrue("expected graph size 3", graph.vertices().size() == 3);
        assertTrue("expected graph to contain the vertex", graph.vertices().contains(vertex3));
    }
    
    // test set()
    
    // covers graph size = n
    //        target, source in graph
    //        new edge
    //        
    @Test
    public void testSetInGraphWeightOneNewEdgeNSize() {
        Graph<String> graph = emptyInstance();
        graph.add(vertex1);
        graph.add(vertex2);
        Map<String, Integer> targets = new HashMap<>();
        targets.put(vertex2, weight1);
        Map<String, Integer> sources = new HashMap<>();
        sources.put(vertex1, weight1);
        
        assertEquals("expected previous weight of edge to be 0", 0, graph.set(vertex1, vertex2, weight1));
        assertEquals("expected Graph containing edge", targets, graph.targets(vertex1));
        assertEquals("expected Graph containing edge", sources, graph.sources(vertex2));
    }
    
    // covers weight = n
    //        modify edge
    @Test
    public void testSetWeightNModifyEdge() {
        Graph<String> graph = emptyInstance();
        graph.add(vertex1);
        graph.add(vertex2);
        graph.set(vertex1, vertex2, weight1);
        Map<String, Integer> targets = new HashMap<>();
        targets.put(vertex2, weight2);
        Map<String, Integer> sources = new HashMap<>();
        sources.put(vertex1, weight2);
        
        assertEquals("expected previous weight of edge to be 1", 1, graph.set(vertex1, vertex2, weight2));
        assertEquals("expected Graph containing edge", targets, graph.targets(vertex1));
        assertEquals("expected Graph containing edge", sources, graph.sources(vertex2));
    }
    
    // covers weight = 0
    @Test
    public void testSetWeightZero() {
        Graph<String> graph = emptyInstance();
        graph.add(vertex1);
        graph.add(vertex2);
        graph.set(vertex1, vertex2, weight1);
        
        assertEquals("Expected previous weight of edge to be 1", 1, graph.set(vertex1, vertex2, weight0));
        assertEquals("expected Graph containing zero edges", Collections.emptyMap(), graph.targets(vertex1));
        assertEquals("expected Graph containing zero edges", Collections.emptyMap(), graph.sources(vertex2));
    }
    
    // covers graph size = 0
    //        source, target not in graph
    @Test
    public void testSetNotInGraphZeroSize() {
        Graph<String> graph = emptyInstance();
        Map<String, Integer> targets = new HashMap<>();
        targets.put(vertex2, weight1);
        Map<String, Integer> sources = new HashMap<>();
        sources.put(vertex1, weight1);
        
        assertEquals("Expected previous weight of edge to be 0", 0, graph.set(vertex1, vertex2, weight1));
        assertTrue("expected graph size 2", graph.vertices().size() == 2);
        assertTrue("expected graph to contain the vertex", graph.vertices().contains(vertex1));
        assertTrue("expected graph to contain the vertex", graph.vertices().contains(vertex2));
        assertEquals("expected Graph containing edge", targets, graph.targets(vertex1));
        assertEquals("expected Graph containing edge", sources, graph.sources(vertex2));
    }
    
    // test remove()
    
    //covers vertex in graph
    //       graph size = 1
    @Test
    public void testRemoveInGraphOneSize() {
        Graph<String> graph = emptyInstance();
        graph.add(vertex1);
        
        assertTrue("expected True, vertex in the Graph", graph.remove(vertex1));
        assertTrue("expected empty graph", graph.vertices().size() == 0);
    }
    
    // covers vertex not in graph
    //        graph size = 0
    @Test
    public void testRemoveNotInGraphZeroSize() {
        Graph<String> graph = emptyInstance();
        
        assertFalse("expected False, vertex not in the Graph", graph.remove(vertex1));
        assertTrue("expected empty graph", graph.vertices().size() == 0);
    }
    
    // covers vertex has edge from
    //        graph size = n
    @Test
    public void testRemoveFromNSize() {
        Graph<String> graph = emptyInstance();
        graph.set(vertex1, vertex2, weight1);
        
        assertTrue("expected True, vertex in the Graph", graph.remove(vertex1));
        assertFalse("expected graph not to contain the vertex", graph.vertices().contains(vertex1));
        assertEquals("expected Graph containing zero edges", Collections.emptyMap(), graph.sources(vertex2));
    }
    
    // covers vertex has edge to
    @Test
    public void testRemoveTo() {
        Graph<String> graph = emptyInstance();
        graph.set(vertex1, vertex2, weight1);
        
        assertTrue("expected True, vertex in the Graph", graph.remove(vertex2));
        assertFalse("expected graph not to contain the vertex", graph.vertices().contains(vertex2));
        assertEquals("expected Graph containing zero edges", Collections.emptyMap(), graph.targets(vertex1));
    }
    
    // covers vertex has edge coming to and from 
    @Test
    public void testRemoveBothToFrom() {
        Graph<String> graph = emptyInstance();
        graph.set(vertex1, vertex2, weight1);
        graph.set(vertex2, vertex3, weight1);
        
        assertTrue("expected True, vertex in Graph", graph.remove(vertex2));
        assertFalse("expected graph not to contain the vertex", graph.vertices().contains(vertex2));
        assertEquals("expected Graph containing zero edges", Collections.emptyMap(), graph.targets(vertex1));
        assertEquals("expected Graph containing zero edges", Collections.emptyMap(), graph.sources(vertex3));
    }
    
    // test vertices()
    
    // covers graph size = 0
    @Test
    public void testVerticesEmpty() {
        assertEquals("expected new graph to have no vertices", Collections.emptySet(), emptyInstance().vertices());
    }
    
    // covers graph size = 1
    @Test
    public void testVerticesOne() {
        Graph<String> graph = emptyInstance();
        graph.add(vertex1);
        
        assertEquals("expected new graph to have one vertex", 1, graph.vertices().size());
        assertTrue("expected graph to contain the vertex", graph.vertices().contains(vertex1));
    }
    
    // covers graph size = n
    @Test
    public void testVerticesTwo() {
        Graph<String> graph = emptyInstance();
        graph.add(vertex1);
        graph.add(vertex2);
        
        assertEquals("expected new graph to have two vertices", 2, graph.vertices().size());
        assertTrue("expected graph to contain the vertex", graph.vertices().contains(vertex1));
        assertTrue("expected graph to contain the vertex", graph.vertices().contains(vertex2));
    }
    
    // test sources()
    
    // covers graph size = 1
    //        edge num = 0
    @Test
    public void testSourcesZeroEdgesOneSize() {
        Graph<String> graph = emptyInstance();
        graph.add(vertex1);
        
        assertEquals("expected the vertex to contain zero sources", Collections.emptyMap(), graph.sources(vertex1));
    }
    
    // covers graph size = n
    //        edge num = 1
    @Test
    public void testSourcesOneEdgeNSize() {
        Graph<String> graph = emptyInstance();
        graph.add(vertex1);
        graph.add(vertex2);
        graph.set(vertex1, vertex2, weight1);
        Map<String, Integer> sources = new HashMap<>();
        sources.put(vertex1, weight1);
        
        assertEquals("expected the vertex to contain one source", sources, graph.sources(vertex2));
    }
    
    // covers edge num = n
    @Test
    public void testSourcesNEdges() {
        Graph<String> graph = emptyInstance();
        graph.add(vertex1);
        graph.add(vertex2);
        graph.add(vertex3);
        graph.set(vertex1, vertex2, weight1);
        graph.set(vertex3, vertex2, weight2);
        Map<String, Integer> sources = new HashMap<>();
        sources.put(vertex1, weight1);
        sources.put(vertex3, weight2);
        
        assertEquals("expected the vertex to contain two sources", sources, graph.sources(vertex2));
    }
    
    // test targets()
    
    // covers edge num = 0
    //        graph size = 1
    @Test
    public void testTargetsZeroEdgesOneSize() {
        Graph<String> graph = emptyInstance();
        graph.add(vertex1);
        
        assertEquals("expected the vertex to contain zero targets", Collections.emptyMap(), graph.sources(vertex1));
    }
    
    // covers edge num = 1
    //        graph size = n
    @Test
    public void testTargetsOneEdgeNSize() {
        Graph<String> graph = emptyInstance();
        graph.add(vertex1);
        graph.add(vertex2);
        graph.set(vertex1, vertex2, weight1);
        Map<String, Integer> targets = new HashMap<>();
        targets.put(vertex2, weight1);
        
        assertEquals("expected the vertex to contain one target", targets, graph.targets(vertex1));
    }
    
    // covers edge num = n
    @Test
    public void testTargetsNEdges() {
        Graph<String> graph = emptyInstance();
        graph.add(vertex1);
        graph.add(vertex2);
        graph.add(vertex3);
        graph.set(vertex2, vertex1, weight1);
        graph.set(vertex3, vertex1, weight2);
        Map<String, Integer> targets = new HashMap<>();
        targets.put(vertex2, weight1);
        targets.put(vertex3, weight2);
        
        assertEquals("expected the vertice to contain two targets", targets, graph.sources(vertex1));
    }
}




