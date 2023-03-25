import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
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
                        System.out.println("xValues are: "+xValues);
                        System.out.println("yValues are: "+yValues);
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
        drawPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                //System.out.println("Are we here");

                //System.out.println("Current Dimensions values: Height: "+drawPanel.getHeight()+"Width: "+drawPanel.getWidth());

                //Set width and height
                newWidth = drawPanel.getWidth();
                newHeight = drawPanel.getHeight();
                //Find the Minimum of Width and Height of current window size to draw the co-ordinate plane and graph
                int minOfWidthOrHeight = Math.min(drawPanel.getWidth(), drawPanel.getHeight());
                System.out.println("Current Min value is: "+minOfWidthOrHeight);
                g.drawLine(0, Math.round(minOfWidthOrHeight/2), minOfWidthOrHeight, Math.round(minOfWidthOrHeight/2));
                g.drawLine(Math.round(minOfWidthOrHeight/2), 0, Math.round(minOfWidthOrHeight/2), minOfWidthOrHeight);

                //Find pixel distance between each tick mark on the co-ordinate plane
                int tickDistance = Math.round(minOfWidthOrHeight/2)/10;

                //draw tick marks on x-axis (positive values)
                for(int i=0;i<10;i++){
                    g.drawLine(Math.round(minOfWidthOrHeight/2)+tickDistance*i, Math.round(minOfWidthOrHeight/2)-5, Math.round(minOfWidthOrHeight/2)+tickDistance*i, Math.round(minOfWidthOrHeight/2)+5);

                    g.drawString(String.valueOf(i), Math.round(minOfWidthOrHeight/2)+tickDistance*i, Math.round(minOfWidthOrHeight/2)+12);

                }

                //draw tick marks on x-axis (negative values)
                for(int i=-1;i>-10;i--){
                    g.drawLine(Math.round(minOfWidthOrHeight/2)+tickDistance*i, Math.round(minOfWidthOrHeight/2)-5, Math.round(minOfWidthOrHeight/2)+tickDistance*i, Math.round(minOfWidthOrHeight/2)+5);

                    g.drawString(String.valueOf(i), Math.round(minOfWidthOrHeight/2)+tickDistance*i, Math.round(minOfWidthOrHeight/2)+12);

                }

                //draw tick marks on y-axis (positive values)
                for(int i=0;i<10;i++){
                    g.drawLine(Math.round(minOfWidthOrHeight/2)-5, Math.round(minOfWidthOrHeight/2)-(tickDistance*i), Math.round(minOfWidthOrHeight/2)+5, Math.round(minOfWidthOrHeight/2)-(tickDistance*i));
                    if(i==0){
                        continue;
                    }
                    g.drawString(String.valueOf(i), Math.round(minOfWidthOrHeight/2)-12, Math.round(minOfWidthOrHeight/2)-(tickDistance*i));
                }

                //draw tick marks on y-axis (negative values)
                for(int i=-1;i>-10;i--){
                    g.drawLine(Math.round(minOfWidthOrHeight/2)-5, Math.round(minOfWidthOrHeight/2)-(tickDistance*i), Math.round(minOfWidthOrHeight/2)+5, Math.round(minOfWidthOrHeight/2)-(tickDistance*i));

                    g.drawString(String.valueOf(i), Math.round(minOfWidthOrHeight/2)-25, Math.round(minOfWidthOrHeight/2)-(tickDistance*i));
                }

                //System.out.println("After: "+g.getColor());

                //counter for edge label
                int edgeCounter = 0;

                //Draw edges
                for(int i=0;i<edg.size();i++){
                    String[] edgArr = edg.get(i).split(" ");
                    int currV1 = Integer.parseInt(edgArr[1]);
                    int currV2 = Integer.parseInt(edgArr[2]);
                    System.out.println("Edge drawn from: "+edgArr[1]+" to "+edgArr[2]);
                    int x1 = xValues.get(currV1)*tickDistance+Math.round(minOfWidthOrHeight/2);
                    int y1 = Math.round(minOfWidthOrHeight/2)-yValues.get(currV1)*tickDistance;
                    int x2 = xValues.get(currV2)*tickDistance+Math.round(minOfWidthOrHeight/2);
                    int y2 = Math.round(minOfWidthOrHeight/2)-yValues.get(currV2)*tickDistance;
                    g.drawLine(xValues.get(currV1)*tickDistance+Math.round(minOfWidthOrHeight/2),Math.round(minOfWidthOrHeight/2)-yValues.get(currV1)*tickDistance, xValues.get(currV2)*tickDistance+Math.round(minOfWidthOrHeight/2), Math.round(minOfWidthOrHeight/2)-yValues.get(currV2)*tickDistance);

                    //Find slope of the line by using above two points on the line
                    //double slope = (y2-y1)/(x2-x1);

//                    int x3 = (int)((x1+x2)/2+(8/(Math.sqrt(Math.pow((y1-y2), 2)+Math.pow((x2-x1), 2))))*(y1-y2));
//                    int y3 = (int)((y1+y2)/2+(8/(Math.sqrt(Math.pow((y1-y2), 2)+Math.pow((x2-x1), 2))))*(x2-x1));

                    double xMid = (x1+x2)/2;
                    double yMid = (y1+y2)/2;

                    int deltaX = Math.abs(x2-x1);
                    int deltaY = Math.abs(y2-y1);

                    double n = Math.sqrt(Math.pow(deltaX, 2)+Math.pow(deltaY, 2));

                    double vX = -deltaY/n;
                    double vY = -deltaX/n;

                    int x3 = (int)Math.round(xMid + (10*vX));
                    int y3 = (int)Math.round(yMid + (10*vY));

                    //label the edge
                    g.drawString(Character.toString(97+edgeCounter), x3, y3);
                    edgeCounter++;

                }

                System.out.println("Current Font: "+g.getFont());
                System.out.println("Current FontMetrics: "+g.getFontMetrics());

                //Label vertices
                for(int i=0;i<xValues.size();i++){
                    g.drawOval(xValues.get(i)*tickDistance+Math.round(minOfWidthOrHeight/2)-(10), Math.round(minOfWidthOrHeight/2)-(tickDistance*yValues.get(i))-(10), 20*(int)Math.sqrt(2), 20*(int)Math.sqrt(2));
                    g.setColor(Color.RED);
                    g.setFont(f1);
                    g.drawString(String.valueOf(i), Math.round(minOfWidthOrHeight/2)+tickDistance*xValues.get(i)-2, Math.round(minOfWidthOrHeight/2)-(tickDistance*yValues.get(i))+3);
                    g.setFont(null);
                    g.setColor(Color.BLACK);
                }



            }
        };
        drawPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        add(drawPanel);
        setVisible(true);
        pack();
    }
}