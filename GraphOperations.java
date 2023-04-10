import java.util.*;
public class GraphOperations {

    List<Integer> xValues;
    List<Integer> yValues;
    List<String> edges;

    List<Integer> oneRegionVertices;

    List<Integer> sizeOfAdjList;

    List<List<Integer>> regions;

    public GraphOperations(List<Integer> xValues, List<Integer> yValues, List<String> edges){
        this.xValues = xValues;
        this.yValues = yValues;
        this.edges = edges;
        this.oneRegionVertices = new ArrayList<>();
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
            //get the co-ordinates of the vertex for which we are currently
            //finding the rotation system
            int originX = xValues.get(i);
            int originY = yValues.get(i);
            //for each vertex that is adjacent to our current vertex for which
            //we are finding the rotation system do the following
            for(int curr:adjacencyList.get(i)){
                int x=xValues.get(curr);
                int y=yValues.get(curr);
                //find new vector (or new co-ordinates for each adjacent vertex)
                //by subtracting our origin vertex co-ordinates
                int newX = x-originX;
                int newY = y-originY;
                //find the angle of curr adjacent vertex using atan2
                int temp = (int)Math.toDegrees(Math.atan2(newY, newX));
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
                angleVertexMap.put(temp, curr);
            }

            //Add the vertices to the rotationSystem in the counter-clockwise order
            for(int currKey:angleVertexMap.keySet()){
                rotationSystem.get(i).add(angleVertexMap.get(currKey));
            }



        }
        System.out.println("\nRotation System is: "+rotationSystem);

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
            temp.visited = true;
            dfs(regions, rotationSystem, visited, temp.val, curr);
        }
        else{
            for(int i=0;i<currList.size();i++){
                if(parent==currList.get(i).val){
                    Node temp = currList.get((i+1)%currList.size());
                    if(temp.visited==true){

                        regions.add(new ArrayList<>(oneRegionVertices));
                        System.out.println("Curr Region vertices: "+regions);
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
                return tempList.get(parentIndex+iterator%size);
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
