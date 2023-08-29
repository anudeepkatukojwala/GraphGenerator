import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RejectionSamplingForPrims {
    List<Double> xValues;
    List<Double> yValues;
    List<String> edges;
    List<Integer> possibleEdgeWeights;
    public RejectionSamplingForPrims(List<Double> xValues, List<Double> yValues, List<String> edges){
        this.xValues = xValues;
        this.yValues = yValues;
        this.edges=edges;
        //Add each number from 1 to totalEdges value to an integer list
        possibleEdgeWeights = new ArrayList<>();
        for(int i=1;i<=edges.size();i++){
            possibleEdgeWeights.add(i);
        }
    }

    public List<List<int[]>> getAdjacencyListOfGraphWithWeights(){
        List<List<int[]>> adj = new ArrayList<>();
        for(int i=0;i<xValues.size();i++){
            adj.add(new ArrayList<>());
        }

        Collections.shuffle(possibleEdgeWeights);
        for(int i=0;i<edges.size();i++){
            String[] arr=edges.get(i).split(" ");
            int x=Integer.parseInt(arr[1]);
            int y=Integer.parseInt(arr[2]);
            int weight=possibleEdgeWeights.get(i);
            adj.get(x).add(new int[]{y, weight});
            adj.get(y).add(new int[]{x, weight});

        }
        //System.out.println("Adjacency list is: "+ adj);
        return adj;
    }



    public List rejectionSamplingProcedureForPrims(){
        //Get the total number of edges
        int totalEdges=edges.size();

//        //Add each number from 1 to totalEdges value to an integer list
//        List<Integer> possibleEdgeWeights = new ArrayList<>();
//        for(int i=1;i<=totalEdges;i++){
//            possibleEdgeWeights.add(i);
//        }
//        Collections.shuffle(possibleEdgeWeights);


//        List<int[]> allEdges = new ArrayList<>();
//
//        for(int i=0;i<edges.size();i++){
//            String[] edgArr = edges.get(i).split(" ");
//            int currV1 = Integer.parseInt(edgArr[1]);
//            int currV2 = Integer.parseInt(edgArr[2]);
//            int[] currEdge = {possibleEdgeWeights.get(i), currV1, currV2};
//            allEdges.add(currEdge);
//        }

        List<List<int[]>> adjacencyListWithWeights = getAdjacencyListOfGraphWithWeights();



        int scoreThreshold=4;
        PrimsAlgorithm runPrimsAlgorithm = new PrimsAlgorithm(xValues, yValues, edges);

        while(true){
            List result = runPrimsAlgorithm.minSpanningTreeUsingPrims(adjacencyListWithWeights);
            int currScore = (int) result.get(0);
            List<int[]> edgesInMst = (List<int[]>)result.get(1);
            if(currScore>=scoreThreshold){
//                System.out.println("Inside rejectionSamplingProcedure method:\nPermutaion of edge weights: "+possibleEdgeWeights);
//                System.out.println("Inside rejectionSamplingProcedure method:\nAll edges: "+allEdges);
                List retResult = new ArrayList<>();
//                retResult.add(allEdges);
                //Create list of edges with weights to return
                //Since possibleEdgeWeights is a global list, we can access it
                //to get the current sequence of edge weights
                List<String> edgesWithWeights = new ArrayList<>();
                for(int i=0;i<edges.size();i++){
                    String currEdge = edges.get(i);
                    currEdge = currEdge+" "+possibleEdgeWeights.get(i);
                    edgesWithWeights.add(currEdge);
                }
                retResult.add(edgesInMst);
                retResult.add(edgesWithWeights);
                return retResult;
            }
            adjacencyListWithWeights.clear();
            adjacencyListWithWeights = getAdjacencyListOfGraphWithWeights();
//            for(int i=0;i<edges.size();i++){
//                String[] edgArr = edges.get(i).split(" ");
//                int currV1 = Integer.parseInt(edgArr[1]);
//                int currV2 = Integer.parseInt(edgArr[2]);
//                int[] currEdge = {possibleEdgeWeights.get(i), currV1, currV2};
//                allEdges.add(currEdge);
//            }
        }
    }



}
