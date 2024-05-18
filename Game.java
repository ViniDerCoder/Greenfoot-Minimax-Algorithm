import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Game here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Game extends Actor
{
    //GameState from the computer's perspective
    public static enum GameState {
        Win,
        Lose,
        Tie,
        Unfinished
    }
    
    public static class GameField {
    
        public GameField makeMove(int move, boolean computer) {
            throw new Error("Method 'GameField.makeMove' not implemented!");
        }
    
        public GameState getGameState() {
            throw new Error("Method 'GameField.getGameState' not implemented!");
        }
        
        public int[] getPossibleMoves() {
            throw new Error("Method 'GameField.getPossibleMoves' not implemented!");
        }
    }
    
    public GameField getCurrentGameField() {
        throw new Error("Method 'getCurrentGameField' not implemented!");
    }
    
    public void generateWorld() {
        throw new Error("Method 'generateWorld' not implemented!");
    }
}
