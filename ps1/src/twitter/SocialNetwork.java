/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * SocialNetwork provides methods that operate on a social network.
 * 
 * A social network is represented by a Map<String, Set<String>> where map[A] is
 * the set of people that person A follows on Twitter, and all people are
 * represented by their Twitter usernames. Users can't follow themselves. If A
 * doesn't follow anybody, then map[A] may be the empty set, or A may not even exist
 * as a key in the map; this is true even if A is followed by other people in the network.
 * Twitter usernames are not case sensitive, so "ernie" is the same as "ERNie".
 * A username should appear at most once as a key in the map or in any given
 * map[A] set.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class SocialNetwork {

    /**
     * Guess who might follow whom, from evidence found in tweets.
     * 
     * @param tweets
     *            a list of tweets providing the evidence, not modified by this
     *            method.
     * @return a social network (as defined above) in which Ernie follows Bert
     *         if and only if there is evidence for it in the given list of
     *         tweets.
     *         One kind of evidence that Ernie follows Bert is if Ernie
     *         @-mentions Bert in a tweet. This must be implemented. Other kinds
     *         of evidence may be used at the implementor's discretion.
     *         All the Twitter usernames in the returned social network must be
     *         either authors or @-mentions in the list of tweets.
     */
    public static Map<String, Set<String>> guessFollowsGraph(List<Tweet> tweets) {
        Map<String, Set<String>> socialNetwork = new HashMap<>();
        Map<String, Set<String>> userHashtags = new HashMap<>();
        
        if (tweets.isEmpty()) {
            return socialNetwork;
        } else {
            // map users by mentions "@" in tweets
            for (Tweet tweet : tweets) {
                String author = tweet.getAuthor().toLowerCase();
                Set<String> mentionedUsernames = Extract.getMentionedUsers(Arrays.asList(tweet));
                // remove self-mention
                if (mentionedUsernames.contains(author)) {
                    mentionedUsernames.remove(author);
                }
                if (socialNetwork.containsKey(author)) {
                    socialNetwork.get(author).addAll(mentionedUsernames);
                } else {
                    socialNetwork.put(author, mentionedUsernames);
                }
                
                // map hashtags to user
                Set<String> hashtags = getHashtags(tweet.getText());
                if (userHashtags.containsKey(author)) {
                    userHashtags.get(author).addAll(hashtags);
                } else {
                    userHashtags.put(author, hashtags);
                }  
             }
            // map users to each other by common hashtags
            for (String user1 : userHashtags.keySet()) {
                for (String user2 : userHashtags.keySet()) {
                    if (!user1.equals(user2)) {
                        Set<String> user1Hashtags = new HashSet<>(userHashtags.get(user1));
                        if (user1Hashtags.removeAll(userHashtags.get(user2))) {
                            if (socialNetwork.containsKey(user1)) {
                                socialNetwork.get(user1).add(user2);
                            } else {
                                socialNetwork.put(user1, new HashSet<String>(Arrays.asList(user2)));
                            }
                            if (socialNetwork.containsKey(user2)) {
                                socialNetwork.get(user2).add(user1);
                            } else {
                                socialNetwork.put(user2, new HashSet<String>(Arrays.asList(user2)));
                            }
                        }
                    }
                }
            }
        }   
        return socialNetwork;
    }
        
    
    /**
     * Finds hashtags in a String
     * 
     * @param text
     *            tweet text
     * @return Set of hashtags in tweet text
     */
    private static Set<String> getHashtags(String text){
        Set<String> hashtags = new HashSet<>();
        String[] words = text.split("\\s");
        
        for (String word : words) {
            if (Pattern.matches("#([A-Za-z0-9_-]+)", word)) {
                word = word.substring(1).toLowerCase();
                hashtags.add(word);
            }
        }
        return hashtags;
    }


    /**
     * Find the people in a social network who have the greatest influence, in
     * the sense that they have the most followers.
     * 
     * @param followsGraph
     *            a social network (as defined above)
     * @return a list of all distinct Twitter usernames in followsGraph, in
     *         descending order of follower count.
     */
    public static List<String> influencers(Map<String, Set<String>> followsGraph) {
        List<String> influenceList = new ArrayList<>();
        Map<String, Integer> influencers = new HashMap<>();
        
        // add followed users to map and count their followers
        for (Set<String> follows : followsGraph.values()) {
            for (String followedUser : follows) {
                followedUser = followedUser.toLowerCase();
                if (influencers.containsKey(followedUser)) {
                    influencers.put(followedUser, influencers.get(followedUser) + 1);
                } else {
                    influencers.put(followedUser, 0);
                }
            }
        }
        // add remaining users
        for (String username : followsGraph.keySet()) {
            username = username.toLowerCase();
            if (!influencers.containsKey(username)) {
                influencers.put(username.toLowerCase(), 0);
            }
        }
        // sort users by followers
        while(!influencers.isEmpty()){
            int topFollowers = Collections.max(influencers.values());
            for (String username : influencers.keySet()){
                if(influencers.get(username).equals(topFollowers)){
                    influenceList.add(username);
                    influencers.remove(username);
                    break;
                }
            }
        }
        return influenceList;
    }
}
