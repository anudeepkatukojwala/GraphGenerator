import java.util.*;

public class KruskalAlgo {
    List<Double> xValues;
    List<Double> yValues;
    List<String> edges;
    int totalTimesMinSpanningTreeCalled;
    public KruskalAlgo(List<Double> xValues, List<Double> yValues, List<String> edges){
        this.xValues = xValues;
        this.yValues = yValues;
        this.edges = edges;
        totalTimesMinSpanningTreeCalled=0;
    }

    public List minSpanningTree(List<int[]> allEdges) {
        totalTimesMinSpanningTreeCalled++;
        //Total number of nodes "n" is
        int n=xValues.size();

        int score=0;

//        // Storing all edges of our complete graph.
//        for (int currNext = 0; currNext < n; ++currNext) {
//            for (int nextNext = currNext + 1; nextNext < n; ++nextNext) {
//                int weight = Math.abs(points[currNext][0] - points[nextNext][0]) +
//                        Math.abs(points[currNext][1] - points[nextNext][1]);
//
//                int[] currEdge = {weight, currNext, nextNext};
//                allEdges.add(currEdge);
//            }
//        }

        // Sort all edges in increasing order.
        Collections.sort(allEdges, (a, b) -> Integer.compare(a[0], b[0]));

        UnionFind uf = new UnionFind(n);
        int mstCost = 0;
        int edgesUsed = 0;
        List<int[]> mstEdges = new ArrayList<>();
        for (int i = 0; i < allEdges.size() && edgesUsed < n - 1; ++i) {
            int node1 = allEdges.get(i)[1];
            int node2 = allEdges.get(i)[2];
            int weight = allEdges.get(i)[0];

            if (uf.union(node1, node2)) {
                mstCost += weight;
                edgesUsed++;
                mstEdges.add(allEdges.get(i));
            }
            else{
                score++;
            }
        }
        System.out.println("Inside minSpanningTree method: curr mstCost: "+mstCost);
        System.out.println("Inside minSpanningTree method: curr score: "+score);
        System.out.println("Inside minSpanningTree method: curr totalTimesMinSpanningTreeCalled: "+totalTimesMinSpanningTreeCalled);
        List result = new ArrayList<>();
        result.add(score);
        result.add(mstEdges);
        return result;
    }
}
