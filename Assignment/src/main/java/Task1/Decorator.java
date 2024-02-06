/* 
        Question 1
        a)
        You are a planner working on organizing a series of events in a row of n venues. Each venue can be decorated with
        one of the k available themes. However, adjacent venues should not have the same theme. The cost of decorating
        each venue with a certain theme varies.
        The costs of decorating each venue with a specific theme are represented by an n x k cost matrix. For example,
        costs [0][0] represents the cost of decorating venue 0 with theme 0, and costs[1][2] represents the cost of
        decorating venue 1 with theme 2. Your task is to find the minimum cost to decorate all the venues while adhering
        to the adjacency constraint.
        For example, given the input costs = [[1, 5, 3], [2, 9, 4]], the minimum cost to decorate all the venues is 5. One
        possible arrangement is decorating venue 0 with theme 0 and venue 1 with theme 2, resulting in a minimum cost of
        1 + 4 = 5. Alternatively, decorating venue 0 with theme 2 and venue 1 with theme 0 also yields a minimum cost of
        3 + 2 = 5.
        Write a function that takes the cost matrix as input and returns the minimum cost to decorate all the venues while
        satisfying the adjacency constraint.
        Please note that the costs are positive integers.
        Example: Input: [[1, 3, 2], [4, 6, 8], [3, 1, 5]] Output: 7
        Explanation: Decorate venue 0 with theme 0, venue 1 with theme 1, and venue 2 with theme 0. Minimum cost: 1 +
        6 + 1 = 7. 
*/


package Task1;

public class Decorator {
    public static int minCost(int[][] costs) {

        // return 0 if the matrix is empty
        if (costs == null || costs.length == 0 || costs[0].length == 0) {
            return 0;
        }

        int n = costs.length;
        int k = costs[0].length;

        // dp[i][j] represents the minimum cost to decorate venues up to index i with theme j
        int[][] dp = new int[n][k];

        // Initialize the first row of dp with the costs of decorating the first venue
        for (int i = 0; i < k; i++) {
            dp[0][i] = costs[0][i];
        }

        // Iterate through each venue starting from the second one
        for (int i = 1; i < n; i++) {
            // Iterate through each theme for the current venue
            for (int j = 0; j < k; j++) {
                // Calculate the minimum cost for the current theme considering the adjacency constraint
                dp[i][j] = costs[i][j] + minCostOfPreviousVenue(dp, i - 1, j, k);
            }
        }

        // Finding the minimum cost in the last row of dp
        int result = Integer.MAX_VALUE;
        for (int i = 0; i < k; i++) {
            result = Math.min(result, dp[n - 1][i]);
        }

        // return final result
        return result;
    }

    private static int minCostOfPreviousVenue(int[][] dp, int row, int currentTheme, int k) {
        int minCost = Integer.MAX_VALUE;
        for (int i = 0; i < k; i++) {
            if (i != currentTheme) {
                minCost = Math.min(minCost, dp[row][i]);
            }
        }
        return minCost;
    }

    public static void main(String[] args) {
        int[][] costs = {{1, 3, 2}, {4, 6, 8}, {3, 1, 5}};
        int result = minCost(costs);
        System.out.println("Well Minimum cost to decorate all venues: " + result);
    }
}


// time complexity :  O(n*k^2)
// space complexity : O(n*k)
// used Dynamic Programming approach due to multiple recursion. 