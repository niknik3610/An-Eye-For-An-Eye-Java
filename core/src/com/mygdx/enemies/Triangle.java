package com.mygdx.enemies;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.enemies.Enemy.Facing;

public class Triangle {
    public Vector2 v1, v2, v3;
    protected Vector2 roundedV1, roundedV2, roundedV3;
    protected Vector2 tempVector;
    protected int overlapNumber;
    
    protected Facing facing;
    
    public Triangle(Vector2 v1,Vector2 v2,Vector2 v3) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        
        this.roundedV1 = v1.cpy();
        this.roundedV2 = v2.cpy();
        this.roundedV3 = v3.cpy();

        tempVector = new Vector2();
        facing = Facing.NORTH;
    }

    public void moveTriangle(Vector3 direction) {
        this.v1.x += direction.x;
        this.v1.y += direction.y;
        this.v2.x += direction.x;
        this.v2.y += direction.y;
        this.v3.x += direction.x;
        this.v3.y += direction.y;
    }
    
    public void setTriangle(Vector2 v1,Vector2 v2,Vector2 v3) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }
    /**
     * @param number input number 1-3
     * @return vertices 1-3, -1 if vertices not found
     */
    public Vector2 getVertices(int number) {
        switch(number) {
            case 1:
                return v1;
            case 2: 
                return v2;
            case 3:
                return v3;
            default:
                System.out.println("This Verticy does not exist");
                return new Vector2(0,0);
        }
    }

    public float area(Vector2 v1, Vector2 v2, Vector2 v3) {
        return Math.abs((v1.x*(v2.y - v3.y) + v2.x*(v3.y - v1.y) + v3.x*(v1.y - v2.y))/2);
    }
   
    public boolean contains(Vector2 point){
        point.x = Math.round(point.x);
        point.y = Math.round(point.y);
        roundAllVerticies();

        float area = area(roundedV1, roundedV2, roundedV3);
        float point_a1 = area(point, roundedV1, roundedV2);
        float point_a2 = area(point, roundedV2, roundedV3);
        float point_a3 = area(point, roundedV1, roundedV3);
        
        if ((point_a1 + point_a2 + point_a3) == area) {
            return true;
        }
        return false;
    }

    public void rotate(Facing newFacing){
        float swap = 0;
        v2.sub(v1);
        v3.sub(v1);
        while (newFacing != this.facing) {
            swap = v3.x;
            v3.x = v3.y;
            v3.y = swap * -1;

            swap = v2.x;
            v2.x = v2.y;
            v2.y = swap * -1;
            this.facing = nextFacing(facing);
        }

        v2.add(v1);
        v3.add(v1);

        setTriangle(v1, v2, v3);
    }

    public int isContaintedByRectangle(Rectangle rec){
        if (rec.contains(v1)){
            return 1;
        } 
        if (rec.contains(v2)){
            return 2;
        } 
        if (rec.contains(v3)){
            return 3;
        }
        return -1;
    }

    public Vector2 getOverlapVector(Rectangle rec){
        float recX2 = rec.x + rec.width;
        float recY2 = rec.y + rec.height;

        if (this.contains(rec.getPosition(tempVector))) {
            return tempVector;
        }

        tempVector.set(recX2, rec.y);
        if (this.contains(tempVector)) {
            return tempVector;
        }
        
        tempVector.set(rec.x, recY2);
        if (this.contains(tempVector)) {
            return tempVector;
        }

        tempVector.set(recX2, recY2);
        if (this.contains(tempVector)) {
            return tempVector;
        }

        tempVector.set(-1, -1);
        return tempVector;
    }

    private Facing nextFacing(Facing facing){
        switch(facing){
            case NORTH:
                return Facing.EAST;
            case EAST:
                return Facing.SOUTH;
            case SOUTH:
                return Facing.WEST;
            case WEST:
                return Facing.NORTH;
            default:    
                return Facing.NORTH;
        }
    }

    @Override
    public Triangle clone(){
        Triangle triangle = new Triangle(v1, v2, v3);
        return triangle;
    }

    
    private void roundAllVerticies() {
        roundedV1.x = Math.round(v1.x);
        roundedV1.y = Math.round(v1.y);
        roundedV2.x = Math.round(v2.x);
        roundedV2.y = Math.round(v2.y);
        roundedV3.x = Math.round(v3.x);
        roundedV3.y = Math.round(v3.y);
    }

}  
