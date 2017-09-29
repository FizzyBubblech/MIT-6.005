/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An implementation of Graph.
 * 
 * <p>PS2 instructions: you MUST use the provided rep.
 */
public class ConcreteVerticesGraph<L> implements Graph<L> {
    
    private final List<Vertex<L>> vertices = new ArrayList<>();
    
    // Abstraction function:
    //   AF(vertices) = a directed graph composed of distinct vertices
    //                  connected by weighted edges
    //
    // Representation invariant:
    //   edges have positive weight
    //
    // Safety from rep exposure:
    //   vertices field is private and final;
    //   vertices is a mutable List, so vertices() make defensive copies 
    //   to avoid sharing the rep with clients.
    
    // constructor
    /**
     * Create an empty ConcreteVerticesGraph
     */
    public ConcreteVerticesGraph() {
        checkRep();
    }
    
    // Check that the rep invariant is true
    private void checkRep() {
        for (Vertex<L> vertex : vertices) {
            Map<L, Integer> inEdges = vertex.getIncoming();
            Map<L, Integer> outEdges = vertex.getOutcoming();
            
            for (Integer weight : inEdges.values()) {
                assert weight > 0;
            }
            for (Integer weight : outEdges.values()) {
                assert weight > 0;
            }
        }
    }
    
    // methods
    /**
     * Find a vertex in vertices that has the label
     * 
     * @param label label of the vertex to find, MUST BE present in the graph
     * @return Vertex with label as its name
     */
    private Vertex<L> findVertex(L label) {
        for (Vertex<L> vertex: vertices) {
            if (vertex.getLabel().equals(label)) {
                return vertex;
            }
        }
        // if vertex was not present, throw error
        throw new AssertionError("Vertex not in graph");
    }
    
    /**
     * Check if vertex is in the graph
     * @return true if vertex is in the graph, false otherwise
     */
    private boolean inGraph(L label){
        Set<L> set = new HashSet<>();
        
        for (Vertex<L> vertex: vertices){
           set.add(vertex.getLabel());
        }
        
        return set.contains(label);
    }

    @Override public boolean add(L vertex) {
        if (inGraph(vertex)) {
            checkRep();
            return false;
        }
        else {
            vertices.add(new Vertex<>(vertex));
            checkRep();
            return true;
        }
    }
    
    @Override public int set(L source, L target, int weight) {
        // find vertices in the graph, create new if they don't exist and weight > 0
        Vertex<L> srcVertex;
        if (inGraph(source)) {
            srcVertex = findVertex(source);
        }
        else {
            srcVertex = new Vertex<>(source);
            if (weight > 0) {
                vertices.add(srcVertex);
                }
        }
        Vertex<L> trgVertex;
        if (inGraph(target)) {
            trgVertex = findVertex(target);
        }    
        else {   
            trgVertex = new Vertex<>(target);
            if (weight > 0) {
                vertices.add(trgVertex);
                }
        }
        // if edge exists - update or remove it
        Map<L, Integer> srcOutEdges = srcVertex.getOutcoming();
        Map<L, Integer> trgInEdges = trgVertex.getIncoming();
        if (srcOutEdges.containsKey(target) && trgInEdges.containsKey(source)) {
            Integer oldWeight = srcOutEdges.get(target);
            if (weight == 0) {
                srcVertex.removeOutcoming(trgVertex);
                return oldWeight;
            }
            srcVertex.addOutcoming(trgVertex, weight);
            return oldWeight;
        }
        else {
            if (weight > 0) {
                srcVertex.addOutcoming(trgVertex, weight);
                return 0;
            }
        }
        return 0;
    }
    
    @Override public boolean remove(L vertex) {
        if (inGraph(vertex)) {
            Vertex<L> vertexToRemove = findVertex(vertex);
            // remove edges
            for (Vertex<L> vertexTemp : vertices) {
                vertexTemp.removeIncoming(vertexToRemove);
                vertexTemp.removeOutcoming(vertexToRemove);
            }
            vertices.remove(vertexToRemove);
            checkRep();
            return true;
        }
        else {
            return false;
        }
    }
    
    @Override public Set<L> vertices() {
        Set<L> result = new HashSet<>();
        for (Vertex<L> vertex : vertices) {
            result.add(vertex.getLabel());
        }
        return result;
    }
    
    @Override public Map<L, Integer> sources(L target) {
        if (inGraph(target)) {
            Vertex<L> targetVertex = findVertex(target);
            return targetVertex.getIncoming();
        }
        return new HashMap<L, Integer>();
    }
    
    @Override public Map<L, Integer> targets(L source) {
        if (inGraph(source)) {
            Vertex<L> sourceVertex = findVertex(source);
            return sourceVertex.getOutcoming();
        }
        return new HashMap<L, Integer>();
    }
    
    @Override public String toString() {
        String result = "";
        for (Vertex<L> vertex : vertices) {
            result += vertex.toString();
        }
        return result;
    }
    
}

/**
 * Mutable.
 * This class is internal to the rep of ConcreteVerticesGraph.
 * 
 * This mutable data-type represents a labeled vertex in a directed graph.
 * A vertex has incoming edges with source vertices, and outcoming edges with
 * target vertices. Edges have positive weights.
 * 
 * <p>PS2 instructions: the specification and implementation of this class is
 * up to you.
 */
class Vertex<L> {
    
    //fields
    private final L label;
    private final Map<L, Integer> inEdges = new HashMap<>();
    private final Map<L, Integer> outEdges = new HashMap<>();
    
    // Abstraction function:
    //   AF(label, inEdges, outEdges) = a labeled vertex with a set of incoming and 
    //                                  outcoming weighted edges
    //
    // Representation invariant:
    //   inEdges and outEdges have positive weights
    //
    // Safety from rep exposure:
    //   all fields are private and final;
    //   inEdges and outEdges are mutable Map, so getIncoming() and getOurcoming() 
    //   make defensive copies to avoid sharing the rep with clients.
    
    // constructor
    /**
     * Create a Vertex with a label
     * 
     * @param label label of a vertex
     */
    public Vertex(L label) {
        this.label = label;
        checkRep();
    }
    
    // Check that the rep invariant is true
    private void checkRep() {
        assert label != null;
        for (Integer weight: inEdges.values()) {
            assert weight > 0;
        }
        for (Integer weight : outEdges.values()) {
            assert weight > 0;
        }
    }
    
    // methods
    /**
     * Get the label of the vertex
     * 
     * @return label of the vertex
     */
    public L getLabel() {
        return label;
    }
    
    /**
     * Get incoming edges of the vertex
     * 
     * @return incoming edges
     */
    public Map<L, Integer> getIncoming() {
        return new HashMap<L, Integer>(inEdges);
    }
    
    /**
     * Get outcoming edges of the vertex
     * 
     * @return outcoming edges
     */
    public Map<L, Integer> getOutcoming() {
        return new HashMap<L, Integer>(outEdges);
    }
    
    /**
     * Add an incoming edge to this vertex and an outcoming edge
     * to the source vertex.  
     * Replace current weight value, if edge is already present.
     * 
     * @param source source vertex to add edge from
     * @param weight weight of an edge, must be >0
     * @return the previous weight of the edge, or zero if there was 
     *         no such edge
     */
    public Integer addIncoming(Vertex<L> source, Integer weight){
        if (inEdges.containsKey(source.getLabel())) {
            Integer oldWeight = inEdges.put(source.getLabel(), weight);
            source.outEdges.put(this.getLabel(), weight);
            checkRep();
            return oldWeight;
        }
        else {
            inEdges.put(source.getLabel(), weight);
            source.outEdges.put(this.getLabel(), weight);
            checkRep();
            return 0;
        }
    }
    
    /**
     * Add an outcoming edge to this vertex and an incoming vertex
     * to the target vertex. 
     * Replace current weight value, if edge is already present
     * 
     * @param target target vertex to add edge to
     * @param weight weight of an edge, must be >0
     * @return the previous weight of the edge, or zero if there was 
     *         no such edge
     */
    public Integer addOutcoming(Vertex<L> target, Integer weight){
        if (outEdges.containsKey(target.getLabel())) {
            Integer oldWeight = outEdges.put(target.getLabel(), weight);
            target.inEdges.put(this.getLabel(), weight);
            checkRep();
            return oldWeight;
        }
        else {
            outEdges.put(target.getLabel(), weight);
            target.inEdges.put(this.getLabel(), weight);
            checkRep();
            return 0;
        }
    }
    
    /**
     * Remove an incoming edge of this vertex, 
     * and remove outcoming edge of the source vertex 
     * 
     * @param source source vertex to remove edge from
     * @return true if this vertex had edge incoming from the source vertex;
     *         otherwise false(and this vertex is not modified)
     */
    public boolean removeIncoming(Vertex<L> source) {
        if (inEdges.containsKey(source.getLabel())) {
            inEdges.remove(source.getLabel());
            source.outEdges.remove(this.getLabel());
            checkRep();
            return true;
        }
        else {
            checkRep();
            return false;
        }
    }
    
    /**
     * Remove an outcoming edge of this vertex, 
     * and remove incoming edge of the target vertex 
     * 
     * @param target target vertex to remove edge to
     * @return true if this vertex had edge outcoming to the source vertex;
     *         otherwise false(and this vertex is not modified)
     */
    public boolean removeOutcoming(Vertex<L> target) {
        if (outEdges.containsKey(target.getLabel())) {
            outEdges.remove(target.getLabel());
            target.inEdges.remove(this.getLabel());
            checkRep();
            return true;
        }
        else {
            checkRep();
            return false;
        }
    }
    
    @Override public String toString() {
        return label + ": inEdges" + getIncoming().toString() 
               + "\toutEdges" + getOutcoming().toString() + "\n";
    }
}
