/*
        b)
        You are given an integer n representing the total number of individuals. Each individual is identified by a unique
        ID from 0 to n-1. The individuals have a unique secret that they can share with others.
        The secret-sharing process begins with person 0, who initially possesses the secret. Person 0 can share the secret
        with any number of individuals simultaneously during specific time intervals. Each time interval is represented by
        a tuple (start, end) where start and end are non-negative integers indicating the start and end times of the interval.
        You need to determine the set of individuals who will eventually know the secret after all the possible secret-
        sharing intervals have occurred.
        Example:
        Input: n = 5, intervals = [(0, 2), (1, 3), (2, 4)], firstPerson = 0
        Output: [0, 1, 2, 3, 4]
        Explanation:
        In this scenario, we have 5 individuals labeled from 0 to 4.
        The secret-sharing process starts with person 0, who has the secret at time 0. At time 0, person 0 can share the
        secret with any other person. Similarly, at time 1, person 0 can also share the secret. At time 2, person 0 shares the
        secret again, and so on.
        Given the intervals [(0, 2), (1, 3), (2, 4)], we can observe that during these intervals, person 0 shares the secret with
        every other individual at least once.
        Hence, after all the secret-sharing intervals, individuals 0, 1, 2, 3, and 4 will eventually know the secret.
 */

package Task2;

import java.util.*;

public class Secretloop {

    public static List<Integer> findIndividuals(int n, int [][] intervals, int p1, String secretMessage) {
        Set<Integer> aunty = new HashSet<Integer>();
        aunty.add(p1); // person 0 knows the secret

        for (int[] interval: intervals) {
            int start = interval[0];
            int end = interval[1];

            // Add individuals who received the secret during this interval
            for (int i = start; i <= end; i++) {
                aunty.add(i);
            }
        }
        // System.out.println("Individuals who eventually know the secret: " + aunty);
        
        return new ArrayList<>(aunty);
    }
    public static void main(String[] args) {
        int n = 5;
        int [][] intervals = {{0,2}, {1,3}, {2,4}};
        int firstPerson = 0;
        String secretMessage = "Apple and Onion is good for health";
        
        List<Integer> result = findIndividuals(n, intervals, firstPerson, secretMessage);
        System.out.println("Individuals who eventually know the secret: " + result);
    }
}

// Time Complexity: O(N + M), where N is the number of individuals and M is the total number of intervals.
// Space Complexity: O(N), where N is the number of individuals.
// Algorithm Used: Simple iteration through intervals with HashSet for tracking known individuals.
