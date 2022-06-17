package agh.ics.oop;

public class Jungle {
    private final Vector2d upperLeft;
    private final Vector2d lowerRight;

    public Jungle(float jungleRatio,int width, int height) {
        int a = (int) (height*Math.sqrt(jungleRatio));
        int b = (int) (Math.sqrt(jungleRatio) * width);
        upperLeft = new Vector2d((int)(width-b)/2,(int)(height+a)/2);
        lowerRight = new Vector2d((int)(width+b)/2,(int)(height-a)/2);
    }

    public Vector2d getUpperLeft(){
        return upperLeft;
    }

    public Vector2d getLowerRight(){
        return lowerRight;
    }
}
