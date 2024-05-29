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
        ConnectFour;
        
        public ExistingGames nextGame() {
            ExistingGames[] values = ExistingGames.values();
            
            int ordinal = this.ordinal();
            int nextOrdinal = (ordinal + 1) % values.length;
            
            return values[nextOrdinal];
        }
        
        public GreenfootImage gameIcon() {
            return new GreenfootImage("images/GameIcons/" + ExistingGames.values()[this.ordinal()].toString() + ".png");
        }
        
        public GreenfootImage nextGameIcon() {
            return nextGame().gameIcon();
        }
    };
    
    private ExistingGames selectedGame = ExistingGames.TicTacToe;
    public ExistingGames getSelectedGame() { return this.selectedGame; }
    
    private Game game;
    
    public GameWorld()
    {    
        super(800, 600, 1); 
        
        resetWorld();
        
        Greenfoot.start();
        Greenfoot.setSpeed(50);
    }
    
    boolean skipNextClick = false;
    int clickPause = 0;
    public void act() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if(mouse != null) {
            int button = mouse.getButton();
            if(button == 1 && !skipNextClick && clickPause <= 0) {
                skipNextClick = true;
                clickPause = 35;
                if(mouse.getX() > 10 && mouse.getX() < 110 && mouse.getY() > 490 && mouse.getY() < 590) {
                    switchGame(this.selectedGame.nextGame());
                } else {
                    this.game.onLeftClick(mouse.getX(), mouse.getY());
                }
            } else if(button == 1) skipNextClick = false;
            if(clickPause > 0) clickPause--;
        }
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
        drawSwitchGameButton(this.selectedGame.nextGameIcon());
        
        game.generateWorld();
    }
    
    public void repaint() {
        this.resetWorld();
    }
    
    private void drawTitle(String title) {
        GreenfootImage canvas = getBackground();
        canvas.setColor(Color.BLACK);
        canvas.setFont(new Font("Comic Sans MS", true, false, 20));
        canvas.drawString(title, 4, 22);
    }
    
    private void drawSwitchGameButton(GreenfootImage image) {
        GreenfootImage canvas = getBackground();
        canvas.setColor(Color.GRAY);
        canvas.drawRect(5, 485, image.getWidth() + 10, image.getHeight() + 10);
        canvas.drawImage(image, 10, 490);
    }
    
    private void clearWorld() {
        this.getBackground().clear();
        this.setBackground(new GreenfootImage(" 0 ", 100, new Color(0, 0, 0, 0), new Color(0, 0, 0, 0)));
        this.removeObjects(this.getObjects(null));
    }
}
