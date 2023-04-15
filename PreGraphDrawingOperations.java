import java.util.*;

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
//                if(diffAngle<=Math.pow(10, -3)){
//                    return false;
//                }
            }
        }
        return true;
    }


}
