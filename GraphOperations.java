import java.util.*;
public class GraphOperations {

    List<Double> xValues;
    List<Double> yValues;
    List<String> edges;

    List<Integer> oneRegionVertices;

    List<Integer> sizeOfAdjList;
    List<HashMap<Integer, Double>> vertexAngleMapping;

    List<List<Integer>> regions;

    public GraphOperations(List<Double> xValues, List<Double> yValues, List<String> edges){
        this.xValues = xValues;
        this.yValues = yValues;
        this.edges = edges;
        this.oneRegionVertices = new ArrayList<>();
        this.vertexAngleMapping = new ArrayList<>();
    }

    //Code for finding Rotation System for each vertex in the graph
    public List<List<Integer>> getRotationSystem(){
        List<List<Integer>> rotationSystem = new ArrayList<>();
        TreeMap<Integer, Integer> angleVertexMap = new TreeMap<>();
        //initialize the list
        for(int i=0;i<xValues.size();i++){
            rotationSystem.add(new ArrayList<>());
        }
        //get the adjacency list representation of our graph
        List<List<Integer>> adjacencyList = getAdjacencyListOfGraph();
        //create the rotation system
        for(int i=0;i<adjacencyList.size();i++){
            //Clear the angleVertexMap values for this iteration
            angleVertexMap.clear();
            vertexAngleMapping.add(new HashMap<>());
            //get the co-ordinates of the vertex for which we are currently
            //finding the rotation system
            double originX = xValues.get(i);
            double originY = yValues.get(i);
            //for each vertex that is adjacent to our current vertex for which
            //we are finding the rotation system do the following
            for(int curr:adjacencyList.get(i)){
                double x=xValues.get(curr);
                double y=yValues.get(curr);
                //find new vector (or new co-ordinates for each adjacent vertex)
                //by subtracting our origin vertex co-ordinates
                double newX = x-originX;
                double newY = y-originY;
                //find the angle of curr adjacent vertex using atan2
                double temp = Math.toDegrees(Math.atan2(newY, newX));
                //if the angle is negative it means our adjacent vertex is in quadrant 3 and 4
                //after moving the vector to origin
                //In that case add 180 to the angle to make it a positive value
                if(temp<0){
                    temp+=360;
                }
                System.out.print(temp+", ");
                //add the angle, vertex mapping to the TreeMap
                //TreeMap allows us to order the adjacent vertices in counter-clockwise order
                //with angle 0 being the start point of counter-clockwise ordering
                //Try saving vertex to angle mapping to avoid loss of information

                //angleVertexMap.put(temp, curr);
                vertexAngleMapping.get(i).put(curr, temp);
            }

//            vertexAngleMapping.add(new HashMap<>());
            //Add the vertices to the rotationSystem in the counter-clockwise order
//            for(int currKey:angleVertexMap.keySet()){
//                //Add vertex and angle as pair to a list of hashmap
////                vertexAngleMapping.get(i).put(angleVertexMap.get(currKey), currKey);
//                rotationSystem.get(i).add(angleVertexMap.get(currKey));
//            }

            //writing new code to order the vertices in the counter clockwise direction
            HashMap<Integer, Double> temp = new HashMap<>(vertexAngleMapping.get(i));
            while(!temp.isEmpty()){
                double min = Double.MAX_VALUE;
                int minKey = Integer.MAX_VALUE;
                for(int currKey:temp.keySet()){
                    if(temp.get(currKey)<min){
                        min = temp.get(currKey);
                        minKey = currKey;
                    }
                }
                rotationSystem.get(i).add(minKey);
                temp.remove(minKey);
            }



        }
        System.out.println("\nRotation System is: "+rotationSystem);
        System.out.println("VertexAngle mappings are: "+vertexAngleMapping);

        sizeOfAdjList = new ArrayList<>();
        for(List<Integer> curr:rotationSystem){
            sizeOfAdjList.add(curr.size());
        }

        return rotationSystem;
    }

    //Code for creating adjacency list out of the given vertices and edges of the graph
    public List<List<Integer>> getAdjacencyListOfGraph(){
        List<List<Integer>> adj = new ArrayList<>();
        for(int i=0;i<xValues.size();i++){
            adj.add(new ArrayList<>());
        }
        for(int i=0;i<edges.size();i++){
            String[] arr=edges.get(i).split(" ");
            int x=Integer.parseInt(arr[1]);
            int y=Integer.parseInt(arr[2]);

            adj.get(x).add(y);
            adj.get(y).add(x);

        }
        System.out.println("Adjacency list is: "+ adj);
        return adj;
    }

    //To get the regions of the given graph.
    //Each region is indicated with the vertices that are present in
    //that bounded region
    public List<List<Integer>> getRegions(){
        regions = new ArrayList<>();
        int[] visited = new int[xValues.size()];
        List<List<Node>> rotationSystemOfObjects = getObjectsOfAdjacencyList();
        dfs(regions, rotationSystemOfObjects, visited, 0, Integer.MIN_VALUE);
        System.out.println("Regions are: "+regions);
        return regions;
    }

    //DFS to find the vertices in each region
    public void dfs(List<List<Integer>> regions, List<List<Node>> rotationSystem, int[] visited, int curr, int parent){

        List<Node> currList = rotationSystem.get(curr);

        if(parent==Integer.MIN_VALUE){
            Node temp = currList.get(0);
            oneRegionVertices.add(temp.val);
            System.out.println("Curr: "+temp.val);
            temp.visited = true;
            dfs(regions, rotationSystem, visited, temp.val, curr);
        }
        else{
            for(int i=0;i<currList.size();i++){
                if(parent==currList.get(i).val){
                    Node temp = currList.get((i+1)%currList.size());
                    if(temp.visited==true){

                        regions.add(new ArrayList<>(oneRegionVertices));
                        //System.out.println("Curr Region vertices: "+regions);
                        oneRegionVertices.clear();
                        Node now = getNextVertex(rotationSystem, curr, parent);
                        if(now==null){
                            return;
                        }
                        else{
                            oneRegionVertices.add(now.val);
                            now.visited=true;
                            dfs(regions, rotationSystem, visited, now.val, curr);
                        }
                    }
                    else{
                        System.out.println("Curr: "+temp.val);
                        oneRegionVertices.add(temp.val);
                        temp.visited = true;
                        dfs(regions, rotationSystem, visited, temp.val, curr);
                        Node now = getNextVertex(rotationSystem, curr, parent);
                        if(now==null){
                            return;
                        }
                        else{
                            oneRegionVertices.add(now.val);
                            now.visited=true;
                            dfs(regions, rotationSystem, visited, now.val, curr);
                        }

                    }
                    break;
                }
            }
        }

    }

    // Helper Method for finding regions:
    // To get next non-visited vertex in an adjacency list after the vertex same as parent
    public Node getNextVertex(List<List<Node>> rotationSystem, int curr, int parent){
        List<Node> tempList = rotationSystem.get(curr);
        int size = tempList.size();
        int didWeFindNonVisitedVertex = 0;
        int parentIndex = Integer.MIN_VALUE;
        for(int i=0;i<tempList.size();i++){
            if(parent==tempList.get(i).val){
                parentIndex = i;
                break;
            }
        }

        int iterator = 1;
        while(size+1!=iterator){
            if(!tempList.get((parentIndex+iterator)%size).visited){
                didWeFindNonVisitedVertex=1;
                return tempList.get((parentIndex+iterator)%size);
            }
            iterator++;
        }
        return null;
    }


    //Note: Rotation system is just the ordering of adjacent vertices for each vertex,
    // which means Rotation System is just an Adjacency List with an order specified for its adjacent vertices for
    // each vertex.


    //To convert adjacency list (or rotation system) to objects of Node class.
    public List<List<Node>> getObjectsOfAdjacencyList(){
        List<List<Integer>> rotationSystem = getRotationSystem();
        List<List<Node>> objects = new ArrayList<>();
        for(int i=0;i<rotationSystem.size();i++){
            List<Integer> currList = rotationSystem.get(i);
            objects.add(new ArrayList<>());
            for(int currVal:currList){
                objects.get(i).add(new Node(currVal));
            }
        }
        System.out.println(objects);
        return objects;
    }

}
