import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.*;

public class SubDivide {
    List<Double> xValues;
    List<Double> yValues;
    List<String> edges;
    List<List<Integer>> regions;
    List<List<Integer>> rotationSystem;
    //Used by getRegions()
    List<Integer> oneRegionVertices;

    public SubDivide(List<Double> xValues, List<Double> yValues, List<String> edges, List<List<Integer>> regions, List<List<Integer>> rotationSystem){
        this.xValues=xValues;
        this.yValues=yValues;
        this.edges=edges;
        this.regions=regions;
        this.rotationSystem=rotationSystem;
        this.oneRegionVertices=new ArrayList<>();
    }

    public List testSubDivideMethod(int n, boolean shouldWeChooseExternalRegion){
        for(int i=0;i<n;i++){
            selectTwoValuesToAddEdge();
        }

        edges.clear();
        for(int i=0;i<rotationSystem.size();i++){
            List<Integer> currRotation = rotationSystem.get(i);
            for(int j:currRotation){
                if(i<j){
                    edges.add("e "+i+" "+j);
                }
            }
        }
        System.out.println("\n\nEdges after our subdivide method: "+edges);
        List result = new ArrayList<>();
        result.add(xValues);
        result.add(yValues);
        result.add(edges);
        result.add(rotationSystem);
        result.add(regions);

        return result;
    }

    public void selectTwoValuesToAddEdge(){
        Random rand = new Random();

        System.out.println("Regions in subdivide: "+regions);
        int randomRegionNumber = rand.nextInt(regions.size());


        List<Integer> currPickedRegion = regions.get(randomRegionNumber);

        while(currPickedRegion.contains(0) && currPickedRegion.contains(1)){
            randomRegionNumber = rand.nextInt(regions.size());


            currPickedRegion = regions.get(randomRegionNumber);
        }

        System.out.println("Curr picked region in subdivide: "+currPickedRegion);

        //Get all the edges in this region into a list
        List<String> edgesInThisRegion = new ArrayList<>();

        for(int i=0;i<currPickedRegion.size();i++){
            int v1=currPickedRegion.get(i);
            int v2=0;
            if(i==currPickedRegion.size()-1){
                v2=currPickedRegion.get(0);
            }
            else{
                v2=currPickedRegion.get(i+1);
            }
            edgesInThisRegion.add("e "+v1+" "+v2);
        }

        int sizeOfCurrKeySpace = edgesInThisRegion.size()+currPickedRegion.size();

        //Now pick any two values from the range of 0 to sizeOfCurrKeySpace-1

        while(true){
            int index1 = rand.nextInt(sizeOfCurrKeySpace);
            int index2 = index1;
            while (index2 == index1) {
                index2 = rand.nextInt(sizeOfCurrKeySpace);
            }
            boolean index1Edge=true;
            boolean index2Edge=true;
            if(index1>edgesInThisRegion.size()-1){
                index1Edge=false;
            }
            if(index2>edgesInThisRegion.size()-1){
                index2Edge=false;
            }


            if(index1Edge && index2Edge){
                String currEdge1 = edgesInThisRegion.get(index1);
                String currEdge2 = edgesInThisRegion.get(index2);
                System.out.println("**************************************");
                subDivideTwoGivenEdgesAndAddEdgeBetweenThem(currEdge1, currEdge2, currPickedRegion);
                System.out.println("**************************************");
            }
            else if((index1Edge && !index2Edge)){
                String currEdge = edgesInThisRegion.get(index1);

                int currVertexIndex = index2-edgesInThisRegion.size();
                int currVertex = currPickedRegion.get(currVertexIndex);
                boolean result = checkIfGivenVertexIsEndPointOfGivenEdge(currEdge, currVertex);
                if(result){
                    continue;
                }
                else{
                    System.out.println("**************************************");
                    subDivideGivenEdgeAndAddEdgeBetweenNewVertexAndGivenVertex(currEdge, currVertex, currPickedRegion);
                    System.out.println("**************************************");
                }
            }
            else if(!index1Edge && index2Edge){
                String currEdge = edgesInThisRegion.get(index2);
                int currVertexIndex = index1-edgesInThisRegion.size();
                int currVertex = currPickedRegion.get(currVertexIndex);

                boolean result = checkIfGivenVertexIsEndPointOfGivenEdge(currEdge, currVertex);
                if(result){
                    continue;
                }
                else{
                    System.out.println("**************************************");
                    subDivideGivenEdgeAndAddEdgeBetweenNewVertexAndGivenVertex(currEdge, currVertex, currPickedRegion);
                    System.out.println("**************************************");
                }
            }
            else{

                int currVertex1Index = index1-edgesInThisRegion.size();
                int currVertex1 = currPickedRegion.get(currVertex1Index);
                int currVertex2Index = index2-edgesInThisRegion.size();
                int currVertex2 = currPickedRegion.get(currVertex2Index);
                boolean result = checkIfEdgeExists(currVertex1, currVertex2, edgesInThisRegion);

                if(result){
                    continue;
                }
                else{
                    System.out.println("**************************************");
                    addEdgeBetweenTwoGivenVertices(currVertex1, currVertex2, currPickedRegion);
                    System.out.println("**************************************");
                }
            }

            break;
        }


    }

    public void subDivideGivenEdgeAndAddEdgeBetweenNewVertexAndGivenVertex(String edge, int vertex, List<Integer> currPickedRegion){
        int sizeOfRegion = currPickedRegion.size();

        //Get the edge to divide
        String edgeToDivide = edge;
        //Vertices of the edge
        String[] edgeArr = edgeToDivide.split(" ");

        int uIndex = Integer.parseInt(edgeArr[1]);
        int vIndex = Integer.parseInt(edgeArr[2]);

        //Get vertex co-ordinates of u and v
        double uX = xValues.get(uIndex);
        double uY = yValues.get(uIndex);

        double vX = xValues.get(vIndex);
        double vY = yValues.get(vIndex);


        //Delete first edge from edge list
//        edges.remove(String.valueOf(firstEdge));

        //Delete second edge as well, cuz if we add any edge, then the indices of firstEdgeIndex
        //and secondEdgeIndex will not be the same
        //Delete second edge from edge list
//        edges.remove(String.valueOf(secondEdge));

        //Add new vertex w between u and v

        //Before adding vertex, calculate the vertex co-ordinates
        //of w as the midpoint of u and v
        //We just need to calculate the midpoint of u and v's
        // x and y co-ordinates
        double wX = (uX + vX)/2.0;
        double wY = (uY + vY)/2.0;

        //Now add x and y co-ordinates of w to xValues and yValues
        xValues.add(wX);
        yValues.add(wY);

        int indexOfNewlyAddedVertex = xValues.size()-1;

        System.out.println("Inside subDivideGivenEdgeAndAddEdgeBetweenNewVertexAndGivenVertex :edge added: "+"e "+uIndex+" "+indexOfNewlyAddedVertex);
        System.out.println("Inside subDivideGivenEdgeAndAddEdgeBetweenNewVertexAndGivenVertex :edge added: "+"e "+indexOfNewlyAddedVertex+" "+vIndex);
        System.out.println("Inside subDivideGivenEdgeAndAddEdgeBetweenNewVertexAndGivenVertex :edge added: "+"e "+indexOfNewlyAddedVertex+" "+vertex);

        //Add vertices to the rotationSystem
        //Get rotationSystem of u, v, w and then update the rotation system
        //Now repeat the above for u2, v2
        List<Integer> rotationOfU = rotationSystem.get(uIndex);
        List<Integer> rotationOfV = rotationSystem.get(vIndex);


        rotationOfU.set(rotationOfU.indexOf(vIndex), indexOfNewlyAddedVertex);


        rotationOfV.set(rotationOfV.indexOf(uIndex), indexOfNewlyAddedVertex);

        List<Integer> indexOfNewlyAddedVertexRotationSystem = Arrays.asList(uIndex, vertex, vIndex);

        //Add these new rotationSystems to our rotationSystem
        rotationSystem.add(new ArrayList<>(indexOfNewlyAddedVertexRotationSystem));

        //Modify the rotationSystem and carefully insert the added edge into the rotationSystem

        //Get adjacency list of vertex
        List<Integer> adjacencyListOfFirstVertex = rotationSystem.get(vertex);

        //Get the indices of vertex before the firstVertex and secondVertex in our currRegion
        int indexVertexBeforeTheFirstVertex = (currPickedRegion.indexOf(vertex) - 1 + sizeOfRegion) % sizeOfRegion;


        //Now get the actual vertex value of the indices we found above
        int fromRegionGetVertexBeforeTheFirstVertex = currPickedRegion.get(indexVertexBeforeTheFirstVertex);


        //In the rotationSystem of firstVertex find index of fromRegionGetVertexBeforeTheFirstVertex
        // to insert secondVertex after this vertex
        int indexInTheRotationSystemForFirstVertex = adjacencyListOfFirstVertex.indexOf(fromRegionGetVertexBeforeTheFirstVertex);


        //Insert secondVertex at (indexInTheRotationSystemForFirstVertex+1)%sizeOfRegion
        // in our rotationSystem of firstVertex
//        adjacencyListOfFirstVertex.add((indexInTheRotationSystemForFirstVertex + 1) % sizeOfRegion, indexOfNewlyAddedVertex);
        adjacencyListOfFirstVertex.add((indexInTheRotationSystemForFirstVertex + 1), indexOfNewlyAddedVertex); //Temporary line

        //Here we have added an ear
        //Now we should verify to see if the rotationSystem reflects the newly added vertices
        // to our rotationSystem
        System.out.println("Verification: " + rotationSystem);


        System.out.println("After adding new rotationSystem for two new edges: "+rotationSystem);
        System.out.println("End of subDivideMethod, edges are: "+edges);
        //Now get the updated regions after our update to the rotationSystem
        regions = getRegions();

    }

    public void subDivideTwoGivenEdgesAndAddEdgeBetweenThem(String edge1, String edge2, List<Integer> currPickedRegion){




        // Get the keys at the random indices
//        String firstEdge = edgesOfThisRegion.get(index1);
//        int firstEdgeIndex=allEdgesInThisRegionWithIndexMap.get(firstEdge);
//
//        String secondEdge = edgesOfThisRegion.get(index2);
//        int secondEdgeIndex=allEdgesInThisRegionWithIndexMap.get(secondEdge);


//        System.out.println("FirstEdge and its index: "+firstEdge+" : "+firstEdgeIndex);
//        System.out.println("SecondEdge and its index: "+secondEdge+" : "+secondEdgeIndex);
//        System.out.println("Total Vertices now are(0 indexed): "+(xValues.size()-1));

//        //////////////////////////////////////
        //Get the edge to divide
        String edgeToDivide = edge1;
        //Vertices of the edge
        String[] edgeArr = edgeToDivide.split(" ");

        int uIndex = Integer.parseInt(edgeArr[1]);
        int vIndex = Integer.parseInt(edgeArr[2]);

        //Get vertex co-ordinates of u and v
        double uX = xValues.get(uIndex);
        double uY = yValues.get(uIndex);

        double vX = xValues.get(vIndex);
        double vY = yValues.get(vIndex);


        //Delete first edge from edge list
//        edges.remove(String.valueOf(firstEdge));

        //Delete second edge as well, cuz if we add any edge, then the indices of firstEdgeIndex
        //and secondEdgeIndex will not be the same
        //Delete second edge from edge list
//        edges.remove(String.valueOf(secondEdge));

        //Add new vertex w between u and v

        //Before adding vertex, calculate the vertex co-ordinates
        //of w as the midpoint of u and v
        //We just need to calculate the midpoint of u and v's
        // x and y co-ordinates
        double wX = (uX + vX)/2.0;
        double wY = (uY + vY)/2.0;

        //Now add x and y co-ordinates of w to xValues and yValues
        xValues.add(wX);
        yValues.add(wY);

        int indexOfNewlyAddedVertex = xValues.size()-1;

        System.out.println("Inside subDivideTwoGivenEdgesAndAddEdgeBetweenThem :edge added: "+"e "+uIndex+" "+indexOfNewlyAddedVertex);
        System.out.println("Inside subDivideTwoGivenEdgesAndAddEdgeBetweenThem :edge added: "+"e "+indexOfNewlyAddedVertex+" "+vIndex);


        /*****************************/

        //Get the edge to divide
        String edgeToDivide2 = edge2;
        //Vertices of the edge
        String[] edgeArr2 = edgeToDivide2.split(" ");


        int uIndex2 = Integer.parseInt(edgeArr2[1]);
        int vIndex2 = Integer.parseInt(edgeArr2[2]);

        //Get vertex co-ordinates of u and v
        double uX2 = xValues.get(uIndex2);
        double uY2 = yValues.get(uIndex2);

        double vX2 = xValues.get(vIndex2);
        double vY2 = yValues.get(vIndex2);

        //Add new vertex w between u and v

        //Before adding vertex, calculate the vertex co-ordinates
        //of w as the midpoint of u and v
        //We just need to calculate the midpoint of u and v's
        // x and y co-ordinates
        double wX2 = (uX2 + vX2)/2.0;
        double wY2 = (uY2 + vY2)/2.0;

        //Now add x and y co-ordinates of w to xValues and yValues
        xValues.add(wX2);
        yValues.add(wY2);

        int indexOfNewlyAddedVertex2 = xValues.size()-1;


        System.out.println("Inside subDivideTwoGivenEdgesAndAddEdgeBetweenThem :edge added: "+"e "+uIndex2+" "+indexOfNewlyAddedVertex2);
        System.out.println("Inside subDivideTwoGivenEdgesAndAddEdgeBetweenThem :edge added: "+"e "+indexOfNewlyAddedVertex2+" "+vIndex2);
        System.out.println("Inside subDivideTwoGivenEdgesAndAddEdgeBetweenThem :edge added: "+"e "+indexOfNewlyAddedVertex+" "+indexOfNewlyAddedVertex2);


        /////////////////////////////////////////////
        System.out.println("Total Vertices now are(0 indexed): "+(xValues.size()-1));

        //Add vertices to the rotationSystem
        //Get rotationSystem of u, v, w and then update the rotation system
        //Now repeat the above for u2, v2
        List<Integer> rotationOfU = rotationSystem.get(uIndex);
        List<Integer> rotationOfV = rotationSystem.get(vIndex);


        rotationOfU.set(rotationOfU.indexOf(vIndex), indexOfNewlyAddedVertex);


        rotationOfV.set(rotationOfV.indexOf(uIndex), indexOfNewlyAddedVertex);


        List<Integer> rotationOfU2 = rotationSystem.get(uIndex2);
        List<Integer> rotationOfV2 = rotationSystem.get(vIndex2);


        rotationOfU2.set(rotationOfU2.indexOf(vIndex2), indexOfNewlyAddedVertex2);

        rotationOfV2.set(rotationOfV2.indexOf(uIndex2), indexOfNewlyAddedVertex2);

//        System.out.println("Updated rotationSystem after adding w2 in v2 rotationSystem: "+rotationOfV2);

        //Now add the edge between indexOfNewlyAddedVertex (wX, wY) && indexOfNewlyAddedVertex2 (wX2, wY2)
//        edges.add("e " + indexOfNewlyAddedVertex + " " + indexOfNewlyAddedVertex2);

        //Above we have added an edge
        //Now we should modify the rotationSystem to reflect the newly added edge in adjacency list
        //Because adding an edge will add a vertex to both of the vertex's rotationSystem


//        System.out.println("After adding edges rotation system is: "+rotationSystem);
//        System.out.println("Edges now are: "+edges);
//        System.out.println("New call to calculate rotationSystem: "+graphOperationsObj.getRotationSystem());
//        graphOperationsObj = new GraphOperations(xValues, yValues, edges);
//        System.out.println("New New call to calculate rotationSystem: "+graphOperationsObj.getRotationSystem());


        //Since (wX, wY) and (wX2, wY2) are two new vertices added
        //There will be no adjacency list for these two vertices
        //So in turn there will be no rotationSystem for these two vertices
        //We need to create two new arraylists in rotationSystem and add the
        //rotationSystem of these two newly created vertices


//        List<Integer> tempAdjListOfindexOfNewlyAddedVertex = Arrays.asList(uIndex, indexOfNewlyAddedVertex2, vIndex);
//        List<Integer> tempAdjListOfindexOfNewlyAddedVertex2 = Arrays.asList(uIndex2, indexOfNewlyAddedVertex, vIndex2);
//
//        List<Integer> indexOfNewlyAddedVertexRotationSystem = getRotationSystemOfGivenAdjacencyList(tempAdjListOfindexOfNewlyAddedVertex, indexOfNewlyAddedVertex);
//        List<Integer> indexOfNewlyAddedVertex2RotationSystem = getRotationSystemOfGivenAdjacencyList(tempAdjListOfindexOfNewlyAddedVertex2, indexOfNewlyAddedVertex2);

        List<Integer> indexOfNewlyAddedVertexRotationSystem = Arrays.asList(uIndex, indexOfNewlyAddedVertex2, vIndex);

        List<Integer> indexOfNewlyAddedVertex2RotationSystem = Arrays.asList(uIndex2, indexOfNewlyAddedVertex, vIndex2);
        System.out.println("newrotationsystem:"+indexOfNewlyAddedVertexRotationSystem);
        System.out.println("newrotationsystem2"+indexOfNewlyAddedVertex2RotationSystem);

        //Add these new rotationSystems to our rotationSystem
        rotationSystem.add(new ArrayList<>(indexOfNewlyAddedVertexRotationSystem));
        rotationSystem.add(new ArrayList<>(indexOfNewlyAddedVertex2RotationSystem));

        System.out.println("After adding new rotationSystem for two new edges: "+rotationSystem);
        System.out.println("End of subDivideMethod, edges are: "+edges);
        regions = getRegions();

    }

    public void addEdgeBetweenTwoGivenVertices(int firstVertex, int secondVertex, List<Integer> currRegion) {
        //get the size of the current region
        int sizeOfRegion = currRegion.size();

        //Get adjacency list of first vertex
        List<Integer> adjacencyListOfFirstVertex = rotationSystem.get(firstVertex);
        //Get adjacency list of second vertex
        List<Integer> adjacencyListOfSecondVertex = rotationSystem.get(secondVertex);
        //check if the two vertices in consideration already have an edge between them.
        //We check this by checking if the second vertex is present in the firstVertex adjacency list
        //We do the same for second vertex as well
        if (!adjacencyListOfFirstVertex.contains(secondVertex) && !adjacencyListOfSecondVertex.contains(firstVertex)) {
            //Add the edge between firstVertex and secondVertex
            edges.add("e " + firstVertex + " " + secondVertex);

            System.out.println("Inside addEdgeBetweenTwoGivenVertices :New Ear Added: e " + firstVertex + " " + secondVertex);

            //Here we have added an ear
            //Now we should modify the rotationSystem to reflect the newly added edge in adjacency list
            //Because adding an edge will add a vertex to both of the vertex's rotationSystem

            //Modify the rotationSystem and carefully insert the added edge into the rotationSystem

            //Get the indices of vertex before the firstVertex and secondVertex in our currRegion
            int indexVertexBeforeTheFirstVertex = (currRegion.indexOf(firstVertex) - 1 + sizeOfRegion) % sizeOfRegion;
            int indexVertexBeforeTheSecondVertex = (currRegion.indexOf(secondVertex) - 1 + sizeOfRegion) % sizeOfRegion;

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
            adjacencyListOfFirstVertex.add((indexInTheRotationSystemForFirstVertex + 1) , secondVertex);

            //Insert firstVertex at (indexInTheRotationSystemForSecondVertex+1)%sizeOfRegion
            //in our rotationSystem of secondVertex
            adjacencyListOfSecondVertex.add((indexInTheRotationSystemForSecondVertex + 1) , firstVertex);

            //Here we have added an ear
            //Now we should verify to see if the rotationSystem reflects the newly added vertices
            // to our rotationSystem
            System.out.println("Verification: " + rotationSystem);

            //Now get the updated regions after our update to the rotationSystem
            regions = getRegions();

        }
    }

    public boolean checkIfGivenVertexIsEndPointOfGivenEdge(String edge, int vertex){
        String[] arr = edge.split(" ");
        int vertex1 = Integer.parseInt(arr[1]);
        int vertex2 = Integer.parseInt(arr[2]);
        if(vertex1==vertex || vertex2==vertex){
            return true;
        }
        return false;
    }

    public boolean checkIfEdgeExists(int v1, int v2, List<String> edgesInThisRegion){
        for(int i=0;i<edgesInThisRegion.size();i++){
            String currEdge = edgesInThisRegion.get(i);
            String[] arr = currEdge.split(" ");
            int vertex1 = Integer.parseInt(arr[1]);
            int vertex2 = Integer.parseInt(arr[2]);
            if((vertex1==v1 && vertex2==v2) || (vertex1==v2 && vertex2==v1)){
                return true;
            }
        }

        return false;
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
                    adjacencyListOfFirstVertex.add((indexInTheRotationSystemForFirstVertex+1), secondVertex);

                    //Insert firstVertex at (indexInTheRotationSystemForSecondVertex+1)%sizeOfRegion
                    //in our rotationSystem of secondVertex
                    adjacencyListOfSecondVertex.add((indexInTheRotationSystemForSecondVertex+1), firstVertex);

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


    public void subDivideGivenEdge(){
        System.out.println("Edges begining of subDivideMethod: "+edges);
        Random rand = new Random();

        System.out.println("Regions in subdivide: "+regions);
        int randomRegionNumber = rand.nextInt(regions.size());
        List<Integer> currPickedRegion = regions.get(randomRegionNumber);
        System.out.println("Curr picked region in subdivide: "+currPickedRegion);


        System.out.println("Rotation system in SubDivide: "+rotationSystem);
        List returnOfEdgesInGivenRegion = edgesInGivenRegion(currPickedRegion);
        Map<String, Integer> allEdgesInThisRegionWithIndexMap = (Map<String, Integer>)returnOfEdgesInGivenRegion.get(0);

//        System.out.println("edgeIndexMap in this region: "+allEdgesInThisRegionWithIndexMap);

        System.out.println("Rotation System before modifications: "+rotationSystem);
        // Get edges in this region

        List<String> edgesOfThisRegion = new ArrayList<>((List<String>)returnOfEdgesInGivenRegion.get(1));
        System.out.println("Only edges in this region: "+edgesOfThisRegion);
        //Now randomly pick two edges from this list
        // Pick two random indices
        int index1 = rand.nextInt(currPickedRegion.size());
        int index2 = index1;
        while (index2 == index1) {
            index2 = rand.nextInt(currPickedRegion.size());
        }

        //Print the index 1 and index 2 you picked and edgesOgfThisRegion
        System.out.println("index1 is: "+index1+" : index2 is: "+index2+" : edgesOfThisRegion"+edgesOfThisRegion);


        // Get the keys at the random indices
//        String firstEdge = edgesOfThisRegion.get(index1);
//        int firstEdgeIndex=allEdgesInThisRegionWithIndexMap.get(firstEdge);
//
//        String secondEdge = edgesOfThisRegion.get(index2);
//        int secondEdgeIndex=allEdgesInThisRegionWithIndexMap.get(secondEdge);


//        System.out.println("FirstEdge and its index: "+firstEdge+" : "+firstEdgeIndex);
//        System.out.println("SecondEdge and its index: "+secondEdge+" : "+secondEdgeIndex);
//        System.out.println("Total Vertices now are(0 indexed): "+(xValues.size()-1));
//        //////////////////////////////////////
//        //Get the edge to divide
//        String edgeToDivide = firstEdge;
//        //Vertices of the edge
//        String[] edgeArr = edgeToDivide.split(" ");

        int uIndex = currPickedRegion.get(index1);
        int vIndex = currPickedRegion.get((index1+1)%currPickedRegion.size());

        //Get vertex co-ordinates of u and v
        double uX = xValues.get(uIndex);
        double uY = yValues.get(uIndex);

        double vX = xValues.get(vIndex);
        double vY = yValues.get(vIndex);


        //Delete first edge from edge list
//        edges.remove(String.valueOf(firstEdge));

        //Delete second edge as well, cuz if we add any edge, then the indices of firstEdgeIndex
        //and secondEdgeIndex will not be the same
        //Delete second edge from edge list
//        edges.remove(String.valueOf(secondEdge));

        //Add new vertex w between u and v

        //Before adding vertex, calculate the vertex co-ordinates
        //of w as the midpoint of u and v
        //We just need to calculate the midpoint of u and v's
        // x and y co-ordinates
        double wX = (uX + vX)/2.0;
        double wY = (uY + vY)/2.0;

        //Now add x and y co-ordinates of w to xValues and yValues
        xValues.add(wX);
        yValues.add(wY);

        int indexOfNewlyAddedVertex = xValues.size()-1;

        //Add new edges (u,w), (w, v);
//        edges.add("e "+uIndex+" "+indexOfNewlyAddedVertex);
//        edges.add("e "+indexOfNewlyAddedVertex+" "+vIndex);


        /*****************************/

        //Get the edge to divide for the secondEdge
//        String edgeToDivide2 = secondEdge;
//        //Vertices of the edge
//        String[] edgeArr2 = edgeToDivide2.split(" ");

        int uIndex2 = currPickedRegion.get(index2);
        int vIndex2 = currPickedRegion.get((index2+1)%currPickedRegion.size());

        //Get vertex co-ordinates of u and v
        double uX2 = xValues.get(uIndex2);
        double uY2 = yValues.get(uIndex2);

        double vX2 = xValues.get(vIndex2);
        double vY2 = yValues.get(vIndex2);




        //Add new vertex w between u and v

        //Before adding vertex, calculate the vertex co-ordinates
        //of w as the midpoint of u and v
        //We just need to calculate the midpoint of u and v's
        // x and y co-ordinates
        double wX2 = (uX2 + vX2)/2.0;
        double wY2 = (uY2 + vY2)/2.0;

        //Now add x and y co-ordinates of w to xValues and yValues
        xValues.add(wX2);
        yValues.add(wY2);

        int indexOfNewlyAddedVertex2 = xValues.size()-1;

        //Add new edges (u,w), (w, v);
//        edges.add("e "+uIndex2+" "+indexOfNewlyAddedVertex2);
//        edges.add("e "+indexOfNewlyAddedVertex2+" "+vIndex2);

        /////////////////////////////////////////////
        System.out.println("Total Vertices now are(0 indexed): "+(xValues.size()-1));

        //Add vertices to the rotationSystem
        //Get rotationSystem of u, v, w and then update the rotation system
        //Now repeat the above for u2, v2
        List<Integer> rotationOfU = rotationSystem.get(uIndex);
        List<Integer> rotationOfV = rotationSystem.get(vIndex);

//        for(int i=0;i<rotationOfU.size();i++){
//            int currV = rotationOfU.get(i);
//            if(currV==vIndex){
//                rotationOfU.set(i, xValues.size()-2);
//            }
//        }

        rotationOfU.set(rotationOfU.indexOf(vIndex), indexOfNewlyAddedVertex);


//        System.out.println("Updated rotationSystem after adding w in u rotationSystem: "+rotationOfU);

//        for(int i=0;i<rotationOfV.size();i++){
//            int currU = rotationOfV.get(i);
//            if(currU==uIndex){
//                rotationOfV.set(i, xValues.size()-2);
//            }
//        }

        rotationOfV.set(rotationOfV.indexOf(uIndex), indexOfNewlyAddedVertex);

//        System.out.println("Updated rotationSystem after adding w in v rotationSystem: "+rotationOfV);



        List<Integer> rotationOfU2 = rotationSystem.get(uIndex2);
        List<Integer> rotationOfV2 = rotationSystem.get(vIndex2);


//        for(int i=0;i<rotationOfU2.size();i++){
//            int currV2 = rotationOfU2.get(i);
//            if(currV2==vIndex2){
//                rotationOfU2.set(i, xValues.size()-1);
//            }
//        }
        rotationOfU2.set(rotationOfU2.indexOf(vIndex2), indexOfNewlyAddedVertex2);

//        System.out.println("Updated rotationSystem after adding w2 in u2 rotationSystem: "+rotationOfU2);

//        for(int i=0;i<rotationOfV2.size();i++){
//            int currU2 = rotationOfV2.get(i);
//            if(currU2==uIndex2){
//                rotationOfV2.set(i, xValues.size()-1);
//            }
//        }

        rotationOfV2.set(rotationOfV2.indexOf(uIndex2), indexOfNewlyAddedVertex2);

//        System.out.println("Updated rotationSystem after adding w2 in v2 rotationSystem: "+rotationOfV2);

        //Now add the edge between indexOfNewlyAddedVertex (wX, wY) && indexOfNewlyAddedVertex2 (wX2, wY2)
//        edges.add("e " + indexOfNewlyAddedVertex + " " + indexOfNewlyAddedVertex2);

        //Above we have added an edge
        //Now we should modify the rotationSystem to reflect the newly added edge in adjacency list
        //Because adding an edge will add a vertex to both of the vertex's rotationSystem


//        System.out.println("After adding edges rotation system is: "+rotationSystem);
//        System.out.println("Edges now are: "+edges);
//        System.out.println("New call to calculate rotationSystem: "+graphOperationsObj.getRotationSystem());
//        graphOperationsObj = new GraphOperations(xValues, yValues, edges);
//        System.out.println("New New call to calculate rotationSystem: "+graphOperationsObj.getRotationSystem());


        //Since (wX, wY) and (wX2, wY2) are two new vertices added
        //There will be no adjacency list for these two vertices
        //So in turn there will be no rotationSystem for these two vertices
        //We need to create two new arraylists in rotationSystem and add the
        //rotationSystem of these two newly created vertices


//        List<Integer> tempAdjListOfindexOfNewlyAddedVertex = Arrays.asList(uIndex, indexOfNewlyAddedVertex2, vIndex);
//        List<Integer> tempAdjListOfindexOfNewlyAddedVertex2 = Arrays.asList(uIndex2, indexOfNewlyAddedVertex, vIndex2);
//
//        List<Integer> indexOfNewlyAddedVertexRotationSystem = getRotationSystemOfGivenAdjacencyList(tempAdjListOfindexOfNewlyAddedVertex, indexOfNewlyAddedVertex);
//        List<Integer> indexOfNewlyAddedVertex2RotationSystem = getRotationSystemOfGivenAdjacencyList(tempAdjListOfindexOfNewlyAddedVertex2, indexOfNewlyAddedVertex2);

        List<Integer> indexOfNewlyAddedVertexRotationSystem = Arrays.asList(uIndex, indexOfNewlyAddedVertex2, vIndex);

        List<Integer> indexOfNewlyAddedVertex2RotationSystem = Arrays.asList(uIndex2, indexOfNewlyAddedVertex, vIndex2);
        System.out.println("newrotationsystem:"+indexOfNewlyAddedVertexRotationSystem);
        System.out.println("newrotationsystem2"+indexOfNewlyAddedVertex2RotationSystem);

        //Add these new rotationSystems to our rotationSystem
        rotationSystem.add(new ArrayList<>(indexOfNewlyAddedVertexRotationSystem));
        rotationSystem.add(new ArrayList<>(indexOfNewlyAddedVertex2RotationSystem));

        System.out.println("After adding new rotationSystem for two new edges: "+rotationSystem);
        System.out.println("End of subDivideMethod, edges are: "+edges);
        regions = getRegions();

    }

    public List<Integer> getRotationSystemOfGivenAdjacencyList(List<Integer> adjList, int index){
        HashMap<Integer, Double> vertexAngleMap=new HashMap<>();
        //get the co-ordinates of the vertex for which we are currently
        //finding the rotation system
        double originX = xValues.get(index);
        double originY = yValues.get(index);
        //for each vertex that is adjacent to our current vertex for which
        //we are finding the rotation system do the following
        for(int curr:adjList){
            double x=xValues.get(curr);
            double y=yValues.get(curr);
            //find new vector (or new co-ordinates for each adjacent vertex)
            //by subtracting our origin vertex co-ordinates
            double newX = x-originX;
            double newY = y-originY;
            //find the angle of curr adjacent vertex using atan2

            double temp = Math.toDegrees(Math.atan2(newY, newX));

            //The range of atan2 is 0 to 180 and 0 to -180
            //If angle is acute, then the vertex is in 1st quadrant
            //If the angle is >90 and <=180, vertex is in 2nd quadrant
            //If the angle is <0 and >-90 vertex is in 4th quadrant
            //If the angle is <-90 and >-180 vertex is in 3rd quadrant
            //if the angle is negative it means our adjacent vertex is in quadrant 3 and 4
            //after moving the vector to origin
            //In that case add 360 to the angle to make it a positive value
            // in the same quadrant
            //When 360 is added, the angle of the vertex from x-axis line will be calculated
            if(temp<0){
                temp+=360;
            }
            //System.out.print(temp+", ");
            //add the angle, vertex mapping to the TreeMap
            //TreeMap allows us to order the adjacent vertices in counter-clockwise order
            //with angle 0 being the start point of counter-clockwise ordering
            //Try saving vertex to angle mapping to avoid loss of information

            //angleVertexMap.put(temp, curr);
            vertexAngleMap.put(curr, temp);
        }

        //writing new code to order the vertices in the counter clockwise direction
        HashMap<Integer, Double> temp = new HashMap<>(vertexAngleMap);
        List<Integer> result=new ArrayList<>();
        while(!temp.isEmpty()){
            double min = Double.MAX_VALUE;
            int minKey = Integer.MAX_VALUE;
            for(int currKey:temp.keySet()){
                if(temp.get(currKey)<min){
                    min = temp.get(currKey);
                    minKey = currKey;
                }
            }
            result.add(minKey);
            temp.remove(minKey);
        }
        return result;
    }

    public List edgesInGivenRegion(List<Integer> region){
        List<String> edgesInRegion = new ArrayList<>();
        Map<String, Integer> result = new HashMap<>();

        for(int i=0;i<region.size();i++){
            for(int j=i+1;j<region.size();j++){
                int v1=region.get(i);
                int v2=region.get(j);

                for(int k=0;k<edges.size();k++){
                    String[] currArr = edges.get(k).split(" ");
                    int currV1=Integer.parseInt(currArr[1]);
                    int currV2=Integer.parseInt(currArr[2]);
                    if((v1==currV1 && v2==currV2) || (v1==currV2 && v2==currV1)){
                        String edge=edges.get(k);
                        int edgeIndex=k;
                        edgesInRegion.add(edge);
                        result.put(edge, edgeIndex);
                        break;
                    }
                }
            }
        }
        List ret = new ArrayList<>();
        ret.add(result);
        ret.add(edgesInRegion);
        return ret;
    }

    //Below are the duplicate methods for calculating regions from other class
    //To get the regions of the given graph.
    //Each region is indicated with the vertices that are present in
    //that bounded region
    public List<List<Integer>> getRegions(){
        regions = new ArrayList<>();
        int[] visited = new int[xValues.size()];
        List<List<Node>> rotationSystemOfObjects = getObjectsOfAdjacencyList();
//        System.out.println("Are we in getRegions");
        dfs(regions, rotationSystemOfObjects, visited, 0, Integer.MIN_VALUE);
        System.out.println("Inside getRegions: Regions are: "+regions);
        return regions;
    }

    //DFS to find the vertices in each region
    public void dfs(List<List<Integer>> regions, List<List<Node>> rotationSystem, int[] visited, int curr, int parent){

        List<Node> currList = rotationSystem.get(curr);
//        System.out.println("Curr rotationSystemList in dfs: "+currList);
//        System.out.println("Are we in dfs call");
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
//                        System.out.println("Here did we find oneRegionVertices: "+oneRegionVertices);
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


    //To convert adjacency list (or rotation system) to objects of Node class.
    public List<List<Node>> getObjectsOfAdjacencyList(){
        //List<List<Integer>> rotationSystem = getRotationSystem();
        List<List<Node>> objects = new ArrayList<>();
//        System.out.println("Inside getObjectsOfAdjacencyList: rotationSystem values: "+rotationSystem);
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
