/*
        b) Assume you were hired to create an application for an ISP, and there are n network devices, such as routers,
        that are linked together to provide internet access to users. You are given a 2D array that represents network
        connections between these network devices. write an algorithm to return impacted network devices, If there is
        a power outage on a certain device, these impacted device list assist you notify linked consumers that there is a
        power outage and it will take some time to rectify an issue.
        Input: edges= {{0,1},{0,2},{1,3},{1,6},{2,4},{4,6},{4,5},{5,7}}
        Target Device (On which power Failure occurred): 4
        Output (Impacted Device List) = {5,7} 
 
 */

//  Algorithm Approach:

//  Depth-First Search (DFS)

// Time Complexity:

//  O(N + E), where N is the number of devices and E is the number of edges in the network. Each edge is visited once in the worst case.

// Space Complexity:

//  O(N), due to the use of sets for tracking visited and impacted devices, assuming the recursion stack space does not exceed O(N) in practical scenarios.


package Task5;

import java.util.*;

public class ISP {

    public static List<Integer> findImpactedDevices(int[][] edges, int targetDevice) {
        Set<Integer> impactedDevices = new HashSet<>();
        Set<Integer> visited = new HashSet<>();
        
        for (int[] edge : edges) {
            if (edge[0] == targetDevice || edge[1] == targetDevice) {
                dfs(edges, edge[0], targetDevice, visited, impactedDevices);
                dfs(edges, edge[1], targetDevice, visited, impactedDevices);
            }
        }

        return new ArrayList<>(impactedDevices);
    }

    private static void dfs(int[][] edges, int current, int targetDevice, Set<Integer> visited, Set<Integer> impacted) {
        if (!visited.contains(current)) {
            visited.add(current);
            impacted.add(current);

            for (int[] edge : edges) {
                if (edge[0] == current && edge[1] != targetDevice) {
                    dfs(edges, edge[1], targetDevice, visited, impacted);
                } else if (edge[1] == current && edge[0] != targetDevice) {
                    dfs(edges, edge[0], targetDevice, visited, impacted);
                }
            }
        }
    }

    public static void main(String[] args) {
        int[][] edges = {{0, 1}, {0, 2}, {1, 3}, {1, 6}, {2, 4}, {4, 6}, {4, 5}, {5, 7}};
        int targetDevice = 4;

        List<Integer> impactedDevices = findImpactedDevices(edges, targetDevice);

        System.out.println("Impacted Device List: " + impactedDevices);
    }
}


