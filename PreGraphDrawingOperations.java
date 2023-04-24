import java.util.*;
import org.apache.commons.math4.legacy.linear.*;

public class PreGraphDrawingOperations {
    List<Integer> xValues;
    List<Integer> yValues;
    List<String> edges;

    public PreGraphDrawingOperations(List<Integer> xValues, List<Integer> yValues, List<String> edges){
        this.xValues = xValues;
        this.yValues = yValues;
        this.edges = edges;
    }

    public boolean checkIfAnyThreePointsAreCollinear(List<HashMap<Integer, Integer>> vertexAngleMapping, List<List<Integer>> rotationSystem){
        for(int i=0;i<rotationSystem.size();i++){
            List<Integer> currList = rotationSystem.get(i);
            HashMap<Integer, Integer> currMap = vertexAngleMapping.get(i);
            for(int j=0;j<currList.size()-1;j++){
                int firstAngle = vertexAngleMapping.get(i).get(currList.get(j));
                int secondAngle = vertexAngleMapping.get(i).get(currList.get(j+1));
                double diffAngle = secondAngle-firstAngle;
                if(diffAngle<=Math.pow(10, -3)){
                    return false;
                }
            }
        }
        return true;
    }

    public boolean checkIfAnyEdgesAreCrossingEachOther(){
        //consider only the edges that do not have
        //common vertices
        for(int i=0;i<edges.size()-1;i++){
            for(int j=i+1;j<edges.size();j++){
                String[] firstEdge = edges.get(i).split(" ");
                String[] secondEdge = edges.get(j).split(" ");
                int firstEdgeFirstVertex = Integer.parseInt(firstEdge[1]);
                int firstEdgeSecondVertex = Integer.parseInt(firstEdge[2]);
                int secondEdgeFirstVertex = Integer.parseInt(secondEdge[1]);
                int secondEdgeSecondVertex = Integer.parseInt(secondEdge[2]);
                if(firstEdgeFirstVertex == secondEdgeFirstVertex || firstEdgeFirstVertex == secondEdgeSecondVertex || firstEdgeSecondVertex == secondEdgeFirstVertex || firstEdgeSecondVertex == secondEdgeSecondVertex){
                    continue;
                }
                boolean result = findIfEdgesCross(firstEdgeFirstVertex, firstEdgeSecondVertex, secondEdgeFirstVertex, secondEdgeSecondVertex);
                if(result){
                    return true;
                }

            }
        }
        return false;
    }

    public boolean findIfEdgesCross(int firstEdgeFirstVertex, int firstEdgeSecondVertex, int secondEdgeFirstVertex, int secondEdgeSecondVertex){
        int ax = xValues.get(firstEdgeFirstVertex);
        int ay = yValues.get(firstEdgeFirstVertex);
        int bx = xValues.get(firstEdgeSecondVertex);
        int by = yValues.get(firstEdgeSecondVertex);
        int cx = xValues.get(secondEdgeFirstVertex);
        int cy = yValues.get(secondEdgeFirstVertex);
        int dx = xValues.get(secondEdgeSecondVertex);
        int dy = yValues.get(secondEdgeSecondVertex);

        //Using Commons Math Library
        RealMatrix coefficients = new Array2DRowRealMatrix(new double[][] { {(bx-ax), (-dx+cx)}, {(by-ay), (-dy+cy)} }, false);
        DecompositionSolver solver = new org.apache.commons.math4.legacy.linear.LUDecomposition(coefficients).getSolver();

        RealVector constants = new ArrayRealVector(new double[] {(cx-ax), (cy-ay)}, false);
        RealVector solution = solver.solve(constants);
        System.out.println("\nCommons Math library solution");
        double t1 = solution.getEntry(0);
        double t2 = solution.getEntry(1);
        System.out.println(solution.getEntry(0)+" "+solution.getEntry(1));
        if((t1>=0 && t1<=1) && (t2>=0 && t2<=1)){
            return false;
        }
        else{
            return false;
        }

    }









}
