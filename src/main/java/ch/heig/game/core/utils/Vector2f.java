/**
 *   Autheur: Theo Bensaci
 *   Date: 18:06 12.11.2025
 *   Description: Class use to manage a geometric vector of 2 float
 */

package ch.heig.game.core.utils;

public class Vector2f {
    public static final Vector2f ZERO = new Vector2f(0,0);
    public float x;
    public float y;

    public Vector2f(float initX, float initY){
        x=initX;
        y=initY;
    }

    /**
     * Copy a vector to a other
     * @param vec vec to copy
     */
    public Vector2f(Vector2f vec){
        this(vec.x,vec.y);
    }

    /**
     * Add a vector to this vector
     * @param vec second vector
     * @return this vector
     */
    public Vector2f add(Vector2f vec){
        x+=vec.x;
        y+=vec.y;
        return this;
    }

    /**
     * Substract a vector to this vector
     * @param vec second vector
     * @return this vector
     */
    public Vector2f sub(Vector2f vec){
        x-=vec.x;
        y-= vec.y;
        return this;
    }

    /**
     * multiply this vector with a scale
     * @param scale scale
     * @return this vector
     */
    public Vector2f mult(float scale){
        x*=scale;
        y*=scale;
        return this;
    }

    /**
     * dot product between this vector and a scond vector
     * @param vec second vector
     * @return this vector
     */
    public float dot(Vector2f vec){
        return (x* vec.x)+(y* vec.y);
    }


    /**
     * get magnetude of the vector
     * @return magnetude
     */
    public float magn(){
        return (float)Math.sqrt(x*x + y*y);
    }

    /**
     * get magnetude^2 of the vector
     * @return magnetude^2
     */
    public float powMagn(){
        return x*x + y*y;
    }

    /**
     * nomilize this vector
     */
    public Vector2f normilize(){
        if(isNull())return this;
        float magn = magn();
        x/=magn;
        y/=magn;
        return this;
    }


    /**
     * Rotate vector by a certain degree
     * @param rad radian
     * @return this vector
     */
    public Vector2f rotate(double rad){
        double newX=(Math.cos(rad)*x-Math.sin(rad)*y);
        double newY=(Math.sin(rad)*x+Math.cos(rad)*y);
        x=(float)newX;
        y=(float)newY;
        return this;
    }

    /**
     * Copy this vector to a new vector
     * @return new vector similar to the hold one
     */
    public Vector2f copy(){
        return new Vector2f(this);
    }

    /**
     * Lerp from vector a -> vector b
     * @param a vector a
     * @param b vector b
     * @param t t value
     * @return new vector lerped
     */
    public static Vector2f lerp(Vector2f a, Vector2f b, float t){
        return new Vector2f(MathUtils.lerp(a.x,b.x,t),MathUtils.lerp(a.y,b.y,t));
    }

    /**
     * give if this vector is null (0,0);
     * @return if the vector is null
     */
    public boolean isNull(){
        return x == 0 && y == 0;
    }

    /**
     * Set the x and y value of this vector in one call
     * @param x new x value
     * @param y new y value
     * @return this vector
     */
    public Vector2f set(float x, float y){
        this.x=x;
        this.y=y;
        return this;
    }

    /**
     * Set the x and y value of this vector to a other vector x and y in one call
     * @param vec referenced vector
     * @return this vector
     */
    public Vector2f set(Vector2f vec){
        return set(vec.x,vec.y);
    }


    /**
     * Check if 2 vector are equal
     * @param vec other vector
     * @return if this vector and the other vector are the same
     */
    public boolean isEqual(Vector2f vec){
        return this.x== vec.x && this.y==vec.y;
    }


    @Override
    public String toString() {
        return String.format("(%.2f || %.2f)",x,y);
    }
}
