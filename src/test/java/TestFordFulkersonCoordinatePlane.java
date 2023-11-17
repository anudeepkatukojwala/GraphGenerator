package src.test.java;

import src.main.java.com.graph_generator.rejection_sampling.RejectionSamplingForFordFulkerson;

import javax.swing.*;
        import java.awt.*;
        import java.awt.event.ActionEvent;
        import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.QuadCurve2D;
import java.io.BufferedReader;
        import java.io.File;
        import java.io.FileReader;
        import java.io.IOException;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.*;

public class TestFordFulkersonCoordinatePlane extends JFrame {

    List<Double> xValues = new ArrayList<>();
    List<Double> yValues =  new ArrayList<>();
    List<String> edg = new ArrayList<>();
    private JButton uploadButton;
    private JFileChooser fileChooser;
    private static int WIDTH = 600;
    private static int HEIGHT = 600;



    public TestFordFulkersonCoordinatePlane() {
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
                int result = fileChooser.showOpenDialog(TestFordFulkersonCoordinatePlane.this);
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
//                        System.out.println("At the start: Edges read are: " + edg);

//                        //Create object for src.main.java.com.graphgenerator.graph.GraphOperations
//                        src.main.java.com.graphgenerator.graph.GraphOperations graphOperationsObj = new src.main.java.com.graphgenerator.graph.GraphOperations(xValues, yValues, edg);
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
//                        src.main.java.com.graphgenerator.graph.TutteEmbedding tutteObj = new src.main.java.com.graphgenerator.graph.TutteEmbedding(xValues, yValues, edg, currRegions, currRotationSystem, new ArrayList<>());
//                        //Get the new co-ordinates of our vertices in the new graph we got after Tutte Embedding
//                        List<List<Double>> newCoordinates = tutteObj.calculateNewVertexPositions();
//                        //Update of x and y coordinates of our vertices
//                        xValues = newCoordinates.get(0);
//                        yValues = newCoordinates.get(1);
//                        /****************************************************/
                        /****************************************************/
                        // Find the maximum flow
                        RejectionSamplingForFordFulkerson rejectionSamplingForFordFulkerson=new RejectionSamplingForFordFulkerson(xValues, yValues, edg, 0, 9);
                        List returnOfFordFulkerson = rejectionSamplingForFordFulkerson.rejecionSamplingProcedure();
                        int maxFlow = (int)returnOfFordFulkerson.get(0);
//                        System.out.println("Max FLow is: "+maxFlow);

                        int[][] graph = (int[][])returnOfFordFulkerson.get(1);
                        int[][] rGraph = (int[][])returnOfFordFulkerson.get(2);
                        int[][] rGraph2 = (int[][])returnOfFordFulkerson.get(5);
                        //Print rGraph
                        System.out.println("rGraph: "+Arrays.deepToString(rGraph));
                        //Print rGraph2
                        System.out.println("rGraph2: "+Arrays.deepToString(rGraph2));

                        //Print unique aumenting path
                        List<Integer> uniqueAugmentingPath = (List<Integer>)returnOfFordFulkerson.get(6);
                        System.out.println("Unique Aumenting Path is: "+uniqueAugmentingPath);

                        List<Integer> sSide = (List<Integer>) returnOfFordFulkerson.get(7);
                        List<Integer> tSide = (List<Integer>) returnOfFordFulkerson.get(8);

                        edg= (List<String>) returnOfFordFulkerson.get(3);
                        /****************************************************/

                        //Create a new dialog and send our vertices co-ordinates
                        //and edge list to draw the graph in this new dialog
                        JDialog dialog1=new TestFordFulkersonNewDialog(TestFordFulkersonCoordinatePlane.this, xValues, yValues, edg, graph, rGraph, sSide, tSide);
                        dialog1.setSize(600, 600);

                        //Create a new dialog for drawing residual graph of ford fulkerson
                        JDialog dialog2=new TestFordFulkersonResidualGraphDialog(TestFordFulkersonCoordinatePlane.this, xValues, yValues, edg, graph, rGraph, rGraph2, uniqueAugmentingPath);
                        dialog2.setSize(600, 600);
                        // Display the dialogs
                        dialog1.setVisible(true);
                        dialog2.setVisible(true);

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
        new TestFordFulkersonCoordinatePlane();
    }
}

class TestFordFulkersonNewDialog extends JDialog{
    private JPanel drawPanel;
    private int newWidth = 600;
    private int newHeight = 600;

    List<Double> xValues;
    List<Double> yValues;
    List<String> edg = new ArrayList<>();
    int originalSizeOfEdg;
    int[][] graph;
    int[][] rGraph;
    List<Integer> sSide;
    List<Integer> tSide;

    public TestFordFulkersonNewDialog(JFrame parent, List<Double> xValues, List<Double> yValues, List<String> edg, int[][] graph, int[][] rGraph, List<Integer> sSide, List<Integer> tSide) {
        super(parent, "Graph1", false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600,600);
        this.originalSizeOfEdg = originalSizeOfEdg;
        this.xValues = xValues;
        this.yValues = yValues;
        this.edg = edg;
        this.graph = graph;
        this.rGraph = rGraph;
        this.sSide = sSide;
        this.tSide = tSide;
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

                System.out.println("\n\n\n\n\nOriginal Size of Edge: "+originalSizeOfEdg);

                //Draw edges
                for(int i=0;i<edg.size();i++){
                    //System.out.println("Edges arr size is: "+edg.size());
                    String[] edgArr = edg.get(i).split(" ");
                    int currV1 = Integer.parseInt(edgArr[1]);
                    int currV2 = Integer.parseInt(edgArr[2]);
                    int currWeight = Integer.parseInt(edgArr[3]);
//                    System.out.println("Are we here");

//                    System.out.println("Edge drawn from: "+edgArr[1]+" to "+edgArr[2]);
                    int x1 = (int)Math.round(margin +  (double)((W1 * (xValues.get(currV1)-xMin))/(xMax-xMin)));


                    int y1 = (int)Math.round(margin + (double)((H1 * (yMax-yValues.get(currV1)))/(yMax-yMin)));


                    int x2 = (int)Math.round(margin +  (double)((W1 * (xValues.get(currV2)-xMin))/(xMax-xMin)));
                    int y2 = (int)Math.round(margin + (double)((H1 * (yMax-yValues.get(currV2)))/(yMax-yMin)));

//                    if(direction==1){
//                        g.setColor(Color.RED);
//                    }
                    g.setColor(Color.BLACK);
                    g.drawLine(x1, y1, x2, y2);
                    drawArrow(g, x1, y1, x2, y2, 0);




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


                    int x3 = (int)Math.round(xMid + (30*vX));
                    int y3 = (int)Math.round(yMid + (30*vY));


                    //label the edge
                    int residualCapacity = rGraph[currV1][currV2];
                    int capacity = graph[currV1][currV2];
                    int flow=capacity-residualCapacity;
//                    System.out.println("Flow/Capacity of this edge: "+flow+"/"+capacity);
                    g.drawString(flow+"/"+capacity, x3, y3);

                    //Now draw the direction for the edge




                    //g.drawLine(xMid, yMid, x3, y3);
                    edgeCounter++;

                    //Draw line from currVertex to its parent

                }





                //Label vertices
                List<List<Integer>> lt = getPixelValues(xValues, yValues, minOfWH);

                int labelCounter=0;

                for(List<Integer> curr:lt){
                    int x=curr.get(0);
                    int y=curr.get(1);
                    g.setColor(Color.WHITE);
                    if(tSide.contains(labelCounter)){
                        g.setColor(Color.CYAN);
                    }
                    g.fillOval(x-(10), y-(10), 20*(int)Math.sqrt(2), 20*(int)Math.sqrt(2));
                    g.setColor(Color.WHITE);
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

    private void drawArrow(Graphics g, int x1, int y1, int x2, int y2, int direction) {
        int arrowLength = 15;
        int arrowWidth = 7;

        // Calculate midpoint
        int mx = (x1 + x2) / 2;
        int my = (y1 + y2) / 2;

        // Calculate direction vector based on the "direction" variable
        double dx, dy;
        if (direction == 0) {
            dx = x2 - x1;
            dy = y2 - y1;
        } else {
            dx = x1 - x2;
            dy = y1 - y2;
        }

        // Normalize direction vector
        double length = Math.sqrt(dx * dx + dy * dy);
        dx /= length;
        dy /= length;

        // Calculate triangle (arrowhead) points using the direction vector and midpoint
        Point arrowTip = new Point((int) (mx + arrowLength * dx), (int) (my + arrowLength * dy));
        Point arrowLeft = new Point((int) (mx - arrowWidth * dy), (int) (my + arrowWidth * dx));
        Point arrowRight = new Point((int) (mx + arrowWidth * dy), (int) (my - arrowWidth * dx));

        // Draw triangle
        Polygon arrowPolygon = new Polygon();
        arrowPolygon.addPoint(arrowTip.x, arrowTip.y);
        arrowPolygon.addPoint(arrowLeft.x, arrowLeft.y);
        arrowPolygon.addPoint(arrowRight.x, arrowRight.y);
        g.fillPolygon(arrowPolygon);
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


class TestFordFulkersonResidualGraphDialog extends JDialog{
    private JPanel drawPanel;
    private int newWidth = 600;
    private int newHeight = 600;

    List<Double> xValues;
    List<Double> yValues;
    List<String> edg = new ArrayList<>();
    int originalSizeOfEdg;
    int[][] graph;
    int[][] rGraph;
    int[][] rGraph2;
    List<Integer> uniqueAugmentingPath;

    public TestFordFulkersonResidualGraphDialog(JFrame parent, List<Double> xValues, List<Double> yValues, List<String> edg, int[][] graph, int[][] rGraph, int[][] rGraph2, List<Integer> uniqueAugmentingPath) {
        super(parent, "Residual Graph", false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600,600);
        this.originalSizeOfEdg = originalSizeOfEdg;
        this.xValues = xValues;
        this.yValues = yValues;
        this.edg = edg;
        this.graph = graph;
        this.rGraph = rGraph;
        this.rGraph2 = rGraph2;
        this.uniqueAugmentingPath = uniqueAugmentingPath;
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

                System.out.println("\n\n\n\n\nOriginal Size of Edge: "+originalSizeOfEdg);

                //Draw edges
                for(int i=0;i<edg.size();i++){
                    //System.out.println("Edges arr size is: "+edg.size());
                    String[] edgArr = edg.get(i).split(" ");
                    int currV1 = Integer.parseInt(edgArr[1]);
                    int currV2 = Integer.parseInt(edgArr[2]);
                    int currWeight = Integer.parseInt(edgArr[3]);
//                    System.out.println("Are we here");

//                    System.out.println("Edge drawn from: "+edgArr[1]+" to "+edgArr[2]);
                    int x1 = (int)Math.round(margin +  (double)((W1 * (xValues.get(currV1)-xMin))/(xMax-xMin)));


                    int y1 = (int)Math.round(margin + (double)((H1 * (yMax-yValues.get(currV1)))/(yMax-yMin)));


                    int x2 = (int)Math.round(margin +  (double)((W1 * (xValues.get(currV2)-xMin))/(xMax-xMin)));
                    int y2 = (int)Math.round(margin + (double)((H1 * (yMax-yValues.get(currV2)))/(yMax-yMin)));

//                    if(direction==1){
//                        g.setColor(Color.RED);
//                    }
                    g.setColor(Color.BLACK);
                    //Create graphics 2D object
                    Graphics2D g2d = (Graphics2D) g.create(); // Create a copy for safe transformations
                    g2d.setColor(Color.BLACK);
                    //boolean variable to indicate if arrow should be black or red color
                    boolean redForForward=false;
                    boolean redForBackward=false;
                    //check if the current forward edge is part of the unique augmenting path
                    if(uniqueAugmentingPath.contains(currV1) && uniqueAugmentingPath.contains(currV2)){
                        int i1=uniqueAugmentingPath.indexOf(currV1);
                        int i2=uniqueAugmentingPath.indexOf(currV2);
                        if(i1==(i2-1)){
                            System.out.println("Augmenting path edge: "+currV1+" to "+currV2);
                            g.setColor(Color.RED);
//                            g2d.setColor(Color.RED);
                            redForForward=true;
                        }
                        else if(i2==(i1-1)){
                            System.out.println("Augmenting path edge: "+currV2+" to "+currV1);
//                            g.setColor(Color.RED);
                            g2d.setColor(Color.RED);
                            redForBackward=true;
                        }
                    }
                    g.drawLine(x1, y1, x2, y2);

                    drawArrow(g, x1, y1, x2, y2, 0, redForForward);

                    //Set control

                    int ctrlX = ((x1+x2)/3)+20, ctrlY = ((y1+y2)/3)+20;
                    //Mid point
                    double xM = (x1+x2)/2.0;
                    double yM = (y1+y2)/2.0;

                    int delX = (x2-x1);
                    int delY = (y2-y1);
                    double lamba=0.2;
                    int pX=-delY, pY=delX;

                    ctrlX = (int)(xM+lamba*pX);
                    ctrlY = (int)(yM+lamba*pY);

                    //Draw reverse edge if present
                    int rEdge = rGraph2[currV2][currV1];
                    double t = 0.5; // Parameter for midpoint

                    int midXOnCurve = (int) (Math.pow(1 - t, 2) * x1 + 2 * (1 - t) * t * ctrlX + Math.pow(t, 2) * x2);
                    int midYOnCurve = (int) (Math.pow(1 - t, 2) * y1 + 2 * (1 - t) * t * ctrlY + Math.pow(t, 2) * y2);

                    // Calculate the slope at the midpoint
                    double slopeX = 2 * (1 - t) * (ctrlX - x1) + 2 * t * (x2 - ctrlX);
                    double slopeY = 2 * (1 - t) * (ctrlY - y1) + 2 * t * (y2 - ctrlY);

                    if(rEdge > 0){
                        try {
                            // Draw the curved line
                            QuadCurve2D q = new QuadCurve2D.Float(x1, y1, ctrlX, ctrlY, x2, y2);
                            g2d.draw(q);
                            //Calculate the flow, capacity and residual capacity for this backward edge
                            //label the edge
                            int residualCapacityForBackwardEdge = rGraph2[currV2][currV1];
//                            int capacityForBackwardEdge = graph[currV2][currV1];
//                            int flowForBackwardEdge=capacityForBackwardEdge-residualCapacityForBackwardEdge;

                            //Draw the arrow on this curved edge
                            drawArrowAndLabelOnCurve(g2d, midXOnCurve, midYOnCurve, slopeX, slopeY, 1, redForBackward, residualCapacityForBackwardEdge+"");
                        } finally {
                            g2d.dispose(); // Dispose of the copy to keep the original Graphics context clean
                        }

                    }

                    g.setColor(Color.BLACK);
                    g2d.setColor(Color.BLACK);

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


                    int x3 = (int)Math.round(xMid + (20*vX));
                    int y3 = (int)Math.round(yMid + (20*vY));


                    //label the edge
                    int residualCapacity = rGraph2[currV1][currV2];
                    int capacity = graph[currV1][currV2];
                    int flow=capacity-residualCapacity;
//                    System.out.println("Flow/Capacity of this edge: "+flow+"/"+capacity);
                    g.drawString(residualCapacity+"/"+capacity+"", x3, y3);

                    //Now draw the direction for the edge




                    //g.drawLine(xMid, yMid, x3, y3);
                    edgeCounter++;

                    //Draw line from currVertex to its parent

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

    private void drawArrow(Graphics g, int x1, int y1, int x2, int y2, int direction, boolean red) {
        int arrowLength = 15;
        int arrowWidth = 7;

        // Calculate midpoint
        int mx = (x1 + x2) / 2;
        int my = (y1 + y2) / 2;

        // Calculate direction vector based on the "direction" variable
        double dx, dy;
        if (direction == 0) {
            dx = x2 - x1;
            dy = y2 - y1;
        } else {
            dx = x1 - x2;
            dy = y1 - y2;
        }

        // Normalize direction vector
        double length = Math.sqrt(dx * dx + dy * dy);
        dx /= length;
        dy /= length;

        // Calculate triangle (arrowhead) points using the direction vector and midpoint
        Point arrowTip = new Point((int) (mx + arrowLength * dx), (int) (my + arrowLength * dy));
        Point arrowLeft = new Point((int) (mx - arrowWidth * dy), (int) (my + arrowWidth * dx));
        Point arrowRight = new Point((int) (mx + arrowWidth * dy), (int) (my - arrowWidth * dx));

        // Draw triangle
        Polygon arrowPolygon = new Polygon();
        arrowPolygon.addPoint(arrowTip.x, arrowTip.y);
        arrowPolygon.addPoint(arrowLeft.x, arrowLeft.y);
        arrowPolygon.addPoint(arrowRight.x, arrowRight.y);
        if(red){
            g.setColor(Color.RED);
        }
        g.fillPolygon(arrowPolygon);
        g.setColor(Color.BLACK);
    }

    private void drawArrowAndLabelOnCurve(Graphics2D g2d, double x, double y, double slopeX, double slopeY, int direction, boolean red, String label) {
        // Save the original transform
        AffineTransform originalTransform = g2d.getTransform();
        // Set color for the arrow
        if (red) {
            g2d.setColor(Color.RED);
        } else {
            g2d.setColor(Color.BLACK);
        }

        // Calculate the angle of the arrow
        double angle = Math.atan2(slopeY, slopeX);

        // Adjust angle based on direction
        if (direction == 1) {
            angle += Math.PI; // Reverse the arrow direction
        }

        // Length and width of the arrow
        int arrowLength = 10;
        int arrowWidth = 5;

        // Create an arrow shape
        Polygon arrowHead = new Polygon();
        arrowHead.addPoint(0, 0);
        arrowHead.addPoint(-arrowWidth, -arrowLength);
        arrowHead.addPoint(arrowWidth, -arrowLength);

        AffineTransform tx = new AffineTransform();
        tx.translate(x, y);
        tx.rotate(angle - Math.PI / 2);

        Shape transformedArrowHead = tx.createTransformedShape(arrowHead);
        g2d.fill(transformedArrowHead);

        // Reset to the original transform before setting a new one for the label
        g2d.setTransform(originalTransform);

        // Set the font size larger for visibility
        g2d.setFont(new Font("SansSerif", Font.BOLD, 12));

        // Calculate the perpendicular offset for the label based on the slope of the curve
        double labelOffsetMagnitude = 20; // You can adjust this value as needed
        double angleToMoveLabel = Math.atan2(slopeY, slopeX) - Math.PI / 2;
        double labelOffsetX = labelOffsetMagnitude * Math.cos(angleToMoveLabel);
        double labelOffsetY = labelOffsetMagnitude * Math.sin(angleToMoveLabel);

        // Move the label position above the arrow
        double labelX = x + (direction == 0 ? labelOffsetX : -labelOffsetX);
        double labelY = y + (direction == 0 ? labelOffsetY : -labelOffsetY);

        // Draw the label centered above the arrow
        FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
        int labelWidth = metrics.stringWidth(label);
        g2d.drawString(label, (float)(labelX - labelWidth / 2), (float)(labelY - metrics.getHeight() / 2));

        // Restore the original transform so that it doesn't affect subsequent drawing
        g2d.setTransform(originalTransform);

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


