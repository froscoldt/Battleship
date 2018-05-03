package g11;

import battleship.interfaces.BattleshipsPlayer;
import battleship.interfaces.Fleet;
import battleship.interfaces.Position;
import battleship.interfaces.Board;
import battleship.interfaces.Ship;
import java.util.ArrayList;
import java.util.Random;

public class RandomPlayer implements BattleshipsPlayer {
    
    private final static Random rnd = new Random();
    private int sizeX;
    private int sizeY;
    private Board myBoard;
    private boolean[][] coordinatesHit = new boolean[10][10];
    ArrayList<int[]> coordinates = new ArrayList();
    ArrayList<int[]> coordinates2 = new ArrayList();
    ArrayList<ArrayList<int[]>> AllPossiblePaths = new ArrayList<>();
    private boolean checkIfHit;
    private int checkAmountOfShips;
    private int x, y, sum;
    private int[] xy;
    boolean[][] myShips = null;
    
    public RandomPlayer() {
    }
    
    @Override
    public void placeShips(Fleet fleet, Board board) {
        myBoard = board;
        sizeX = board.sizeX();
        sizeY = board.sizeY();
        myShips = new boolean[sizeX][sizeY];
        for (int i = 0; i < fleet.getNumberOfShips(); ++i) {
            Ship s = fleet.getShip(i);
            Position pos;
            boolean vertical;
            
            do {
                vertical = rnd.nextBoolean();
                if (vertical) {
                    int xCoordinate = rnd.nextInt(sizeX);
                    int yCoordinate = rnd.nextInt(sizeY - (s.size() - 1));
                    pos = new Position(xCoordinate, yCoordinate);
                } else {
                    int xCoordinate = rnd.nextInt(sizeX - (s.size() - 1));
                    int yCoordinate = rnd.nextInt(sizeY);
                    pos = new Position(xCoordinate, yCoordinate);
                }
            } while (collision(pos, s, vertical)); // skal se dette i forhold vores array

            for (int j = 0; j < s.size(); j++) {
                if (vertical) {
                    myShips[pos.x][pos.y + j] = true;
                } else {
                    myShips[pos.x + j][pos.y] = true;
                }
            }
            board.placeShip(pos, s, vertical);
        }
    }
    
    @Override
    public void incoming(Position pos) {
        //Do nothing
    }
    
    @Override
    public Position getFireCoordinates(Fleet enemyShips) {
        checkAmountOfShips = enemyShips.getNumberOfShips();
        if (checkIfHit == true) {
            return huntShip(enemyShips);
        }
        return search(enemyShips);
    }
    
    @Override
    public void hitFeedBack(boolean hit, Fleet enemyShips) {
        
        if (hit == true && AllPossiblePaths.isEmpty()) {
            checkIfHit = true;
            for (int i = 0; i < 4; i++) {
                AllPossiblePaths.add(new ArrayList());
            }
            
            for (int i = 1; i < 6; i++) {
                int[] makeCoordinate = {this.x + i, this.y};
                AllPossiblePaths.get(0).add(makeCoordinate);
            }
            for (int i = -1; i > -6; i--) {
                int[] makeCoordinate = {this.x + i, this.y};
                AllPossiblePaths.get(1).add(makeCoordinate);
            }
            for (int i = 1; i < 6; i++) {
                int[] makeCoordinate = {this.x, this.y + i};
                AllPossiblePaths.get(2).add(makeCoordinate);
            }
            for (int i = -1; i > -6; i--) {
                int[] makeCoordinate = {this.x, this.y + i};
                AllPossiblePaths.get(3).add(makeCoordinate);
            }
        }
        
        if (hit == false && checkIfHit == true && AllPossiblePaths.size() > 0) {
            AllPossiblePaths.remove(0);
        }
        
        if (checkAmountOfShips != enemyShips.getNumberOfShips()) {
            checkIfHit = false;
            AllPossiblePaths.clear();
        }
        
    }
    
    private Position search(Fleet enemyShips) {
        this.x = rnd.nextInt(10);
        this.y = rnd.nextInt(10);
        if (coordinates2.isEmpty()) {
            for (int i = 0; i < 10; i++) {
                for (int j = 0 + i % 2; j < 10; j += 2) {
                    int[] xyt = {i,j};
                    coordinates2.add(xyt);
                }
            }
        }

        int num = rnd.nextInt(coordinates2.size());
        int[] fireCoordinate = coordinates2.get(num);
        coordinates2.remove(num);
        this.x = fireCoordinate[0];
        this.y = fireCoordinate[1];
        return new Position(fireCoordinate[0], fireCoordinate[1]);
        /*
        
        do {
            this.x = rnd.nextInt(10);
            this.y = rnd.nextInt(10);
            this.sum = x + y;
        } while ((sum % 2 == 1 || coordinatesHit[x][y] == true));
        coordinatesHit[x][y] = true;
         */
        // return new Position(x, y);
        
    }
    
    private Position huntShip(Fleet enemyShip) {
        if (AllPossiblePaths.get(0).isEmpty()) {
            AllPossiblePaths.remove(0);
        }
        int[] checkCoordinate = AllPossiblePaths.get(0).get(0);
        AllPossiblePaths.get(0).remove(0);
        return new Position(checkCoordinate[0], checkCoordinate[1]);
        
    }
    
    @Override
    public void startMatch(int rounds, Fleet ships, int sizeX, int sizeY) {
        
    }
    
    @Override
    public void startRound(int round) {
        
    }
    
    @Override
    public void endRound(int round, int points, int enemyPoints) {
        //Do nothing
    }
    
    @Override
    public void endMatch(int won, int lost, int draw) {
        //Do nothing
    }
    
    private boolean collision(Position pos, Ship s, boolean vertical) {
        for (int j = 0; j < s.size(); j++) {
            if (vertical && myShips[pos.x][pos.y + j] == true) {
                return true;
            }
            if (!vertical && myShips[pos.x + j][pos.y] == true) {
                return true;
            }
        }
        return false;
    }
    
}
