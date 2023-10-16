import java.util.ArrayList;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class RejectionSamplingForDijkstra {
    List<Double> xValues;
    List<Double> yValues;
    List<String> edges;

    public RejectionSamplingForDijkstra(List<Double> xValues, List<Double> yValues, List<String> edges){
        this.xValues = xValues;
        this.yValues = yValues;
        this.edges=edges;
    }

    public List rejectionSamplingProcedure(){
        //Random class object to generate random edge weights
        Random random = new Random();

        //Get the total number of edges
        int totalEdges=edges.size();

        //Add each number from 1 to totalEdges value to an integer list
        List<Integer> possibleEdgeWeights = new ArrayList<>();
        for(int i=1;i<=totalEdges;i++){
            possibleEdgeWeights.add(i);
        }

        List<int[]> allEdges = new ArrayList<>();

//        //Generate random edge weights between 1 and E/2
//        for(int i=0;i<edges.size();i++){
//            String currEdg = edges.get(i);
//            String[] edgArr = currEdg.split(" ");
//            edgArr[3] = String.valueOf(random.nextInt(edges.size()/2)+1);
//            edges.set(i, edgArr[0]+" "+edgArr[1]+" "+edgArr[2]+" "+edgArr[3]);
//        }


        //Generate random edge weights between 1 and E
        for(int i=0;i<edges.size();i++){
            String currEdg = edges.get(i);
            String[] edgArr = currEdg.split(" ");
            edgArr[3] = String.valueOf(random.nextInt(edges.size())+1);
            edges.set(i, edgArr[0]+" "+edgArr[1]+" "+edgArr[2]+" "+edgArr[3]);
        }

        //Print the edges and their weights
        System.out.println("Curr Edge Weight allocation: "+edges);

        //Right threshold would be between (v-1 to E) inclusive
        int scoreThreshold=(xValues.size()+edges.size())/2;
        DijkstrasAlgorithm runDijkstrasAlgorithm = new DijkstrasAlgorithm(xValues, yValues, edges);

        while(true){
            List result = runDijkstrasAlgorithm.shortestPath(0,4, edges);
            int currScore = (int)result.get(1);
            int[] currParentArr = (int[])result.get(0);
            System.out.println("Curr Score: "+currScore);
            List<Integer> parentList = new ArrayList<>(Arrays.stream(currParentArr).boxed().collect(Collectors.toList()));
            System.out.println("Curr Parent array: "+parentList);
            if(currScore>=scoreThreshold){
                List retResult = new ArrayList<>();
                retResult.add(currParentArr);
                retResult.add(currScore);
                System.out.println("Final Selected Score: "+currScore);
                List<Integer> finParentList = new ArrayList<>(Arrays.stream(currParentArr).boxed().collect(Collectors.toList()));
                System.out.println("Curr Parent array: "+finParentList);
                return retResult;
            }
            System.out.println(Arrays.asList(currParentArr));

//            //Generate random edge weights between 1 and E/2
//            for(int i=0;i<edges.size();i++){
//                String currEdg = edges.get(i);
//                String[] edgArr = currEdg.split(" ");
//                edgArr[3] = String.valueOf(random.nextInt(edges.size()/2)+1);
//                edges.set(i, edgArr[0]+" "+edgArr[1]+" "+edgArr[2]+" "+edgArr[3]);
//            }

            //Generate random edge weights between 1 and E
            for(int i=0;i<edges.size();i++){
                String currEdg = edges.get(i);
                String[] edgArr = currEdg.split(" ");
                edgArr[3] = String.valueOf(random.nextInt(edges.size())+1);
                edges.set(i, edgArr[0]+" "+edgArr[1]+" "+edgArr[2]+" "+edgArr[3]);
            }

            //Print the edges and their weights
            System.out.println("Curr Edge Weight allocation: "+edges);
        }

    }
}
