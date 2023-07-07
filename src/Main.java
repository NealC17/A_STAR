import processing.core.PApplet;

import java.util.ArrayList;
import java.util.Arrays;

public class Main extends PApplet {

    public static final int NUM_TILES_ROW = 15;
    public static final int NUM_TILES_COL = 15;
    public static final int SIDE = 50;

    private Tile[][] tiles = new Tile[NUM_TILES_ROW][NUM_TILES_COL];

    public static final int WIDTH = NUM_TILES_ROW*Tile.SIDE, HEIGHT = NUM_TILES_COL * Tile.SIDE;

    private boolean creationPhase = true;


    public void settings(){
        size(WIDTH,HEIGHT);
        int j;
        for (int i = 0; i < tiles.length; i++) {
            for (j = 0; j < tiles[i].length; j++) {
                tiles[i][j] = new Tile(i*Tile.SIDE, j*Tile.SIDE);
            }
        }

        tiles[1][1].setIsStart(true);
        Tile[] neighbors;

        for (int i = 1; i < tiles.length-1; i++) {
            for (j = 1; j < tiles[i].length-1; j++) {
                neighbors = tiles[i][j].getNeighbors();

                neighbors[Tile.DOWN] = tiles[i][j+1];
                neighbors[Tile.UP] = tiles[i][j-1];
                neighbors[Tile.LEFT] = tiles[i-1][j];
                neighbors[Tile.RIGHT] = tiles[i+1][j];

            }
        }




    }



    public void draw(){
        background(200);

        if(!creationPhase) {
            int goalX = mouseX;
            int goalY = mouseY;

            Tile goal = setGoal(goalX, goalY);

            ArrayList<Tile> path = AStar(tiles[1][1], goal);
            if(path != null) {
                for (int i = 0; i < tiles.length; i++) {
                    for (Tile n : tiles[i]) {
                        n.setIsPartOfPath(path.contains(n));
                    }
                }
            }


        }

        drawTiles();
    }

    private void initializeTiles(Tile goal) {
        int i, j;
        for (i = 0; i < tiles.length; i++) {
            for (j = 0; j < tiles[i].length; j++) {
                if(!tiles[i][j].isBarrier())tiles[i][j].calculateFGH(tiles[0][0], goal);
            }
        }
    }

    private ArrayList<Tile> AStar(Tile start, Tile goal) {
        if(goal == null || start ==null) {
            return null;
        }
        ArrayList<Tile> path = new ArrayList<>();
        ArrayList<Tile> open = new ArrayList<>(), closed = new ArrayList<>();

        for (int i = 0; i < tiles.length; i++) {
            for(Tile t: tiles[i]){
                if(t.isBarrier()){
                    closed.add(t);
                }
            }
        }


        open.add(tiles[1][1]);

        Tile current;
        while(open.size()>0){
            current = lowestTileF(open);
            open.remove(current);
            closed.add(current);

            if(current.isGoal()){
                initPath(start, goal, path);
                return path;
            }

            for(Tile n: current.getNeighbors()){

                if(closed.contains(n) || n ==null){
                    continue;
                }

                double newMovementCost = current.getG() + getDist(current, n);
                if(newMovementCost < n.getG() || !open.contains(n)){
                    n.setG(newMovementCost);
                    n.setH(getDist(n, goal));
                    n.setParent(current);

                    if(!open.contains(n)){
                        open.add(n);
                    }
                }


            }


        }



        return path;
    }

    private void initPath(Tile start, Tile goal, ArrayList<Tile> path) {
        Tile current = goal;
        while(current != null){
            path.add(current);
            current = current.getParent();
        }
    }

    public int getDist(Tile a, Tile b){
        int x = Math.abs(a.getX() - b.getX());
        int y = Math.abs(a.getY() - b.getY());

        return x+y;

    }

    private Tile lowestTileF(ArrayList<Tile> open) {
        Tile min = open.get(0);
        for (int i = 1; i < open.size(); i++) {
            if(min.getF() < open.get(i).getF()){
                min = open.get(i);
            }
        }
        return min;
    }

    private Tile setGoal(int goalX, int goalY) {
        int j;

        for (int i = 0; i < tiles.length; i++) {
            for (j = 0;  j < tiles[i].length; j++) {

                if(tiles[i][j].isGoal()){
                    tiles[i][j].setGoal(false);
                }

                if(tiles[i][j].incorporates(goalX, goalY) && !tiles[i][j].isBarrier()){
                    tiles[i][j].setGoal(true);
                    return tiles[i][j];
                }


            }
        }

        return null;
    }



    private void drawTiles() {
        fill(255);

        for(Tile[] a: tiles){
            for(Tile t: a){
                t.draw(this);
            }
        }


    }

    public void mouseDragged(){
        if(!creationPhase){
            return;
        }
        int x = mouseX;
        int y = mouseY;

        for(Tile[] row: tiles){
            for(Tile t: row){
                if(t.incorporates(x,y)){
                    t.setIsBarrier(true);
                }
            }
        }



    }

    public void keyPressed(){
        if(key=='e'){
            creationPhase = !creationPhase;
        }
    }

    public static void main(String[] args) {
        PApplet.main("Main");
    }
}
