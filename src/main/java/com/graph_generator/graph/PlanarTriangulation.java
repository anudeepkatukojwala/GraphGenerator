package src.main.java.com.graph_generator.graph;

import java.util.ArrayList;
import java.util.List;

public class PlanarTriangulation {
    List<Double> xValues;
    List<Double> yValues;
    List<String> edges;
    List<List<Integer>> regions;
    List<List<Integer>> rotationSystem;
    //Used by getRegions()
    List<Integer> oneRegionVertices;


    public PlanarTriangulation(List<Double> xValues, List<Double> yValues, List<String> edges, List<List<Integer>> regions, List<List<Integer>> rotationSystem){
        this.xValues = xValues;
        this.yValues = yValues;
        this.edges = edges;
        this.regions = regions;
        this.rotationSystem = rotationSystem;
        this.oneRegionVertices = new ArrayList<>();

    }

    //After Planar Triangulation the edges should be
    // equivalent to E = 2V-6
    //where E is edges, V are vertices
    public List createPlanarTriangulationOfGivenGraph(){
        for(int i=0;i<regions.size();){
            List<Integer> currRegion = regions.get(i);
            if(currRegion.size()>3){
                boolean didWeAddAnEar = addEar(currRegion);
                if(didWeAddAnEar){
                    i=0;
                    continue;
                }

            }
            i++;
        }

        List returnList = new ArrayList<>();
        returnList.add(rotationSystem);
        returnList.add(regions);
        returnList.add(edges);

        //System.out.println("Return list: "+returnList);
        return returnList;

    }

    //In any region [r1, r2, . . . , rk], where k ≥ 4, there is a
    // diagonal (ri, r(i+2) mod k) that can be added to the graph,
    // i.e., these two vertices are distinct, and the edge
    // between them is not already in the graph.
    //These diagonals are usually called ears.
    public boolean addEar(List<Integer> currRegion){
        //get the size of the current region
        int sizeOfRegion = currRegion.size();

        //for each pair of vertices that satisfy the formula (ri, r(i+2) mod k)
        for(int i=0;i<currRegion.size();i++){
            int firstVertex = currRegion.get(i);
            int secondVertex = currRegion.get((i+2)%sizeOfRegion);
            //check if the two vertices in consideration are same
            if(firstVertex!=secondVertex){
                //If the two vertices in consideration are not same do the below
                //Get adjacency list of first vertex
                List<Integer> adjacencyListOfFirstVertex = rotationSystem.get(firstVertex);
                //Get adjacency list of second vertex
                List<Integer> adjacencyListOfSecondVertex = rotationSystem.get(secondVertex);
                //check if the two vertices in consideration already have an edge between them.
                //We check this by checking if the second vertex is present in the firstVertex adjacency list
                //We do the same for second vertex as well
                if(!adjacencyListOfFirstVertex.contains(secondVertex) && !adjacencyListOfSecondVertex.contains(firstVertex)){
                    //Add the edge between firstVertex and secondVertex
                    edges.add("e " + firstVertex + " " + secondVertex);

                    System.out.println("New Ear Added: e " + firstVertex + " " + secondVertex);

                    //Here we have added an ear
                    //Now we should modify the rotationSystem to reflect the newly added edge in adjacency list
                    //Because adding an edge will add a vertex to both of the vertex's rotationSystem

                    //Modify the rotationSystem and carefully insert the added edge into the rotationSystem

                    //Get the indices of vertex before the firstVertex and secondVertex in our currRegion
                    int indexVertexBeforeTheFirstVertex = (i-1+sizeOfRegion)%sizeOfRegion;
                    int indexVertexBeforeTheSecondVertex = (i+1)%sizeOfRegion;

                    //Now get the actual vertex value of the indices we found above
                    int fromRegionGetVertexBeforeTheFirstVertex = currRegion.get(indexVertexBeforeTheFirstVertex);
                    int fromRegionGetVertexBeforeTheSecondVertex = currRegion.get(indexVertexBeforeTheSecondVertex);

                    //In the rotationSystem of firstVertex find index of fromRegionGetVertexBeforeTheFirstVertex
                    // to insert secondVertex after this vertex
                    int indexInTheRotationSystemForFirstVertex = adjacencyListOfFirstVertex.indexOf(fromRegionGetVertexBeforeTheFirstVertex);

                    //In the rotationSystem of secondVertex find index of fromRegionGetVertexBeforeTheSecondVertex
                    // to insert firstVertex after this vertex
                    int indexInTheRotationSystemForSecondVertex = adjacencyListOfSecondVertex.indexOf(fromRegionGetVertexBeforeTheSecondVertex);

                    //Insert secondVertex at (indexInTheRotationSystemForFirstVertex+1)%sizeOfRegion
                    // in our rotationSystem of firstVertex
                    adjacencyListOfFirstVertex.add((indexInTheRotationSystemForFirstVertex+1)%sizeOfRegion, secondVertex);

                    //Insert firstVertex at (indexInTheRotationSystemForSecondVertex+1)%sizeOfRegion
                    //in our rotationSystem of secondVertex
                    adjacencyListOfSecondVertex.add((indexInTheRotationSystemForSecondVertex+1)%sizeOfRegion, firstVertex);

                    //Here we have added an ear
                    //Now we should verify to see if the rotationSystem reflects the newly added vertices
                    // to our rotationSystem
                    System.out.println("Verification: "+rotationSystem);

                    //Now get the updated regions after our update to the rotationSystem
                    regions = getRegions();
                    return true;
                }
            }
        }

        return false;
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
            //System.out.println("Curr: "+temp.val);
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
                        //System.out.println("Curr: "+temp.val);
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


    //To convert adjacency list (or rotation system) to objects of src.main.java.com.graphgenerator.graph.Node class.
    public List<List<Node>> getObjectsOfAdjacencyList(){
        //List<List<Integer>> rotationSystem = getRotationSystem();
        List<List<Node>> objects = new ArrayList<>();
        for(int i=0;i<rotationSystem.size();i++){
            List<Integer> currList = rotationSystem.get(i);
            objects.add(new ArrayList<>());
            for(int currVal:currList){
                objects.get(i).add(new Node(currVal));
            }
        }
        //System.out.println(objects);
        return objects;
    }

}
