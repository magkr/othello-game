import java.util.ArrayList;

/**
 *
 * @author
 * @version
 */
public class MyAI implements IOthelloAI{

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
 		//System.out.println("max: " + s.getPlayerInTurn() + " player turn");
 		ArrayList<Position> moves = s.legalMoves();
 		int v = Integer.MIN_VALUE;
 		Position res = new Position(-1,-1);
 		for(Position p : moves) {
 			//System.out.println(p);
 			GameState state = new GameState(s.getBoard(), s.getPlayerInTurn());
 				int m = minValue(result(state, p));
 				//System.out.println(m);
 				if(m > v) {
 					res = p;
 					v = m;
 				}
 		}
 		return res;
 	}

 	private int minValue(GameState s) {
 		//System.out.println("min: " + s.getPlayerInTurn() + " player turn");
 		if(s.isFinished()) return utility(s);
 		ArrayList<Position> moves = s.legalMoves();
 		if(moves.isEmpty()) {
 			s.changePlayer();
 			return maxValue(s);
 		}
 		int v = Integer.MAX_VALUE;
 		for(Position p : moves) {
 			v = Math.min(v, maxValue(result(s, p)));
 		}
 		return v;
 	}

 	private int maxValue(GameState s) {
 		//System.out.println("max: " + s.getPlayerInTurn() + " player turn");
 		if(s.isFinished()) return utility(s);
 		ArrayList<Position> moves = s.legalMoves();
 		if(moves.isEmpty()) {
 			s.changePlayer();
 			return minValue(s);
 		}
 		int v = Integer.MIN_VALUE;
 		for(Position p : moves) {
 			v = Math.max(v, minValue(result(s, p)));
 		}
 		return v;
 	}

 	private GameState result(GameState s, Position a) {
 		s.insertToken(a);
 		return s;
 	}

 	private int utility(GameState s) {
 		int[] tokens = s.countTokens();
 		int p = s.getPlayerInTurn();
 		int pt1 =  (p == 1 ? tokens[0] : tokens[1]);
 		int pt2 =  (p == 2 ? tokens[1] : tokens[0]);

 		if(pt1 > pt2) return 1;
 		else if (pt1 == pt2) return 0;
 		else return -1;
 	}

}
