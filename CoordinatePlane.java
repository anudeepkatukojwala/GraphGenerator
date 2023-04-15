import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.*;
import java.util.List;
import javax.swing.*;

public class CoordinatePlane extends JFrame {

    List<Integer> xValues = new ArrayList<>();
    List<Integer> yValues =  new ArrayList<>();

    Queue<Map<Integer, Integer>> edges = new ArrayDeque<>();

    List<String> edg = new ArrayList<>();
    private JButton uploadButton;
    private JFileChooser fileChooser;
    private static int WIDTH = 600;
    private static int HEIGHT = 600;
    private static final int ORIGIN_X = WIDTH / 2;
    private static final int ORIGIN_Y = HEIGHT / 2;
    private static final int TICK_SIZE = 5;
    private static final int TICK_INTERVAL = 20;
    private static final int AXIS_WIDTH = 2;

    private JPanel planePanel;

    private JDialog ndialog;


    public CoordinatePlane() {
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
                int result = fileChooser.showOpenDialog(CoordinatePlane.this);
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
                                xValues.add(Integer.parseInt(currArr[1]));
                                yValues.add(Integer.parseInt(currArr[2]));

                            }
                            else{
                                edg.add(line);
                            }
                        }
                        //System.out.println("xValues are: "+xValues);
                        //System.out.println("yValues are: "+yValues);
                        System.out.println("Edges are: "+edg);
                        reader.close();
                        new NewDialog(CoordinatePlane.this, xValues, yValues, edg);

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
        new CoordinatePlane();
    }
}

class NewDialog extends JDialog{
    private JPanel drawPanel;
    private int newWidth = 600;
    private int newHeight = 600;

    List<Integer> xValues = new ArrayList<>();
    List<Integer> yValues =  new ArrayList<>();

    Queue<Map<Integer, Integer>> edges = new ArrayDeque<>();

    List<String> edg = new ArrayList<>();

    public NewDialog(JFrame parent, List<Integer> xValues, List<Integer> yValues, List<String> edg) {
        super(parent, "Graph", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600,600);



        this.xValues = xValues;
        this.yValues = yValues;
        this.edg = edg;
        createGUI();


    }

    public void createGUI(){

        Font f1 = new Font(Font.SERIF, Font.BOLD,  10);

        GraphOperations newObj = new GraphOperations(xValues, yValues, edg);
        PreGraphDrawingOperations tempObj = new PreGraphDrawingOperations(xValues, yValues, edg);
        boolean shouldWeContinue = tempObj.checkIfAnyThreePointsAreCollinear(newObj.vertexAngleMapping, newObj.getRotationSystem());
        System.out.println("Return of checkIfAnyThreePointsAreCollinear: "+shouldWeContinue);
        if(!shouldWeContinue){
            System.out.println("Coordinates are collinear");
            return;
        }

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

                List<Integer> extremes = getExtremes();

                int xMin = extremes.get(0);
                int xMax = extremes.get(1);
                int yMin = extremes.get(2);
                int yMax = extremes.get(3);

                //counter for edge label
                int edgeCounter = 0;

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

                    g.drawLine(x1, y1, x2, y2);

                    int xMid = (int)Math.round((double)(x1+x2)/2);
                    int yMid = (int)Math.round((double)(y1+y2)/2);

                    int deltaX = Math.abs(x2-x1);
                    int deltaY = Math.abs(y2-y1);

                    double n = Math.sqrt(Math.pow(deltaX, 2)+Math.pow(deltaY, 2));
                    //System.out.println("n value: "+ n);

                    //Below code is to make sure all edge labels are drawn perpendicular
                    //to their edges
                    int dummyX1 = xValues.get(currV1);
                    int dummyY1 = yValues.get(currV1);
                    int dummyX2 = xValues.get(currV2);
                    int dummyY2 = yValues.get(currV2);

                    //move one vertex to (0,0) coordinate and find the angle of the
                    //other vertex using atan2 function
                    int newX = (dummyX1+(-dummyX2));
                    int newY = (dummyY1+(-dummyY2));
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
        GraphOperations obj = new GraphOperations(xValues, yValues, edg);
        //obj.getAdjacencyListOfGraph();
        obj.getObjectsOfAdjacencyList();
        obj.getRegions();
        //obj.getRotationSystem();
        //System.out.println("Regions are: "+obj.getRegions());
        drawPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        drawPanel.setBackground(Color.WHITE);
        add(drawPanel);
        setVisible(true);
        pack();
    }


    //Find xMin, xMax, yMin, yMax
    public List<Integer> getExtremes(){
        List<Integer> lt = new ArrayList<>();

        //Find extremes in x values
        int xMin = Integer.MAX_VALUE;
        int xMax = Integer.MIN_VALUE;

        for(int curr:xValues){
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
        int yMin = Integer.MAX_VALUE;
        int yMax = Integer.MIN_VALUE;

        for(int curr:yValues){
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
    public List<List<Integer>> getPixelValues(List<Integer> xValues, List<Integer> yValues, int minOfWH){
        List<List<Integer>> lt = new ArrayList<>();

        int W=minOfWH;
        int H=minOfWH;

        int margin = 40;

        int W1 = W-(2*margin);
        int H1 = H-(2*margin);

        List<Integer> extremes = getExtremes();
        //System.out.println("Extremes are: "+extremes);
        int xMin = extremes.get(0);
        int xMax = extremes.get(1);
        int yMin = extremes.get(2);
        int yMax = extremes.get(3);

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