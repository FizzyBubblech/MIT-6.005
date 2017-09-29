package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class MySocialNetworkTest {
    
    /*
     * 
     * Partition for guessFollowsGraph():
     * number of usernames sharing "#": 0, > 0
     * 
     */
    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "OMG #hype", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(2, "mike", "super #hype", d2);
    private static final Tweet tweet4 = new Tweet(2, "den", "check out this #topkek", d2);
    private static final Tweet tweet5 = new Tweet(2, "bill", "who #resist here???", d2);
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
   
    // covers sharing "#": 0
    @Test
    public void testGuessFollowsGraphZeroSharing() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet4, tweet5));
        Map<String, Set<String>> followsGraphLower = new HashMap<>();
        
        for (String key : followsGraph.keySet()) {
            Set<String> value = ExtractTest.toLowerCase(followsGraph.get(key));
            followsGraphLower.put(key.toLowerCase(), value);
        }
        
        Set<String> values = new HashSet<String>();
        
        assertTrue("expected graph containing keys", followsGraphLower.keySet().containsAll(Arrays.asList("den", "bill")));
        assertEquals("expected key 'bill' contaning values", values, followsGraphLower.get("den"));
        assertEquals("expected key 'den' contaning values", values, followsGraphLower.get("bill"));
    }
    
    // covers sharing "#": > 0
    @Test
    public void testGuessFollowsGraphMultipleSharing() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1, tweet2, tweet3));
        Map<String, Set<String>> followsGraphLower = new HashMap<>();
        
        for (String key : followsGraph.keySet()) {
            Set<String> value = ExtractTest.toLowerCase(followsGraph.get(key));
            followsGraphLower.put(key.toLowerCase(), value);
        }
        
        Set<String> values1 = new HashSet<String>();
        values1.addAll(Arrays.asList("alyssa", "bbitdiddle"));
        Set<String> values2 = new HashSet<String>();
        values2.addAll(Arrays.asList("bbitdiddle", "mike"));
        Set<String> values3 = new HashSet<String>();
        values3.addAll(Arrays.asList("alyssa", "mike"));
        
        assertTrue("expected graph containing keys", followsGraphLower.keySet().containsAll(Arrays.asList("alyssa", "bbitdiddle", "mike")));
        assertEquals("expected key 'alyssa' contaning values", values2, followsGraphLower.get("alyssa"));
        assertEquals("expected key 'bbitdiddle' contaning values", values3, followsGraphLower.get("bbitdiddle"));
        assertEquals("expected key 'mike' contaning values", values1, followsGraphLower.get("mike"));
    }

}
