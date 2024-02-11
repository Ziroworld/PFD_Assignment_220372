package Task5;

import java.util.*;

public class Q5B {
    int[] disc, low;
    int time = 1;
    List<List<Integer>> ans = new ArrayList<>();
    Map<Integer, List<Integer>> edgeMap = new HashMap<>();

    public List<Integer> findImpactedDevices(int n, List<List<Integer>> connections, int targetDevice) {
        disc = new int[n];
        low = new int[n];
        for (int i = 0; i < n; i++)
            edgeMap.put(i, new ArrayList<Integer>());
        for (List<Integer> conn : connections) {
            edgeMap.get(conn.get(0)).add(conn.get(1));
            edgeMap.get(conn.get(1)).add(conn.get(0));
        }
        dfs(targetDevice, -1);

        // Check if the target device is a source node in any connection
        boolean isSourceNode = false;
        for (List<Integer> conn : connections) {
            if (conn.get(0) == targetDevice) {
                isSourceNode = true;
                break;
            }
        }

        if (!isSourceNode) {
            return new ArrayList<>();
        }

        Set<Integer> impactedDevicesSet = new HashSet<>();
        for (List<Integer> connection : ans) {
            int u = connection.get(0);
            int v = connection.get(1);

            if (u == targetDevice) {
                impactedDevicesSet.add(v);
            } else if (v == targetDevice) {
                impactedDevicesSet.add(u);
            }
        }

        Set<Integer> additionalAffectedDevices = new HashSet<>();
        for (int affectedDevice : impactedDevicesSet) {
            for (int neighbor : edgeMap.get(affectedDevice)) {
                if (!impactedDevicesSet.contains(neighbor)) {
                    additionalAffectedDevices.add(neighbor);
                }
            }
        }

        impactedDevicesSet.addAll(additionalAffectedDevices);
        impactedDevicesSet.remove(targetDevice);

        return new ArrayList<>(impactedDevicesSet);
    }

    public void dfs(int curr, int prev) {
        disc[curr] = low[curr] = time++;
        for (int next : edgeMap.get(curr)) {
            if (next == prev) continue;
            if (disc[next] == 0) {
                dfs(next, curr);
                low[curr] = Math.min(low[curr], low[next]);
                if (low[next] > disc[curr])
                    ans.add(Arrays.asList(curr, next));
            } else {
                low[curr] = Math.min(low[curr], disc[next]);
            }
        }
    }

    public static void main(String[] args) {
        Q5B q5B = new Q5B();

        int n = 8;
        List<List<Integer>> connections = new ArrayList<>();
        connections.add(Arrays.asList(0, 1));
        connections.add(Arrays.asList(0, 2));
        connections.add(Arrays.asList(1, 3));
        connections.add(Arrays.asList(1, 6));
        connections.add(Arrays.asList(2, 4));
        connections.add(Arrays.asList(4, 6));
        connections.add(Arrays.asList(4, 5));
        connections.add(Arrays.asList(5, 7));

        int targetDevice = 4;

        List<Integer> impactedDevices = q5B.findImpactedDevices(n, connections, targetDevice);

        System.out.println("Impacted Devices (other than target device " + targetDevice + "): " + impactedDevices);
    }
}