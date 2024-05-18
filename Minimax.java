/**
 * Write a description of class Minimax here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Minimax {
    Game game;   
    int maxDepth;
    
    public Minimax(Game game, int maxDepth) {
        this.maxDepth = maxDepth;
        this.game = game;
    }
    
    private int getScore(Game.GameState gameState) {
        switch(gameState) {
            case Win: return 10;
            case Lose: return -10;
            default: return 0;
        }
    }
    
    public int calculateBestMove() {
        int[] possibleMoves = this.game.getCurrentGameField().getPossibleMoves();
        
        int bestScore = Integer.MIN_VALUE;
        int bestMove = possibleMoves[0];
        
        for(int move : possibleMoves) {
            int score = minimax(this.game.getCurrentGameField().makeMove(move, true), false, 0);
            if(score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }
        return bestMove;
    }
    
    private int minimax(Game.GameField field, boolean maximizing, int depth) {
        Game.GameState gameState = field.getGameState();
        if(gameState != Game.GameState.Unfinished || depth > this.maxDepth) return getScore(gameState);
        
        int[] possibleMoves = field.getPossibleMoves();
        
        if(maximizing) {
            int bestScore = Integer.MIN_VALUE;
            
            for(int move : possibleMoves) {
                int score = minimax(field.makeMove(move, true), false, depth+1);
                bestScore = Math.max(bestScore, score);
            }
            
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            
            for(int move : possibleMoves) {
                int score = minimax(field.makeMove(move, false), true, depth+1);
                
                bestScore = Math.min(bestScore, score);
            }
            
            return bestScore;
        }
    }
}
