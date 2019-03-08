import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

/**
 *
 * @author Liv & Magnus
 * @version
 */
public class MyAI implements IOthelloAI{

	//Holds current player
	private int p;
	// Max depth for search in game tree
	private static int cutoff = 6;
	// Holds number of players move
	private int pMoves;

	/**
	 *  Inherited from IOthelloAI, calls minimax
	 */
	 public Position decideMove(GameState s){
 		if (!s.legalMoves().isEmpty())
 			return miniMax(s);
 		else
 			return new Position(-1,-1);
 	}

	/**
	 *	Start the minimax search based on the players possible moves
	 */
 	private Position miniMax(GameState s) {
		p = s.getPlayerInTurn();

		Set<Position> moves = new HashSet<Position>(s.legalMoves());
		pMoves = moves.size();

 		int v = Integer.MIN_VALUE;
		// Values for alpha beta pruning
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;

 		Position res = new Position(-1,-1);

		//start depth dtermined by number of moves
		int startDepth = (pMoves < cutoff ? 0 : (pMoves >= cutoff * 2 ? cutoff - 2 : pMoves - cutoff));

 		for(Position p : moves) {
			// New gamestate for each branch
			GameState state = new GameState(s.getBoard(), s.getPlayerInTurn());
			//Start the by calling minValue
			int m = minValue(result(state, p),alpha,beta, startDepth);
			if(m > v) {
				res = p;
				v = m;
			}

 		}
 		return res;
 	}

	/**
	 *	minValue search
	 */
 	private int minValue(GameState s, int alpha, int beta, int depth) {
 		if(s.isFinished() || depth == cutoff) { //Checks if finished or time to cutoff
			return utility(s);
		}

		Set<Position> moves = new HashSet<Position>(s.legalMoves());
		pMoves = moves.size();

 		if(moves.isEmpty()) { //If no moves, changes player pov
 			s.changePlayer();
 			return maxValue(s,alpha,beta,depth + 1);
 		}

		int v = Integer.MAX_VALUE;
		int counter = 0; //Counter for finding last move/branch, to reuse gamestate
		GameState state;
		for(Position p : moves) {
			if(counter != moves.size() - 1) state = new GameState(s.getBoard(), s.getPlayerInTurn());
			else state = s;
			counter++;
			//calls maxValue and increases depth
 			v = Math.min(v, maxValue(result(state, p),alpha,beta,depth + 1));
			if(v <= alpha) return v; //pruning
			beta = Math.min(beta, v);
	 	}
 		return v;
 	}

	/**
	 *	maxValue search
	 */
 	private int maxValue(GameState s, int alpha, int beta, int depth) {
 		if(s.isFinished() || depth == cutoff) { //Checks if finished or time to cutoff
			return utility(s);
		}

		Set<Position> moves = new HashSet<Position>(s.legalMoves());
		pMoves = moves.size();

 		if(moves.isEmpty()) { //If no moves, changes player pov
 			s.changePlayer();
 			return minValue(s,alpha,beta,depth + 1);
 		}

 		int v = Integer.MIN_VALUE;
		int counter = 0; //Counter for finding last move/branch, to reuse gamestate
		GameState state;
		for(Position p : moves) {
			if(counter != moves.size() - 1) state = new GameState(s.getBoard(), s.getPlayerInTurn());
			else state = s;
			counter++;
			//calls minValue and increases depth
 			v = Math.max(v, minValue(result(state, p),alpha,beta,depth + 1));
			if(v >= beta) return v; //pruning
			alpha = Math.max(alpha, v);
 		}
 		return v;
 	}

	/**
	 *	Inserts token in gamestate and returns it
	 */
 	private GameState result(GameState s, Position a) {
 		s.insertToken(a);
 		return s;
 	}

	/**
	 *	Calculates difference between the players tokens
	 */
	private int difference(GameState s) {
		int[] tokens = s.countTokens();
 		int playerTokens = (p == 1 ? tokens[0] : tokens[1]);
		int enemyTokens = (p == 1 ? tokens[1] : tokens[0]);
		return 100 * (playerTokens - enemyTokens) / (playerTokens + enemyTokens);
	}

	/**
	 *	Calculates difference between the players moves
	 */
	private int moves(GameState s) {
		int enemyMoves = s.legalMoves().size();
		if (pMoves + enemyMoves != 0) return 100 * (pMoves - enemyMoves) / (pMoves + enemyMoves);
		else return 0;
	}

	/**
	 *	Calculates difference between the players tokens in corners
	 */
	private int corners(GameState s) {
		int[][] board = s.getBoard();
		int e = (p == 1 ? 2 : 1);
		int player = 0;
		int enemy = 0;

		if(board[0][0] == p) player++;
		else if(board[0][0] == e) enemy++;

		if(board[board.length-1][0] == p) player++;
		else if(board[board.length-1][0] == e) enemy++;

		if(board[0][board.length-1] == p) player++;
		else if(board[0][board.length-1] == e) enemy++;

		if(board[board.length-1][board.length-1] == p) player++;
		else if(board[board.length-1][board.length-1] == e) enemy++;

		if((player - enemy) != 0) return 100 * (player + enemy) / (player - enemy);
		else return 0;
	}

	/**
	 *	Calculates utility value based on number of tokens, tokens in corners and
	 * 	difference in moves.
	 */
 	private int utility(GameState s) {
		int dif = difference(s);
		if(dif == 100) return dif * 10; //If player holds all tokens return 1000
		int corners = corners(s);
		int moves = moves(s);
		if(moves == 100) return dif * 4 + corners * 2 + moves * 4; //If enemy has no moves, moves is weighted higher
		return dif * 6 + corners * 3 + moves * 1; //difference in tokens weighs 60%, tokens in corners 30%, and difference in moves 10%
 	}

}
