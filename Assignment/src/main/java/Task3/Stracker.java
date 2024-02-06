/*
        Question 3
        a) You are developing a student score tracking system that keeps track of scores from different assignments. The
        ScoreTracker class will be used to calculate the median score from the stream of assignment scores. The class
        should have the following methods:
         ScoreTracker() initializes a new ScoreTracker object.
         void addScore(double score) adds a new assignment score to the data stream.
         double getMedianScore() returns the median of all the assignment scores in the data stream. If the number
        of scores is even, the median should be the average of the two middle scores.
        Input:
        ScoreTracker scoreTracker = new ScoreTracker();
        scoreTracker.addScore(85.5); // Stream: [85.5]
        scoreTracker.addScore(92.3); // Stream: [85.5, 92.3]
        scoreTracker.addScore(77.8); // Stream: [85.5, 92.3, 77.8]
        scoreTracker.addScore(90.1); // Stream: [85.5, 92.3, 77.8, 90.1]
        double median1 = scoreTracker.getMedianScore(); // Output: 88.9 (average of 90.1 and 85.5)
        scoreTracker.addScore(81.2); // Stream: [85.5, 92.3, 77.8, 90.1, 81.2]
        scoreTracker.addScore(88.7); // Stream: [85.5, 92.3, 77.8, 90.1, 81.2, 88.7]
        double median2 = scoreTracker.getMedianScore(); // Output: 86.95 (average of 88.7 and 85.5)
 */


package Task3;

import java.util.Collections;
import java.util.PriorityQueue;

public class Stracker {

    PriorityQueue<Double> maxHeap;  // Stores the lower half of the scores
    PriorityQueue<Double> minHeap;  // Stores the upper half of the scores

    public Stracker() {
        maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        minHeap = new PriorityQueue<>();
    }

    public void addScore(double score) {
        maxHeap.offer(score);
        minHeap.offer(maxHeap.poll());
        
        // Balance the heaps
        if (maxHeap.size() < minHeap.size()) {
            maxHeap.offer(minHeap.poll());
        }
    }

    public double getMedianScore() {
        if (maxHeap.isEmpty() && minHeap.isEmpty()) {
            return 0.0;  // No scores available
        }

        if (maxHeap.size() == minHeap.size()) {
            return (maxHeap.peek() + minHeap.peek()) / 2.0;
        } else {
            return maxHeap.peek();
        }
    }

    public static void main(String[] args) {
        Stracker st = new Stracker();
        st.addScore(85.5);
        st.addScore(92.3);
        st.addScore(77.8);

        double median1 = st.getMedianScore();
        System.out.println("Median 1: " + median1);

        st.addScore(90.1);
        st.addScore(81.2);
        st.addScore(88.7);

        double median2 = st.getMedianScore();
        System.out.println("Median 2: " + median2);
    }
}

// Time Complexity: O(log N) for each score addition and O(1) for calculating the median.
// Space Complexity: O(N), where N is the number of scores.
// algortithim used:  "Two Heaps" algorithm.
