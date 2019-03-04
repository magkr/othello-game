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

	/**
	 *
	 */
	 public Position decideMove(GameState s){
 		if (!s.legalMoves().isEmpty())
 			return miniMax(s);
 		else
 			return new Position(-1,-1);
 	}

 	private Position miniMax(GameState s) {
		p = s.getPlayerInTurn();

		Set<Position> moves = new HashSet<Position>(s.legalMoves());

 		int v = Integer.MIN_VALUE;
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;

 		Position res = new Position(-1,-1);

 		for(Position p : moves) {
 			GameState state = new GameState(s.getBoard(), s.getPlayerInTurn());
			int m = minValue(result(state, p),alpha,beta);
			if(m > v) {
				res = p;
				v = m;
			}

 		}

 		return res;
 	}

 	private int minValue(GameState s, int alpha, int beta) {
 		if(s.isFinished()) return utility(s);

		Set<Position> moves = new HashSet<Position>(s.legalMoves());

 		if(moves.isEmpty()) {
 			s.changePlayer();
 			return maxValue(s,alpha,beta);
 		}

		int v = Integer.MAX_VALUE;

		for(Position p : moves) {
 			v = Math.min(v, maxValue(result(s, p),alpha,beta));
			if(v <= alpha) return v;
			beta = Math.min(beta, v);
 		}

 		return v;
 	}

 	private int maxValue(GameState s, int alpha, int beta) {
 		if(s.isFinished()) return utility(s);

		Set<Position> moves = new HashSet<Position>(s.legalMoves());

 		if(moves.isEmpty()) {
 			s.changePlayer();
 			return minValue(s,alpha,beta);
 		}

 		int v = Integer.MIN_VALUE;

 		for(Position p : moves) {
 			v = Math.max(v, minValue(result(s, p),alpha,beta));
			if(v >= beta) return v;
			alpha = Math.max(alpha, v);
 		}

 		return v;
 	}

 	private GameState result(GameState s, Position a) {
 		s.insertToken(a);
 		return s;
 	}

 	private int utility(GameState s) {
 		int[] tokens = s.countTokens();
 		int playerTokens = (p == 1 ? tokens[0] : tokens[1]);
		return playerTokens;
 	}

}
