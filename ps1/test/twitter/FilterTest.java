/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FilterTest {

    /*
     * your testing strategies for these methods should go here.
     * See the ic03-testing exercise for examples of what a testing strategy comment looks like.
     * Make sure you have partitions.
     * 
     * Partition for writtenBy():
     * tweets.size(): 0, 1, > 1
     * username in the list: 0, 1, > 1
     *           
     * include different cases          
     * result in the same order as input list
     * check that the list is not modified
     * 
     * Partition for inTimespan():
     * tweets.size(): 0, 1, > 1
     * tweets in timespan: 0, 1, > 1
     * length of timespan: 0, > 0
     * 
     * result in the same order as input list
     * check that the list is not modified
     *  
     * Partition for containing():
     * tweets.size(): 0, 1, > 1
     * words.size():  0, 1, > 1
     * tweets containing the word at least once: 0, 1, > 1
     * 
     * include different cases
     * result in same order as input
     * check that the list is not modified
     * 
     */
    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-18T12:00:00Z");
    private static final Instant d4 = Instant.parse("2016-02-18T13:00:00Z");
    private static final Instant d5 = Instant.parse("2016-02-18T14:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "bbitdiddle", "@alyssa sup?", d3);
    private static final Tweet tweet4 = new Tweet(4, "alyssa", "@bbitdiddle not much, super tired", d4);
    private static final Tweet tweet5 = new Tweet(5, "mike", "@Bbitdiddle @Alyssa message me at mike@gmail.com!", d5);
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    // test writtenBy()
    
    // covers tweets.size > 1
    //        username in lowercase
    //        one result
    @Test
    public void testWrittenByLowerCaseMultipleTweetsSingleResult() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "alyssa");
        
        assertEquals("expected singleton list", 1, writtenBy.size());
        assertTrue("expected list to contain tweet", writtenBy.contains(tweet1));
    }
    
    // covers tweets.size = 0
    @Test
    public void testWrittenByZeroTweets() {
        List<Tweet> writtenBy = Filter.writtenBy(new ArrayList<Tweet>(), "alyssa");
        
        assertEquals("expected empty list", 0, writtenBy.size());
    }
    
    // covers tweets.size = 1
    @Test
    public void testWrittenByOneTweet() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet2), "alyssa");
        
        assertEquals("expected empty list", 0, writtenBy.size());
    }
    
    // covers zero results
    @Test
    public void testWrittenByZeroResults() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2, tweet3, tweet4), "mike");
        
        assertEquals("expected empty list", 0, writtenBy.size());
    }
    
    // covers username in uppercase
    //        multiple results
    //        result in same order
    @Test
    public void testWrittenByUpperCaseMultipleResults() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2, tweet3, tweet4), "Alyssa");
        
        assertEquals("expected list of size 2", 2, writtenBy.size());
        assertTrue("expected list to contain tweet", writtenBy.contains(tweet1));
        assertTrue("expected list to contain tweet", writtenBy.contains(tweet4));
        assertEquals("expected same order", 0, writtenBy.indexOf(tweet1));
    }
    
    // checks for mutation
    @Test
    public void testWrittenByMutation() {
        List<Tweet> tweets = Arrays.asList(tweet1, tweet2);
        Filter.writtenBy(Arrays.asList(tweet1, tweet2), "alyssa");
        
        assertEquals("expected unmodified list", Arrays.asList(tweet1, tweet2), tweets);
    }
    
    
    // test inTimespan()
    
    // covers tweets.size() > 1
    //        multiple tweets in the timespan
    //        result in the same order
    @Test
    public void testInTimespanMultipleTweetsMultipleResults() {
        Instant testStart = Instant.parse("2016-02-17T09:00:00Z");
        Instant testEnd = Instant.parse("2016-02-17T12:00:00Z");
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2), new Timespan(testStart, testEnd));
        
        assertFalse("expected non-empty list", inTimespan.isEmpty());
        assertTrue("expected list to contain tweets", inTimespan.containsAll(Arrays.asList(tweet1, tweet2)));
        assertEquals("expected same order", 0, inTimespan.indexOf(tweet1));
    }
    
    // covers tweets.size() = 0
    @Test
    public void testInTimespanZeroTweets() {
        Instant testStart = Instant.parse("2016-02-17T09:00:00Z");
        Instant testEnd = Instant.parse("2016-02-17T12:00:00Z");
        
        List<Tweet> inTimespan = Filter.inTimespan(new ArrayList<Tweet>(), new Timespan(testStart, testEnd));
        
        assertTrue("expected empty list", inTimespan.isEmpty());
    }
    
    // covers tweets.size() = 1
    //        timespan length = 0
    @Test
    public void testInTimespanOneTweetZeroTimespan() {
        Instant testStart = Instant.parse("2016-02-17T09:00:00Z");
        Instant testEnd = Instant.parse("2016-02-17T09:00:00Z");
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1), new Timespan(testStart, testEnd));
        
        assertTrue("expected empty list", inTimespan.isEmpty());
    }
    
    // covers one tweet in the timespan
    //            timespan length > 0
    @Test
    public void testInTimespanOneResult() {
        Instant testStart = Instant.parse("2016-02-17T09:00:00Z");
        Instant testEnd = Instant.parse("2016-02-17T12:00:00Z");
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet3), new Timespan(testStart, testEnd));
        
        assertEquals("expected singleton list", 1, inTimespan.size());
        assertTrue("expected list to contain tweets", inTimespan.containsAll(Arrays.asList(tweet1)));
    }
    
    // covers zero tweets in the timespan
    @Test
    public void testInTimespanZeroResults() {
        Instant testStart = Instant.parse("2016-02-17T09:00:00Z");
        Instant testEnd = Instant.parse("2016-02-17T12:00:00Z");
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet3, tweet4), new Timespan(testStart, testEnd));
        
        assertTrue("expected empty list", inTimespan.isEmpty());
    }
    
    // checks for mutation
    @Test
    public void testInTimespanMutation() {
        Instant testStart = Instant.parse("2016-02-17T09:00:00Z");
        Instant testEnd = Instant.parse("2016-02-17T12:00:00Z");
        List<Tweet> tweets = Arrays.asList(tweet1, tweet2);
        Filter.inTimespan(tweets, new Timespan(testStart, testEnd));
        
        assertEquals("expected unmodified list", Arrays.asList(tweet1, tweet2), tweets);
    }
    
    
    // test containing()
    
    // covers tweets.size() > 1 and
    //        words.size() = 1
    //        in same case 
    //        output in same order as input
    @Test
    public void testContainingOneWordMultipleTweetsSameCase() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("talk"));
        
        assertEquals("expected list of size 2", 2, containing.size());
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet1, tweet2)));
        assertEquals("expected same order", 0, containing.indexOf(tweet1));
    }
    
    // covers tweets.size() = 0
    @Test
    public void testContainingZeroTweets() {
        List<Tweet> containing = Filter.containing(new ArrayList<Tweet>(), Arrays.asList("talk"));
        
        assertTrue("expected empty list", containing.isEmpty());
    }
    
    // covers tweets.size() = 1
    @Test
    public void testContainingOneTweet() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet3), Arrays.asList("talk"));
        
        assertTrue("expected empty list", containing.isEmpty());
    }
    
    // covers words.size() = 0
    @Test
    public void testContainingZeroWords() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), new ArrayList<String>());
        
        assertTrue("expected empty list", containing.isEmpty());
    }
    
    // covers words.size > 1 and
    //        in different cases
    @Test
    public void testContainingMultipleWordsDifferentCase() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2, tweet3, tweet4, tweet5), Arrays.asList("Sup", "MESSAGE"));
        
        assertEquals("expected list of size 2", 2, containing.size());
        assertTrue("expected list to contain tweets 3 and 4", containing.containsAll(Arrays.asList(tweet3, tweet5)));
    }
    
    // checks for mutation
    @Test
    public void testContainingMutation() {
        List<Tweet> tweets = Arrays.asList(tweet1, tweet2);
        List<String> words = Arrays.asList("Sup", "MESSAGE");
        Filter.containing(tweets, words);
        
        assertEquals("expected unmodified list", Arrays.asList(tweet1, tweet2), tweets);
        assertEquals("expected unmodified list", Arrays.asList("Sup", "MESSAGE"), words);
    }
    
    

    /*
     * Warning: all the tests you write here must be runnable against any Filter
     * class that follows the spec. It will be run against several staff
     * implementations of Filter, which will be done by overwriting
     * (temporarily) your version of Filter with the staff's version.
     * DO NOT strengthen the spec of Filter or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Filter, because that means you're testing a stronger
     * spec than Filter says. If you need such helper methods, define them in a
     * different class. If you only need them in this test class, then keep them
     * in this test class.
     */

}
