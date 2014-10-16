package display;

import java.util.ArrayList;

import logic.Board;
import logic.Future;
import logic.Move;
import logic.Vector2D;
import misc.BestOrder;

public class MoveThread extends Thread {
	private Board board;
	private ArrayList<Move>[][] moves;
	private ArrayList<Integer>[][] values;
	private boolean turn;
	public MoveThread(Board board, ArrayList<Move>[][] moves, ArrayList<Integer>[][] values,
			boolean turn) {
		this.board = board;
		this.moves = moves;
		this.values = values;
		this.turn = turn;
		start();
	}
	
	public void swap(ArrayList<Integer> pos, int index1, int index2) {
		if(index1 != index2) {
			pos.set(index1, (pos.get(index1)+pos.get(index2)));
			pos.set(index2, (pos.get(index1)-pos.get(index2)));
			pos.set(index1, (pos.get(index1)-pos.get(index2)));
		}
	}
	
	public void moveTo(ArrayList<Integer> pos, int from, int to) {
		int ans = pos.remove(from);
		pos.add(to, ans);
	}
	
	public void run() {
		ArrayList<Integer> pos = BestOrder.putPlyEfficient(6);
		//for(int i = 0; i < 64; i++) {//detination
			//int bestInd = -1;
			//long bestScore = Integer.MAX_VALUE;
			//for(int j = 0; j < 64; j++) {//move
				//this.moveTo(pos, j, i);
				long startTime = System.nanoTime();
				values = Future.minimax(6, this.board, this.moves, turn, pos);
				long endTime = System.nanoTime();
				System.out.println("time taken: "+((endTime-startTime)/1000000000.0)+", with "+Future.numNodes+" and "+
						Future.numMoves+" moves");
				//if(bestScore > Future.numNodes) {
					//bestInd = j;
					//bestScore = Future.numNodes;
				//}
				//else if(bestScore == Future.numNodes && i == j) {
					//bestInd = j;
				//}
				//this.moveTo(pos, i, j);
			//}
			//if(bestInd != -1) {
				//this.moveTo(pos, bestInd, i);
			//}
			//System.out.println("prog: "+i+": "+bestScore);
		//}
		System.out.println("world: ");
		/*for(int i = 0; i < pos.size(); i++) {
			System.out.println("["+pos.get(i)+"]");
		}*/
		System.out.println("time: "+Future.numNodes);
	}
	
	public Board getBoard() {
		return board;
	}
	
	public ArrayList<Integer>[][] getValues() {
		return values;
	}
}