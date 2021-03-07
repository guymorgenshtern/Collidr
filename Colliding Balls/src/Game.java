//Graphics &GUI imports
import javax.swing.*;
import java.awt.*;

//Keyboard imports
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//Util
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;


class Game extends JFrame {

    // how many balls are added each time
    static int threshold = 100;
    //if the one ball is on top of the other
    boolean hitOnTop = false;
    //creates the head node of the tree
    BallNode quadTree= new BallNode(0, new Rectangle(0,0,800,800));

    //holds all the balls
    static ArrayList<BouncingBall> list = new ArrayList<>();


    //class variables
    static JFrame window;
    JPanel gamePanel;


    //Main
    public static void main(String[] args) {
        //creates balls
        createBalls(threshold);
        //initiates game
        window = new Game();


    }

    /*
    * createBalls
    * creates a number of ball objects dependent on the threshold
    * @param number of balls added
     */
    public static void createBalls(int threshold) {
        for (int i = 0; i < threshold; i++) {
            list.add(new BouncingBall()); //adds balls to arraylist
        }
    }


    //Constructor - this runs first
    Game() {
        super("My Game");
        // Set the frame to full screen
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 800);
        // this.setUndecorated(true);  //Set to true to remove title bar
        //frame.setResizable(false);



        //Set up the game panel (where we put our graphics)
        gamePanel = new GameAreaPanel();
        this.add(new GameAreaPanel());

        MyKeyListener keyListener = new MyKeyListener();
        this.addKeyListener(keyListener);

        this.requestFocusInWindow(); //make sure the frame has focus
        gamePanel.setBackground(Color.BLACK);
        this.setVisible(true);




    } //End of Constructor

    /*
    * drawBox
    * takes in graphics and the current node and draws the boundaries of the node on screen
    * recursively calls itself if the current node drawn has subnodes to be drawn as well
    * @param graphics to draw and a BallNode which is the current node being drawn
     */
    public void drawBox (Graphics g, BallNode node) {
        //draws the quadrant
        g.drawRect((int)node.getBox().getX(), (int)node.getBox().getY(), (int)node.getBox().getWidth(), (int)node.getBox().getHeight());

        //recursive call when current quadrant has subquadrants
        if (node.getSplit()) {
            drawBox(g, node.getSubNodes()[0]);
            drawBox(g, node.getSubNodes()[1]);
            drawBox(g, node.getSubNodes()[2]);
            drawBox(g, node.getSubNodes()[3]);

        }
    }

    /*
    * checkCollision
    * takes in the current node and graphics to run through the arraylist of balls in the current node to check if any collide
    * recursively calls itself to check all of it's subnodes
    * @param the current node and graphics to highlight the balls colliding
     */
    public void checkCollision(BallNode quadTree, Graphics g) {
        BouncingBall current;
        BouncingBall check;

        //checks for collisions between each ball in each subnode
        for (int i = 0; i < quadTree.ballList.size(); i++) {
            current = quadTree.ballList.get(i);
            for (int j = 0; j < quadTree.ballList.size(); j ++) {
                check = quadTree.ballList.get(j);
                if (current.getBoundingBox().intersects(check.getBoundingBox())) {
                    if (current != check) { //makes sure ball isn't checking collision with itself
                        fixPosition(current, check, g);
                    }
                }
            }
        }

        //recursive call if the current node has subnodes
        if (quadTree.getSplit()) {
            checkCollision(quadTree.getSubNodes()[0], g);
            checkCollision(quadTree.getSubNodes()[1], g);
            checkCollision(quadTree.getSubNodes()[2], g);
            checkCollision(quadTree.getSubNodes()[3], g);
        }
    }





    /*
    * fixPosition
    * fixes position so that collision doesn't happen more than once
    * @param two balls that are colliding
     */
    private void fixPosition (BouncingBall ballOne, BouncingBall ballTwo, Graphics g) {

        //stops the method if the balls aren't colliding
        if (!(ballOne.getBoundingBox().intersects(ballTwo.getBoundingBox()))) {
            return;
        }

        //checks if either ball is hitting the other from above
        if (ballOne.getY() > ballTwo.getY() || ballTwo.getY() > ballOne.getY()) {
            hitOnTop = true;
        }

        //finds the distance between the two balls
        int xDisp = ballOne.getX() - ballTwo.getX();
        int yDisp = ballOne.getY() - ballTwo.getY();

        //hypotenuse between the two balls
        double length = Math.sqrt(Math.pow(xDisp, 2) + Math.pow(yDisp, 2));
        if (length == 0) {
            length = 1;
        }
        double unitX = (xDisp/length);
        double unitY =  (yDisp/length);

        //finds minimum distance at which balls collide
        int minDistance = (int) (ballOne.getBoundingBox().getWidth()/2 + ballTwo.getBoundingBox().getWidth()/2);

        //sets distance of balls so only one collision occurs
        ballOne.setX((int)(ballTwo.getX() + ((minDistance + 1) * unitX)));

        //calls method for ball collision
        collision(ballOne, ballTwo);

    }

    /*
    *Collision
    * does the math for colliding balls
    * @param two colliding balls
     */
    public void collision(BouncingBall ballOne, BouncingBall ballTwo) {
        //If the masses of the objects are the same, swap their velocities
        if (ballOne.getMass() == ballTwo.getMass()) {

            // if ball was hit from above swap y velocities
            if (hitOnTop) {
                int oldY1 = ballOne.getSlopeY();
                int oldY2 = ballTwo.getSlopeY();

                ballOne.setSlopeY(oldY2);
                ballTwo.setSlopeY(oldY1);
            }

            //swap x velocities
            int oldV1 = ballOne.getSlopeX();
            int oldV2 = ballTwo.getSlopeX();

            ballOne.setSlopeX(oldV2);
            ballTwo.setSlopeX(oldV1);
        }
    }

    /** --------- INNER CLASSES ------------- **/

    // Inner class for the the game area - This is where all the drawing of the screen occurs
    private class GameAreaPanel extends JPanel {
        public void paintComponent(Graphics g) {
            super.paintComponent(g); //required
            setDoubleBuffered(true);
            //clears the current tree
            quadTree.clear();
            // creates a black background
            g.setColor(Color.BLACK);
            g.fillRect(0,0,800,800);
            for (int i = 0; i < list.size(); i++) {

                //inserts current ball into proper quadrant
                quadTree.insert(list.get(i));

                //draws current ball
                g.setColor(list.get(i).getColor());
                g.fillOval(list.get(i).getX(), list.get(i).getY(), 10, 10);

                //checks box boundaries
                if (list.get(i).getX() > 800) {
                    list.get(i).setX(799);
                    list.get(i).setSlopeX(list.get(i).getSlopeX() * -1);
                }
                if (list.get(i).getX() < 0) {
                    list.get(i).setX(1);
                    list.get(i).setSlopeX(list.get(i).getSlopeX() * -1);
                }

                if (list.get(i).getY() > 800) {
                    list.get(i).setY(799);
                    list.get(i).setSlopeY(list.get(i).getSlopeY() * -1);
                }
                if (list.get(i).getY() < 0) {
                    list.get(i).setY(1);
                    list.get(i).setSlopeY(list.get(i).getSlopeY() * -1);
                }

                //sets new x and y positions for the ball and updates hitbox
                list.get(i).setX(list.get(i).getX() + list.get(i).getSlopeX());
                list.get(i).setY(list.get(i).getY() + list.get(i).getSlopeY());
                list.get(i).getBoundingBox().setLocation(list.get(i).getX(), list.get(i).getY());
            }

            //checks collisions once per frame
            checkCollision(quadTree, g);

            //calls method to draw quadrant borders
            g.setColor(Color.DARK_GRAY);
            drawBox(g, quadTree);

            repaint();
        }
    }

    // -----------  Inner class for the keyboard listener - this detects key presses and runs the corresponding code
    private class MyKeyListener implements KeyListener {

        public void keyTyped(KeyEvent e) {
        }

        public void keyPressed(KeyEvent e) {

            if (KeyEvent.getKeyText(e.getKeyCode()).equals("A")) {  //If 'A' is pressed
                //create more balls
                createBalls(threshold);
            } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {  //If ESC is pressed
                System.out.println("Quitting!"); //close frame & quit
                window.dispose();

            }
        }

        public void keyReleased(KeyEvent e) {
        }
    } //end of keyboard listener



}
