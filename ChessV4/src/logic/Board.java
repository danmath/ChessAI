package logic;

import java.util.ArrayList;

public class Board {
	public byte[][] state;
	public boolean[] cop;
	public byte captured;
	public boolean prom;
	public boolean enpass;
	public boolean enpassed;
	public int pieceScore;
	public byte wKingX;
	public byte wKingY;
	public byte bKingX;
	public byte bKingY;
	public int bMoveAprox;
	public int wMoveAprox;
	
	public Board() {
		this.state = new byte[8][8];
		this.cop = new boolean[4];
		this.reset();
	}
	
	public Board(Board board) {
		this.state = new byte[8][8];
		this.cop = new boolean[4];
		this.setTo(board);
	}
	
	public void reset() {
		for(byte x = 0; x < 8; x++) {
			for(byte y = 0; y < 8; y++) {
				if(y < 2) {
					if(y == 1) {
						this.state[x][y] = -1;
					}
					else {
						switch(x) {
						case 0:
						case 7:
							this.state[x][y] = -4;
							break;
						case 1:
						case 6:
							state[x][y] = -3;
							break;
						case 2:
						case 5:
							state[x][y] = -2;
							break;
						case 3:
							state[x][y] = -5;
							break;
						case 4:
							state[x][y] = -6;
							break;
						}
					}
				}
				else if(y > 5) {
					if(y == 6) {
						this.state[x][y] = 1;
					}
					else {
						switch(x) {
						case 0:
						case 7:
							state[x][y] = 4;
							break;
						case 1:
						case 6:
							state[x][y] = 3;
							break;
						case 2:
						case 5:
							state[x][y] = 2;
							break;
						case 3:
							state[x][y] = 5;
							break;
						case 4:
							state[x][y] = 6;
							break;
						}
					}
				}
				else {
					this.state[x][y] = 0;
				}
			}
		}
		for(byte i = 0; i < 4; i++) {
			this.cop[i] = true;
		}
		pieceScore = 0;
		wKingX = 4;
		wKingY = 7;
		
		bKingX = 4;
		bKingY = 0;
		bMoveAprox = 20;
		wMoveAprox = 20;
	}
	
	public void setTo(Board board) {
		wMoveAprox = 0;
		bMoveAprox = 0;
		pieceScore = 0;
		for(byte x = 0; x < 8; x++) {
			for(byte y = 0; y < 8; y++) {
				this.state[x][y] = board.state[x][y];
				if(board.state[x][y] > 0) {
					if(board.state[x][y] == 6) {
						wKingX = x;
						wKingY = y;
					}
					pieceScore += this.weight(board.state[x][y]);
				}
				else {
					if(board.state[x][y] == -6) {
						bKingX = x;
						bKingY = y;
					}
					pieceScore -= this.weight((byte)-board.state[x][y]);
				}
			}
		}
		for(byte x = 0; x < 8; x++) {
			for(byte y = 0; y < 8; y++) {
				if(board.state[x][y] > 0) {
					wMoveAprox += board.getPossibleMoves(null, x, y).size();
				}
				else {
					bMoveAprox += board.getPossibleMoves(null, x, y).size();
				}
			}
		}
		for(byte i = 0; i < 4; i++) {
			this.cop[i] = board.cop[i];
		}
	}
	
	public int getNumMoves(Move lastMove, byte x, byte y) {
		int moves = 0;
		switch(Math.abs(state[x][y])) {
		case 1://pawn
			if(state[x][y] > 0) {//white
				if(state[x][y-1] == 0) {
					if(y == 6 && state[x][y-2] == 0) {
						moves++;
					}
					if(y == 1) {
						for(byte i = 2; i < 6; i++) {
							moves++;
						}
					}
					else {
						moves++;
					}
				}
				if(x > 0) {
					if(state[x-1][y-1] < 0) {
						if(y == 1) {
							for(byte i = 2; i < 6; i++) {
								moves++;
							}
						}
						else {
							moves++;
						}
					}
					else if(lastMove != null && lastMove.type == 1  &&
							(lastMove.to.y-lastMove.from.y) == 2 && lastMove.to.x == (x-1) &&
							lastMove.to.y == y) {
						moves++;
					}
				}
				if(x < 7) {
					if(state[x+1][y-1] < 0) {
						if(y == 1) {
							for(byte i = 2; i < 6; i++) {
								moves++;
							}
						}
						else {
							moves++;
						}
					}
					else if(lastMove != null && lastMove.type == 1  &&
							(lastMove.to.y-lastMove.from.y) == 2 && lastMove.to.x == (x+1) &&
							lastMove.to.y == y) {
						moves++;
					}
				}
			}
			else {//black
				if(state[x][y+1] == 0) {
					if(y == 1 && state[x][y+2] == 0) {
						moves++;
					}
					if(y == 6) {
						for(byte i = -2; i > -6; i--) {
							moves++;
						}
					}
					else {
						moves++;
					}
				}
				if(x > 0) {
					if(state[x-1][y+1] > 0) {
						if(y == 6) {
							for(byte i = -2; i > -6; i--) {
								moves++;
							}
						}
						else {
							moves++;
						}
					}
					else if(lastMove != null && lastMove.type == 1  &&
							(lastMove.from.y-lastMove.to.y) == 2 && lastMove.to.x == (x-1) &&
							lastMove.to.y == y) {
						moves++;
					}
				}
				if(x < 7) {
					if(state[x+1][y+1] > 0) {
						if(y == 6) {
							for(byte i = -2; i > -6; i--) {
								moves++;
							}
						}
						else {
							moves++;
						}
					}
					else if(lastMove != null && lastMove.type == 1  &&
							(lastMove.from.y-lastMove.to.y) == 2 && lastMove.to.x == (x+1) &&
							lastMove.to.y == y) {
						moves++;
					}
				}
			}
			break;
		case 2://bishop
			for(byte xx = -1; xx < 2; xx+=2) {
				for(byte yy = -1; yy < 2; yy+=2) {
					for(byte i = 1; i < 8; i++) {
						int movX = x+(xx*i);
						int movY = y+(yy*i);
						if(movX >= 0 && movX < 8 && movY >= 0 && movY < 8) {
							if(state[movX][movY] == 0) {
								moves++;
							}
							else {
								if(state[movX][movY] == 0 || (state[movX][movY] < 0) !=
									(state[x][y] < 0)) {
									moves++;
								}
								break;
							}
						}
						else {
							break;
						}
					}
				}
			}
			break;
		case 3://knight
			for(byte xx = -1; xx < 2; xx+=2) {
				for(byte yy = -1; yy < 2; yy+=2) {
					for(byte i = 0; i < 2; i++) {
						int movX = x+((xx)*(1+(i)));
						int movY = y+((yy)*(1+((1-i))));
						if(movX >= 0 && movX < 8 && movY >= 0 && movY < 8) {
							if(state[movX][movY] == 0 || (state[movX][movY] > 0) !=
								(state[x][y] > 0)) {
								moves++;
							}
						}
					}
				}
			}
			break;
		case 4://rook
			for(byte ver = 0; ver < 2; ver++) {
				for(byte pos = -1; pos < 2; pos+=2) {
					for(byte i = 1; i < 8; i++) {
						int movX = x+(ver*pos*i);
						int movY = y+((1-ver)*pos*i);
						if(movX >= 0 && movX < 8 && movY >= 0 && movY < 8) {
							if(movX >= 0 && movX < 8 && movY >= 0 && movY < 8) {
								if(state[movX][movY] == 0) {
									moves++;
								}
								else {
									if(state[movX][movY] == 0 || (state[movX][movY] < 0) !=
										(state[x][y] < 0)) {
										moves++;
									}
									break;
								}
							}
						}
						else {
							break;
						}
					}
				}
			}
			break;
		case 5://queen
			for(byte xx = -1; xx < 2; xx++) {
				for(byte yy = -1; yy < 2; yy++) {
					if(yy != 0 || xx != 0) {
						for(byte i = 1; i < 8; i++) {
							int movX = x+(xx*i);
							int movY = y+(yy*i);
							if(movX >= 0 && movX < 8 && movY >= 0 && movY < 8) {
								if(state[movX][movY] == 0) {
									moves++;
								}
								else {
									if(state[movX][movY] == 0 || (state[movX][movY] < 0) !=
										(state[x][y] < 0)) {
										moves++;
									}
									break;
								}
							}
							else {
								break;
							}
						}
					}
				}
				
			}
			break;
		case 6://king
			for(byte xx = -1; xx < 2; xx++) {
				for(byte yy = -1; yy < 2; yy++) {
					if((xx != 0 || yy != 0)) {
						int movX = x+xx;
						int movY = y+yy;
						if(movX >= 0 && movX < 8 && movY >= 0 && movY < 8) {
							if(state[movX][movY] == 0 ||
									((state[movX][movY] < 0) != (state[x][y] < 0))) {
								moves++;
							}
						}
					}
				}
			}
			if(((cop[0] && state[x][y] > 0) || (cop[2] && state[x][y] < 0)) &&
					state[x+1][y] == 0 && state[x+2][y] == 0) {
				moves++;
			}
			if(((cop[1] && state[x][y] > 0) || (cop[3] && state[x][y] < 0)) &&
					state[x-1][y] == 0 && state[x-2][y] == 0 && state[x-3][y] == 0) {
				moves++;
			}
			break;
		}
		return moves;
	}
	
	public ArrayList<Move> getPossibleMoves(Move lastMove, byte x, byte y) {
		ArrayList<Move> moves = new ArrayList<Move>();
		byte kingX = 0;
		byte kingY = 0;
		if(state[x][y] > 0) {
			kingX = wKingX;
			kingY = wKingY;
		}
		else {
			kingX = bKingX;
			kingY = bKingY;
		}
		byte captured = 0;
		switch(Math.abs(state[x][y])) {
		case 1://pawn
			if(state[x][y] > 0) {//white
				if(state[x][y-1] == 0) {
					if(y == 6 && state[x][y-2] == 0) {
						state[x][y-2] = state[x][y];
						state[x][y] = 0;
						if(!this.inCheck(kingX, kingY)) {
							moves.add(new Move(new Vector2D(x, y), new Vector2D(x,
									(byte)(y-2)), (byte)1));
						}
						state[x][y] = state[x][y-2];
						state[x][y-2] = 0;
					}
					if(y == 1) {
						for(byte i = 2; i < 6; i++) {
							state[x][y-1] = i;
							state[x][y] = 0;
							if(!this.inCheck(kingX, kingY)) {
								moves.add(new Move(new Vector2D(x, y), new Vector2D(x,
										(byte)(y-1)), i));
							}
							state[x][y] = 1;
							state[x][y-1] = 0;
						}
					}
					else {
						state[x][y-1] = state[x][y];
						state[x][y] = 0;
						if(!this.inCheck(kingX, kingY)) {
							moves.add(new Move(new Vector2D(x, y), new Vector2D(x, (byte)(y-1)),
									(byte)1));
						}
						state[x][y] = state[x][y-1];
						state[x][y-1] = 0;
					}
				}
				if(x > 0) {
					if(state[x-1][y-1] < 0) {
						if(y == 1) {
							for(byte i = 2; i < 6; i++) {
								captured = state[x-1][y-1];
								state[x-1][y-1] = i;
								state[x][y] = 0;
								if(!this.inCheck(kingX, kingY)) {
									moves.add(new Move(new Vector2D(x, y), new Vector2D(
											(byte)(x-1), (byte)(y-1)), i));
								}
								state[x][y] = 1;
								state[x-1][y-1] = captured;
							}
						}
						else {
							captured = state[x-1][y-1];
							state[x-1][y-1] = state[x][y];
							state[x][y] = 0;
							if(!this.inCheck(kingX, kingY)) {
								moves.add(new Move(new Vector2D(x, y), new Vector2D((byte)(x-1),
										(byte)(y-1)), (byte)1));
							}
							state[x][y] = state[x-1][y-1];
							state[x-1][y-1] = captured;
						}
					}
					else if(lastMove != null && lastMove.type == 1  &&
							(lastMove.to.y-lastMove.from.y) == 2 && lastMove.to.x == (x-1) &&
							lastMove.to.y == y) {
						captured = state[x-1][y];
						state[x-1][y-1] = state[x][y];
						state[x][y] = 0;
						if(!this.inCheck(kingX, kingY)) {
							moves.add(new Move(new Vector2D(x, y), new Vector2D((byte)(x-1),
									(byte)(y-1)), (byte)1));
						}
						state[x][y] = state[x-1][y-1];
						state[x-1][y-1] = 0;
						state[x-1][y] = captured;
					}
				}
				if(x < 7) {
					if(state[x+1][y-1] < 0) {
						if(y == 1) {
							for(byte i = 2; i < 6; i++) {
								captured = state[x+1][y-1];
								state[x+1][y-1] = i;
								state[x][y] = 0;
								if(!this.inCheck(kingX, kingY)) {
									moves.add(new Move(new Vector2D(x, y), new Vector2D(
											(byte)(x+1), (byte)(y-1)), i));
								}
								state[x][y] = 1;
								state[x+1][y-1] = captured;
							}
						}
						else {
							captured = state[x+1][y-1];
							state[x+1][y-1] = state[x][y];
							state[x][y] = 0;
							if(!this.inCheck(kingX, kingY)) {
								moves.add(new Move(new Vector2D(x, y), new Vector2D((byte)(x+1),
										(byte)(y-1)), (byte)1));
							}
							state[x][y] = state[x+1][y-1];
							state[x+1][y-1] = captured;
						}
					}
					else if(lastMove != null && lastMove.type == 1  &&
							(lastMove.to.y-lastMove.from.y) == 2 && lastMove.to.x == (x+1) &&
							lastMove.to.y == y) {
						captured = state[x+1][y];
						state[x+1][y-1] = state[x][y];
						state[x][y] = 0;
						if(!this.inCheck(kingX, kingY)) {
							moves.add(new Move(new Vector2D(x, y), new Vector2D((byte)(x+1),
									(byte)(y-1)), (byte)1));
						}
						state[x][y] = state[x+1][y-1];
						state[x+1][y-1] = 0;
						state[x+1][y] = captured;
					}
				}
			}
			else {//black
				if(state[x][y+1] == 0) {
					if(y == 1 && state[x][y+2] == 0) {
						state[x][y+2] = state[x][y];
						state[x][y] = 0;
						if(!this.inCheck(kingX, kingY)) {
							moves.add(new Move(new Vector2D(x, y), new Vector2D(x,
									(byte)(y+2)), (byte)-1));
						}
						state[x][y] = state[x][y+2];
						state[x][y+2] = 0;
					}
					if(y == 6) {
						for(byte i = -2; i > -6; i--) {
							state[x][y+1] = i;
							state[x][y] = 0;
							if(!this.inCheck(kingX, kingY)) {
								moves.add(new Move(new Vector2D(x, y), new Vector2D(x,
										(byte)(y+1)), i));
							}
							state[x][y] = -1;
							state[x][y+1] = 0;
						}
					}
					else {
						state[x][y+1] = state[x][y];
						state[x][y] = 0;
						if(!this.inCheck(kingX, kingY)) {
							moves.add(new Move(new Vector2D(x, y), new Vector2D(x, (byte)(y+1)),
									(byte)-1));
						}
						state[x][y] = state[x][y+1];
						state[x][y+1] = 0;
					}
				}
				if(x > 0) {
					if(state[x-1][y+1] > 0) {
						if(y == 6) {
							for(byte i = -2; i > -6; i--) {
								captured = state[x-1][y+1];
								state[x-1][y+1] = i;
								state[x][y] = 0;
								if(!this.inCheck(kingX, kingY)) {
									moves.add(new Move(new Vector2D(x, y), new Vector2D(
											(byte)(x-1), (byte)(y+1)), i));
								}
								state[x][y] = -1;
								state[x-1][y+1] = captured;
							}
						}
						else {
							captured = state[x-1][y+1];
							state[x-1][y+1] = state[x][y];
							state[x][y] = 0;
							if(!this.inCheck(kingX, kingY)) {
								moves.add(new Move(new Vector2D(x, y), new Vector2D((byte)(x-1),
										(byte)(y+1)), (byte)-1));
							}
							state[x][y] = state[x-1][y+1];
							state[x-1][y+1] = captured;
						}
					}
					else if(lastMove != null && lastMove.type == 1  &&
							(lastMove.from.y-lastMove.to.y) == 2 && lastMove.to.x == (x-1) &&
							lastMove.to.y == y) {
						captured = state[x-1][y];
						state[x-1][y+1] = state[x][y];
						state[x][y] = 0;
						if(!this.inCheck(kingX, kingY)) {
							moves.add(new Move(new Vector2D(x, y), new Vector2D((byte)(x-1),
									(byte)(y+1)), (byte)-1));
						}
						state[x][y] = state[x-1][y+1];
						state[x-1][y+1] = 0;
						state[x-1][y] = captured;
					}
				}
				if(x < 7) {
					if(state[x+1][y+1] > 0) {
						if(y == 6) {
							for(byte i = -2; i > -6; i--) {
								captured = state[x+1][y+1];
								state[x+1][y+1] = i;
								state[x][y] = 0;
								if(!this.inCheck(kingX, kingY)) {
									moves.add(new Move(new Vector2D(x, y), new Vector2D(
											(byte)(x+1), (byte)(y+1)), i));
								}
								state[x][y] = -1;
								state[x+1][y+1] = captured;
							}
						}
						else {
							captured = state[x+1][y+1];
							state[x+1][y+1] = state[x][y];
							state[x][y] = 0;
							if(!this.inCheck(kingX, kingY)) {
								moves.add(new Move(new Vector2D(x, y), new Vector2D((byte)(x+1),
										(byte)(y+1)), (byte)-1));
							}
							state[x][y] = state[x+1][y+1];
							state[x+1][y+1] = captured;
						}
					}
					else if(lastMove != null && lastMove.type == 1  &&
							(lastMove.from.y-lastMove.to.y) == 2 && lastMove.to.x == (x+1) &&
							lastMove.to.y == y) {
						captured = state[x+1][y];
						state[x+1][y+1] = state[x][y];
						state[x][y] = 0;
						if(!this.inCheck(kingX, kingY)) {
							moves.add(new Move(new Vector2D(x, y), new Vector2D((byte)(x+1),
									(byte)(y+1)), (byte)-1));
						}
						state[x][y] = state[x+1][y+1];
						state[x+1][y+1] = 0;
						state[x+1][y] = captured;
					}
				}
			}
			break;
		case 2://bishop
			for(byte xx = -1; xx < 2; xx+=2) {
				for(byte yy = -1; yy < 2; yy+=2) {
					for(byte i = 1; i < 8; i++) {
						int movX = x+(xx*i);
						int movY = y+(yy*i);
						if(movX >= 0 && movX < 8 && movY >= 0 && movY < 8) {
							if(state[movX][movY] == 0) {
								captured = state[movX][movY];
								state[movX][movY] = state[x][y];
								state[x][y] = 0;
								if(!this.inCheck(kingX, kingY)) {
									moves.add(new Move(new Vector2D(x, y), new Vector2D(
											(byte)movX, (byte)movY), (byte)state[movX][movY]));
								}
								state[x][y] = state[movX][movY];
								state[movX][movY] = captured;
							}
							else {
								if(state[movX][movY] == 0 || (state[movX][movY] < 0) !=
									(state[x][y] < 0)) {
									captured = state[movX][movY];
									state[movX][movY] = state[x][y];
									state[x][y] = 0;
									if(!this.inCheck(kingX, kingY)) {
										moves.add(new Move(new Vector2D(x, y), new Vector2D(
												(byte)movX, (byte)movY), state[movX][movY]));
									}
									state[x][y] = state[movX][movY];
									state[movX][movY] = captured;
								}
								break;
							}
						}
						else {
							break;
						}
					}
				}
			}
			break;
		case 3://knight
			for(byte xx = -1; xx < 2; xx+=2) {
				for(byte yy = -1; yy < 2; yy+=2) {
					for(byte i = 0; i < 2; i++) {
						int movX = x+((xx)*(1+(i)));
						int movY = y+((yy)*(1+((1-i))));
						if(movX >= 0 && movX < 8 && movY >= 0 && movY < 8) {
							if(state[movX][movY] == 0 || (state[movX][movY] > 0) !=
								(state[x][y] > 0)) {
								captured = state[movX][movY];
								state[movX][movY] = state[x][y];
								state[x][y] = 0;
								if(!this.inCheck(kingX, kingY)) {
									moves.add(new Move(new Vector2D(x, y), new Vector2D(
											(byte)movX, (byte)movY), state[movX][movY]));
								}
								state[x][y] = state[movX][movY];
								state[movX][movY] = captured;
							}
						}
					}
				}
			}
			break;
		case 4://rook
			for(byte ver = 0; ver < 2; ver++) {
				for(byte pos = -1; pos < 2; pos+=2) {
					for(byte i = 1; i < 8; i++) {
						int movX = x+(ver*pos*i);
						int movY = y+((1-ver)*pos*i);
						if(movX >= 0 && movX < 8 && movY >= 0 && movY < 8) {
							if(movX >= 0 && movX < 8 && movY >= 0 && movY < 8) {
								if(state[movX][movY] == 0) {
									captured = state[movX][movY];
									state[movX][movY] = state[x][y];
									state[x][y] = 0;
									if(!this.inCheck(kingX, kingY)) {
										moves.add(new Move(new Vector2D(x, y), new Vector2D(
												(byte)movX, (byte)movY), state[movX][movY]));
									}
									state[x][y] = state[movX][movY];
									state[movX][movY] = captured;
								}
								else {
									if(state[movX][movY] == 0 || (state[movX][movY] < 0) !=
										(state[x][y] < 0)) {
										captured = state[movX][movY];
										state[movX][movY] = state[x][y];
										state[x][y] = 0;
										if(!this.inCheck(kingX, kingY)) {
											moves.add(new Move(new Vector2D(x, y), new Vector2D(
													(byte)movX, (byte)movY), state[movX][movY]));
										}
										state[x][y] = state[movX][movY];
										state[movX][movY] = captured;
									}
									break;
								}
							}
						}
						else {
							break;
						}
					}
				}
			}
			break;
		case 5://queen
			for(byte xx = -1; xx < 2; xx++) {
				for(byte yy = -1; yy < 2; yy++) {
					if(yy != 0 || xx != 0) {
						for(byte i = 1; i < 8; i++) {
							int movX = x+(xx*i);
							int movY = y+(yy*i);
							if(movX >= 0 && movX < 8 && movY >= 0 && movY < 8) {
								if(state[movX][movY] == 0) {
									captured = state[movX][movY];
									state[movX][movY] = state[x][y];
									state[x][y] = 0;
									if(!this.inCheck(kingX, kingY)) {
										moves.add(new Move(new Vector2D(x, y), new Vector2D(
												(byte)movX, (byte)movY), state[movX][movY]));
									}
									state[x][y] = state[movX][movY];
									state[movX][movY] = captured;
								}
								else {
									if(state[movX][movY] == 0 || (state[movX][movY] < 0) !=
										(state[x][y] < 0)) {
										captured = state[movX][movY];
										state[movX][movY] = state[x][y];
										state[x][y] = 0;
										if(!this.inCheck(kingX, kingY)) {
											moves.add(new Move(new Vector2D(x, y), new Vector2D(
													(byte)movX, (byte)movY), state[movX][movY]));
										}
										state[x][y] = state[movX][movY];
										state[movX][movY] = captured;
									}
									break;
								}
							}
							else {
								break;
							}
						}
					}
				}
				
			}
			break;
		case 6://king
			for(byte xx = -1; xx < 2; xx++) {
				for(byte yy = -1; yy < 2; yy++) {
					if((xx != 0 || yy != 0)) {
						int movX = x+xx;
						int movY = y+yy;
						if(movX >= 0 && movX < 8 && movY >= 0 && movY < 8) {
							if(state[movX][movY] == 0 ||
									((state[movX][movY] < 0) != (state[x][y] < 0))) {
								captured = state[movX][movY];
								state[movX][movY] = state[x][y];
								state[x][y] = 0;
								if(!this.inCheck((byte)movX, (byte)movY)) {
									moves.add(new Move(new Vector2D(x, y), new Vector2D(
											(byte)movX, (byte)movY), state[movX][movY]));
								}
								state[x][y] = state[movX][movY];
								state[movX][movY] = captured;
							}
						}
					}
				}
			}
			if(((cop[0] && state[x][y] > 0) || (cop[2] && state[x][y] < 0)) &&
					state[x+1][y] == 0 && state[x+2][y] == 0) {
				if(!this.inCheck(kingX, kingY)) {
					state[x+1][y] = state[x][y];
					state[x][y] = 0;
					if(!this.inCheck((byte)(x+1), kingY)) {
						state[x+2][y] = state[x+1][y];
						state[x+1][y] = 0;
						if(!this.inCheck((byte)(x+2), kingY)) {
							state[5][y] = state[7][y];
							state[7][y] = 0;
							if(!this.inCheck((byte)(x+2), kingY)) {
								moves.add(new Move(new Vector2D(x, y), new Vector2D(
										(byte)(x+2), (byte)y), state[x+2][y]));
							}
							state[7][y] = state[5][y];
							state[5][y] = 0;
							state[x][y] = state[x+2][y];
							state[x+2][y] = 0;
						}
						else {
							state[x][y] = state[x+2][y];
							state[x+2][y] = 0;
						}
					}
					else {
						state[x][y] = state[x+1][y];
						state[x+1][y] = 0;
					}
				}
			}
			if(((cop[1] && state[x][y] > 0) || (cop[3] && state[x][y] < 0)) &&
					state[x-1][y] == 0 && state[x-2][y] == 0 && state[x-3][y] == 0) {
				if(!this.inCheck(kingX, kingY)) {
					state[x-1][y] = state[x][y];
					state[x][y] = 0;
					if(!this.inCheck((byte)(x-1), kingY)) {
						state[x-2][y] = state[x-1][y];
						state[x-1][y] = 0;
						if(!this.inCheck((byte)(x-2), kingY)) {
							state[3][y] = state[0][y];
							state[0][y] = 0;
							if(!this.inCheck((byte)(x-2), kingY)) {
								moves.add(new Move(new Vector2D(x, y), new Vector2D(
										(byte)(x-2), (byte)y), state[x-2][y]));
							}
							state[0][y] = state[3][y];
							state[3][y] = 0;
							state[x][y] = state[x-2][y];
							state[x-2][y] = 0;
						}
						else {
							state[x][y] = state[x-2][y];
							state[x-2][y] = 0;
						}
					}
					else {
						state[x][y] = state[x-1][y];
						state[x-1][y] = 0;
					}
				}
			}
			break;
		}
		return moves;
	}
	
	public byte doMove(Move move) {
		prom = false;
		enpassed = false;
		byte captured = 0;
		switch(Math.abs(this.state[move.from.x][move.from.y])) {
		case 1:
			if((move.from.x-move.to.x) != 0) {
				if(this.state[move.to.x][move.to.y] == 0) {
					captured = this.state[move.to.x][move.from.y];
					state[move.to.x][move.from.y] = 0;
					enpassed = true;
				}
				else {
					captured = this.state[move.to.x][move.to.y];
				}
			}
			if(move.to.y == 0 || move.to.y == 7) {
				prom = true;
			}
			break;
		case 4:
			if(move.from.x == 0) {
				if(move.type > 0) {
					if(move.from.y == 7) {
						this.cop[0] = false;
					}
				}
				else if(move.from.y == 0) {
					this.cop[2] = false;
				}
			}
			else if(move.from.x == 7) {
				if(move.type > 0) {
					if(move.from.y == 7) {
						this.cop[1] = false;
					}
				}
				else if(move.from.y == 0) {
					this.cop[3] = false;
				}
			}
			captured = this.state[move.to.x][move.to.y];
			break;
		case 6:
			if(state[move.from.x][move.from.y] > 0) {
				wKingX = move.to.x;
				wKingY = move.to.y;
			}
			else {
				bKingX = move.to.x;
				bKingY = move.to.y;
			}
			if(Math.abs(move.from.x-move.to.x) > 1) {
				if(move.to.x > move.from.x) {
					this.state[5][move.from.y] = this.state[7][move.from.y];
					this.state[7][move.from.y] = 0;
				}
				else {
					this.state[3][move.from.y] = this.state[0][move.from.y];
					this.state[0][move.from.y] = 0;
				}
			}
			if(move.type > 0) {
				this.cop[0] = false;
				this.cop[1] = false;
			}
			else {
				this.cop[2] = false;
				this.cop[3] = false;
			}
			captured = this.state[move.to.x][move.to.y];
			break;
			default:
				captured = this.state[move.to.x][move.to.y];
				break;
		}
		if(this.prom) {
			if(this.state[move.from.x][move.from.y] > 0) {
				pieceScore += (this.weight(move.type)-1);
			}
			else {
				pieceScore -= (this.weight(move.type)-1);
			}
		}
		this.state[move.to.x][move.to.y] = move.type;
		this.state[move.from.x][move.from.y] = 0;
		this.captured = captured;
		if(captured != 0) {
			if(captured > 0) {
				pieceScore -= this.weight(captured);
			}
			else {
				pieceScore += this.weight(captured);
			}
		}
		return captured;
	}
	
	public void undoMove(Move move, boolean[] oldCop, byte captured, boolean prom,
			boolean enpass, boolean enpassed) {
		if(prom) {
			state[move.from.x][move.from.y] = (byte)(move.type/(Math.abs(move.type)));
			state[move.to.x][move.to.y] = captured;
			if(state[move.from.x][move.from.y] > 0) {
				pieceScore -= (this.weight(move.type)-1);
			}
			else {
				pieceScore += (this.weight(move.type)-1);
			}
		}
		else if(enpassed){
			state[move.from.x][move.from.y] = move.type;
			state[move.to.x][move.to.y] = 0;
			state[move.to.x][move.from.y] = captured;
		}
		else {
			if(Math.abs(move.type) == 6) {
				if(move.type > 0) {
					wKingX = move.from.x;
					wKingY = move.from.y;
				}
				else {
					bKingX = move.from.x;
					bKingY = move.from.y;
				}
				if((move.to.x-move.from.x) > 1) {
					state[7][move.from.y] = state[5][move.from.y];
					state[5][move.from.y] = 0;
				}
				else if((move.to.x-move.from.x) < -1) {
					state[0][move.from.y] = state[3][move.from.y];
					state[3][move.from.y] = 0;
				}
				state[move.from.x][move.from.y] = move.type;
				state[move.to.x][move.to.y] = captured;
			}
			else {
				state[move.from.x][move.from.y] = move.type;
				state[move.to.x][move.to.y] = captured;
			}
		}
		for(int i = 0; i < 4; i++) {
			cop[i] = oldCop[i];
		}
		if(captured != 0) {
			if(captured > 0) {
				pieceScore += this.weight(captured);
			}
			else {
				pieceScore -= this.weight((byte)-captured);
			}
		}
	}
	
	public boolean inCheck(byte x, byte y) {
		for(byte xx = -1; xx < 2; xx++) {
	        for(byte yy = -1; yy < 2; yy++) {
	          for(byte i = 0; i < 2; i++) {
	        		  int checkX = x+(xx*(i+1));
	        		  int checkY = y+(yy*((1-i)+1));
	        		  if(checkX >= 0 && checkX < 8 && checkY >= 0 && checkY < 8 &&
	        				  state[checkX][checkY] != 0 &&
	        				  (state[checkX][checkY] < 0) == 
	        				  (state[x][y] > 0) && Math.abs(state[checkX][checkY]) == 3) {
	        			  return true;
	            	}
	          }
	          for(int i = 1; i < 8; i++) {
	            int checkX = x+(xx*i);
	            int checkY = y+(yy*i);
	            if(checkX >= 0 && checkX < 8 && checkY >= 0 && checkY < 8) {
	              if(state[checkX][checkY] != 0) {
	                if((state[checkX][checkY] < 0) == (state[x][y] > 0)) {
	                  int type = Math.abs(state[checkX][checkY]);
	                  if(xx == 0 || yy == 0) {//straight
	                    if(i == 1 && type == 6) {
	                      return true;
	                    }
	                    switch(type) {
	                      case 4:
	                      case 5:
	                        return true;
	                    }
	                  }
	                  else {//diagonal
	                    if(i == 1) {
	                      if(type == 6) {
	                        return true;
	                      }
	                      else if((yy < 0) == state[x][y] > 0 &&
	                        type == 1) {
	                        return true;
	                      }
	                    }
	                    switch(type) {
	                      case 2:
	                      case 5:
	                        return true;
	                    }
	                  }
	                }
	                break;
	              }
	            }
	            else {
	              break;
	            }
	          }
	        }
	      }
		return false;
	}
	
	public byte pressuring(Board board, byte x, byte y, Move lastMove) {
		byte valve = 0;
		if(board.state[x][y] == 0) {
			for(int xx = -1; xx < 2; xx++) {
				for(int yy = -1; yy < 2; yy++) {
					if(xx != 0 || yy != 0) {
						for(int i = 1; i < 8; i++) {
							int movX = x+(xx*i);
							int movY = y+(yy*i);
							if(movX >= 0 && movX < 8 && movY >= 0 && movY < 8) {
								if(board.state[movX][movY] != 0) {
									boolean white = board.state[movX][movY] > 0;
									boolean diag = (xx != 0 && yy != 0);
									int takes = Math.abs(board.state[movX][movY]);
									int j = i-1;
									movX = x+(xx*j);
									movY = y+(yy*j);
									while(movX >= 0 && movX < 8 && movY >= 0 && movY < 8) {
										if(board.state[movX][movY] != 0) {
											if((board.state[movX][movY] < 0) == white &&
													(takes == 5 || (diag && takes == 2) ||
															(!diag && takes == 4))) {
												//System.out.println("check 1: "+
														//takes+" "+x+", "+y+" : "+movX+", "+movY+
														//"   "+state[movX][movY]);
												if(this.weight((byte)takes) >=
														this.weight(state[movX][movY])) {
													return 0;
												}
												else {
													valve = -1;
												}
											}
											break;
										}
										j--;
										movX = x+(xx*j);
										movY = y+(yy*j);
									}
									break;
								}
							}
						}
					}
				}
			}
		}
		else {
			boolean whites = state[x][y] > 0;
			switch(Math.abs(state[x][y])) {
			case 1://pawn
				if(state[x][y] > 0) {//white
					if(x > 0) {
						if(state[x-1][y-1] < 0) {
							//System.out.println("check 2");
							valve = state[x-1][y-1];
						}
						else if(lastMove != null && lastMove.type == 1  &&
								(lastMove.to.y-lastMove.from.y) == 2 && lastMove.to.x == (x-1) &&
								lastMove.to.y == y) {
							//System.out.println("check 3");
							if(Math.abs(valve) < Math.abs(state[x-1][y])) {
								valve = state[x-1][y];
							}
						}
					}
					if(x < 7) {
						if(state[x+1][y-1] < 0) {
							//System.out.println("check 4");
							if(Math.abs(valve) < Math.abs(state[x+1][y-1])) {
								valve = state[x+1][y-1];
							}
						}
						else if(lastMove != null && lastMove.type == 1  &&
								(lastMove.to.y-lastMove.from.y) == 2 && lastMove.to.x == (x+1) &&
								lastMove.to.y == y) {
							//System.out.println("check 5");
							if(Math.abs(valve) < Math.abs(state[x-1][y])) {
								valve = state[x-1][y];
							}
						}
					}
				}
				else {//black
					if(x > 0) {
						if(state[x-1][y+1] > 0) {
							//System.out.println("check 6");
							if(Math.abs(valve) < Math.abs(state[x-1][y+1])) {
								valve = state[x-1][y+1];
							}
						}
						else if(lastMove != null && lastMove.type == 1  &&
								(lastMove.from.y-lastMove.to.y) == 2 && lastMove.to.x == (x-1) &&
								lastMove.to.y == y) {
							//System.out.println("check 7");
							if(Math.abs(valve) < Math.abs(state[x-1][y])) {
								valve = state[x-1][y];
							}
						}
					}
					if(x < 7) {
						if(state[x+1][y+1] > 0) {
							//System.out.println("check 8");
							if(Math.abs(valve) < Math.abs(state[x+1][y+1])) {
								valve = state[x+1][y+1];
							}
						}
						else if(lastMove != null && lastMove.type == 1  &&
								(lastMove.from.y-lastMove.to.y) == 2 && lastMove.to.x == (x+1) &&
								lastMove.to.y == y) {
							//System.out.println("check 9");
							if(Math.abs(valve) < Math.abs(state[x+1][y])) {
								valve = state[x+1][y];
							}
						}
					}
				}
				break;
			case 2://bishop
				for(byte xx = -1; xx < 2; xx+=2) {
					for(byte yy = -1; yy < 2; yy+=2) {
						for(byte i = 1; i < 8; i++) {
							int movX = x+(xx*i);
							int movY = y+(yy*i);
							if(movX >= 0 && movX < 8 && movY >= 0 && movY < 8) {
								if((state[movX][movY] < 0) == whites) {
									//System.out.println("check 10");
									if(Math.abs(valve) < Math.abs(state[movX][movY])) {
										valve = state[movX][movY];
									}
								}
								else if(state[movX][movY] != 0) {
									break;
								}
							}
							else {
								break;
							}
						}
					}
				}
				break;
			case 3://knight
				for(byte xx = -1; xx < 2; xx+=2) {
					for(byte yy = -1; yy < 2; yy+=2) {
						for(byte i = 0; i < 2; i++) {
							int movX = x+((xx)*(1+(i)));
							int movY = y+((yy)*(1+((1-i))));
							if(movX >= 0 && movX < 8 && movY >= 0 && movY < 8) {
								if((state[movX][movY] < 0) == whites) {
									//System.out.println("check 11");
									if(Math.abs(valve) < Math.abs(state[movX][movY])) {
										valve = state[movX][movY];
									}
								}
							}
						}
					}
				}
				break;
			case 4://rook
				for(byte ver = 0; ver < 2; ver++) {
					for(byte pos = -1; pos < 2; pos+=2) {
						for(byte i = 1; i < 8; i++) {
							int movX = x+(ver*pos*i);
							int movY = y+((1-ver)*pos*i);
							if(movX >= 0 && movX < 8 && movY >= 0 && movY < 8) {
								if(movX >= 0 && movX < 8 && movY >= 0 && movY < 8) {
									if((state[movX][movY] > 0) == whites) {
										//System.out.println("check 12");
										if(Math.abs(valve) < Math.abs(state[movX][movY])) {
											valve = state[movX][movY];
										}
									}
									else if(state[movX][movY] != 0) {
										break;
									}
								}
							}
							else {
								break;
							}
						}
					}
				}
				break;
			case 5://queen
				for(byte xx = -1; xx < 2; xx++) {
					for(byte yy = -1; yy < 2; yy++) {
						if(yy != 0 || xx != 0) {
							for(byte i = 1; i < 8; i++) {
								int movX = x+(xx*i);
								int movY = y+(yy*i);
								if(movX >= 0 && movX < 8 && movY >= 0 && movY < 8) {
									if((state[movX][movY] > 0) == whites) {
										//System.out.println("check 13");
										if(Math.abs(valve) < Math.abs(state[movX][movY])) {
											valve = state[movX][movY];
										}
									}
									else if(state[movX][movY] != 0) {
										break;
									}
								}
								else {
									break;
								}
							}
						}
					}
					
				}
				break;
			case 6://king
				for(byte xx = -1; xx < 2; xx++) {
					for(byte yy = -1; yy < 2; yy++) {
						if((xx != 0 || yy != 0)) {
							int movX = x+xx;
							int movY = y+yy;
							if(movX >= 0 && movX < 8 && movY >= 0 && movY < 8) {
								if((state[movX][movY] > 0) == whites) {
									//System.out.println("check 14");
									if(Math.abs(valve) < Math.abs(state[movX][movY])) {
										valve = state[movX][movY];
									}
								}
							}
						}
					}
				}
				break;
			}
		}
		return valve;
	}
	
	public int weight(byte piece) {
		switch(Math.abs(piece)) {
		case 1:
			return 1;
		case 2:
			return 3;
		case 3:
			return 3;
		case 4:
			return 5;
		case 5:
			return 9;
		case 6:
			return 200;
		}
		return 0;
	}
	
	public String toString() {
		String entry = "";
		for(int y = 0; y < 8; y++) {
			for(int x = 0; x < 8; x++) {
				entry += state[x][y]+"	";
			}
			entry += "\n";
		}
		return entry;
	}
}