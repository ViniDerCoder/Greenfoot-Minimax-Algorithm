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
    
        public GameField makeMove(int move, boolean computer) {
            return this.makeMove(move, computer ? ConnectFour.computerPlayer : ConnectFour.humanPlayer);
        }
            
        public GameField makeMove(int move, PlayerColor player) {
            if(this.field[move][FIELD_HEIGHT - 1] != null) throw new Error("Field is already occupied!");
            
            PlayerColor[][] newField = new PlayerColor[FIELD_WIDTH][];
            for(int i = 0; i < FIELD_WIDTH; i++) newField[i] = this.field[i].clone();
            
            for(int i = 0; i < FIELD_HEIGHT; i++) {
                if(newField[move][i] == null) {
                    newField[move][i] = player;
                    break;
                }
            }
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
                        /*Check for bounds*/        line-3 >= 0 - 1 && column+3 <= FIELD_WIDTH - 1 
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
    }
    
    private ConnectFourField gameField = new ConnectFourField();
    
    public void onLeftClick(int mouseX, int mouseY) {
        throw new Error("Method 'onLeftClick' not implemented!");
    }
    
    public GameField getCurrentGameField() {
        return this.gameField;
    }
    
    public void generateWorld() {
        throw new Error("Method 'generateWorld' not implemented!");
    }
}
