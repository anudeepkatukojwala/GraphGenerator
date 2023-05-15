import java.util.*;
import org.apache.commons.math4.legacy.linear.*;


public class PreGraphDrawingOperations {
    List<Double> xValues;
    List<Double> yValues;
    List<String> edges;

    public PreGraphDrawingOperations(List<Double> xValues, List<Double> yValues, List<String> edges){
        this.xValues = xValues;
        this.yValues = yValues;
        this.edges = edges;
    }

    public boolean checkIfAnyThreePointsAreCollinear(List<HashMap<Integer, Double>> vertexAngleMapping, List<List<Integer>> rotationSystem){
        for(int i=0;i<rotationSystem.size();i++){
            List<Integer> currList = rotationSystem.get(i);
            HashMap<Integer, Double> currMap = vertexAngleMapping.get(i);
            for(int j=0;j<currList.size()-1;j++){
                double firstAngle = vertexAngleMapping.get(i).get(currList.get(j));
                double secondAngle = vertexAngleMapping.get(i).get(currList.get(j+1));
                double diffAngle = secondAngle-firstAngle;
                if(diffAngle<=Math.pow(10, -3)){
                    return false;
                }
            }
        }
        return true;
    }

    public boolean checkIfAnyEdgesAreCrossingEachOther(String[] edge1, String[] edge2){
        //Extract vertices from the given edges
        //We already know that these are the vertices that do not share any common vertex
        int firstEdgeFirstVertex = Integer.parseInt(edge1[1]);
        int firstEdgeSecondVertex = Integer.parseInt(edge1[2]);
        int secondEdgeFirstVertex = Integer.parseInt(edge2[1]);
        int secondEdgeSecondVertex = Integer.parseInt(edge2[2]);

        //Check if the edges cross
        boolean result = findIfEdgesCross(firstEdgeFirstVertex, firstEdgeSecondVertex, secondEdgeFirstVertex, secondEdgeSecondVertex);
        //If the edgess cross return true
        if(result){
            return true;
        }
        //Return false since the edges do not cross
        return false;
    }

    public boolean findIfEdgesCross(int firstEdgeFirstVertex, int firstEdgeSecondVertex, int secondEdgeFirstVertex, int secondEdgeSecondVertex){
        double ax = xValues.get(firstEdgeFirstVertex);
        double ay = yValues.get(firstEdgeFirstVertex);
        double bx = xValues.get(firstEdgeSecondVertex);
        double by = yValues.get(firstEdgeSecondVertex);
        double cx = xValues.get(secondEdgeFirstVertex);
        double cy = yValues.get(secondEdgeFirstVertex);
        double dx = xValues.get(secondEdgeSecondVertex);
        double dy = yValues.get(secondEdgeSecondVertex);

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
            return true;
        }
        else{
            return false;
        }

    }

    //Check for edges overlap and crossover for every two edges that don’t share any endpoints
    //The way we do this is:
    //    if parallel,
    //    1. check if collinear
    //    if collinear
    //        2. check if overlapping
    //        if overlapping: reject
    //        else: accept
    //    else: accept
    //else: do regular crossing check
    public boolean checkForOverlapOrCrossOverOfAnyTwoEdges(){
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
                boolean areParallel = checkIfTwoEdgesAreParallel(firstEdge, secondEdge);
                if(areParallel){
                    boolean areOnSameLine = checkIfTwoEdgesAreOnSameLine(firstEdge, secondEdge);
                    if(areOnSameLine){
                        boolean doTheTwoSegmentsOverlap = checkIfTwoSegmentsOverlap(firstEdge, secondEdge);
                        if(doTheTwoSegmentsOverlap){
                            //We return true to reject this graph
                            System.out.println("Edges: "+edges.get(i)+" : "+edges.get(j)+"; " +
                                    "These two segments overlap");
                            return true;
                        }
                        else{
                            System.out.println("Parallel and on same line, but do not overlap");
                        }
                    }
                    else{
                        System.out.println("Parallel but not on same line");
                    }

                }
                else{
                    //Check if the two edges are crossing each other with checkIfAnyEdgesAreCrossingEachOther()
                    boolean doTheEdgesCross = checkIfAnyEdgesAreCrossingEachOther(firstEdge, secondEdge);
                    if(doTheEdgesCross){
                        //We reject this graph because of the edges in this
                        // graph crossing each other
                        System.out.println("Edges: "+edges.get(i)+" : "+edges.get(j)+"; " +
                                "These two edges cross each other");
                        return true;
                    }

                }

            }
        }
        return false;
    }

    //To check if two edges that do not share the endpoint are parallel are not using the angle test
    public boolean checkIfTwoEdgesAreParallel(String[] edge1, String[] edge2){
        //Move the edge1 to the origin with one vertex as origin
        double originX1 = xValues.get(Integer.parseInt(edge1[1]));
        double originY1 = yValues.get(Integer.parseInt(edge1[1]));

        //Vertex using which we find the angle
        double x1 = xValues.get(Integer.parseInt(edge1[2]));
        double y1 = yValues.get(Integer.parseInt(edge1[2]));

        double newX1 = x1-originX1;
        double newY1 = y1-originY1;

        double angle1 = Math.toDegrees(Math.atan2(newY1, newX1));

        //if the angle is negative it means our edge1[2] vertex is in quadrant 3 and 4
        //after moving the vector to origin
        //In that case add 180 to the angle to make it a positive value
        //and bring that vertex to 1st or 2nd quadrant
        if(angle1<0){
            System.out.println("Angle1 modified: Original is: "+angle1);
            angle1+=180;
        }

        //Move the edge2 to the origin with one vertex as origin
        double originX2 = xValues.get(Integer.parseInt(edge2[1]));
        double originY2 = yValues.get(Integer.parseInt(edge2[1]));

        //Vertex using which we find the angle
        double x2 = xValues.get(Integer.parseInt(edge2[2]));
        double y2 = yValues.get(Integer.parseInt(edge2[2]));

        double newX2 = x2-originX2;
        double newY2 = y2-originY2;

        double angle2 = Math.toDegrees(Math.atan2(newY2, newX2));

        //if the angle is negative it means our edge2[2] vertex is in quadrant 3 and 4
        //after moving the vector to origin
        //In that case add 180 to the angle to make it a positive value
        //and bring that vertex to 1st or 2nd quadrant
        if(angle2<0){
            System.out.println("Angle2 modified: Original is: "+angle2);
            angle2+=180;
        }

        System.out.println("Angle 1 is: "+angle1+" : "+"Angle 2 is: "+angle2);
        System.out.println("Inisde Parallel Check: Edges are: "+edge1[1]+":"+edge1[2]+" and "+edge2[1]+":"+edge2[2]);

        //Here we are sure that the edges when moved to origin, are either in 1st or 2nd quadrant
        if(angle1==angle2){
            return true;
        }
        else{
            //Check the case where they are on the same line;
            // Example 0 and 180, they are parallel but not have same angle
            if(angle1<angle2){
                double smallest = angle1;
                //Add 180 to the smallest angle
                smallest+=180;
                if(angle2==smallest){
                    System.out.println("Angles are same after adding 180 to angle1");
                    return true;
                }
            }
            else{
                double smallest = angle2;
                //Add 180 to the smallest angle
                smallest+=180;
                if(angle1==smallest){
                    System.out.println("Angles are same after adding 180 to angle2");
                    return true;
                }
            }
            //Now the angles are not same and the angles are not 180 degrees apart
            //We are sure that the angles are not parallel to each other
            return false;
        }

    }


    //To check if two edges that do not share a common endpoint lie on the same line
    public boolean checkIfTwoEdgesAreOnSameLine(String[] edge1, String[] edge2){
        //vertex 1 of edge1
        double edge1X1 = xValues.get(Integer.parseInt(edge1[1]));
        double edge1Y1 = yValues.get(Integer.parseInt(edge1[1]));

        //vertex 2 of edge1
        double edge1X2 = xValues.get(Integer.parseInt(edge1[2]));
        double edge1Y2 = yValues.get(Integer.parseInt(edge1[2]));

        //vertex 1 of edge2
        double edge2X1 = xValues.get(Integer.parseInt(edge2[1]));
        double edge2Y1 = yValues.get(Integer.parseInt(edge2[1]));

        //vertex 2 of edge2
        double edge2X2 = xValues.get(Integer.parseInt(edge2[2]));
        double edge2Y2 = yValues.get(Integer.parseInt(edge2[2]));

        //To check if two edges lie on the same line we check the angle made
        //between two vectors using v.w = |v|.|w|.cos(theta), where theta is the
        //angle formed by two vectors v and w.
        //If (theta) is 0 or 180 between two vectors then two edges lie on
        //the same line.
        //If (A,B) are vertices of edge1 and (C,D) are the vertices of edge2
        //v=(B-A) for both x and y coordinates.
        //w1=(C-A) for both x and y coordinates.
        //w can be w2=(D-A) both x and y coordinates.
        //Now we find angles between (v and w1) and (v and w2)

        //Coordinates of vector v
        double vX = edge1X2 - edge1X1;
        double vY = edge1Y2 - edge1Y1;

        //Coordinates of vector w1
        double w1X = edge2X1 - edge1X1;
        double w1Y = edge2Y1 - edge1Y1;

        //Coordinates of vector w2
        double w2X = edge2X2 - edge1X1;
        double w2Y = edge2Y2 - edge1Y1;

        //Angle between vector v and w1
        //arc cosine ((v.w1)/(|v|.|w1|))

        //Lets find v.w1
        //v.w1 = vx * w1x + vy * w1y
        double dotProductOfVandW1 = (vX * w1X) + (vY*w1Y);
        double magnitudeOfV = Math.sqrt((Math.pow(vX, 2)+Math.pow(vY, 2)));
        double magnitudeOfW1 = Math.sqrt((Math.pow(w1X, 2)+Math.pow(w1Y, 2)));

        double angle1 = Math.toDegrees(Math.acos((dotProductOfVandW1)/(magnitudeOfV*magnitudeOfW1)));

        //Angle between vector v and w2
        //arc cosine ((v.w2)/(|v|.|w2|))
        double dotProductOfVandW2 = (vX*w2X)+(vY*w2Y);
        double magnitudeOfW2 = Math.sqrt((Math.pow(w2X, 2)+Math.pow(w2Y, 2)));

        double angle2 = Math.toDegrees(Math.acos((dotProductOfVandW2)/(magnitudeOfV*magnitudeOfW2)));

        System.out.println("Inside checkIfTwoEdgesAreOnSameLine subroutine: Angle 1 is: "+angle1+" : "+"Angle 2 is: "+angle2);

        if((angle1==0) || (angle1==180)){
            if((angle2==0) || (angle2==180)){
                //if two edges lie on same line
                return true;
            }
            else{
                return false;
            }
        }
        else{
            //if two edges do not lie on the same line
            return false;
        }



    }

    //To check if two segments overlap
    public boolean checkIfTwoSegmentsOverlap(String[] edge1, String[] edge2){
        //vertex 1 of edge1
        double edge1X1 = xValues.get(Integer.parseInt(edge1[1]));
        double edge1Y1 = yValues.get(Integer.parseInt(edge1[1]));

        //vertex 2 of edge1
        double edge1X2 = xValues.get(Integer.parseInt(edge1[2]));
        double edge1Y2 = yValues.get(Integer.parseInt(edge1[2]));

        //vertex 1 of edge2
        double edge2X1 = xValues.get(Integer.parseInt(edge2[1]));
        double edge2Y1 = yValues.get(Integer.parseInt(edge2[1]));

        //vertex 2 of edge2
        double edge2X2 = xValues.get(Integer.parseInt(edge2[2]));
        double edge2Y2 = yValues.get(Integer.parseInt(edge2[2]));


        //If (A,B) are vertices of edge1 and (C,D) are the vertices of edge2
        //v=(B-A) for both x and y coordinates.
        //w1=(C-A) for both x and y coordinates.
        //w can be w2=(D-A) both x and y coordinates.

        //Coordinates of vector v
        double vX = edge1X2 - edge1X1;
        double vY = edge1Y2 - edge1Y1;

        //Coordinates of vector w1
        double w1X = edge2X1 - edge1X1;
        double w1Y = edge2Y1 - edge1Y1;

        //Coordinates of vector w2
        double w2X = edge2X2 - edge1X1;
        double w2Y = edge2Y2 - edge1Y1;

        //Let b=B−A, c=C−A,and d = D−A.
        //compute x = (c·b)/(b·b) and y = (d·b)/(b·b).
        //If both x,y > 1 or x, y < 0, then the line segments don’t intersect.
        //Here b is v, c is w1, d is w2 for both x and y coordinates

        double x = ((w1X*vX)+(w1Y*vY))/((vX*vX)+(vY*vY));
        double y = ((w2X*vX)+(w2Y*vY))/((vX*vX)+(vY*vY));

        System.out.println("Inside checkIfTwoSegmentsOverlap: Value of x:" + x + ": Value of y:"+y);

        if(x>1 || x<0){
            if(y>1 || y<0){
                return false;
            }
            else{
                return true;
            }
        }
        else{
            return true;
        }

    }





}
