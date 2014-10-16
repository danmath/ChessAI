package logic;

import java.util.ArrayList;
import java.util.HashMap;


public class Future {
	public static long numNodes = 0;
	public static long numMoves = 0;
	public static int won = 0;
	public static int draw = 0;
	public static int loss = 0;
	public static int estiMoves = 0;
	public static boolean go = true;
	/*public static void main(String[]args) {
		Board board = new Board();
		ArrayList<Move>[][] moves = new ArrayList[8][8];
		for(byte x = 0; x < 8; x++) {
			for(byte y = 0; y < 8; y++) {
				moves[x][y] = board.getPossibleMoves(null, x, y);
			}
		}
		ArrayList<Integer>[][] solution = minimax(4, board, moves, true, 10,
				new ArrayList<Vector2D>());
		for(byte y = 0; y < 8; y++) {
			for(byte x = 0; x < 8; x++) {
				int bestMove = Integer.MIN_VALUE;
				if(solution[x][y].size() > 0) {
					for(int i = 0; i < solution[x][y].size(); i++) {
						int value = solution[x][y].get(i);
						if(bestMove < value) {
							bestMove = value;
						}
					}
					System.out.print(bestMove+" ");
				}
				else {
					System.out.print("B ");
				}
			}
			System.out.println();
		}
	}*/
	
	public static int countPieces(Board board, ArrayList<Integer> pos) {
		int count = 0;
		for(int i = 0; i < pos.size(); i++) {
			int x = (pos.get(i)%8);
			int y = (pos.get(i)/8);
			if(board.state[x][y] != 0) {
				if(board.state[x][y] > 0) {
					count += board.weight(board.state[x][y]);
				}
				else {
					count -= board.weight(board.state[x][y]);
				}
			}
		}
		return count;
	}
	
	public static int evaluate(Board board, Move lastMove, boolean white) {
		int material = board.pieceScore;
		int mobility = 0;
		for(byte x = 0; x < 8; x++) {
			for(byte y = 0; y < 8; y++) {
				if(board.state[x][y] > 0) {
					mobility += board.getNumMoves(lastMove, x, y);
				}
				else {
					mobility -= board.getNumMoves(lastMove, x, y);
				}
			}
		}
		if(white) {
			return (material)+(mobility/20);
		}
		else {
			return -((material)+(mobility/20));
		}
	}
	
	public static ArrayList<Integer>[][] minimax(int depth, Board board,
			ArrayList<Move>[][] moves, boolean white, ArrayList<Integer> pos) {
		go = true;
		estiMoves = 420;
		won = 0;
		draw = 0;
		loss = 0;
		ArrayList<Integer>[][] values = new ArrayList[8][8];
		numNodes = 0;
		numMoves = 0;
		for(int k = 0; k < pos.size(); k++) {
			int x = (pos.get(k)%8);
			int y = (pos.get(k)/8);
			values[x][y] = new ArrayList<Integer>();
			for(int i = 0; i < moves[x][y].size(); i++) {
				Move move = moves[x][y].get(i);
				boolean[] oldCop = new boolean[4];
				for(int j = 0; j < 4; j++) {
					oldCop[j] = board.cop[j];
				}
				board.doMove(move);
				byte captured = board.captured;
				boolean prom = board.prom;
				boolean enpass = board.enpass;
				boolean enpassed = board.enpassed;
				if(white) {
					values[x][y].add(negaMax(depth-1, board, move, Integer.MAX_VALUE, Integer.MIN_VALUE, pos, white));
				}
				else {
					values[x][y].add(-negaMax(depth-1, board, move, Integer.MIN_VALUE, Integer.MAX_VALUE, pos, white));
				}
				board.undoMove(move, oldCop, captured, prom, enpass, enpassed);
			}
		}
		return values;
	}
	
	private static int negaMax(int depth, Board board, Move lastMove, int alpha, 
			int beta, ArrayList<Integer> pos, boolean white) {
		numNodes++;
		//System.out.println("moves: "+numNodes);
		if(depth <= 0) {
			numMoves++;
			return evaluate(board, lastMove, white);
		}
		int best = Integer.MIN_VALUE;
		for(byte k = 0; k < pos.size(); k++) {
			int cur = pos.get(k);
			int x = (cur%8);
			int y = (cur/8);
			if(board.state[x][y] != 0 && board.state[x][y] > 0 == white) {
				ArrayList<Move> moves = board.getPossibleMoves(lastMove, (byte)x, (byte)y);
				for(int i = 0; i < moves.size(); i++) {
					Move move = moves.get(i);
					boolean[] oldCop = new boolean[4];
					for(int j = 0; j < 4; j++) {
						oldCop[j] = board.cop[j];
					}
					board.doMove(move);
					byte captured = board.captured;
					boolean prom = board.prom;
					boolean enpass = board.enpass;
					boolean enpassed = board.enpassed;
					int value = -negaMax(depth-1, board, move, -alpha, -beta, pos, !white);
					board.undoMove(move, oldCop, captured, prom, enpass, enpassed);
					if(value > best) {
						best = value;
					}
					if(best > alpha) {
						alpha = best;
					}
					if(best >= beta) {
						break;
					}
				}
			}
		}
		return best;
	}
	
	/*private static int miniSearch(int depth, Board board, Move lastMove,
			int alpha, ArrayList<Integer> pos) {//black's turn
		numNodes++;
		//System.out.println("moves: "+numNodes);
		if(depth <= 0) {
			numMoves++;
			return evaluate(board, lastMove);
		}
		int bestValue = Integer.MAX_VALUE;
		for(byte k = 0; k < pos.size(); k++) {
			int cur = pos.get(k);
			int x = (cur%8);
			int y = (cur/8);
			if(board.state[x][y] < 0) {
				ArrayList<Move> moves = board.getPossibleMoves(lastMove, (byte)x, (byte)y);
				for(int i = 0; i < moves.size(); i++) {
					Move move = moves.get(i);
					boolean[] oldCop = new boolean[4];
					for(int j = 0; j < 4; j++) {
						oldCop[j] = board.cop[j];
					}
					board.doMove(move);
					byte captured = board.captured;
					boolean prom = board.prom;
					boolean enpass = board.enpass;
					boolean enpassed = board.enpassed;
					int value = maxSearch(depth-1, board, move, bestValue, pos);
					if(bestValue > value) {
						bestValue = value;
					}
					board.undoMove(move, oldCop, captured, prom, enpass, enpassed);
					if(alpha >= bestValue) {
						return bestValue;
					}
				}
			}
		}
		if(depth == 1)
			System.out.println("finished total: "+Future.numMoves);
		return bestValue;
	}
	
	private static int maxSearch(int depth, Board board, Move lastMove,
			int beta, ArrayList<Integer> pos) {//white's turn
		numNodes++;
		//System.out.println("moves: "+numNodes);
		if(depth <= 0) {
			numMoves++;
			return evaluate(board, lastMove);
		}
		int bestValue = Integer.MIN_VALUE;
		for(byte k = 0; k < pos.size(); k++) {
			int cur = pos.get(k);
			int x = (cur%8);
			int y = (cur/8);
			if(board.state[x][y] > 0) {
				ArrayList<Move> moves = board.getPossibleMoves(lastMove, (byte)x, (byte)y);
				for(int i = 0; i < moves.size(); i++) {
					Move move = moves.get(i);
					boolean[] oldCop = new boolean[4];
					for(int j = 0; j < 4; j++) {
						oldCop[j] = board.cop[j];
					}
					board.doMove(move);
					byte captured = board.captured;
					boolean prom = board.prom;
					boolean enpass = board.enpass;
					boolean enpassed = board.enpassed;
					int value = miniSearch(depth-1, board, move, bestValue, pos);
					if(bestValue < value) {
						bestValue = value;
					}
					board.undoMove(move, oldCop, captured, prom, enpass, enpassed);
					if(beta <= bestValue) {
						return bestValue;
					}
				}
			}
		}
		if(depth == 1)
			System.out.println("finished total: "+Future.numMoves);
		return bestValue;
	}*/
}