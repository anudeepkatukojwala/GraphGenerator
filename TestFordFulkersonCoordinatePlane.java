import javax.swing.*;
        import java.awt.*;
        import java.awt.event.ActionEvent;
        import java.awt.event.ActionListener;
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

//                        //Create object for GraphOperations
//                        GraphOperations graphOperationsObj = new GraphOperations(xValues, yValues, edg);
//
//                        //Create object for PreGraphOperations
//                        PreGraphDrawingOperations preGraphObj = new PreGraphDrawingOperations(xValues, yValues, edg);
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
//                        PlanarTriangulation pt = new PlanarTriangulation(xValues, yValues, edg, graphOperationsObj.getRegions(), graphOperationsObj.getRotationSystem());
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
//                        TutteEmbedding tutteObj = new TutteEmbedding(xValues, yValues, edg, currRegions, currRotationSystem, new ArrayList<>());
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
                        edg= (List<String>) returnOfFordFulkerson.get(3);
                        /****************************************************/

                        //Create a new dialog and send our vertices co-ordinates
                        //and edge list to draw the graph in this new dialog
                        JDialog dialog1=new TestFordFulkersonNewDialog(TestFordFulkersonCoordinatePlane.this, xValues, yValues, edg, graph, rGraph);
                        dialog1.setSize(600, 600);

                        //Create a new dialog for drawing residual graph of ford fulkerson
                        JDialog dialog2=new TestFordFulkersonResidualGraphDialog(TestFordFulkersonCoordinatePlane.this, xValues, yValues, edg, graph, rGraph, rGraph2);
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

    public TestFordFulkersonNewDialog(JFrame parent, List<Double> xValues, List<Double> yValues, List<String> edg, int[][] graph, int[][] rGraph) {
        super(parent, "Graph1", false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600,600);
        this.originalSizeOfEdg = originalSizeOfEdg;
        this.xValues = xValues;
        this.yValues = yValues;
        this.edg = edg;
        this.graph = graph;
        this.rGraph = rGraph;
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
//        GraphOperations obj = new GraphOperations(xValues, yValues, edg);
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

    public TestFordFulkersonResidualGraphDialog(JFrame parent, List<Double> xValues, List<Double> yValues, List<String> edg, int[][] graph, int[][] rGraph, int[][] rGraph2) {
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
//        GraphOperations obj = new GraphOperations(xValues, yValues, edg);
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


