import java.awt.*;
import java.util.Random;
public class BouncingBall {

    //ball characteristics
    private int slopeX, slopeY;
    private double speed;
    private int x, y;
    private  int mass;
    private Color color;
    private int xMomentum;
    private int yMomentum;
    private double angle;
    private Rectangle boundingBox;
    BouncingBall () {

        //creates a random object used for a a bunch of different values
        Random ran1 = new Random();

        //picks unique colour of ball in specific palette
        float r = ran1.nextFloat();
        float gr = ran1.nextFloat() / 2f;
        float b = ran1.nextFloat() / 2f;
        color = new Color(r,gr,b);

        //creating the slope of the ball
        while (slopeY == 0 || slopeX == 0) {
            angle = ran1.nextDouble() * (2 * Math.PI);
            speed = ran1.nextDouble() * 2;
            slopeX = (int) (Math.cos(angle) * speed);
            slopeY = (int) (Math.sin(angle) * speed);
        }

        //randomizing position
        x = ran1.nextInt(799);
        y = ran1.nextInt(799);


        //creates the ball's hit box
        boundingBox = new Rectangle(x,y,15,15);


    }

    public Color getColor() {
        return color;
    }

    public void setxMomentum(int xMomentum) {
        this.xMomentum = xMomentum;
    }

    public void setyMomentum(int yMomentum) {
        this.yMomentum = yMomentum;
    }

    /*
    * @return mass of the ball
     */
    public int getMass() { return mass; }

    /*
    * @return the horizontal momentum of the ball
     */
    public int getMomentumX (){
        return xMomentum;
    }

    /*
    *@return vertical momentum of the ball
     */
    public int getMomentumY (){
        return yMomentum;
    }

    /*
    *@return horizontal slope
     */
    public int getSlopeX() {
        return slopeX;
    }
    /*
     *@param new value for horizontal slope
     */
    public void setSlopeX(int slopeX) {
        this.slopeX = slopeX;
    }

    /*
     *@return vertical slope
     */
    public int getSlopeY() {
        return slopeY;
    }

    /*
     *@param new value for vertical slope
     */
    public void setSlopeY(int slopeY) {
        this.slopeY = slopeY;
    }

    /*
     *@return speed of the object
     */
    public double getSpeed() {
        return speed;
    }

    /*
     *@param new value for speed
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /*
     *@return x position of object
     */
    public int getX() {
        return x;
    }

    /*
     *@param new value for x pos
     */
    public void setX(int x) {
        this.x = x;
    }

    /*
     *@return y position of object
     */
    public int getY() {
        return y;
    }

    /*
     *@param new value for y pos
     */
    public void setY(int y) {
        this.y = y;
    }

    /*
     *@return hit box of ball
     */
    public Rectangle getBoundingBox() {
        return boundingBox;
    }
}
