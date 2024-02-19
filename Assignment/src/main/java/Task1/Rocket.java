/*
        b)
        You are the captain of a spaceship and you have been assigned a mission to explore a distant galaxy. Your
        spaceship is equipped with a set of engines, where each engine represented by a block. Each engine requires a
        specific amount of time to be built and can only be built by one engineer.
        Your task is to determine the minimum time needed to build all the engines using the available engineers. The
        engineers can either work on building an engine or split into two engineers, with each engineer sharing the
        workload equally. Both decisions incur a time cost.
        The time cost of splitting one engineer into two engineers is given as an integer split. Note that if two engineers
        split at the same time, they split in parallel so the cost would be split.
        Your goal is to calculate the minimum time needed to build all the engines, considering the time cost of splitting
        engineers.
        Input: engines= [3, 4, 5, 2]
        Split cost (k)=2
        Output: 4
        Example:
        Imagine you have the list of engines: [3, 4, 5, 2] and the split cost is 2. Initially, there is only one engineer
        available.
        The optimal strategy is as follows:
        1. The engineer splits into two engineers, increasing the total count to two. (Time: 2)
        2. Each engineer takes one engine, with one engineer building the engine that requires 3 units of time and the
        other engineer building the engine that requires 4 units of time.
        3. Once the engineer finishes building the engine that requires 3 units of time, the engineer splits into two,
        increasing the total count to three. (Time: 4)
        4. Each engineer takes one engine, with two engineers building the engines that require 2 and 5 units of time,
        respectively.
        Therefore, the minimum time needed to build all the engines using optimal decisions on splitting engineers and
        assigning them to engines is 4 units.
        Note: The splitting process occurs in parallel, and the goal is to minimize the total time required to build all the
        engines using the available engineers while considering the time cost of splitting.
 */

package Task1;
import java.util.Arrays;

public class Rocket {
    public static int engineTime(int[] engines, int splitCost) {
        Arrays.sort(engines); 
        int max = engines.length;
        int[] dp = new int[max + 1]; // Dp array

        // Initialize dp
        Arrays.fill(dp, Integer.MAX_VALUE - splitCost);

        // feed initial values
        dp[1] = engines[0];

        // Iterate through each engine
        for (int i = 2; i <= max; i++) {
            // Iterate from current enginer down to 1 
            for (int j = i; j >= 1; j--) {
                int time = Math.max(dp[j], engines[i - 1]); // Time to build current engine
                // If more than 1 engineer, add splitting cost
                if (j > 1) {
                    time = Math.min(time, dp[j / 2] + splitCost);
                }
                // Update min time for current no of engineer
                dp[i] = Math.min(dp[i], time);
            }
        }
        return dp[max]; // Return value
    }
    public static void main(String[] args) {
        int[] engines = {3, 4, 5, 2};
        int splitCost = 2;
        int minTime = engineTime(engines, splitCost);
        System.out.println("Minimum time needed to build all engines: " + minTime);
    }
}

/*
 *  Time Complexity: O(n^2), where N is the number of engines.
    Space Complexity: O(n), where N is the number of engines.
    Algorithm Used: Dynamic algorithm used in this solution.
 */