package src.main.java.com.graph_generator.algorithm;

import java.util.*;

public class PrimsAlgorithm {
    List<Double> xValues;
    List<Double> yValues;
    List<String> edges;
    int totalTimesPrimsWasCalled;
    public PrimsAlgorithm(List<Double> xValues, List<Double> yValues, List<String> edges){
        this.xValues = xValues;
        this.yValues = yValues;
        this.edges = edges;
        totalTimesPrimsWasCalled=0;
    }



    public List minSpanningTreeUsingPrims( List<List<int[]>> adjacencyList) {
        int n = xValues.size();
        List<Integer> nodesIncludedInMst = new ArrayList<>();
        List<int[]> edgesInMst = new ArrayList<>();
        // Min-heap to store minimum weight edge at top.
        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> (a[0] - b[0]));

        // Track nodes which are included in MST.
        boolean[] inMST = new boolean[n];

        heap.add(new int[]{0, -1, 0});
        int mstCost = 0;
        int edgesUsed = 0;
        int score=0;
        int lastIncludedNode=0;
        while (edgesUsed < n) {

            int[] topElement = heap.poll();
            int weight = topElement[0];
            int prevNode = topElement[1];
            int currNode = topElement[2];



            // If node was already included in MST we will discard this edge.
            if (inMST[currNode]) {
                continue;
            }
            System.out.println("Inside src.main.java.com.graphgenerator.algorithm.PrimsAlgorithm: prevNode: "+prevNode+"currNode: "+currNode);
            inMST[currNode] = true;
            mstCost += weight;
            edgesUsed++;
//            int lastNodeIncludedInMst = nodesIncludedInMst.get(nodesIncludedInMst.size()-1);

            //If the currNode is from the previously added node, then increment the score
            if(prevNode!=-1 && prevNode!=lastIncludedNode){
                System.out.println("It skipped the above node");
                score++;
            }
            edgesInMst.add(new int[]{prevNode, currNode});
            nodesIncludedInMst.add(currNode);
            //Update our lastIncludedNode
            lastIncludedNode=currNode;
            List<int[]> currAdjacencyList = adjacencyList.get(currNode);
            for (int i = 0; i < currAdjacencyList.size(); ++i) {
                int[] nextArr = currAdjacencyList.get(i);
                int nextNode = nextArr[0];
                int nextWeight = nextArr[1];
                // If next node is not in MST, then edge from curr node
                // to next node can be pushed in the priority queue.
                if (!inMST[nextNode]) {
                    heap.add(new int[]{nextWeight, currNode, nextNode});
                }
            }
        }

//        for(int i=0;i<nodesIncludedInMst.size()-1;i++){
//            int v1=nodesIncludedInMst.get(i);
//            int v2=nodesIncludedInMst.get(i+1);
//            edgesInMst.add(new int[]{v1, v2});
//        }
        System.out.println("Inside src.main.java.com.graphgenerator.algorithm.PrimsAlgorithm: nodesIncludedInMst: "+nodesIncludedInMst);
        System.out.println("Inside src.main.java.com.graphgenerator.algorithm.PrimsAlgorithm: mstCost: "+mstCost);
        System.out.println("Inside src.main.java.com.graphgenerator.algorithm.PrimsAlgorithm: score: "+score);
        List result = new ArrayList<>();
        result.add(score);
        result.add(edgesInMst);
        return result;
    }
}