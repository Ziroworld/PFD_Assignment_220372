/*
        Question 2
        a)
        You are the manager of a clothing manufacturing factory with a production line of super sewing machines. The
        production line consists of n super sewing machines placed in a line. Initially, each sewing machine has a certain
        number of dresses or is empty.
        For each move, you can select any m (1 <= m <= n) consecutive sewing machines on the production line and pass
        one dress from each selected sewing machine to its adjacent sewing machine simultaneously.
        Your goal is to equalize the number of dresses in all the sewing machines on the production line. You need to
        determine the minimum number of moves required to achieve this goal. If it is not possible to equalize the number
        of dresses, return -1.
        Input: [2, 1, 3, 0, 2]
        Output: 5
        Example 1:
        Imagine you have a production line with the following number of dresses in each sewing machine: [2, 1, 3, 0, 2].
        The production line has 5 sewing machines.
        Here's how the process works:
        1. Initial state: [2, 1, 3, 0, 2]
        2. Move 1: Pass one dress from the second sewing machine to the first sewing machine, resulting in [2, 2, 2,
        0, 2]
        3. Move 2: Pass one dress from the second sewing machine to the first sewing machine, resulting in [3, 1, 2,
        0, 2]
        4. Move 3: Pass one dress from the third sewing machine to the second sewing machine, resulting in [3, 2, 1,
        0, 2]
        5. Move 4: Pass one dress from the third sewing machine to the second sewing machine, resulting in [3, 3, 0,
        0, 2]
        6. Move 5: Pass one dress from the fourth sewing machine to the third sewing machine, resulting in [3, 3, 1,
        0, 1]
        After these 5 moves, the number of dresses in each sewing machine is equalized to 1. Therefore, the minimum
        number of moves required to equalize the number of dresses is 5.

 */

package Task2;

import java.util.Arrays;

public class DressEqualizer {

    public static int minMovesToEqualize(int[] machines) {
        int totalDresses = 0;
        int n = machines.length;

        // Calculate total dresses
        for (int dresses : machines) {
            totalDresses += dresses;
        }

        // Check if equal distribution is possible
        if (totalDresses % n != 0) {
            return -1;
        }

        // Calculate target dresses for each machine
        int targetDresses = totalDresses / n;

        int moves = 0;
        int balance = 0;

        // Iterate through machines and perform greedy adjustments
        for (int dresses : machines) {
            // Calculate imbalance for the current machine
            balance += dresses - targetDresses;

            // Accumulate moves by taking absolute imbalance
            // as we are moving dresses between adjacent machines
            moves += Math.abs(balance);

            // Update balance for the next machine
            balance = Math.max(balance, 0);
        }

        return moves;
    }

    public static void main(String[] args) {
        int[] machines = {2, 1, 3, 0, 2};
        int result = minMovesToEqualize(machines);
        System.out.println("Input: " + Arrays.toString(machines));
        System.out.println("Minimum moves to equalize: " + result);
    }
}

// Time Complexity: O(n) where n is the number of sewing machines.
// Space Complexity: O(1) constant amount of space.
// Algorithm: Greedy Algorithm

// Pseudo Code
/*
1. Calculate the total number of dresses (sum) across all sewing machines.
2. If sum is not divisible by the number of machines (n), return -1 (it's impossible to equalize).
3. Calculate the target number of dresses for each machine (target = sum / n).
4. Initialize a variable to track the total number of moves required.
5. Loop through the array of sewing machines:
    a. For each machine, calculate the difference between the current number of dresses and the target.
    b. Add this difference to a running difference total (this tracks the net dresses passed along the line).
    c. The absolute value of the running difference at each step is the minimum number of moves required up to that point.
    d. Update the total number of moves required with the maximum of its current value or the absolute running difference.
6. Return the total number of moves required.
*/
