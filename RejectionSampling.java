import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RejectionSampling {
    List<Double> xValues;
    List<Double> yValues;
    List<String> edges;
    List<Integer> possibleEdgeWeights;
    public RejectionSampling (List<Double> xValues, List<Double> yValues, List<String> edges){
        this.xValues = xValues;
        this.yValues = yValues;
        this.edges=edges;
        //Used for Prim's: Add each number from 1 to totalEdges value to an integer list
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



    public List<List<int[]>> rejectionSamplingProcedure(){

        /* Prepare to run Kruskal Algorithms */

        //Get the total number of edges
        int totalEdges=edges.size();

        Collections.shuffle(possibleEdgeWeights);


        List<int[]> allEdges = new ArrayList<>();

        for(int i=0;i<edges.size();i++){
            String[] edgArr = edges.get(i).split(" ");
            int currV1 = Integer.parseInt(edgArr[1]);
            int currV2 = Integer.parseInt(edgArr[2]);
            int[] currEdge = {possibleEdgeWeights.get(i), currV1, currV2};
            allEdges.add(currEdge);
        }



        int scoreThresholdForKruskal=3;
        KruskalAlgo runKruskalAlgorithm = new KruskalAlgo(xValues, yValues, edges);

        /* Prepare to run Prim's Algorithm */

        List<List<int[]>> adjacencyListWithWeights = getAdjacencyListOfGraphWithWeights();

        int scoreThresholdForPrims=3;
        PrimsAlgorithm runPrimsAlgorithm = new PrimsAlgorithm(xValues, yValues, edges);


        while(true){
            //Run Kruskal Algorithm
            List kruskalResult = runKruskalAlgorithm.minSpanningTree(allEdges);
            int currKruskalScore = (int) kruskalResult.get(0);
            List<int[]> mstEdgesOfKruskal = (List<int[]>)kruskalResult.get(1);

            //Run Prims Algorithm
            List primsResult = runPrimsAlgorithm.minSpanningTreeUsingPrims(adjacencyListWithWeights);
            int currPrimsScore = (int) primsResult.get(0);
            List<int[]> edgesInMstForPrims = (List<int[]>)primsResult.get(1);

            if(currKruskalScore>=scoreThresholdForKruskal && currPrimsScore>=scoreThresholdForPrims){
                System.out.println("/**************************************************/");
                System.out.println("Inside rejectionSamplingProcedure method:\nPermutaion of edge weights: "+possibleEdgeWeights);
                System.out.println("Inside rejectionSamplingProcedure method:\nAll edges: "+allEdges);
                System.out.println("/**************************************************/");
                List<List<int[]>> retResult = new ArrayList<>();
                retResult.add(allEdges);
                retResult.add(mstEdgesOfKruskal);
                System.out.println("Kruskal Score is: "+currKruskalScore);
                System.out.println("Prim's Score is: "+currPrimsScore);
                return retResult;
            }

            //This shuffle will be used by both prims and kruskal
            Collections.shuffle(possibleEdgeWeights);

            /* Prepare to run Kruskal Algorithm for the next iteration */
            allEdges.clear();

            for(int i=0;i<edges.size();i++){
                String[] edgArr = edges.get(i).split(" ");
                int currV1 = Integer.parseInt(edgArr[1]);
                int currV2 = Integer.parseInt(edgArr[2]);
                int[] currEdge = {possibleEdgeWeights.get(i), currV1, currV2};
                allEdges.add(currEdge);
            }

            /* Prepare to run Prim's Algorithm for the next iteration */
            adjacencyListWithWeights.clear();
            adjacencyListWithWeights = getAdjacencyListOfGraphWithWeights();

        }
    }
}
