package src.main.java.com.graph_generator.rejection_sampling;

import src.main.java.com.graph_generator.algorithm.KruskalAlgo;

import java.util.*;
public class RejectionSamplingForKruskal {
    List<Double> xValues;
    List<Double> yValues;
    List<String> edges;
    public RejectionSamplingForKruskal(List<Double> xValues, List<Double> yValues, List<String> edges){
        this.xValues = xValues;
        this.yValues = yValues;
        this.edges=edges;
    }

    public List<List<Integer>> getAdjacencyListOfGraphWithWeights(){
        List<List<Integer>> adj = new ArrayList<>();
        for(int i=0;i<xValues.size();i++){
            adj.add(new ArrayList<>());
        }
        for(int i=0;i<edges.size();i++){
            String[] arr=edges.get(i).split(" ");
            int x=Integer.parseInt(arr[1]);
            int y=Integer.parseInt(arr[2]);

            adj.get(x).add(y);
            adj.get(y).add(x);

        }
        //System.out.println("Adjacency list is: "+ adj);
        return adj;
    }

//    public List<Object> addRandomWeights(List<String> edges){
//
//    }

    public List<List<int[]>> rejectionSamplingProcedure(){
        //Get the total number of edges
        int totalEdges=edges.size();

        //Add each number from 1 to totalEdges value to an integer list
        List<Integer> possibleEdgeWeights = new ArrayList<>();
        for(int i=1;i<=totalEdges;i++){
            possibleEdgeWeights.add(i);
        }
        Collections.shuffle(possibleEdgeWeights);


        List<int[]> allEdges = new ArrayList<>();

        for(int i=0;i<edges.size();i++){
            String[] edgArr = edges.get(i).split(" ");
            int currV1 = Integer.parseInt(edgArr[1]);
            int currV2 = Integer.parseInt(edgArr[2]);
            int[] currEdge = {possibleEdgeWeights.get(i), currV1, currV2};
            allEdges.add(currEdge);
        }


        //Max threshold could be (E-V+1)
        int scoreThreshold=3;
        KruskalAlgo runKruskalAlgorithm = new KruskalAlgo(xValues, yValues, edges);

        while(true){
            List result = runKruskalAlgorithm.minSpanningTree(allEdges);
            int currScore = (int) result.get(0);
            List<int[]> mstEdges = (List<int[]>)result.get(1);
            if(currScore>=scoreThreshold){
                System.out.println("Inside rejectionSamplingProcedure method:\nPermutaion of edge weights: "+possibleEdgeWeights);
                System.out.println("Inside rejectionSamplingProcedure method:\nAll edges: "+allEdges);
                List<List<int[]>> retResult = new ArrayList<>();
                retResult.add(allEdges);
                retResult.add(mstEdges);
                return retResult;
            }
            allEdges.clear();
            Collections.shuffle(possibleEdgeWeights);
            for(int i=0;i<edges.size();i++){
                String[] edgArr = edges.get(i).split(" ");
                int currV1 = Integer.parseInt(edgArr[1]);
                int currV2 = Integer.parseInt(edgArr[2]);
                int[] currEdge = {possibleEdgeWeights.get(i), currV1, currV2};
                allEdges.add(currEdge);
            }
        }
    }

}
