import org.apache.commons.math4.legacy.linear.*;

import java.util.*;

public class TutteEmbedding {
    List<Double> xValues;
    List<Double> yValues;
    List<String> edges;
    List<List<Integer>> regions;
    List<List<Integer>> rotationSystem;

    List<Integer> selectedRegion;

    public TutteEmbedding(List<Double> xValues, List<Double> yValues, List<String> edges, List<List<Integer>> regions, List<List<Integer>> rotationSystem){
        this.xValues = xValues;
        this.yValues = yValues;
        this.edges = edges;
        this.regions = regions;
        this.rotationSystem = rotationSystem;
    }

    public void selectRegion(){
        selectedRegion = regions.get(0);

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

        //Calculate the vertices that should go inside the Regular Convex Polygon
        //using the system of linear equations which here in our case
        //is using Commons Math library

        //The formula that we use is A.X = B

        //Create matrix A with size equivalent (2 * (total number of vertices in this graph))
        int matrixSize = 2*xValues.size();
        double[][] A = new double[matrixSize][matrixSize];

        //Fill the diagonal elements with 1's
        for(int i=0;i< A.length;i++){
            A[i][i] = 1;
        }

        //For each non-polygon vertex(verticesOutsideOfPolygon) set the
        //co-efficient values for the respective positions
        for(int i=0;i<verticesOutsideOfPolygon.size();i++){
            int currVertex = verticesOutsideOfPolygon.get(i);
            List<Integer> adjList = rotationSystem.get(currVertex);
            for(int curr:adjList){
                A[2*currVertex][2*curr] = -1.0/adjList.size();
                A[(2*currVertex)+1][(2*curr)+1] = -1.0/adjList.size();
            }
        }

        //Create matrix B with size equivalent to (2 * (total number of vertices in this graph))
        double[] B = new double[matrixSize];

        //Set the values in matrix B
        for(int i=0;i<xValues.size();i++){
            if(!verticesOutsideOfPolygon.contains(i)){
                B[2*i] = xValues.get(i);
                B[(2*i)+1] = yValues.get(i);
            }
        }

        System.out.println("Matrix A is: " + A);
        System.out.println("Matrix B is: " + B);

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
