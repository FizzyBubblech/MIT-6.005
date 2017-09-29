/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An implementation of Graph.
 * 
 * <p>PS2 instructions: you MUST use the provided rep.
 */
public class ConcreteEdgesGraph<L> implements Graph<L> {
    
    private final Set<L> vertices = new HashSet<>();
    private final List<Edge<L>> edges = new ArrayList<>();
    
    // Abstraction function:
    //   AF(vertices, edges) = a directed graph composed of distinct vertices
    //                         connected by weighted edges
    //
    // Representation invariant:
    //   edges have positive weight
    //
    // Safety from rep exposure:
    //   all fields are private and final;
    //   vertices is a mutable Set, so vertices() make defensive copies 
    //   to avoid sharing the rep with clients.
    
    // constructor
    /**
     * Create an empty ConcreteEdgesGraph
     */
    public ConcreteEdgesGraph() {
        checkRep();
    }
    
    // Check that the rep invariant is true
    private void checkRep() {
        assert vertices != null;
        for (Edge<L> edge : edges) {
            assert edge.getWeight() > 0;
        }
    }
    
    // methods
    
    @Override public boolean add(L vertex) {
        boolean result = vertices.add(vertex);
        checkRep();
        return result;
    }
     
    @Override public int set(L source, L target, int weight) {
        // remove or modify existing edge
        for (Edge<L> edge : edges) {
            if (edge.getSource().equals(source) && 
                edge.getTarget().equals(target)) {
                int oldWeight = edge.getWeight();
                
                if (weight == 0) {
                    edges.remove(edge);
                    checkRep();
                    return oldWeight;
                }
                edges.remove(edge);
                edges.add(new Edge<L>(source, target, weight));
                checkRep();
                return oldWeight;
            }
        }
        // else create new edge
        if (weight > 0) {
            if (!vertices.contains(source)) {
                this.add(source);
            }
            if (!vertices.contains(target)) {
                this.add(target);
            }  
            edges.add(new Edge<L>(source, target, weight));
            checkRep();
            return 0;
        }
        checkRep();
        return 0;
    }
    
    @Override public boolean remove(L vertex) {
        if (!vertices.contains(vertex)) {
            checkRep();
            return false;
        }
        vertices.remove(vertex);
        for (Iterator<Edge<L>> iterator = edges.iterator(); iterator.hasNext();) {
            Edge<L> edge = iterator.next();
            
            if (edge.getSource().equals(vertex) ||
                edge.getTarget().equals(vertex)) {
                iterator.remove();
            }
        }
        checkRep();
        return true;
    }
    
    @Override public Set<L> vertices() {
        return new HashSet<L>(vertices);
    }
    
    @Override public Map<L, Integer> sources(L target) {
        Map<L, Integer> result = new HashMap<>();
        for (Edge<L> edge : edges) {
            if (edge.getTarget().equals(target)) {
                result.put(edge.getSource(), edge.getWeight());
            }
        }
        return result;
    }
    
    @Override public Map<L, Integer> targets(L source) {
        Map<L, Integer> result = new HashMap<>();
        for (Edge<L> edge : edges) {
            if (edge.getSource().equals(source)) {
                result.put(edge.getTarget(), edge.getWeight());
            }
        }
        return result;
    }
    
    @Override public String toString() {
        String result = "";
        for (Edge<L> edge : edges) {
            result += edge.toString() + "\n"; 
        }
        return result;
    }
    
}

/**
 * Immutable.
 * This class is internal to the rep of ConcreteEdgesGraph.
 *  
 * This immutable data-type represents an edge in a directed graph.
 * An edge has a source vertex, a target vertex and a positive weight.
 * 
 * <p>PS2 instructions: the specification and implementation of this class is
 * up to you.
 */
class Edge<L> {
    
    // fields
    private final L source;
    private final L target;
    private final Integer weight;
    
    // Abstraction function:
    //   AF(source, target, weight) = an edge in a directed graph with a
    //                                source and target vertices and weight
    //
    // Representation invariant:
    //   weight > 0
    //
    // Safety from rep exposure:
    //   all fields are private and final;
    //   source and target are immutable type L, weight is Integer - so are guaranteed immutable
    
    // constructor
    
    /**
     * Create an Edge with a source name, target name, and weight.
     *  
     * @param source source vertex of the edge
     * @param target target vertex of the edge
     * @param weight weight of the edge
     */
     public Edge(L source, L target, Integer weight) {
         this.source = source;
         this.target = target;
         this.weight = weight;
         checkRep();
     }
    
     // Check that the rep invariant is true
     private void checkRep() {
         assert source != null;
         assert target != null;
         assert weight > 0;
     }
    
     // methods
     
     /**
      * Gets the source of the edge
      * 
      * @return  source of the edge
      */
     public L getSource(){
         return source;
     }
     
     /**
      * Gets the target of the edge
      * 
      * @return  target of the edge
      */
     public L getTarget(){
         return target;
     }
     
     /**
      * Gets the weight of the edge
      * 
      * @return  weight of the edge
      */
     public Integer getWeight(){
         return weight;
     }
     
     @Override
     public String toString(){
         return source + "->" + target + "(weight = " + weight + ")";
     }
}
