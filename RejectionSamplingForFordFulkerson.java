import java.util.List;
import java.util.*;

public class RejectionSamplingForFordFulkerson {
    List<Double> xValues;
    List<Double> yValues;
    List<String> edges;
    int start;
    int end;

    public RejectionSamplingForFordFulkerson(List<Double> xValues, List<Double> yValues, List<String> edges, int start, int end){
        this.xValues = xValues;
        this.yValues = yValues;
        this.edges=edges;
        this.start = start;
        this.end = end;
    }

    public List rejecionSamplingProcedure(){


        while(true){
            while(true){
                //Random class object to generate random edge weights
                Random random = new Random();

                //Get the total number of edges
                int totalEdges=edges.size();

                //Generate random edge weights between 1 and E
                for(int i=0;i<edges.size();i++){
                    String currEdg = edges.get(i);
                    String[] edgArr = currEdg.split(" ");
                    edgArr[3] = String.valueOf(random.nextInt(edges.size())+1);
                    edges.set(i, edgArr[0]+" "+edgArr[1]+" "+edgArr[2]+" "+edgArr[3]+" "+"0");
                }
                List<Integer> indicesThatAreNotSOrT = new ArrayList<>();
                // Add all indices except start and end
                for(int i=0; i<xValues.size(); i++) {
                    if(i != start && i != end) {
                        indicesThatAreNotSOrT.add(i);
                    }
                }
                // Generate all combinations of S and T
                List<List<Integer>> sCombinations = new ArrayList<>();
                List<List<Integer>> tCombinations = new ArrayList<>();
                generateCombinations(indicesThatAreNotSOrT, new ArrayList<>(), new ArrayList<>(), 0, sCombinations, tCombinations);

                // Print results
//        for (int i = 0; i < sCombinations.size(); i++) {
//            System.out.println("S: " + sCombinations.get(i) + ", T: " + tCombinations.get(i));
//        }

                List<Integer> cutValues = new ArrayList<>();
                //Calculate the cut value for each combination of S and T
                for(int i=0; i<sCombinations.size(); i++){
                    List<Integer> sNodes = sCombinations.get(i);
                    List<Integer> tNodes = tCombinations.get(i);
                    sNodes.add(start);
                    tNodes.add(end);

                    int cutValue = calculateCut(sNodes, tNodes);
//                    System.out.println("S: " + sNodes + ",\nT: " + tNodes+" ,\ncuValue: "+cutValue+"\nCapacities: "+edges);

                    cutValues.add(cutValue);
                }

                //Find the min cut value from cutValues
                int minCut = cutValues.stream().mapToInt(Integer::intValue).min().orElseThrow(NoSuchElementException::new);
//                System.out.println("Inside the rejection sampling loop: minCut is: "+minCut);
                //Check if there is a duplicate of minCut in cutValues
                if(Collections.frequency(cutValues, minCut)==1){
                    break;
                }
            }

            //Run the Ford Fulkerson algorithm to find the max flow
            //Call Ford-Fulkerson algorithm to find maximum flow
            //Construct the adjacency matrix from the edges list
            int[][] adjacencyMatrix = new int[xValues.size()][xValues.size()];
            for(String edge : edges){
                String[] vertices = edge.split(" ");
                int u = Integer.parseInt(vertices[1]);
                int v = Integer.parseInt(vertices[2]);
                int w = Integer.parseInt(vertices[3]);
                adjacencyMatrix[u][v] = w;

            }
            MaxFlow fordFulkerson = new MaxFlow(xValues, yValues, edges);

            // Find the maximum flow
            List returnOfFordFulkerson = fordFulkerson.fordFulkerson(adjacencyMatrix, 0, 5);
            int maxFlow = (int)returnOfFordFulkerson.get(0);
            System.out.println("Max FLow is: "+maxFlow);
            int[][] graph = (int[][])returnOfFordFulkerson.get(1);
            int[][] rGraph = (int[][])returnOfFordFulkerson.get(2);
            boolean uniquePathExists = (boolean)returnOfFordFulkerson.get(4);

            System.out.println("Final Edges are: "+edges);
            if(uniquePathExists){
                return returnOfFordFulkerson;
            }
        }

    }

    public static void generateCombinations(List<Integer> indices, List<Integer> currentS, List<Integer> currentT, int index,
                                            List<List<Integer>> sCombinations, List<List<Integer>> tCombinations) {
        if (index == indices.size()) {
            sCombinations.add(new ArrayList<>(currentS));
            tCombinations.add(new ArrayList<>(currentT));
        } else {
            int currentIndex = indices.get(index);

            // Include current index in S
            currentS.add(currentIndex);
            generateCombinations(indices, currentS, currentT, index + 1, sCombinations, tCombinations);
            currentS.remove(currentS.size() - 1);

            // Include current index in T
            currentT.add(currentIndex);
            generateCombinations(indices, currentS, currentT, index + 1, sCombinations, tCombinations);
            currentT.remove(currentT.size() - 1);
        }
    }

    // Function to calculate the total weight of edges crossing from S to T
    public int calculateCut(List<Integer> S, List<Integer> T) {
        int cutWeight = 0;

        // Iterate through all edges in the graph
        for (int u = 0; u < edges.size(); u++) {
            // Check if edge is from S to T
            String edge = edges.get(u);
            String[] edgeArr = edge.split(" ");
            int source = Integer.parseInt(edgeArr[1]);
            int dest = Integer.parseInt(edgeArr[2]);
            int weight = Integer.parseInt(edgeArr[3]);

            if(S.contains(source) && T.contains(dest)) {
                //                System.out.println("Edge from "+source+" to "+dest+" crosses S and T");
                cutWeight += weight;
            }
        }

        return cutWeight;
    }
}
