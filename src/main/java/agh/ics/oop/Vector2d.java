package agh.ics.oop;

import java.util.Objects;


public class Vector2d {
    public int x;
    public int y;

    public Vector2d(int x,int y){
        this.x=x;
        this.y=y;
    }

    public String toString(){
        return "("+x+","+y+")";
    }

    Vector2d add(Vector2d other){
        return new Vector2d(x+other.x,y+other.y);
    }

    Vector2d subtract(Vector2d other){
        return new Vector2d(x-other.x,y-other.y);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Vector2d vector2d = (Vector2d) other;
        return x == vector2d.x && y == vector2d.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }


}
