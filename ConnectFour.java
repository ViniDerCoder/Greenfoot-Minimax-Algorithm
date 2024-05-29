import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
import java.util.Random;

/**
 * Write a description of class ConnectFour here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ConnectFour extends Game
{
    public static enum PlayerColor {
        RED, YELLOW
    }
    
    public static final GreenfootImage OUTLINE = new GreenfootImage("images/ConnectFourGameBoard.png");
    public static final GreenfootImage PLAYER_RED = new GreenfootImage("images/ConnectFourRed.png");
    public static final GreenfootImage PLAYER_YELLOW = new GreenfootImage("images/ConnectFourYellow.png");
    
    private PlayerColor currentPlayer = new Random().nextBoolean() ? PlayerColor.YELLOW : PlayerColor.RED;
    public static PlayerColor computerPlayer = PlayerColor.YELLOW;
    public static PlayerColor humanPlayer = ConnectFour.computerPlayer == PlayerColor.YELLOW ? PlayerColor.RED : PlayerColor.YELLOW;
    private boolean gameFinished = false;
    
    public static class ConnectFourField extends Game.GameField {
        PlayerColor[][] field;
        
        static final int FIELD_HEIGHT = 6;
        static final int FIELD_WIDTH = 7;
        
        public ConnectFourField(PlayerColor[][] field) {
            this.field = field;
        };
        
        public ConnectFourField() {
            this.field = new PlayerColor[FIELD_WIDTH][FIELD_HEIGHT];
        };
    
        public ConnectFourField makeMove(int move, boolean computer) {
            return this.makeMove(move, computer ? ConnectFour.computerPlayer : ConnectFour.humanPlayer);
        }
        
        public int getLowestLine(int column) {
           for(int i = 0; i < FIELD_HEIGHT; i++) {
                if(this.field[column][i] == null) {
                    return i;
                }
           }
           return -1;
        }
            
        public ConnectFourField makeMove(int move, PlayerColor player) {
            if(this.field[move][FIELD_HEIGHT - 1] != null) throw new Error("Field is already occupied!");
            
            PlayerColor[][] newField = new PlayerColor[FIELD_WIDTH][];
            for(int i = 0; i < FIELD_WIDTH; i++) newField[i] = this.field[i].clone();
            
            newField[move][this.getLowestLine(move)] = player;
            
            return new ConnectFourField(newField);
        }
    
        public Game.GameState getGameState() {
            for(int column = 0; column < FIELD_WIDTH; column++) {
                for(int line = 0; line < FIELD_HEIGHT; line++) {
                    
                    PlayerColor cField = this.field[column][line];
                    if(cField == null) continue;
                    if(( 
                        /*Check for bounds*/        column+3 <= FIELD_WIDTH - 1 
                        /*Check horizontal line*/   && cField == this.field[column+1][line] && cField == this.field[column+2][line] && cField == this.field[column+3][line]
                    )||(
                        /*Check for bounds*/        line+3 <= FIELD_HEIGHT - 1 
                        /*Check vertical line*/     && cField == this.field[column][line+1] && cField == this.field[column][line+2] && cField == this.field[column][line+3]
                    )||(
                        /*Check for bounds*/        line+3 <= FIELD_HEIGHT - 1 && column+3 <= FIELD_WIDTH - 1
                        /*Check 1. diagonal line*/  && cField == this.field[column+1][line+1] && cField == this.field[column+2][line+2] && cField == this.field[column+3][line+3]
                    )||(
                        /*Check for bounds*/        line-3 >= 0 && column+3 <= FIELD_WIDTH - 1 
                        /*Check 2. diagonal line*/  && cField == this.field[column+1][line-1] && cField == this.field[column+2][line-2] && cField == this.field[column+3][line-3]
                    )) return cField == ConnectFour.humanPlayer ? Game.GameState.Lose : Game.GameState.Win;
                }
            }
            if(this.getPossibleMoves().length < 1) return Game.GameState.Tie;
            else return Game.GameState.Unfinished;
        }
        
        public int[] getPossibleMoves() {
            ArrayList<Integer> emptyFieldIndexes = new ArrayList<>();
            for (int i = 0; i < FIELD_WIDTH; i++) {
                if (this.field[i][FIELD_HEIGHT - 1] == null) {
                    emptyFieldIndexes.add(i);
                }
            }
            int[] newArray = new int[emptyFieldIndexes.size()];
            for (int i = 0; i < emptyFieldIndexes.size(); i++) {
                newArray[i] = emptyFieldIndexes.get(i);
            }
            return newArray;
        }
        
        public String toString() {
            String str = "";
            
            for(PlayerColor[] i : this.field) {
                for(PlayerColor j : i) {
                    if(j == PlayerColor.RED) str = str + "R";
                    else if(j == PlayerColor.YELLOW) str = str + "Y";
                    else str = str + "-";
                }
            }
            
            return str;
        }
    }
    
    private ConnectFourField gameField = new ConnectFourField();
    private Minimax minimax = new Minimax(this, 8);
    
    int computerSleepTimer = 50;
    public void act() {
        if(this.currentPlayer == ConnectFour.computerPlayer && computerSleepTimer <= 0) computerMove();
        else if(this.currentPlayer != ConnectFour.computerPlayer) computerSleepTimer = 50;
        
        computerSleepTimer--;
    }
    
    public void onLeftClick(int mouseX, int mouseY) {
        if(this.gameFinished) getWorld().repaint();
        double column = (mouseX - 174 + 6.0) / (PLAYER_RED.getWidth()  +  12.0);
        if(column > 0) {
            int nearestColumn = (int)column;
            playerMove(nearestColumn);
        } 
    }
    
    public void playerMove(int column) {
        if(this.currentPlayer == ConnectFour.computerPlayer || this.gameFinished) return;
        int line = this.gameField.getLowestLine(column);
        if(line < 0) return;
        
        this.currentPlayer = ConnectFour.computerPlayer;
        this.gameField = this.gameField.makeMove(column, ConnectFour.humanPlayer);
        this.markField(ConnectFour.humanPlayer, column, line);
        if(this.gameField.getGameState() != Game.GameState.Unfinished) finishGame();
    }
    
    public void computerMove() {
        if(this.gameFinished || this.currentPlayer != ConnectFour.computerPlayer) return;
        int move = this.minimax.calculateBestMove();
        int line = this.gameField.getLowestLine(move);
        if(line < 0) return;
        
        this.currentPlayer = ConnectFour.humanPlayer;
        this.gameField = this.gameField.makeMove(move, ConnectFour.computerPlayer);
        this.markField(ConnectFour.computerPlayer, move, line);
        if(this.gameField.getGameState() != Game.GameState.Unfinished) finishGame();
    }
    
    private void finishGame() {
        this.gameFinished = true;
        Game.GameState gameState = this.gameField.getGameState();
        
        GreenfootImage canvas = getWorld().getBackground();
        canvas.setColor(Color.RED);
        if(gameState == Game.GameState.Tie) canvas.drawString("Tie!", 30, 200);
        if(gameState == Game.GameState.Win) canvas.drawString("You lost!", 15, 200);
        if(gameState == Game.GameState.Lose) canvas.drawString("You won!", 16, 200);
        
        Greenfoot.delay(10);
    }
    
    public GameField getCurrentGameField() {
        return this.gameField;
    }
    
    public void generateWorld() {
        GreenfootImage canvas = getWorld().getBackground();
        canvas.drawImage(OUTLINE, ((canvas.getWidth() - OUTLINE.getWidth()) / 2) + 50, ((canvas.getHeight() - OUTLINE.getHeight()) / 2));
        
        this.setImage("images/nothing.png");
        
        canvas.setColor(Color.BLACK);
        canvas.drawString("Minimax: ", 5, 80);
        canvas.setColor(this.computerPlayer == PlayerColor.RED ? Color.RED : Color.YELLOW);
        canvas.drawString("#", 100, 80);
        canvas.setColor(Color.BLACK);
        canvas.drawString("  Player: ", 5, 100);
        canvas.setColor(this.humanPlayer == PlayerColor.RED ? Color.RED : Color.YELLOW);
        canvas.drawString("#", 100, 100);
    }
    
    private void markField(PlayerColor player, int column, int line) {
        int x = column            *   (PLAYER_RED.getWidth()  +  12)  +  174;
        int y = Math.abs(line-5)  *   (PLAYER_RED.getHeight() +  12)  +  63;
        
        GreenfootImage canvas = getWorld().getBackground();
        
        canvas.drawImage(player == PlayerColor.RED ? PLAYER_RED : PLAYER_YELLOW, x, y);
    }
}
