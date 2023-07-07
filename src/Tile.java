import processing.core.PApplet;

public class Tile {
    private int x,y;
    public static final int SIDE = Main.SIDE, UP = 0, DOWN = 1, LEFT = 2, RIGHT = 3;

    private boolean isGoal;
    private boolean isBarrier;
    private boolean isStart;

    private Tile[] neighbors;
    private Tile parent;


    //g is distance from start, h is distance to goal, f is g+h
    private double g, h, f;
    private boolean onPath;


    public Tile(int x, int y){
        this.x = x;
        this.y=y;

        neighbors = new Tile[4];

        isBarrier = (x == 0 || x == Main.NUM_TILES_ROW* SIDE - SIDE || y == 0 || y == Main.NUM_TILES_COL * SIDE - SIDE);
    }

    public boolean isBarrier(){return isBarrier;}

    public Tile[] getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(Tile[] neighbors) {
        this.neighbors = neighbors;
    }

    public void setGoal(boolean goal) { isGoal = goal; }


    public void setIsBarrier(boolean b) { this.isBarrier=b; }

    public boolean isGoal() { return isGoal; }

    public boolean isStart() { return isStart; }

    public void setStart(boolean start) { isStart = start; }



    public void draw(PApplet window){

        window.fill(255);
        if(isGoal){
            window.fill(0,255,0);
            window.rect( x, y, SIDE, SIDE);
            window.fill(255);

        } else if(isBarrier){

            window.fill(0);
            window.rect( x, y, SIDE, SIDE);
            window.fill(255);

        } else if(isStart){
            window.fill(0,0,255);
            window.rect( x, y, SIDE, SIDE);
            window.fill(255);
        }
        else if(onPath){
            window.fill(255,255,0);
            window.rect( x, y, SIDE, SIDE);
            window.fill(255);
        } else {
            window.rect( x, y, SIDE, SIDE);
        }



    }

    public String toString(){
        return "[x = " + x + "], [y = ]" + y + "]";
    }

    public boolean incorporates(int x, int y) {
        return this.x < x && this.y<y && x<this.x+SIDE && y<this.y+SIDE;
    }

    public void setIsStart(boolean b) {
        isStart = b;
    }

    public Tile lowestNeighboringF(Tile parent){
        this.parent=parent;
        Tile t = neighbors[0];

        for (int i = 1; i < neighbors.length; i++) {
            if(neighbors[i] != parent && t.f>neighbors[i].f){
                t = neighbors[i];
            }
        }

        return t;
    }

    public void calculateFGH(Tile start, Tile goal){
        if(isGoal){
            f=0;
            return;
        } else if(isStart){
            f = Float.MAX_VALUE;
            return;
        }

        g = Math.abs(goal.x-x + goal.y-y);
        h=Math.abs(start.x-x + start.y-y);

    }

    public static Tile min(Tile a, Tile b){
        return a.f<b.f? a : b;
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getG() {
        return g;
    }


    public void setG(double g) {
        this.g = g;
    }

    public void setH(int h) {
        this.h=h;
    }

    public void setParent(Tile parent) {
        this.parent=parent;
    }

    public Tile getParent() {
        return parent;
    }

    public void setIsPartOfPath(boolean contains) {
        onPath = contains;
    }

    public double getF() {
        return f;
    }
}
