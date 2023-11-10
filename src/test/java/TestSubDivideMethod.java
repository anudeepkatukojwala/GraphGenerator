package src.test.java;

import src.main.java.com.graph_generator.graph.GraphOperations;
import src.main.java.com.graph_generator.graph.SubDivide;
import src.main.java.com.graph_generator.graph.TutteEmbedding;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;
import javax.swing.*;

public class TestSubDivideMethod extends JFrame {

    List<Double> xValues = new ArrayList<>();
    List<Double> yValues =  new ArrayList<>();
    List<String> edg = new ArrayList<>();
    private JButton uploadButton;
    private JFileChooser fileChooser;
    private static int WIDTH = 600;
    private static int HEIGHT = 600;



    public TestSubDivideMethod() {
        setTitle("Coordinate Plane");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        uploadButton = new JButton("Upload");
        uploadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (fileChooser == null) {
                    fileChooser = new JFileChooser();
                }
                int result = fileChooser.showOpenDialog(TestSubDivideMethod.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try {
                        BufferedReader reader = new BufferedReader(new FileReader(file));
                        String line;
                        xValues.clear();
                        yValues.clear();
                        edg.clear();
                        while ((line = reader.readLine()) != null) {
                            String[] currArr = line.split(" ");
                            if(currArr[0].equals("v")){
                                xValues.add(Double.parseDouble(currArr[1]));
                                yValues.add(Double.parseDouble(currArr[2]));
                            }
                            else{
                                edg.add(line);
                            }
                        }

                        reader.close();

                        //Create object for src.main.java.com.graphgenerator.graph.GraphOperations
                        GraphOperations graphOperationsObj = new GraphOperations(xValues, yValues, edg);

                        /****************************************************/

                        //Divide the edge as needed
                        SubDivide subDivideObj = new SubDivide(xValues, yValues, edg, graphOperationsObj.getRegions(), graphOperationsObj.getRotationSystem());

                        //Parameter for testSubDivideMethod
                        boolean shouldWeChooseExternalRegion = false;

                        List returnOfSubDivide = subDivideObj.testSubDivideMethod(9, shouldWeChooseExternalRegion);

                        xValues = (List<Double>) returnOfSubDivide.get(0);
                        yValues = (List<Double>) returnOfSubDivide.get(1);
                        edg = (List<String>) returnOfSubDivide.get(2);

                        List<List<Integer>> rotationSystemAfterSubDivide = (List<List<Integer>>) returnOfSubDivide.get(3);

                        List<List<Integer>> regionsAfterSubdivide = (List<List<Integer>>) returnOfSubDivide.get(4);



                        /****************************************************/




                        /****************************************************/
                        //Now prepare the graph to input to graphviz
                        List<Integer> regionToPin=null;
                        if(shouldWeChooseExternalRegion){
                            int largest=-1;

                            for(List<Integer> currRegion:regionsAfterSubdivide){
                                if(currRegion.size()>largest){
                                    largest=currRegion.size();
                                    regionToPin=currRegion;
                                }
                            }
                        }
                        else{

                            for(List<Integer> currRegion:regionsAfterSubdivide){
                                if(currRegion.contains(0) && currRegion.contains(1)){
                                    regionToPin=currRegion;
                                }
                            }
                        }
                        System.out.println("Region to Pin is: "+regionToPin);

                        /****************************************************/
                        int originalVerticesSize = xValues.size();
//                        System.out.println("Vertices size before Adding Embedding: "+xValues.size());
                        for(List<Integer> currReg:regionsAfterSubdivide){
                            if(currReg==regionToPin){
                                continue;
                            }
                            double totalXValue = 0;
                            double totalYValue = 0;
                            for(int cV:currReg){
                                double currXV=xValues.get(cV);
                                double currYV = yValues.get(cV);
                                totalXValue+=currXV;
                                totalYValue+=currYV;
                            }
                            double newXV = totalXValue/currReg.size();
                            double newYV = totalYValue/currReg.size();

                            xValues.add(newXV);
                            yValues.add(newYV);
                            int newIndex = xValues.size()-1;

                            ArrayList<Integer> reversedRegionList = new ArrayList<>(currReg);
                            Collections.reverse(reversedRegionList);
                            rotationSystemAfterSubDivide.add(new ArrayList<>(reversedRegionList));
                            for(int i=0;i<currReg.size();i++){
                                int currVertexInThisRegion=currReg.get(i);
                                List<Integer> currRotationSystem = rotationSystemAfterSubDivide.get(currVertexInThisRegion);
                                int  nextVertexAfterCurrVertexInRegion = currReg.get((i+1)%currReg.size());
                                int findThisNextVertexInRotationSystem = currRotationSystem.indexOf(nextVertexAfterCurrVertexInRegion);
                                currRotationSystem.add((findThisNextVertexInRotationSystem)%currRotationSystem.size(), newIndex);
                            }
                        }

                        edg.clear();
                        for(int i=0;i<rotationSystemAfterSubDivide.size();i++){
                            List<Integer> currRotation = rotationSystemAfterSubDivide.get(i);
                            for(int j:currRotation){
                                if(i<j){
                                    edg.add("e "+i+" "+j);
                                }
                            }
                        }

                        System.out.println("New edges in the main method: "+edg);

                        int sizeOfVerticesAfterMakingIt3Connected = xValues.size();

                        /****************************************************/
                        //Change the co-ordinates for Tutte Embedding here

                        TutteEmbedding tutteObj = new TutteEmbedding(xValues, yValues, edg, regionsAfterSubdivide, rotationSystemAfterSubDivide, regionToPin);
                        //Get the new co-ordinates of our vertices in the new graph we got after Tutte Embedding
                        List<List<Double>> newCoordinates = tutteObj.calculateNewVertexPositions();
                        //Update of x and y coordinates of our vertices
                        xValues = newCoordinates.get(0);
                        yValues = newCoordinates.get(1);

                        System.out.println("Vertices size after Tutte Embedding: "+xValues.size());
                        System.out.println("Vertices size before Adding Extra vertices: "+originalVerticesSize);
                        //Delete the extra added vertices
//                        int extraVerticesStartIndex = sizeOfVerticesAfterMakingIt3Connected-originalVerticesSize;
                        xValues = xValues.subList(0, originalVerticesSize);
                        yValues = yValues.subList(0, originalVerticesSize);

                        System.out.println("Edges here after removing the extra vertices: "+edg);

                        //Delete the edges
                        List<Integer> edgeIndicesToRemove = new ArrayList<>();

                        //Get all edges that have at least one endpoint as an extra vertex
                        for(int i=0;i<edg.size();i++){
                            String currEdge = edg.get(i);
                            String[] currEdgeArr = currEdge.split(" ");
                            int v1=Integer.parseInt(currEdgeArr[1]);
                            int v2=Integer.parseInt(currEdgeArr[2]);
                            if(v1>=originalVerticesSize || v2>=originalVerticesSize){
                                edgeIndicesToRemove.add(i);
                            }
                        }

                        // Delete extra vertices and edges
                        //The reason we reversed edgeIndicesToRemove is because after removing an edge, the indices of remaining edges change
                        //So we reverse to remove from last to first
                        //Otherwise indices will be incorrect

                        Collections.reverse(edgeIndicesToRemove);
                        for(int index : edgeIndicesToRemove){

                            edg.remove(index);
                        }

                        System.out.println("Final final List of Edges after removing the extra: "+edg);

                        /****************************************************/





//                        int nSided = regionToPin.size();
//                        for(int i=0;i<xValues.size();i++){
//                            xValues.set(i, 0.0);
//                            yValues.set(i, 0.0);
//                        }
//                        for(int i=0;i<nSided;i++){
//                            int curr = regionToPin.get(i);
//                            xValues.set(curr, Math.cos(((2*Math.PI*i)/nSided)));
//                            yValues.set(curr, Math.sin(((2*Math.PI*i)/nSided)));
////            xValues.add(Math.cos(((2*Math.PI*i)/nSided)));
////
////            yValues.add(Math.sin(((2*Math.PI*i)/nSided)));
////            //System.out.print(Math.cos(((2*Math.PI*i)/nSided))+" "+Math.sin(((2*Math.PI*i)/nSided))+"\n");
//
//                        }
//


                        /****************************************************/

//                        try {
//                            // Create a file to store the graph in DOT format
//                            BufferedWriter writer = new BufferedWriter(new FileWriter("graph.dot"));
//
//                            // Start the graph definition
//                            writer.write("graph G {\n");
////                            writer.write("graph [K=0.01];\n");
//                            // Write vertices
//                            for (int i = 0; i < xValues.size(); i++) {
//                                String pinAttribute = regionToPin.contains(i) ? ", pin=true" : "";
//                                writer.write("  " + i + " [pos=\"" + xValues.get(i) + "," + yValues.get(i) + "\"" + pinAttribute + "];\n");
//                            }
//
//                            // Write edges
//                            for (int i = 0; i < edg.size(); i++) {
//                                String currEdge = edg.get(i);
//                                String[] edgeArr = currEdge.split(" ");
////                                writer.write("  " + edgeArr[1] + " -- " + edgeArr[2] + "[weight=0.1]"+";\n");
//                                writer.write("  " + edgeArr[1] + " -- " + edgeArr[2] + ";\n");
//                            }
//
//                            // End the graph definition
//                            writer.write("}");
//                            // Close the file
//                            writer.close();
//
//                            // Run Graphviz fdp command
//                            String cmd = "neato -Tplain graph.dot -o graph.plain";
//                            Process process = Runtime.getRuntime().exec(cmd);
//                            int exitCode = process.waitFor();
//
//                            System.out.println("/******************************/");
//                            // Open the .plain file
//                            File fil = new File("graph.plain");
//                            Scanner scanner = new Scanner(fil);
//
//                            // Read and process the file line by line
//                            while (scanner.hasNextLine()) {
//                                String lin = scanner.nextLine();
//                                String[] tokens = lin.split("\\s+");
//
//                                if (tokens[0].equals("graph")) {
//                                    System.out.println("Processing graph, width: " + tokens[1] + ", height: " + tokens[2]);
//                                } else if (tokens[0].equals("node")) {
//                                    xValues.set(Integer.parseInt(tokens[1]), Double.parseDouble(tokens[2]));
//                                    yValues.set(Integer.parseInt(tokens[1]), Double.parseDouble(tokens[3]));
//                                    System.out.println("Processing node, name: " + tokens[1] + ", x: " + tokens[2] + ", y: " + tokens[3]);
//                                } else if (tokens[0].equals("edge")) {
//                                    System.out.println("Processing edge, from: " + tokens[1] + ", to: " + tokens[2]);
//                                } else {
//                                    System.out.println("Unknown line type: " + lin);
//                                }
//                            }
//
//                            System.out.println("/******************************/");
//
//                            // Close the scanner
//                            scanner.close();
//
//
//                            System.out.println("Exit code: " + exitCode);
//
//                        } catch (IOException | InterruptedException ex) {
//                            System.out.println("An error occurred: " + ex.getMessage());
//                        }
//
//
//                        /****************************************************/

                        /****************************************************/
                        //Now call graphviz from this java program and store it's output

                        /****************************************************/

//
//                        //Create object for PreGraphOperations
//                        src.main.java.com.graphgenerator.graph.PreGraphDrawingOperations preGraphObj = new src.main.java.com.graphgenerator.graph.PreGraphDrawingOperations(xValues, yValues, edg);
//
//                        //Check if any three points in the given graph are collinear
//                        boolean shouldWeContinue = preGraphObj.checkIfAnyThreePointsAreCollinear(graphOperationsObj.vertexAngleMapping, graphOperationsObj.getRotationSystem());
//
//
//                        //If any three points in the given graph are collinear do not continue drawing the graph
//                        if(!shouldWeContinue){
//                            System.out.println("Coordinates are collinear");
//                            return;
//                        }
//
//                        //Check if any two edges are overlapping or crossing each other
//                        boolean checkForOverlapOrCrossOver = preGraphObj.checkForOverlapOrCrossOverOfAnyTwoEdges();
//                        //If any two edges are overlapping or crossing over, we reject this graph
//                        // and do not continue drawing the graph
//                        if(checkForOverlapOrCrossOver){
//                            return;
//                        }
//
//                        //Size of edg (Number of edges)
//                        int originalSizeOfEdg = edg.size();
//                        System.out.println("\n\nOriginal Size of Edge: "+originalSizeOfEdg);
//
//                        /****************************************************/
//                        //Create a Planar Triangulation of our graph
//                        src.main.java.com.graphgenerator.graph.PlanarTriangulation pt = new src.main.java.com.graphgenerator.graph.PlanarTriangulation(xValues, yValues, edg, graphOperationsObj.getRegions(), graphOperationsObj.getRotationSystem());
//                        List returnOfPlanarTriangulation = pt.createPlanarTriangulationOfGivenGraph();
//                        //Save the new rotationSystem, regions and edges we got after making
//                        //our graph Planar Triangular
//                        //Save the rotationSystem
//                        List<List<Integer>> currRotationSystem = (List<List<Integer>>)returnOfPlanarTriangulation.get(0);
//                        //Save the regions
//                        List<List<Integer>> currRegions = (List<List<Integer>>)returnOfPlanarTriangulation.get(1);
//                        //Update the edges list
//                        edg = (List<String>) returnOfPlanarTriangulation.get(2);
//
//
//                        /****************************************************/
//
//                        //After Planar Triangulation the edges should be
//                        // equivalent to E = 2V-6
//                        //where E is edges, V are vertices
//                        System.out.println("Edges size is: "+edg.size());
//                        System.out.println("Total Vertices are: "+xValues.size());
//
//                        /****************************************************/
//                        //Change the co-ordinates for Tutte Embedding here
//
//                        src.main.java.com.graphgenerator.graph.GraphOperations gObj = new src.main.java.com.graphgenerator.graph.GraphOperations(xValues, yValues, edg);
//                        List<List<Integer>> currRegions = gObj.getRegions();
//                        List<List<Integer>> currRotationSystem = gObj.getRotationSystem();
//
//                        src.main.java.com.graphgenerator.graph.TutteEmbedding tutteObj = new src.main.java.com.graphgenerator.graph.TutteEmbedding(xValues, yValues, edg, currRegions, currRotationSystem);
//                        //Get the new co-ordinates of our vertices in the new graph we got after Tutte Embedding
//                        List<List<Double>> newCoordinates = tutteObj.calculateNewVertexPositions();
//                        //Update of x and y coordinates of our vertices
//                        xValues = newCoordinates.get(0);
//                        yValues = newCoordinates.get(1);
//                        /****************************************************/





                        //Create a new dialog and send our vertices co-ordinates
                        //and edge list to draw the graph in this new dialog
                        new SubDivideNewDialog(TestSubDivideMethod.this, xValues, yValues, edg, 0);

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

            }});

        add(uploadButton, BorderLayout.SOUTH);


        pack();
        setVisible(true);
    }



    public static void main(String[] args) {
        new TestSubDivideMethod();
    }
}

class SubDivideNewDialog extends JDialog{
    private JPanel drawPanel;
    private int newWidth = 600;
    private int newHeight = 600;

    List<Double> xValues;
    List<Double> yValues;
    List<String> edg = new ArrayList<>();
    int originalSizeOfEdg;

    public SubDivideNewDialog(JFrame parent, List<Double> xValues, List<Double> yValues, List<String> edg, int originalSizeOfEdg) {
        super(parent, "Graph", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600,600);
        this.originalSizeOfEdg = originalSizeOfEdg;
        this.xValues = xValues;
        this.yValues = yValues;
        this.edg = edg;
        createGUI();
    }

    public void createGUI(){

        Font f1 = new Font(Font.SERIF, Font.BOLD,  10);


        drawPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                /***************************************************************/
                //Dynamic Scaling Code
                //Set width and height
                newWidth = drawPanel.getWidth();
                newHeight = drawPanel.getHeight();

                int minOfWH = Math.min(newHeight, newWidth);

                int W=minOfWH;
                int H=minOfWH;

                int margin = 40;

                int W1 = W-(2*margin);
                int H1 = H-(2*margin);

                List<Double> extremes = getExtremes();

                double xMin = extremes.get(0);
                double xMax = extremes.get(1);
                double yMin = extremes.get(2);
                double yMax = extremes.get(3);

                //counter for edge label
                int edgeCounter = 0;

//                System.out.println("\n\n\n\n\nOriginal Size of Edge: "+originalSizeOfEdg);

                //Draw edges
                for(int i=0;i<edg.size();i++){
                    //System.out.println("Edges arr size is: "+edg.size());
                    String[] edgArr = edg.get(i).split(" ");
                    int currV1 = Integer.parseInt(edgArr[1]);
                    int currV2 = Integer.parseInt(edgArr[2]);
                    //System.out.println("Edge drawn from: "+edgArr[1]+" to "+edgArr[2]);
                    int x1 = (int)Math.round(margin +  (double)((W1 * (xValues.get(currV1)-xMin))/(xMax-xMin)));


                    int y1 = (int)Math.round(margin + (double)((H1 * (yMax-yValues.get(currV1)))/(yMax-yMin)));


                    int x2 = (int)Math.round(margin +  (double)((W1 * (xValues.get(currV2)-xMin))/(xMax-xMin)));
                    int y2 = (int)Math.round(margin + (double)((H1 * (yMax-yValues.get(currV2)))/(yMax-yMin)));

                    if(i>=originalSizeOfEdg){
                        g.setColor(Color.RED);
                    }

                    g.drawLine(x1, y1, x2, y2);

                    g.setColor(Color.BLACK);

                    int xMid = (int)Math.round((double)(x1+x2)/2);
                    int yMid = (int)Math.round((double)(y1+y2)/2);

                    int deltaX = Math.abs(x2-x1);
                    int deltaY = Math.abs(y2-y1);

                    double n = Math.sqrt(Math.pow(deltaX, 2)+Math.pow(deltaY, 2));
                    //System.out.println("n value: "+ n);

                    //Below code is to make sure all edge labels are drawn perpendicular
                    //to their edges
                    double dummyX1 = xValues.get(currV1);
                    double dummyY1 = yValues.get(currV1);
                    double dummyX2 = xValues.get(currV2);
                    double dummyY2 = yValues.get(currV2);

                    //move one vertex to (0,0) coordinate and find the angle of the
                    //other vertex using atan2 function
                    double newX = (dummyX1+(-dummyX2));
                    double newY = (dummyY1+(-dummyY2));
                    int sign=0;
                    double angle = Math.toDegrees(Math.atan2(newY, newX));
                    //if angle is obtuse then flip the sign of vX and vY values
                    if((angle>90 && angle <180) || (angle<0 && angle>-90)){
                        sign=1;
                    }
                    double vX=0;
                    double vY=0;

                    if(sign==0){
                        vX = -(double)(deltaY/n);
                        vY = -(double)(deltaX/n);
                    }
                    else{
                        vX = (double)(deltaY/n);
                        vY = -(double)(deltaX/n);
                    }


                    int x3 = (int)Math.round(xMid + (15*vX));
                    int y3 = (int)Math.round(yMid + (15*vY));


                    //label the edge
                    g.drawString(Character.toString(97+edgeCounter), x3, y3);
                    //g.drawLine(xMid, yMid, x3, y3);
                    edgeCounter++;

                }

                //Label vertices
                List<List<Integer>> lt = getPixelValues(xValues, yValues, minOfWH);

                int labelCounter=0;

                for(List<Integer> curr:lt){
                    int x=curr.get(0);
                    int y=curr.get(1);
                    g.setColor(Color.WHITE);
                    g.fillOval(x-(10), y-(10), 20*(int)Math.sqrt(2), 20*(int)Math.sqrt(2));
                    g.setColor(Color.BLACK);
                    g.drawOval(x-(10), y-(10), 20*(int)Math.sqrt(2), 20*(int)Math.sqrt(2));

                    //g.setColor(Color.RED);
                    g.setFont(f1);
                    g.drawString(String.valueOf(labelCounter), x-2, y+3);
                    labelCounter++;
                    g.setFont(null);
                    //g.setColor(Color.BLACK);

                }
            }
        };
        //System.out.println("Adjacency List: "+getAdjacencyListOfGraph(xValues, yValues, edg));
//        src.main.java.com.graphgenerator.graph.GraphOperations obj = new src.main.java.com.graphgenerator.graph.GraphOperations(xValues, yValues, edg);
//        //obj.getAdjacencyListOfGraph();
//        obj.getObjectsOfAdjacencyList();
//        obj.getRegions();
        //obj.getRotationSystem();
        //System.out.println("Regions are: "+obj.getRegions());
        drawPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        drawPanel.setBackground(Color.WHITE);
        add(drawPanel);
        setVisible(true);
        pack();
    }


    //Find xMin, xMax, yMin, yMax
    public List<Double> getExtremes(){
        List<Double> lt = new ArrayList<>();

        //Find extremes in x values
        double xMin = Double.MAX_VALUE;
        double xMax = Double.MIN_VALUE;

        for(double curr:xValues){
            if(curr<xMin){
                xMin=curr;
            }
            if(curr>xMax){
                xMax=curr;
            }
        }
        lt.add(xMin);
        lt.add(xMax);

        //Find extremes in y values
        double yMin = Double.MAX_VALUE;
        double yMax = Double.MIN_VALUE;

        for(double curr:yValues){
            if(curr<yMin){
                yMin=curr;
            }
            if(curr>yMax){
                yMax=curr;
            }
        }
        lt.add(yMin);
        lt.add(yMax);

        return lt;

    }

    //Get pixel values for the given vertices as coordinates
    public List<List<Integer>> getPixelValues(List<Double> xValues, List<Double> yValues, int minOfWH){
        List<List<Integer>> lt = new ArrayList<>();

        int W=minOfWH;
        int H=minOfWH;

        int margin = 40;

        int W1 = W-(2*margin);
        int H1 = H-(2*margin);

        List<Double> extremes = getExtremes();
        //System.out.println("Extremes are: "+extremes);
        double xMin = extremes.get(0);
        double xMax = extremes.get(1);
        double yMin = extremes.get(2);
        double yMax = extremes.get(3);

        for(int i=0;i<xValues.size();i++){
            int x = (int)Math.round(margin +  (double)((W1 * (xValues.get(i)-xMin))/(xMax-xMin)));
            int y = (int)Math.round(margin + (double)((H1 * (yMax-yValues.get(i)))/(yMax-yMin)));
            lt.add(new ArrayList<>());
            lt.get(i).add(x);
            lt.get(i).add(y);
        }

        return lt;
    }




}