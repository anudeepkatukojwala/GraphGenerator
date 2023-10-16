import java.util.*;

class DijkstrasAlgorithmDirected {
    private int V; // Number of vertices
    private List<List<DEdgeDirected>> adj;

    List<Double> xValues;
    List<Double> yValues;
    List<String> edges;

    public DijkstrasAlgorithmDirected(List<Double> xValues, List<Double> yValues, List<String> edges) {
        this.xValues=xValues;
        this.yValues=yValues;
        this.edges=edges;
        this.V = xValues.size();
        adj = new ArrayList<>(V);
        createAdjacencyList();
    }

    public void createAdjacencyList() {
        adj.clear();
        for (int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
        }
        for(int i=0;i<edges.size();i++){
            String currEdge = edges.get(i);
            String[] currEdgeArr = currEdge.split(" ");
            int u = Integer.parseInt(currEdgeArr[1]);
            int v = Integer.parseInt(currEdgeArr[2]);
            int weight = Integer.parseInt(currEdgeArr[3]);
            int direction = Integer.parseInt(currEdgeArr[4]);
            if(direction==0){
                adj.get(u).add(new DijkstrasAlgorithmDirected.DEdgeDirected(v, weight));
            }
            else{
                adj.get(v).add(new DijkstrasAlgorithmDirected.DEdgeDirected(u, weight));
            }
        }

    }

    public List shortestPath(int start, int end, List<String> edges) {
        this.edges = edges;
        createAdjacencyList();
        int countOfParentUpdates=0;

        //List to return
        List result = new ArrayList<>();

        int[] dist = new int[V];
        Arrays.fill(dist, Integer.MAX_VALUE);

        dist[start] = 0;

        PriorityQueue<DNodeDirected> pq = new PriorityQueue<>(V, Comparator.comparingInt(node -> node.distance));
        pq.add(new DNodeDirected(start, 0));

        int[] parent = new int[V];
        Arrays.fill(parent, -1);

        while (!pq.isEmpty()) {
            int u = pq.poll().vertex;
            for (DEdgeDirected edge : adj.get(u)) {
                int v = edge.destination;
                int weight = edge.weight;
                if (dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                    parent[v] = u;
                    countOfParentUpdates++;
                    pq.add(new DNodeDirected(v, dist[v]));
                }
            }
        }

        if (dist[end] == Integer.MAX_VALUE) {
            System.out.println("No path exists from " + start + " to " + end);
            result.add(parent);
            result.add(countOfParentUpdates);
            return result;
        }
        List<Integer> distancesArrayList = new ArrayList<>();
        for(int i=0;i<dist.length;i++){
            distancesArrayList.add(dist[i]);
        }
        System.out.println("Distances for source to each vertex: "+distancesArrayList);

        System.out.println("Shortest path from " + start + " to " + end + ":");
        printPath(end, parent);
        System.out.println("\nShortest path value: " + dist[end]);
        result.add(parent);
        result.add(countOfParentUpdates);
        result.add(distancesArrayList);
        return result;
    }

    private void printPath(int currentVertex, int[] parent) {
        if (currentVertex == -1) {
            return;
        }
        printPath(parent[currentVertex], parent);
        System.out.print(currentVertex + " ");
    }

    private class DEdgeDirected

    {
        int destination;
        int weight;

        public DEdgeDirected(int destination, int weight) {
            this.destination = destination;
            this.weight = weight;
        }
    }

    private class DNodeDirected {
        int vertex;
        int distance;

        public DNodeDirected(int vertex, int distance) {
            this.vertex = vertex;
            this.distance = distance;
        }
    }
}


