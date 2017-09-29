/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package poet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import graph.Graph;

/**
 * A graph-based poetry generator.
 * 
 * <p>GraphPoet is initialized with a corpus of text, which it uses to derive a
 * word affinity graph.
 * Vertices in the graph are words. Words are defined as non-empty
 * case-insensitive strings of non-space non-newline characters. They are
 * delimited in the corpus by spaces, newlines, or the ends of the file.
 * Edges in the graph count adjacencies: the number of times "w1" is followed by
 * "w2" in the corpus is the weight of the edge from w1 to w2.
 * 
 * <p>For example, given this corpus:
 * <pre>    Hello, HELLO, hello, goodbye!    </pre>
 * <p>the graph would contain two edges:
 * <ul><li> ("hello,") -> ("hello,")   with weight 2
 *     <li> ("hello,") -> ("goodbye!") with weight 1 </ul>
 * <p>where the vertices represent case-insensitive {@code "hello,"} and
 * {@code "goodbye!"}.
 * 
 * <p>Given an input string, GraphPoet generates a poem by attempting to
 * insert a bridge word between every adjacent pair of words in the input.
 * The bridge word between input words "w1" and "w2" will be some "b" such that
 * w1 -> b -> w2 is a two-edge-long path with maximum-weight weight among all
 * the two-edge-long paths from w1 to w2 in the affinity graph.
 * If there are no such paths, no bridge word is inserted.
 * In the output poem, input words retain their original case, while bridge
 * words are lower case. The whitespace between every word in the poem is a
 * single space.
 * 
 * <p>For example, given this corpus:
 * <pre>    This is a test of the Mugar Omni Theater sound system.    </pre>
 * <p>on this input:
 * <pre>    Test the system.    </pre>
 * <p>the output poem would be:
 * <pre>    Test of the system.    </pre>
 * 
 * <p>PS2 instructions: this is a required ADT class, and you MUST NOT weaken
 * the required specifications. However, you MAY strengthen the specifications
 * and you MAY add additional methods.
 * You MUST use Graph in your rep, but otherwise the implementation of this
 * class is up to you.
 */
public class GraphPoet {
    
    private final Graph<String> graph = Graph.empty();
    
    // Abstraction function:
    //   AF(graph) = a poetry generator
    //
    // Representation invariant:
    //   vertices of the graph are non-empty case-insensitive strings 
    //   of non-space non-newline characters
    //
    // Safety from rep exposure:
    //   graph field is private and final;
    
    // Check that the rep invariant is true
    private void checkRep() {
        for (String vertex : graph.vertices()) {
            String copy = vertex.toLowerCase().trim().replaceAll("\\s+", "");
            
            assert vertex.equals(copy);
            assert !vertex.equals("");
        }
    }
    
    // constructor
    /**
     * Create a new poet with the graph from corpus (as described above).
     * 
     * @param corpus text file from which to derive the poet's affinity graph
     * @throws IOException if the corpus file cannot be found or read
     */
    public GraphPoet(File corpus) throws IOException {
        // read file and add words to wordList
        List<String> wordList = new ArrayList<>();
        Scanner s = new Scanner(new BufferedReader(new FileReader(corpus)));
        while (s.hasNext()) {
          wordList.add(s.next());
        }
        s.close();
        // convert to lower case
        for (int i=0; i < wordList.size(); i++) {
            wordList.set(i, wordList.get(i).toLowerCase());
        }
        // add words to graph
        for (String word : wordList) {
            if (!graph.vertices().contains(word)) {
                graph.add(word);
            }
        }
        // set edges
        for (int i = 0; i < wordList.size()-1; i++) {
            String src = wordList.get(i);
            String trg = wordList.get(i+1);
            Map<String, Integer> targets = graph.targets(src);
            
            if (targets.containsKey(trg)) {
                int oldWeight = targets.get(trg);
                graph.set(src, trg, oldWeight + 1);
            }
            else {
                graph.set(src, trg, 1);
            }
        }
        checkRep();
    }
    
    // methods
    /**
     * Generate a poem.
     * 
     * @param input string from which to create the poem
     * @return poem (as described above)
     */
    public String poem(String input) {
        List<String> inputList = Arrays.asList(input.trim().split("\\s+"));
        List<String> result = new ArrayList<>();
        
        for (int i = 0; i < inputList.size()-1; i++) {
            String bridge = "";
            String src = inputList.get(i).toLowerCase();
            String trg = inputList.get(i+1).toLowerCase();
            Map<String, Integer> trgSources = graph.sources(trg);
            Map<String, Integer> srcTargets = graph.targets(src);
            Set<String> s1 = new HashSet<>(trgSources.keySet());
            Set<String> s2 = new HashSet<>(srcTargets.keySet());
            
            // keys common to both maps
            Set<String> commonKeys = new HashSet<String>(s2);
            commonKeys.retainAll(s1);
            
            // find highest-weighted bridge words
            Map<String, Integer> bridgeMap = new HashMap<>();
            if (commonKeys.size() > 0) {
                for (String bridgeKey: commonKeys) {
                    int totalWeight = srcTargets.get(bridgeKey) + trgSources.get(bridgeKey);
                    bridgeMap.put(bridgeKey, totalWeight);
                }
                List<Map.Entry<String, Integer>> nameList = new ArrayList<>(bridgeMap.entrySet());
                Collections.sort(nameList, new Comparator<Map.Entry<String, Integer>>() {
                    public int compare(Map.Entry<String, Integer> entry1, Map.Entry<String, Integer> entry2) {
                        return -entry1.getValue().compareTo(entry2.getValue());
                    }
                });
                bridge = nameList.get(0).getKey();
            }
            result.add(inputList.get(i));
            if(!bridge.equals("")) {
                result.add(bridge);
            }
        }
        result.add(inputList.get(inputList.size()-1));
        checkRep();
        return String.join(" ", result);
    }
    
    @Override public String toString() {
        return graph.toString();
    }
}
