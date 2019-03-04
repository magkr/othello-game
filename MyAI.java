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
		ArrayList<Position> moves = s.legalMoves();
		if ( !moves.isEmpty() )
			return moves.get((int)(Math.random() * moves.size()));
		else
			return new Position(-1,-1);
	}

}
