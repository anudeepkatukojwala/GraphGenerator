package src.main.java.com.graph_generator.algorithm;

import java.util.LinkedList;
import java.util.*;

public class MaxFlow {
    List<Double> xValues;
    List<Double> yValues;
    List<String> edges;

    int V; // Number of vertices in graph
    private int[][] graph;
    private boolean[] visited;
    private int pathCount;
    List<Integer> path;
    List<List<Integer>> allPaths;


    public MaxFlow(List<Double> xValues, List<Double> yValues, List<String> edges) {
        this.xValues = xValues;
        this.yValues = yValues;
        this.edges = edges;
        this.V = xValues.size();
        this.visited = new boolean[xValues.size()];
        this.pathCount = 0;
        this.path = new ArrayList<>();
        this.allPaths = new ArrayList<>();
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


    public List fordFulkerson(int[][] graph, int s, int t) {
        int u, v;

        // Create the residual graph and fill it with input capacities
        int[][] rGraph = new int[V][V];
        for (u = 0; u < V; u++)
            for (v = 0; v < V; v++)
                rGraph[u][v] = graph[u][v];

        //Create a secondary residual graph to hold the last to second residual graph for later
        //purpose of finding if unique path exists
        int[][] rGraph2 = new int[V][V];

        // Parent array to store the path found by BFS
        int[] parent = new int[V];

        // The overall max flow result
        int maxFlow = 0;

        //save current rGraph in rGraph2 before modification
        for (u = 0; u < V; u++) {
            for (v = 0; v < V; v++) {
                rGraph2[u][v] = rGraph[u][v];
            }
        }
        boolean test=false;
        System.out.println("Residual graph: This iteration: "+Arrays.deepToString(rGraph));
        // Continue finding paths from 's' to 't' until none are found
        while (test) {
            // Find bottleneck (minimum residual capacity) along the path
            int pathFlow = Integer.MAX_VALUE;
            for (v = t; v != s; v = parent[v]) {
                u = parent[v];
                pathFlow = Math.min(pathFlow, rGraph[u][v]);
            }

            //save current rGraph in rGraph2 before modification
            for (u = 0; u < V; u++) {
                for (v = 0; v < V; v++) {
                    rGraph2[u][v] = rGraph[u][v];
                }
            }

            // Update residual capacities of edges and reverse edges
            for (v = t; v != s; v = parent[v]) {
                u = parent[v];
                rGraph[u][v] -= pathFlow;
                rGraph[v][u] += pathFlow;
            }

            System.out.println("Residual graph: This iteration: "+Arrays.deepToString(rGraph));

            // Increment the overall max flow by bottleneck of the path
            maxFlow += pathFlow;

//            //save current rGraph in rGraph2 before modification
//            for (u = 0; u < V; u++) {
//                for (v = 0; v < V; v++) {
//                    rGraph2[u][v] = rGraph[u][v];
//                }
//            }

        }
        //Check whether there is unique path from source to sink in the
        //residual graph
//        boolean uniquePathExists = uniquesPathExists(graph, rGraph2, s, t);
        // ckeck if unique path exists
        this.pathCount=0;
        this.graph = rGraph2;
        this.visited = new boolean[xValues.size()];

        int paths=countPaths(s, t);
        System.out.println("paths is: "+paths);
        System.out.println("All paths are: "+allPaths);
        boolean uniquePathExists = (paths == 1);
//        int[][] test=new int[V][V];
//        for(int i=0;i<V;i++){
//            for(int j=0;j<V;j++){
//                test[i][j]=graph[i][j]-rGraph[i][j];
//            }
//        }
        System.out.println("Inside forFulkerson: rGraph2 graph = "+Arrays.deepToString(rGraph2));
        List result = new ArrayList<>();
        result.add(maxFlow);
        result.add(graph);
        result.add(rGraph);
        result.add(edges);
        result.add(true); //add boolean indicating unique path exists
        result.add(rGraph2); //add residual graph from last iteration
        // Return the max flow
        return result;
    }

    public boolean uniquesPathExists(int[][] graph, int[][] rGraph, int s, int t) {
//        System.out.println("Inside uniquePathExists: rGraph is: "+Arrays.deepToString(rGraph));
//        System.out.println("Inside uniquePathExists: graph is: "+Arrays.deepToString(graph));
//        for(int i=0;i<V;i++){
//            for(int j=0;j<V;j++){
//                rGraph[i][j]=graph[i][j]-rGraph[i][j]; //subtract residual capacity from original capacity
//            }
//        }
//        System.out.println("Inside uniquePathExists: rGraph after subtraction is: "+Arrays.deepToString(rGraph));
        //Construct adjacency list from edgelist

        //Create a 2D array to do DP
        int[][] dp = new int[V][2*V+1];
        //set the base cases for DP
        for(int i=0; i<V; i++){
            if(i==s){
                dp[i][0]=1;
            }
            else{
                dp[i][0]=0;
            }
        }
        // Perform DP to check if unique path exists
        for(int d=1;d<2*V+1;d++){
            for(int v=0;v<V;v++){
                //Check for incoming edges
                for(int u=0;u<V;u++){
                    if(rGraph[u][v]>0){
                        dp[v][d]+=dp[u][d-1];
                    }
                }
            }
        }
        int totalPaths=0;
        // Check total paths reaching sink
        for(int d=0;d<2*V+1;d++){
            totalPaths+=dp[t][d];
        }
//        System.out.println("Inside uniquePathExists method: dp array is: "+Arrays.deepToString(dp));
        System.out.println("Inside uniquePathExists method: totalPaths: "+totalPaths);
        return 1==1; //return true if only one path exists
    }

    public int countPaths(int s, int t) {
        visited[s] = true;
        path.add(s);
        if (s == t) {
            allPaths.add(new ArrayList<>(path));
            pathCount++;

        } else {
            for (int i = 0; i < graph.length; i++) {
                if (!visited[i] && graph[s][i] > 0) { // Check if there's an edge
                    countPaths(i, t);
                }
            }
        }
        path.remove(path.size()-1);
        visited[s] = false; // Backtrack
        return pathCount;
    }

}
