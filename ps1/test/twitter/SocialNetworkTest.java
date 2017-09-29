/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class SocialNetworkTest {

    /*
     * your testing strategies for these methods should go here.
     * See the ic03-testing exercise for examples of what a testing strategy comment looks like.
     * Make sure you have partitions.
     * 
     * Partition for guessFollowsGraph():
     * tweets.size: 0, 1, > 1
     * number of "@" mentions: 0, 1, > 1 
     * 
     * check that user is not following himself
     * check that the list is not modified
     * 
     * Partition for influencers():
     * followsGraph.keySet().size(): 0, 1, > 1 
     * followsGraph.get(key).size(): 0, 1, > 1
     * 
     * result in descending order
     * 
     */
    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-18T12:00:00Z");
    private static final Instant d4 = Instant.parse("2016-02-18T13:00:00Z");
    private static final Instant d5 = Instant.parse("2016-02-18T14:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet3 = new Tweet(3, "bbitdiddle", "@alyssa sup?", d3);
    private static final Tweet tweet4 = new Tweet(4, "alyssa", "@bbitdiddle not much, super tired", d4);
    private static final Tweet tweet5 = new Tweet(5, "mike", "@Bbitdiddle message me at mike@gmail.com!", d5);
    private static final Tweet tweet6 = new Tweet(5, "mike", "@Mike I messaged myself", d5);
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    // test guessFollowsGraph()
    
    // covers tweets.size = 0
    @Test
    public void testGuessFollowsGraphEmpty() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(new ArrayList<>());
        
        assertTrue("expected empty graph", followsGraph.isEmpty());
    }
    
    // covers tweets.size = 1
    //        number of "@" mentions = 0
    @Test
    public void testGuessFollowsGraphOneTweetNoMention() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1));
        
        Set<String> values = new HashSet<>();
        
        assertEquals("expected key 'alyssa' contaning no values", values, followsGraph.get("alyssa"));
    }
    
    // covers tweets.size > 1
    //        number of "@" mentions = 1, > 1
    @Test
    public void testGuessFollowsGraphMultipleTweetsMultipleMentions() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet3, tweet4, tweet5));
        Map<String, Set<String>> followsGraphLower = new HashMap<>();
        
        for (String key : followsGraph.keySet()) {
            Set<String> value = ExtractTest.toLowerCase(followsGraph.get(key));
            followsGraphLower.put(key.toLowerCase(), value);
        }
        
        Set<String> values1 = new HashSet<String>();
        values1.add("alyssa");
        Set<String> values2 = new HashSet<String>();
        values2.add("bbitdiddle");
        
        assertTrue("expected graph containing keys", followsGraphLower.keySet().containsAll(Arrays.asList("alyssa", "bbitdiddle", "mike")));
        assertEquals("expected key 'alyssa' contaning values", values2, followsGraphLower.get("alyssa"));
        assertEquals("expected key 'bbitdiddle' contaning values", values1, followsGraphLower.get("bbitdiddle"));
        assertEquals("expected key 'mike' contaning values", values2, followsGraphLower.get("mike"));
    }
    
    // checks for mutation
    @Test
    public void testGuessFollowsGraphMutation() {
        List<Tweet> tweets = Arrays.asList(tweet1, tweet3, tweet4, tweet5);
        SocialNetwork.guessFollowsGraph(tweets);
        
        assertEquals("expected unmodified list", Arrays.asList(tweet1, tweet3, tweet4, tweet5), tweets);
    }
    
    // checks that user is not following himself/herself
    @Test
    public void testGuessFollowsGraphNotFollowingYourself() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet6));
        
        Set<String> values = new HashSet<>();
        
        assertEquals("expected key 'mike' contaning no values", values, followsGraph.get("mike"));
    }

    
    //test influencers()
    
    // covers followsGraph.keySet().size() = 0
    //        followsGraph.get(key).size() = 0
    @Test
    public void testInfluencersEmpty() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertTrue("expected empty list", influencers.isEmpty());
    }
    
    // covers followsGraph.keySet().size() = 1
    //        followsGraph.get(key).size() = 1
    @Test
    public void testInfluencersOneInfluencer() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        Set<String> following = new HashSet<>(Arrays.asList("bbitdiddle"));
        followsGraph.put("alyssa", following);
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        for (int i = 0; i < influencers.size(); i++) {
            influencers.set(i, influencers.get(i).toLowerCase());
        }
        
        assertEquals("expected list of size 2", 2, influencers.size());
        assertEquals("expected same order", 0, influencers.indexOf("bbitdiddle"));
        assertEquals("expected same order", 1, influencers.indexOf("alyssa"));
    }
    
    // covers followsGraph.keySet().size() > 1
    //        followsGraph.get(key).size() > 1
    //        descending order
    @Test
    public void testInfluencersMultipleInfluencers() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        
        Set<String> Following1 = new HashSet<>(Arrays.asList("alyssa"));
        Set<String> Following2 = new HashSet<>(Arrays.asList("bbitdiddle"));
        
        followsGraph.put("alyssa", Following2);
        followsGraph.put("bbitdiddle", Following1);
        followsGraph.put("mike", Following2);
        
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        for (int i = 0; i < influencers.size(); i++) {
            influencers.set(i, influencers.get(i).toLowerCase());
        }
        
        assertEquals("expected list of size 3", 3, influencers.size());
        assertEquals("expected same order", 0, influencers.indexOf("bbitdiddle"));
        assertEquals("expected same order", 1, influencers.indexOf("alyssa"));
        assertEquals("expected same order", 2, influencers.indexOf("mike"));
    }

    /*
     * Warning: all the tests you write here must be runnable against any
     * SocialNetwork class that follows the spec. It will be run against several
     * staff implementations of SocialNetwork, which will be done by overwriting
     * (temporarily) your version of SocialNetwork with the staff's version.
     * DO NOT strengthen the spec of SocialNetwork or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in SocialNetwork, because that means you're testing a
     * stronger spec than SocialNetwork says. If you need such helper methods,
     * define them in a different class. If you only need them in this test
     * class, then keep them in this test class.
     */

}
