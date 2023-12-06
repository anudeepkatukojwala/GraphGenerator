package src.main.java.com.graph_generator.graph;

import org.apache.commons.math4.legacy.linear.*;

import java.util.*;

public class TutteEmbedding {
    List<Double> xValues;
    List<Double> yValues;
    List<String> edges;
    List<List<Integer>> regions;
    List<List<Integer>> rotationSystem;

    List<Integer> selectedRegion;

    List<Integer> regionToPin;
    double r;

    public TutteEmbedding(List<Double> xValues, List<Double> yValues, List<String> edges, List<List<Integer>> regions, List<List<Integer>> rotationSystem, List<Integer> regionToPin, double r){
        this.xValues = xValues;
        this.yValues = yValues;
        this.edges = edges;
        this.regions = regions;
        this.rotationSystem = rotationSystem;
        this.regionToPin = regionToPin;
        this.r = r;
        System.out.println("Rotation System in Tuttes: "+ rotationSystem);
        System.out.println("Regions in Tuttes: "+regions);
    }

    public void selectRegion(){
        selectedRegion = regionToPin;
        System.out.println("Selected region: " + selectedRegion);

    }

    //From the selected region we update the vertices with new co-ordinates
    //of the regular convex polygon we draw
    public void updateWithNewVertices(){
        //Get size of selectedRegion to get the co-ordinates of the
        //new nSided Regular Convex Polygon
        int nSided = selectedRegion.size();

        for(int i=0;i<nSided;i++){
            int curr = selectedRegion.get(i);
            xValues.set(curr, Math.cos(((2*Math.PI*i)/nSided)));
            yValues.set(curr, Math.sin(((2*Math.PI*i)/nSided)));
//            xValues.add(Math.cos(((2*Math.PI*i)/nSided)));
//
//            yValues.add(Math.sin(((2*Math.PI*i)/nSided)));
//            //System.out.print(Math.cos(((2*Math.PI*i)/nSided))+" "+Math.sin(((2*Math.PI*i)/nSided))+"\n");

        }
//        System.out.println(xValues);
//        System.out.println(yValues);
//
    }

    public List<List<Double>> calculateNewVertexPositions(){
        selectRegion(); //Select any one region
        updateWithNewVertices(); //update some vertices with new vertices of regular n-sided polygon
        List<Integer> verticesOutsideOfPolygon = new ArrayList<>();
        HashMap<Integer, List<Integer>> vertexAdjListMap = new HashMap<>();
        //To get the vertices that are not part of the region we have selected
        //So that we can find the location of these vertices inside the convex n-sided regular polygon
        //that we drew where n is equal to the size of the selectedRegion list.
        for(int i=0;i<xValues.size();i++){
            if(!selectedRegion.contains(i)){
                verticesOutsideOfPolygon.add(i);
            }
        }


        for(int i=0;i<verticesOutsideOfPolygon.size();i++){
            int currVer = verticesOutsideOfPolygon.get(i);
            //For each vertex that is outside of the polygon
            //Get the adjacency list for that vertex
            List<Integer> adjListOfCurrVer = rotationSystem.get(currVer);
            //Add the mapping of vertex and it's adjacency list from rotationSystem to
            //a new mapping
            vertexAdjListMap.put(currVer, adjListOfCurrVer);
        }
        //Create adjacencyList with each adjacent vertex and index to the edge that makes
        // the connection between the vertex and its adjacent vertex
        Map<Integer, List<int[]>> adjacencyList = new HashMap<>();
        for(int i=0;i<edges.size();i++){
            String currEdge = edges.get(i);
            String[] edgeArr = currEdge.split(" ");
            int v1 = Integer.parseInt(edgeArr[1]);
            int v2 = Integer.parseInt(edgeArr[2]);
            if(!adjacencyList.containsKey(v1)){
                adjacencyList.put(v1, new ArrayList<>());
            }
            adjacencyList.get(v1).add(new int[]{v2, i});

            if(!adjacencyList.containsKey(v2)){
                adjacencyList.put(v2, new ArrayList<>());
            }
            adjacencyList.get(v2).add(new int[]{v1, i});
        }
        System.out.println("/*******************************************/");
        //Do BFS from all the vertices that are outside to find the weights of each edge
        Queue<Integer> queue = new LinkedList<>();

        for(int curr:selectedRegion){
            queue.offer(curr);
        }
        System.out.println("Initial queue: " + queue);
        boolean[] visited = new boolean[xValues.size()]; //to check if a vertex is visited
        int level = 1; //level in BFS tree


        //Mapping from edge index to level of that edge in BFS tree
        Map<Integer, Integer> edgeLevelMap = new HashMap<>();

        //BFS to find weights
        while(!queue.isEmpty()){
            int size=queue.size();
            for(int i=0;i<size;i++){
                int curr=queue.poll();
                if(visited[curr]){
                    continue;
                }
                visited[curr]=true;
                List<int[]> adjList = adjacencyList.get(curr);
                for(int[] adj: adjList){
                    int adjVertex = adj[0];
                    int indexToEdgeList = adj[1];
                    if(!visited[adjVertex]){
                        queue.offer(adjVertex);
                        //Put edge index to level mapping
                        edgeLevelMap.put(indexToEdgeList, level);
                        //Find the edge between curr and adj in edges list
                        String edge = edges.get(indexToEdgeList);
                        String[] edgeArr = edge.split(" ");
//                        double weight = Double.parseDouble(edgeArr[3]);
//                        weight = (1.0)/(Math.pow(r, level));
                        edge=edgeArr[0]+" "+edgeArr[1]+" "+edgeArr[2]+" "+level;
                        edges.set(indexToEdgeList, edge);
                        System.out.println(curr+":to:"+adjVertex+":"+edges.get(indexToEdgeList));
                    }
                    else{
                        int oldLevel = edgeLevelMap.get(indexToEdgeList);
                        if(level<oldLevel){
                            edgeLevelMap.put(indexToEdgeList, level);
                        }
                        //Find the edge between curr and adj in edges list
                        String edge = edges.get(indexToEdgeList);
                        String[] edgeArr = edge.split(" ");
                        double weight = Double.parseDouble(edgeArr[3]);
                        double nWeight = (1.0)/(Math.pow(r, level));
                        edge=edgeArr[0]+" "+edgeArr[1]+" "+edgeArr[2]+" "+(level<oldLevel?level:oldLevel);
                        edges.set(indexToEdgeList, edge);
                        System.out.println(curr+":to:"+adjVertex+":"+edges.get(indexToEdgeList)+":oldLevel"+oldLevel+":newLevel"+level);
                    }
                }
            }
            System.out.println("**************Finished a level***********");
            level++;
        }
        System.out.println("/*******************************************/");
        System.out.println("Edge levels here are: "+edges);


        //Calculate the vertices that should go inside the Regular Convex Polygon
        //using the system of linear equations which here in our case
        //is using Commons Math library

        //The formula that we use is A.X = B

        //Create matrix A with size equivalent (2 * (total number of vertices in this graph))
        int matrixSize = 2*xValues.size();
        double[][] A = new double[matrixSize][matrixSize];

        //Set one for the vertices on the polygon boundary
        for(int i=0;i<selectedRegion.size();i++){
            int curr = selectedRegion.get(i);
            A[2*curr][2*curr] = 1;
            A[(2*curr)+1][(2*curr)+1] = 1;
        }
//
//        //Set the values in the matrix for each edge
//        for(String edge: edges){
//
//            String[] edgeArr = edge.split(" ");
//            int v1 = Integer.parseInt(edgeArr[1]);
//            int v2 = Integer.parseInt(edgeArr[2]);
//            double weight = Double.parseDouble(edgeArr[3]);
//            A[v1][v2] = -1*weight;
//            A[v2][v1] = -1*weight;
//            A[v1][v1] += weight;
//            A[v2][v2] += weight;
//        }

        //For each non-polygon vertex(verticesOutsideOfPolygon) set the
        //co-efficient values for the respective positions
        for(int i=0;i<verticesOutsideOfPolygon.size();i++){
            int currVertex = verticesOutsideOfPolygon.get(i);
            List<int[]> adjList = adjacencyList.get(currVertex);
            for(int[] currArr:adjList){
                int curr=currArr[0];
                int indexToEdgeList = currArr[1];
                String edge = edges.get(indexToEdgeList);
                String[] edgeArr = edge.split(" ");
                int v1 = Integer.parseInt(edgeArr[1]);
                int v2 = Integer.parseInt(edgeArr[2]);
                int currLevel = Integer.parseInt(edgeArr[3]);
                double weight = 1.0/(Math.pow(r, currLevel));
                if(!selectedRegion.contains(v1)){
                    A[2*v1][2*v2] += -1*weight;
                    A[(2*v1)+1][(2*v2)+1] += -1*weight;
                    A[2*v1][2*v1] += weight;
                    A[(2*v1)+1][(2*v1)+1] += weight;
                }
                if(!selectedRegion.contains(v2)){
                    A[2*v2][2*v1] += -1*weight;
                    A[(2*v2)+1][(2*v1)+1] += -1*weight;
                    A[2*v2][2*v2] += weight;
                    A[(2*v2)+1][(2*v2)+1] += weight;
                }

//                A[2*currVertex][2*curr] = -1.0/adjList.size();
//                A[(2*currVertex)+1][(2*curr)+1] = -1.0/adjList.size();
            }
        }



        System.out.println("Array A is: "+Arrays.deepToString(A));

        //Create matrix B with size equivalent to (2 * (total number of vertices in this graph))
        double[] B = new double[matrixSize];

        //Set the values in matrix B
        for(int i=0;i<xValues.size();i++){
            if(!verticesOutsideOfPolygon.contains(i)){
                B[2*i] = xValues.get(i);
                B[(2*i)+1] = yValues.get(i);
            }
        }

//        System.out.println("Matrix A is: " + A);
//        System.out.println("Matrix B is: " + B);

        //Using Commons Math Library
        RealMatrix coefficients = new Array2DRowRealMatrix(A, false);
        DecompositionSolver solver = new org.apache.commons.math4.legacy.linear.LUDecomposition(coefficients).getSolver();

        RealVector constants = new ArrayRealVector(B, false);
        RealVector solution = solver.solve(constants);
        System.out.println("\nCommons Math library solution");

        for(int i=0;i<xValues.size();i++){
            xValues.set(i, solution.getEntry(2*i));
            yValues.set(i, solution.getEntry((2*i)+1));
            System.out.println(solution.getEntry(2*i)+" : "+solution.getEntry((2*i)+1));
        }

        List<List<Double>> result = new ArrayList<>();
        result.add(xValues);
        result.add(yValues);

        return result;
    }

}
