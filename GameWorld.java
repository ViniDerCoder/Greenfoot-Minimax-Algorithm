import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
/**
 * Write a description of class TicTacToeWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class GameWorld extends World
{       
    public static enum ExistingGames {
        TicTacToe,
        ConnectFour
    };
    
    private ExistingGames selectedGame = ExistingGames.TicTacToe;
    public ExistingGames getSelectedGame() { return this.selectedGame; }
    
    private Game game;
    
    public GameWorld()
    {    
        super(800, 600, 1); 
        
        resetWorld();
        
        Greenfoot.start();
    }
    
    private void createNewGame() {
        switch(this.selectedGame) {
            case TicTacToe: game = new TicTacToe(); break;
            case ConnectFour: game = new ConnectFour(); break;
        }
        
    }
    
    public void switchGame(ExistingGames game) {
        this.selectedGame = game;
        this.resetWorld();
    }
    
    public void resetWorld() {
        System.out.println("Reseting World...");
        this.clearWorld();
        
        createNewGame();
        this.addObject(this.game, 1, 1);
        drawTitle(this.selectedGame.toString());
        
        game.generateWorld();
    }
    
    private void drawTitle(String title) {
        GreenfootImage canvas = getBackground();
        canvas.setColor(Color.BLACK);
        canvas.setFont(new Font("Comic Sans MS", true, false, 20));
        canvas.drawString(title, 4, 22);
    }
    
    public void clearWorld() {
        this.getBackground().clear();
        this.setBackground(new GreenfootImage(" 0 ", 100, new Color(0, 0, 0, 0), new Color(0, 0, 0, 0)));
        this.removeObjects(this.getObjects(null));
    }
}
