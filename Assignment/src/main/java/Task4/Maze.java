/*
        Quesion 4        
        a)
        You are given a 2D grid representing a maze in a virtual game world. The grid is of size m x n and consists of
        different types of cells:
        'P' represents an empty path where you can move freely. 'W' represents a wall that you cannot pass through. 'S'
        represents the starting point. Lowercase letters represent hidden keys. Uppercase letters represent locked doors.
        You start at the starting point 'S' and can move in any of the four cardinal directions (up, down, left, right) to
        adjacent cells. However, you cannot walk through walls ('W').
        As you explore the maze, you may come across hidden keys represented by lowercase letters. To unlock a door
        represented by an uppercase letter, you need to collect the corresponding key first. Once you have a key, you can
        pass through the corresponding locked door.
        For some 1 <= k <= 6, there is exactly one lowercase and one uppercase letter of the first k letters of the English
        alphabet in the maze. This means that there is exactly one key for each door, and one door for each key. The letters
        used to represent the keys and doors follow the English alphabet order.
        Your task is to find the minimum number of moves required to collect all the keys and reach the exit point. The
        exit point is represented by 'E'. If it is impossible to collect all the keys and reach the exit, return -1.
        Example:
        Input: grid = [ ["S","P","P","P"], ["W","P","P","E"], ["P","b","W","P"], ["P","P","P","P"] ]
        Input: grid = ["SPaPP","WWWPW","bPAPB"]
        Output: 8
        The goal is to Collect all key
 */


//  Algorithm: Breadth-First Search (BFS) with state representation.
//  Time Complexity: O(m * n * 2^k)
//  Space Complexity: O(m * n * 2^k)

package Task4;

import java.util.*;

class Maze {
    static final int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // Up, Down, Left, Right

    public static int shortestPathAllKeys(String[] grid) {
        int m = grid.length;
        int n = grid[0].length();
        Queue<Node> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        int startX = 0, startY = 0, totalKeys = 0;

        // Scan the grid to find the starting point and total number of keys
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                char cell = grid[i].charAt(j);
                if (cell == 'S') {
                    startX = i;
                    startY = j;
                } else if (cell >= 'a' && cell <= 'f') {
                    totalKeys |= (1 << (cell - 'a')); // Mark the key as needed to be collected
                }
            }
        }

        queue.offer(new Node(startX, startY, 0, 0)); // Position x, y, steps, keys collected
        visited.add(startX + "," + startY + ",0"); // Initial state

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            if (current.keys == totalKeys) return current.steps; // Found all keys

            for (int[] dir : directions) {
                int newX = current.x + dir[0], newY = current.y + dir[1];
                int newKeys = current.keys;
                if (newX >= 0 && newX < m && newY >= 0 && newY < n) {
                    char nextCell = grid[newX].charAt(newY);
                    if (nextCell == 'W') continue; // Wall
                    if (nextCell >= 'A' && nextCell <= 'F' && (newKeys & (1 << (nextCell - 'A'))) == 0) continue; // Locked door without key
                    if (nextCell >= 'a' && nextCell <= 'f') newKeys |= (1 << (nextCell - 'a')); // Collect key

                    String newState = newX + "," + newY + "," + newKeys;
                    if (!visited.contains(newState)) {
                        visited.add(newState);
                        queue.offer(new Node(newX, newY, current.steps + 1, newKeys));
                    }
                }
            }
        }

        return -1; // If not possible to collect all keys
    }

    static class Node {
        int x, y, steps, keys;

        Node(int x, int y, int steps, int keys) {
            this.x = x;
            this.y = y;
            this.steps = steps;
            this.keys = keys;
        }
    }

    public static void main(String[] args) {
        String[] grid = {"SPaPP", "WWWPW", "bPAPB"};
        System.out.println("Minimum steps: " + shortestPathAllKeys(grid));
    }
}

