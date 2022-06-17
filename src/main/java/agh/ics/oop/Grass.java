package agh.ics.oop;

import java.util.Random;

public class Grass implements IMapElement{
    Random random=new Random();
    private Vector2d grassPosition;


    public void makeJungleGrass(Vector2d upperLeft,Vector2d lowerRight){
        int xLeft= upperLeft.x;
        int xRight=lowerRight.x;
        int yLower= lowerRight.y;
        int yUpper= upperLeft.y;
        int x= random.nextInt(xRight-xLeft)+xLeft;
        int y= random.nextInt(yUpper-yLower)+yLower;
        grassPosition=new Vector2d(x,y);
    }

    public void makeSavannaGrass(Vector2d upperLeft,Vector2d lowerRight,int width,int height){
        int xLeft= upperLeft.x;
        int xRight=lowerRight.x;
        int yLower= lowerRight.y;
        int yUpper= upperLeft.y;
        int x;
        int y;
        if (xLeft==0 && xRight==width && yLower==0 && yUpper==height){
            makeJungleGrass(upperLeft,lowerRight);
        }
        else{
            x= random.nextInt(width);
            y= random.nextInt(height);
            while(x>=xLeft && x<=xRight && y>=yLower && y<=yUpper){
                x= random.nextInt(width);
                y= random.nextInt(height);
            }
            grassPosition=new Vector2d(x,y);
        }
    }


    @Override
    public Vector2d getPosition(){
        return grassPosition;
    }

}
