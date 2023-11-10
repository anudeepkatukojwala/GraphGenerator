package src.main.java.com.graph_generator.rejection_sampling;

import src.main.java.com.graph_generator.algorithm.DijkstrasAlgorithmDirected;

import java.util.ArrayList;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class RejectionSamplingForDijkstraDirected {
    List<Double> xValues;
    List<Double> yValues;
    List<String> edges;
    int start;
    int end;

    public RejectionSamplingForDijkstraDirected(List<Double> xValues, List<Double> yValues, List<String> edges, int start, int end){
        this.xValues = xValues;
        this.yValues = yValues;
        this.edges=edges;
        this.start = start;
        this.end = end;
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
//            System.out.println("In Rejection Sampling Procedure: "+currEdg);
            String[] edgArr = currEdg.split(" ");
            edgArr[3] = String.valueOf(random.nextInt(edges.size())+1);
            edges.set(i, edgArr[0]+" "+edgArr[1]+" "+edgArr[2]+" "+edgArr[3]+" "+"0");
        }

        //Print the edges and their weights
        System.out.println("Curr Edge Weight allocation: "+edges);

        //Run Prims algorithm from the start vertex to see if all the vertices are reachable
        List resulOfPrims = minSpanningTreeUsingPrims();
        int currScoreOfPrims = (int) resulOfPrims.get(0);
        List<int[]> edgesInMst = (List<int[]>)resulOfPrims.get(1);
        List<Integer> edgesThatGotDirection = new ArrayList<>();
        //Set the direction for a given edge if it is part of mst
        for(int i=0;i<edges.size();i++){
            String currEdge = edges.get(i);
            String[] currEdgeArr = currEdge.split(" ");
            int currV1 = Integer.parseInt(currEdgeArr[1]);
            int currV2 = Integer.parseInt(currEdgeArr[2]);
            int weight = Integer.parseInt(currEdgeArr[3]);
//            System.out.println("Are we here");
            int direction = Integer.parseInt(currEdgeArr[4]);

            for(int iterator=1;iterator<edgesInMst.size();iterator++){
                int[] currEdgeInMst = edgesInMst.get(iterator);
                int v1 = currEdgeInMst[0];
                int v2 = currEdgeInMst[1];
                if((currV1==v1 && currV2==v2)){
                    edges.set(i, "e"+" "+currV1+" "+currV2+" "+weight+" "+"0");
                    edgesThatGotDirection.add(i);
                }
                else if((currV1==v2 && currV2==v1)){
                    edges.set(i, "e"+" "+currV1+" "+currV2+" "+weight+" "+"1");
                    edgesThatGotDirection.add(i);
                }
            }
        }


        //Direct every edge away from the start
        for(int i=0;i<edges.size();i++){
            String currEdge = edges.get(i);
            String[] currEdgeArr = currEdge.split(" ");
            int currV1 = Integer.parseInt(currEdgeArr[1]);
            int currV2 = Integer.parseInt(currEdgeArr[2]);
            int weight = Integer.parseInt(currEdgeArr[3]);
            int direction = Integer.parseInt(currEdgeArr[4]);

            if(currV1==start){
                edges.set(i, "e"+" "+currV1+" "+currV2+" "+weight+" "+"0");
                if(!edgesThatGotDirection.contains(i)){
                    edgesThatGotDirection.add(i);
                }
            }
            else if(currV2==start){
                edges.set(i, "e"+" "+currV1+" "+currV2+" "+weight+" "+"1");
                if(!edgesThatGotDirection.contains(i)){
                    edgesThatGotDirection.add(i);
                }
            }

        }

        //Randomly assign direction for remaining edges
        for(int i=0;i<edges.size();i++){
            String currEdge = edges.get(i);
            String[] currEdgeArr = currEdge.split(" ");
            int currV1 = Integer.parseInt(currEdgeArr[1]);
            int currV2 = Integer.parseInt(currEdgeArr[2]);
            int weight = Integer.parseInt(currEdgeArr[3]);
            int direction = Integer.parseInt(currEdgeArr[4]);

            if(!edgesThatGotDirection.contains(i)){
                Random randDirec = new Random();
                int nextDirection = randDirec.nextInt(2);

                edges.set(i, "e"+" "+currV1+" "+currV2+" "+weight+" "+String.valueOf(nextDirection));
            }

        }


        //Right threshold would be between (v-1 to E) inclusive
        int scoreThreshold=(xValues.size()+edges.size())/2;
        DijkstrasAlgorithmDirected runDijkstrasAlgorithm = new DijkstrasAlgorithmDirected(xValues, yValues, edges);

        while(true){
            List result = runDijkstrasAlgorithm.shortestPath(start,end, edges);
            int currScore = (int)result.get(1);
            int[] currParentArr = (int[])result.get(0);
            List<Integer> distancesArrayList = (List<Integer>) result.get(2);
            System.out.println("Curr Score: "+currScore);
            List<Integer> parentList = new ArrayList<>(Arrays.stream(currParentArr).boxed().collect(Collectors.toList()));
            System.out.println("Curr Parent array: "+parentList);
            if(currScore>=scoreThreshold){
                List retResult = new ArrayList<>();
                retResult.add(currParentArr);
                retResult.add(currScore);
                retResult.add(edges);
                retResult.add(distancesArrayList);

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
                edges.set(i, edgArr[0]+" "+edgArr[1]+" "+edgArr[2]+" "+edgArr[3]+" "+"0");
            }

            //Run Prims algorithm from the start vertex to see if all the vertices are reachable
            List resulOfPrimsInside = minSpanningTreeUsingPrims();
            int currScoreOfPrimsInside = (int) resulOfPrimsInside.get(0);
            List<int[]> edgesInMstInside = (List<int[]>)resulOfPrimsInside.get(1);
            List<Integer> edgesThatGotDirectionInside = new ArrayList<>();


            //Set the direction for a given edge if it is part of mst
            for(int i=0;i<edges.size();i++){
                String currEdge = edges.get(i);
                String[] currEdgeArr = currEdge.split(" ");
                int currV1 = Integer.parseInt(currEdgeArr[1]);
                int currV2 = Integer.parseInt(currEdgeArr[2]);
                int weight = Integer.parseInt(currEdgeArr[3]);
                int direction = Integer.parseInt(currEdgeArr[4]);

                for(int iterator=1;iterator<edgesInMstInside.size();iterator++){
                    int[] currEdgeInMst = edgesInMstInside.get(iterator);
                    int v1 = currEdgeInMst[0];
                    int v2 = currEdgeInMst[1];
                    if((currV1==v1 && currV2==v2)){
                        edges.set(i, "e"+" "+currV1+" "+currV2+" "+weight+" "+"0");
                        edgesThatGotDirectionInside.add(i);
                    }
                    else if((currV1==v2 && currV2==v1)){
                        edges.set(i, "e"+" "+currV1+" "+currV2+" "+weight+" "+"1");
                        edgesThatGotDirectionInside.add(i);
                    }
                }
            }

            System.out.println("edgesThatGotDirectionInside1: "+edgesThatGotDirectionInside);
            //Direct every edge away from the start
            for(int i=0;i<edges.size();i++){
                String currEdge = edges.get(i);
                String[] currEdgeArr = currEdge.split(" ");
                int currV1 = Integer.parseInt(currEdgeArr[1]);
                int currV2 = Integer.parseInt(currEdgeArr[2]);
                int weight = Integer.parseInt(currEdgeArr[3]);
                int direction = Integer.parseInt(currEdgeArr[4]);

                if(currV1==start){
                    edges.set(i, "e"+" "+currV1+" "+currV2+" "+weight+" "+"0");
                    if(!edgesThatGotDirectionInside.contains(i)){
                        edgesThatGotDirectionInside.add(i);
                    }
                }
                else if(currV2==start){
                    edges.set(i, "e"+" "+currV1+" "+currV2+" "+weight+" "+"1");
                    if(!edgesThatGotDirectionInside.contains(i)){
                        edgesThatGotDirectionInside.add(i);
                    }
                }

            }
            System.out.println("edgesThatGotDirectionInside2: "+edgesThatGotDirectionInside);
            //Randomly assign direction for remaining edges
            for(int i=0;i<edges.size();i++){
                String currEdge = edges.get(i);
                String[] currEdgeArr = currEdge.split(" ");
                int currV1 = Integer.parseInt(currEdgeArr[1]);
                int currV2 = Integer.parseInt(currEdgeArr[2]);
                int weight = Integer.parseInt(currEdgeArr[3]);
                int direction = Integer.parseInt(currEdgeArr[4]);

                if(!edgesThatGotDirectionInside.contains(i)){
                    Random randDirec = new Random();
                    int nextDirection = randDirec.nextInt(2);

                    edges.set(i, "e"+" "+currV1+" "+currV2+" "+weight+" "+String.valueOf(nextDirection));
                }

            }
            System.out.println("edgesThatGotDirectionInside3: "+edgesThatGotDirectionInside);

            //Print the edges and their weights
            System.out.println("Curr Edge Weight allocation: "+edges);
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
            int weight=Integer.parseInt(arr[3]);
            adj.get(x).add(new int[]{y, weight});
            adj.get(y).add(new int[]{x, weight});
        }
        //System.out.println("Adjacency list is: "+ adj);
        return adj;
    }


    public List minSpanningTreeUsingPrims() {
        List<List<int[]>> adjacencyList = getAdjacencyListOfGraphWithWeights();
        int n = xValues.size();
        System.out.println("Size of graph vertices: " + n);
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
            System.out.println("EdgesUsed value is: "+edgesUsed);
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
