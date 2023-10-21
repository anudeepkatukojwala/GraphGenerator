import java.util.LinkedList;
import java.util.*;

public class MaxFlow {
    List<Double> xValues;
    List<Double> yValues;
    List<String> edges;

    int V; // Number of vertices in graph


    public MaxFlow(List<Double> xValues, List<Double> yValues, List<String> edges) {
        this.xValues = xValues;
        this.yValues = yValues;
        this.edges = edges;
        this.V = xValues.size();
    }


    boolean bfs(int[][] rGraph, int s, int t, int[] parent) {
        // Create an array to store visited vertices
        boolean[] visited = new boolean[V];
        for (int i = 0; i < V; ++i)
            visited[i] = false;

        // Use a queue to perform BFS
        LinkedList<Integer> queue = new LinkedList<Integer>();
        queue.add(s);
        visited[s] = true;
        parent[s] = -1;

        while (queue.size() != 0) {
            int u = queue.poll();

            // Explore all adjacent vertices of 'u'
            for (int v = 0; v < V; v++) {
                // If vertex 'v' is not visited and there's residual capacity
                // from 'u' to 'v', visit 'v'.
                if (!visited[v] && rGraph[u][v] > 0) {
                    queue.add(v);
                    parent[v] = u;
                    visited[v] = true;
                }
            }
        }

        // Return true if the sink 't' was reached during BFS
        return (visited[t] == true);
    }


    int fordFulkerson(int[][] graph, int s, int t) {
        int u, v;

        // Create the residual graph and fill it with input capacities
        int[][] rGraph = new int[V][V];
        for (u = 0; u < V; u++)
            for (v = 0; v < V; v++)
                rGraph[u][v] = graph[u][v];

        // Parent array to store the path found by BFS
        int[] parent = new int[V];

        // The overall max flow result
        int maxFlow = 0;

        // Continue finding paths from 's' to 't' until none are found
        while (bfs(rGraph, s, t, parent)) {
            // Find bottleneck (minimum residual capacity) along the path
            int pathFlow = Integer.MAX_VALUE;
            for (v = t; v != s; v = parent[v]) {
                u = parent[v];
                pathFlow = Math.min(pathFlow, rGraph[u][v]);
            }

            // Update residual capacities of edges and reverse edges
            for (v = t; v != s; v = parent[v]) {
                u = parent[v];
                rGraph[u][v] -= pathFlow;
                rGraph[v][u] += pathFlow;
            }

            // Increment the overall max flow by bottleneck of the path
            maxFlow += pathFlow;
        }

        // Return the max flow
        return maxFlow;
    }

}
