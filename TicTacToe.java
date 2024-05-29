import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
import java.util.Random;
/**
 * Write a description of class TicTacToe here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class TicTacToe extends Game
{
    public static final GreenfootImage OUTLINE = new GreenfootImage("images/TicTacToeOutlines.png");
    public static final GreenfootImage PLAYER_X = new GreenfootImage("images/TicTacToeX.png");
    public static final GreenfootImage PLAYER_O = new GreenfootImage("images/TicTacToeO.png");
    
    private static final int borderThickness = 18;
    
    public static enum PlayerSymbol {
        X,
        O
    }
    
    private int[][] fieldImageCoordinates;
    
    private class TicTacToeField extends Game.GameField {
        public PlayerSymbol[] field;
        
        public TicTacToeField(PlayerSymbol[] field) {
            this.field = field;
        };
        
        public TicTacToeField() {
            this.field = new PlayerSymbol[9];
        }
        
        public TicTacToeField makeMove(int move, boolean computer) {
            return this.makeMove(move, computer ? TicTacToe.computerPlayer : TicTacToe.humanPlayer);
        }
        
        public TicTacToeField makeMove(int field, PlayerSymbol player) {
            if(this.field[field] != null) throw new Error("Field is already occupied!");
            PlayerSymbol[] newField = this.field.clone();
            newField[field] = player;
            return new TicTacToeField(newField);
        }
        
        public int[] getPossibleMoves() {
            ArrayList<Integer> emptyFieldIndexes = new ArrayList<>();
            for (int i = 0; i < this.field.length; i++) {
                if (this.field[i] == null) {
                    emptyFieldIndexes.add(i);
                }
            }
            int[] newArray = new int[emptyFieldIndexes.size()];
            for (int i = 0; i < emptyFieldIndexes.size(); i++) {
                newArray[i] = emptyFieldIndexes.get(i);
            }
            return newArray;
        }
        
        public Game.GameState getGameState() {
            int[][] winningConditions = {
                {0, 1, 2},
                {3, 4, 5},
                {6, 7, 8},
                
                {0, 3, 6},
                {1, 4, 7},
                {2, 5, 8},
                
                {0, 4, 8},
                {2, 4, 6}
            };
            
            for(int[] condition : winningConditions) {
                if(this.field[condition[0]] == this.field[condition[1]] && this.field[condition[1]] == this.field[condition[2]] && this.field[condition[0]] != null) return this.field[condition[0]] == TicTacToe.computerPlayer ? Game.GameState.Win : Game.GameState.Lose;
            }
            
            if(this.getPossibleMoves().length < 1) return Game.GameState.Tie;
            else return Game.GameState.Unfinished;
        }
        
        public String toString() {
            String str = "";
            
            for(PlayerSymbol i : this.field) {
                if(i == PlayerSymbol.X) str = str + "X";
                else if(i == PlayerSymbol.O) str = str + "O";
                else str = str + "-";
            }
            
            return str;
        }
    };
    
    private TicTacToeField gameField = new TicTacToeField();
    private PlayerSymbol currentPlayer = new Random().nextBoolean() ? PlayerSymbol.X : PlayerSymbol.O;
    public static final PlayerSymbol computerPlayer = PlayerSymbol.X;
    public static final PlayerSymbol humanPlayer = TicTacToe.computerPlayer == PlayerSymbol.X ? PlayerSymbol.O : PlayerSymbol.X;
    private boolean gameFinished = false;
    private Minimax minimax = new Minimax(this, 10);
    
    public void act() {
        if(currentPlayer == computerPlayer) computerMove();
    }
    
    public void onLeftClick(int mouseX, int mouseY) {
        if(this.gameFinished) getWorld().repaint();
        else playerMove(calculateFieldOfClick(mouseX, mouseY)); 
    }
    
    public void generateWorld() {
        GreenfootImage canvas = getWorld().getBackground();
        canvas.drawImage(OUTLINE, ((canvas.getWidth() - OUTLINE.getWidth()) / 2), ((canvas.getHeight() - OUTLINE.getHeight()) / 2));

        this.setImage("images/nothing.png");
        
        canvas.setColor(Color.BLACK);
        canvas.drawString("Minimax: " + computerPlayer.toString(), 5, 80);
        canvas.drawString("  Player: " + humanPlayer.toString(), 5, 100);
        
        this.fieldImageCoordinates = calculatePlayerImagePositions();
    }
    
    private int[][] calculatePlayerImagePositions() {
        int height = getWorld().getBackground().getHeight();
        int width = getWorld().getBackground().getWidth();
        int imgHeight = PLAYER_X.getHeight();
        int imgWidth = PLAYER_X.getWidth();
        
        int widthMiddle = (width / 2 - imgWidth / 2);
        int heightMiddle = (height / 2 - imgHeight / 2);
        
        int[][] coordinates = {
            { widthMiddle - imgWidth - borderThickness, heightMiddle - imgWidth - borderThickness },     //top-left
            { widthMiddle, heightMiddle - imgWidth - borderThickness },                                  //top-middle
            { widthMiddle + imgWidth + borderThickness, heightMiddle - imgWidth - borderThickness },     //top-right
            { widthMiddle - imgWidth - borderThickness, heightMiddle },                                  //middle-left
            { widthMiddle, heightMiddle },                                                               //middle-middle
            { widthMiddle + imgWidth + borderThickness, heightMiddle },                                  //middle-right
            { widthMiddle - imgWidth - borderThickness, heightMiddle + imgWidth + borderThickness },     //bottom-left
            { widthMiddle, heightMiddle + imgWidth + borderThickness },                                  //bottom-middle
            { widthMiddle + imgWidth + borderThickness, heightMiddle + imgWidth + borderThickness },     //bottom-right
        };
        return coordinates;
    }
    
    private int calculateFieldOfClick(int mouseX, int mouseY) {
        int row;
        int line;
        
        int widthMiddle = (getWorld().getBackground().getWidth() / 2);
        int heightMiddle = (getWorld().getBackground().getHeight() / 2);
        
        int imgHeight = PLAYER_X.getHeight();
        int imgWidth = PLAYER_X.getWidth();
        
        if(mouseX < (widthMiddle - imgWidth / 2 - borderThickness / 2)) row = 0;
        else if(mouseX > (widthMiddle + imgWidth / 2 + borderThickness / 2)) row = 2;
        else row = 1;
        
        if(mouseY < (heightMiddle - imgHeight / 2 - borderThickness / 2)) line = 0;
        else if(mouseY > (heightMiddle + imgHeight / 2 + borderThickness / 2)) line = 2;
        else line = 1;
        
        return line * 3 + row;
    }
    
    public boolean playerMove(int field) {
        if(this.currentPlayer == TicTacToe.computerPlayer || this.gameFinished) return false;
        this.markField(field);
        return true;
    }
    
    public boolean computerMove() {
        if(this.gameFinished || this.currentPlayer != TicTacToe.computerPlayer) return false;
        int move = this.minimax.calculateBestMove();
        
        this.markField(move);
        return true;
    }
    
    private void markField(int field) {
        GreenfootImage canvas = getWorld().getBackground();
        if(this.gameField.field[field] != null) return;
        
        this.gameField = this.gameField.makeMove(field, this.currentPlayer);
        if(this.currentPlayer == PlayerSymbol.X) {
            this.currentPlayer = PlayerSymbol.O;
            canvas.drawImage(PLAYER_X, fieldImageCoordinates[field][0], fieldImageCoordinates[field][1]);
        }
        else {
            this.currentPlayer = PlayerSymbol.X;
            canvas.drawImage(PLAYER_O, fieldImageCoordinates[field][0], fieldImageCoordinates[field][1]);
        }
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
    
    public Game.GameField getCurrentGameField() {
        return this.gameField;
    }
}
