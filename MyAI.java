import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

/**
 *
 * @author
 * @version
 */
public class MyAI implements IOthelloAI{

	private int p;
	private static int cutoff = 10;
	private int pMoves;
	private int[][] positions;

	/**
	 *
	 */
	 public Position decideMove(GameState s){
		createPositions(s);
 		if (!s.legalMoves().isEmpty())
 			return miniMax(s);
 		else
 			return new Position(-1,-1);
 	}

 	private Position miniMax(GameState s) {
		p = s.getPlayerInTurn();

		Set<Position> moves = new HashSet<Position>(s.legalMoves());
		pMoves = moves.size();

 		int v = Integer.MIN_VALUE;
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;
 		Position res = new Position(-1,-1);

 		for(Position p : moves) {
			GameState state = new GameState(s.getBoard(), s.getPlayerInTurn());
			int m = minValue(result(state, p),alpha,beta, 0 + (moves.size() < cutoff ? moves.size() : 6)); //REVIEW dynamic cutoff depending on moves avaliable
			if(m > v) {
				res = p;
				v = m;
			}

 		}

 		return res;
 	}


 	private int minValue(GameState s, int alpha, int beta, int depth) {
 		if(s.isFinished() || depth == cutoff) {
			return utility(s);
		}

		Set<Position> moves = new HashSet<Position>(s.legalMoves());
		pMoves = moves.size();

 		if(moves.isEmpty()) {
 			s.changePlayer();
 			return maxValue(s,alpha,beta,depth + 1);
 		}

		int v = Integer.MAX_VALUE;
		int counter = 0;
		GameState state;
		for(Position p : moves) {
			if(counter != moves.size() - 1) state = new GameState(s.getBoard(), s.getPlayerInTurn());
			else state = s;
			counter++;
 			v = Math.min(v, maxValue(result(state, p),alpha,beta,depth + 1));
			if(v <= alpha) return v;
			beta = Math.min(beta, v);
	 	}
 		return v;
 	}

 	private int maxValue(GameState s, int alpha, int beta, int depth) {
 		if(s.isFinished() || depth == cutoff) {
			return utility(s);
		}

		Set<Position> moves = new HashSet<Position>(s.legalMoves());
		pMoves = moves.size();
 		if(moves.isEmpty()) {
 			s.changePlayer();
 			return minValue(s,alpha,beta,depth + 1);
 		}

 		int v = Integer.MIN_VALUE;
		int counter = 0;
		GameState state;
		for(Position p : moves) {
			if(counter != moves.size() - 1) state = new GameState(s.getBoard(), s.getPlayerInTurn());
			else state = s;
			counter++;
 			v = Math.max(v, minValue(result(state, p),alpha,beta,depth + 1));
			if(v >= beta) return v;
			alpha = Math.max(alpha, v);
 		}
 		return v;
 	}

 	private GameState result(GameState s, Position a) {
 		s.insertToken(a);
 		return s;
 	}

	private int difference(GameState s) {
		int[] tokens = s.countTokens();
 		int playerTokens = (p == 1 ? tokens[0] : tokens[1]);
		int enemyTokens = (p == 1 ? tokens[1] : tokens[0]);
		return 100 * (playerTokens - enemyTokens) / (playerTokens + enemyTokens);
	}

	private int moves(GameState s) {
		int enemyMoves = s.legalMoves().size();
		if (pMoves + enemyMoves != 0) return 100 * (pMoves - enemyMoves) / (pMoves + enemyMoves);
		else return 0;
	}

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


		private void createPositions(GameState s) {
			int l = s.getBoard.length;
			for(int i = 0; i < l; i++) {
				for(int j = 0; j < l; j++) {
					positions[i][j] = 10;
				}
			}
		}

 	private int utility(GameState s) {
		return (difference(s)+corners(s)+moves(s))/3;
 	}

}
