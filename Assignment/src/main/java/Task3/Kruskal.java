// Implement Kruskal algorithm and priority queue using minimum heap

// Time Complexity: O(E log E) where E is the number of edges 
// Space Complexity: O(V + E) where V means the number of vertices or nodes and E is number of Edges

package Task3;

import java.util.PriorityQueue;

public class Kruskal {

    public static class Edge implements Comparable<Edge> {
        int s;  // source
        int d;  // destination
        int w;  // weight

        Edge(int s, int d, int w) {
            this.s = s;
            this.d = d;
            this.w = w;
        }

        @Override
        public int compareTo(Edge o) {
            return this.w - o.w;
        }
    }

    int v;
    PriorityQueue<Edge> pq;

    Kruskal(int v) {
        this.v = v;
        this.pq = new PriorityQueue<>();
    }

    void addEdge(int s, int d, int w) {
        pq.add(new Edge(s, d, w));
    }

    void kruskal() {
        // Array to keep track of the parent of each node
        int[] parent = new int[v];

        // Initialize all vertices as belonging to different sets
        for (int i = 0; i < v; i++) {
            parent[i] = i;
        }

        while (!pq.isEmpty()) {
            Edge e = pq.poll();
            int x = find(e.s, parent);
            int y = find(e.d, parent);

            // If including this edge does't cause cycle,
            // include it in result and union the two vertices.
            if (x != y) {
                System.out.println(e.s + " - " + e.d + " : " + e.w);
                union(x, y, parent);
            }
        }
    }

    int find(int i, int[] parent) {
        if (parent[i] == i) {
            return i;
        }
        return parent[i] = find(parent[i], parent);
    }

    void union(int x, int y, int[] parent) {
        int xset = find(x, parent);
        int yset = find(y, parent);
        if (xset != yset) {
            parent[xset] = yset;
        }
    }

    public static void main(String[] args) {
        Kruskal graph = new Kruskal(4);

        graph.addEdge(0, 1, 10);
        graph.addEdge(0, 2, 6);
        graph.addEdge(0, 3, 5);
        graph.addEdge(1, 3, 15);
        graph.addEdge(2, 3, 4);

        graph.kruskal();
    }
}
