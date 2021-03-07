import java.awt.*;
import java.util.ArrayList;

class BallNode {
    //all tree specific variables
    private int level;
    private boolean hasSplit = false;
    private int xPos;
    private int yPos;
    private int maxSplit = 5;
    private int maxBalls = 5;
    private Rectangle box;
    private boolean ableToAdd = true;
    ArrayList<BouncingBall> ballList = new ArrayList<>();
    private BallNode[] subNodes = new BallNode[4];

    //constructor
    BallNode (int level, Rectangle box) {
        this.level = level;
        this.box = box;
        this.hasSplit = false;
    }

    /*
    * getBox
    * getter for the boundingbox of the node
    * @return rectangle
     */
    public Rectangle getBox() {
        return box;
    }

    /*
    *getSplt
    * @return boolean depending on if the node has split already or not
     */
    public boolean getSplit(){
        return hasSplit;
    }

    /*
    * @return array of subnodes of the node
     */
    public BallNode[] getSubNodes () {
        if (subNodes != null) {
            return subNodes;
        }
        return null;
    }

    /*
    * splits the current node into subnodes if necessary
     */
    public void split () {

        if (level < maxSplit) { //is the max split limit hit
            int subWidth = (int) (box.getWidth() / 2); // sets width for subnodes
            int subHeight = (int) (box.getHeight() / 2); //sets height for new subnodes

            xPos = (int) box.getX();
            yPos = (int) box.getY();

            //creates each new subnode with proper boundaries
            subNodes[0] = new BallNode(level + 1, new Rectangle(xPos, yPos, subWidth, subHeight));
            subNodes[1] = new BallNode(level + 1, new Rectangle(xPos + subWidth, yPos, subWidth, subHeight));
            subNodes[2] = new BallNode(level + 1, new Rectangle(xPos, yPos + subHeight, subWidth, subHeight));
            subNodes[3] = new BallNode(level + 1, new Rectangle(xPos + subWidth, yPos + subHeight, subWidth, subHeight));

            //sets node's split value to true
            hasSplit = true;

            //since node has split, balls should no longer be added to it
            ableToAdd = false;

            //insert all of it's balls into their proper subnodes
            for (int i = 0; i < ballList.size(); i++) {
                insert(ballList.get(i));
            }
            ballList.clear();
        }
    }

    /*
    * finds the proper quadrant that the ball passed in fits into
    * @param ball
    * @return int that represents quadrant the ball fits into
     */
    public int getIndex (BouncingBall object) {

        //sets midpoint for current node
        int vMidpoint = (int) (box.getX() + (box.getWidth())/2);
        int hMidpoint = (int) (box.getY() + (box.getHeight())/2);

        //default value for index
        int index = -1;


        boolean bottomQuadrant = false;
        boolean topQuadrant = false;

        //checks which section half of the quadrant the ball is in
        if (object.getY() < hMidpoint) {
            topQuadrant = true;
        } else if (object.getY() > hMidpoint) {
            bottomQuadrant = true;
        }

        //if it's in the top portion of the quadrant check which side it's on
        if (topQuadrant) {
            if (object.getX() < vMidpoint) {
                index = 0;
            } else if (object.getX() > vMidpoint) {
                index = 1;
            }

            //if it's in the top portion of the quadrant check which side it's on
     }   else if (bottomQuadrant) {
            if (object.getX() < vMidpoint) {
                index = 2;
            } else if (object.getX() > vMidpoint) {
                index = 3;
            }
        }
        return index;
    }

    /*
    * inserts the ball into it's correct node
    * @param ball being inserted
     */
    public void insert (BouncingBall ball) {
        int index;

        //if the ball doesn't fit into any quadrants stop the method
        if (getIndex(ball) == -1) {
            return;
        }

        //if the size of the quadrant list is smaller than the max and if it can still be added to
        if (ballList.size() < maxBalls && ableToAdd ) {
            ballList.add(ball);
        } else {
            if (!hasSplit) { //calls the method to split the node
                split();
            } else {
                index = getIndex(ball); // finds ball's proper subnode array index/quadrant
                subNodes[index].insert(ball); //inserts the ball into it's proper subnode
            }
        }
    }

    /*
    * clears the whole node of any balls inside
     */
    public void clear() {
        ballList.clear(); //clears arraylist of balls
        if (subNodes != null) { //if subnodes exist for the node clear them
            for (int i = 0; i < subNodes.length; i++) {
                    subNodes[i] = null;
            }
        }
        hasSplit = false;

    }

}
