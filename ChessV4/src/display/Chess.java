package display;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JApplet;

import logic.Board;
import logic.Future;
import logic.Move;
import logic.Vector2D;

public class Chess extends JApplet implements MouseListener, MouseMotionListener {
	
	private static final long MEGABYTE = 1024L * 1024L;

	  public static long bytesToMegabytes(long bytes) {
	    return bytes / MEGABYTE;
	  }
	  
	public Board board;
	public ArrayList<Move> moves;
	private int turn = 0;
	private Image[][] pieces;
	private static final int WIDTH = 812;
	private static final int HEIGHT = 512;
	private static final long serialVersionUID = 1L;
	private int tWidth = (WIDTH-300)/8;
	private int tHeight = HEIGHT/8;
	private Point pieceMoving;
	private Vector2D moveTo;
	private boolean moving;
	private Point last;
	public ArrayList<Move>[][] validMoves;
	public ArrayList<Integer>[][] moveValues;
	private int selected;
	private boolean inCheck;
	private int numMoves;
	private MoveThread t;
	private int promPiece = 0;
	



	//double buffering
	private Graphics bufferGraphics;
	private Image offscreen;
	
	public void init() {
		 try {
	            URL pic = new URL(getDocumentBase(), "../img/sprite.gif");
	            BufferedImage img = ImageIO.read(pic);
	            this.pieces = new Image[2][6];
	            for(int y = 0; y < 2; y++) {
	            	for(int x = 0; x < 6; x++) {
	            		this.pieces[y][x] = (Image)img.getSubimage((x*64), (y*64), 64, 64);
	            	}
	            }
		 }
		 catch(Exception e) {
			 e.printStackTrace(); 
		 }
		this.setSize(new Dimension(WIDTH, HEIGHT));
		this.pieceMoving = new Point(0, 0);
		addMouseListener(this);
		addMouseMotionListener(this);
		this.moveTo = null;
		this.moving = false;
		this.last = new Point(0, 0);
		this.offscreen = createImage(WIDTH, HEIGHT);
		this.bufferGraphics = offscreen.getGraphics();
		this.board = new Board();
		this.reset();
		this.validMoves = new ArrayList[8][8];
		this.moves = new ArrayList<Move>();
		this.moveValues = new ArrayList[8][8];
		this.getValidMoves();
		this.selected = 0;
	}
	
	public void stop() {
	}
	
	public void reset() {
		board.reset();
	}
	
	private void getValidMoves() {
		this.numMoves = 0;
		this.inCheck = false;
		Move lastMove;
		if(this.turn > 0) {
			lastMove = this.moves.get(this.turn-1);
		}
		else {
			lastMove = null;
		}
		for(byte x = 0; x < 8; x++) {
			for(byte y = 0; y < 8; y++) {
				if(board.state[x][y] != 0 && ((this.turn%2) == 0) == (board.state[x][y] > 0)) {
					if(Math.abs(board.state[x][y]) == 6){
						this.inCheck = board.inCheck(x, y);
					}
					this.validMoves[x][y] = this.board.getPossibleMoves(lastMove, x, y);
					this.numMoves += this.validMoves[x][y].size();
							this.moveValues[x][y] = new ArrayList<Integer>();
							for(int j = 0; j < this.validMoves[x][y].size(); j++) {
								this.moveValues[x][y].add(0);
							}
				}
				else {
					this.validMoves[x][y] = new ArrayList<Move>();
					//this.moveValues[x][y] = new ArrayList<Integer>();
				}
			}
		}
		getMovePoints();
	}
	
	private void getMovePoints() {
		if(this.turn > -1) {
			t = new MoveThread(board, validMoves, moveValues, ((this.turn%2) == 0));
		}
	}
	
	private boolean validateMove(byte fromX, byte fromY, byte toX, byte toY) {
		for(byte i = 0; i < this.validMoves[fromX][fromY].size(); i++) {
			if(this.validMoves[fromX][fromY].get(i).to.x == toX &&
					this.validMoves[fromX][fromY].get(i).to.y == toY) {
				this.selected = i;
				return true;
			}
		}
		return false;
	}
	
	private boolean makeMove() {
		if(this.pieceMoving.x != 0 || this.pieceMoving.y != 0) {
			Move move = this.validMoves[this.moveTo.x][this.moveTo.y].get(this.selected);
			if(Math.abs(board.state[move.from.x][move.from.y]) == 1 &&
					(move.to.y == 0 || move.to.y == 7)) {
				if(this.promPiece == 0) {//Queen
					move = this.validMoves[this.moveTo.x][this.moveTo.y].get(this.selected+3);
				}
				else if(this.promPiece == 1) {//rook
					move = this.validMoves[this.moveTo.x][this.moveTo.y].get(this.selected+2);
				}
				else if(this.promPiece == 2) {//knight
					move = this.validMoves[this.moveTo.x][this.moveTo.y].get(this.selected+1);
				}
				//bishop
			}
			this.moves.add(move);
			this.turn++;
			board.doMove(move);
			this.moveTo = null;
			this.getValidMoves();
			return true;
		}
		return false;
	}
	
	public void paint(Graphics g) {
		if(t != null && !t.isAlive()) {
			moveValues = t.getValues();
			t = null;
			/*System.out.println("num nodes: "+Future.numNodes);
			System.out.println("num moves: "+Future.numMoves);
			System.out.println("won: "+Future.won+", draw: "+Future.draw+", loss: "+Future.loss);
			System.out.println();*/
		}
		Color oldColor = this.bufferGraphics.getColor();
		this.bufferGraphics.clearRect(0, 0, WIDTH, HEIGHT);
		this.bufferGraphics.setColor(Color.black);
		for(int x = 0; x < 8; x++) {
			for(int y = 0; y < 8; y++) {
				if((x+y)%2 != 0) {
					this.bufferGraphics.fillRect((x*tWidth), (y*tHeight), tWidth, tHeight);
				}
			}
		}
		for(int y = 0; y < 8; y++) {
			for(int x = 0; x < 8; x++) {
				if(board.state[x][y] != 0) {
					if(board.state[x][y] > 0) {
						if(this.moveTo != null && this.moveTo.x == x && this.moveTo.y == y) {
							this.bufferGraphics.drawImage(this.pieces[0][Math.abs(board.state[x][y])-1],
									(x*this.tWidth)+this.pieceMoving.x,
									(y*this.tHeight)+this.pieceMoving.y, this);
						}
						else {
							this.bufferGraphics.drawImage(this.pieces[0][Math.abs(board.state[x][y])-1],
									(x*this.tWidth), (y*this.tHeight), this);
						}
					}
					else {
						if(this.moveTo != null && this.moveTo.x == x && this.moveTo.y == y) {
							this.bufferGraphics.drawImage(this.pieces[1][Math.abs(board.state[x][y])-1],
									(x*this.tWidth)+this.pieceMoving.x,
									(y*this.tHeight)+this.pieceMoving.y, this);
						}
						else if(0 <= Math.abs(board.state[x][y])-1 &&
								 this.pieces[1].length > Math.abs(board.state[x][y])-1) {
							//System.out.println("1: "+Math.abs(board.state[x][y])+", x: "+x+", y: "+y);
							this.bufferGraphics.drawImage(this.pieces[1][Math.abs(board.state[x][y])-1],
									(x*this.tWidth), (y*this.tHeight), this);
						}
					}
				}
			}
		}
		
		if(this.moveTo != null) {
			this.bufferGraphics.setColor(Color.red);
			for(int i = -2 ; i < 3; i++) {
				this.bufferGraphics.drawLine(this.moveTo.x*this.tWidth+i,
						this.moveTo.y*this.tHeight+i, (this.moveTo.x+1)*this.tWidth+i,
						this.moveTo.y*this.tHeight+i);
				this.bufferGraphics.drawLine((this.moveTo.x+1)*this.tWidth+i,
						this.moveTo.y*this.tHeight+i, (this.moveTo.x+1)*this.tWidth+i,
						(this.moveTo.y+1)*this.tHeight+i);
				this.bufferGraphics.drawLine((this.moveTo.x+1)*this.tWidth+i,
						(this.moveTo.y+1)*this.tHeight+i, this.moveTo.x*this.tWidth+i,
						(this.moveTo.y+1)*this.tHeight+i);
				this.bufferGraphics.drawLine(this.moveTo.x*this.tWidth+i,
						(this.moveTo.y+1)*this.tHeight+i, this.moveTo.x*this.tWidth+i,
						this.moveTo.y*this.tHeight+i);
			}
			this.bufferGraphics.setColor(Color.green);
			this.bufferGraphics.drawString(this.validMoves
					[this.moveTo.x][this.moveTo.y].size()+"",
					this.moveTo.x*this.tWidth+(this.tWidth/2),
					this.moveTo.y*this.tHeight+(this.tHeight/2));
				for(int i = 0; i < this.validMoves[this.moveTo.x][this.moveTo.y].size(); i++) {
					Move move = this.validMoves[this.moveTo.x][this.moveTo.y].get(i);
					for(int j = -2; j < 3; j++) {
						this.bufferGraphics.drawLine(
								(move.to.x*this.tWidth)+j,
								(move.to.y*this.tHeight)+j,
								((move.to.x*this.tWidth)+this.tWidth)+j,
								(move.to.y*this.tHeight)+j);
						this.bufferGraphics.drawLine(
								((move.to.x*this.tWidth)+this.tWidth)+j,
								(move.to.y*this.tHeight)+j,
								((move.to.x*this.tWidth)+this.tWidth)+j,
								((move.to.y*this.tHeight)+this.tHeight)+j);
						this.bufferGraphics.drawLine(
								((move.to.x*this.tWidth)+this.tWidth+j),
								((move.to.y*this.tHeight)+this.tHeight)+j,
								(move.to.x*this.tWidth)+j,
								((move.to.y*this.tHeight)+this.tHeight)+j);
						this.bufferGraphics.drawLine(
								(move.to.x*this.tWidth)+j,
								((move.to.y*this.tHeight)+this.tHeight)+j,
								(move.to.x*this.tWidth)+j,
								(move.to.y*this.tHeight)+j);
					}
					Font oldFont = this.bufferGraphics.getFont();
					this.bufferGraphics.setFont(new Font("arial", Font.BOLD, 40));
					this.bufferGraphics.drawString(
							this.moveValues[this.moveTo.x][this.moveTo.y].get(i).toString()+"",
							(move.to.x*this.tWidth)+(this.tWidth/2),
							(move.to.y*this.tHeight)+(this.tHeight/2));
					this.bufferGraphics.setFont(oldFont);
				}
				this.bufferGraphics.setColor(Color.black);
		}
		
		//make move button:
		this.bufferGraphics.drawRect(525, 450, 100, 50);
		this.bufferGraphics.drawString("make move", 550, 475);
		
		//promote pawns buttons:
		this.bufferGraphics.drawRect(525, 200, 100, 50);
		if(this.promPiece == 0) {
			this.bufferGraphics.setColor(Color.blue);
			this.bufferGraphics.fillRect(525, 200, 100, 50);
			this.bufferGraphics.setColor(Color.black);
		}
		this.bufferGraphics.drawString("queen", 550, 225);
		
		this.bufferGraphics.drawRect(525, 250, 100, 50);
		if(this.promPiece == 1) {
			this.bufferGraphics.setColor(Color.blue);
			this.bufferGraphics.fillRect(525, 250, 100, 50);
			this.bufferGraphics.setColor(Color.black);
		}
		this.bufferGraphics.drawString("rook", 550, 275);
		
		
		this.bufferGraphics.drawRect(525, 300, 100, 50);
		if(this.promPiece == 2) {
			this.bufferGraphics.setColor(Color.blue);
			this.bufferGraphics.fillRect(525, 300, 100, 50);
			this.bufferGraphics.setColor(Color.black);
		}
		this.bufferGraphics.drawString("knight", 550, 325);
		
		this.bufferGraphics.drawRect(525, 350, 100, 50);
		if(this.promPiece == 3) {
			this.bufferGraphics.setColor(Color.blue);
			this.bufferGraphics.fillRect(525, 350, 100, 50);
			this.bufferGraphics.setColor(Color.black);
		}
		this.bufferGraphics.drawString("bishop", 550, 375);
		
		this.bufferGraphics.setColor(Color.green);
		//Current Score:
		this.bufferGraphics.drawString("Score: "+this.board.pieceScore, 600, 150);
		
		//Number of moves:
		this.bufferGraphics.drawString("possible moves: "+this.numMoves, 600, 50);
		
		//outcome
		if(this.inCheck) {
			if(this.numMoves > 0) {
				this.bufferGraphics.drawString("check!", 600, 100);
			}
			else {
				this.bufferGraphics.drawString("checkmate!", 600, 100);
			}
		}
		else if(this.numMoves == 0) {
			this.bufferGraphics.drawString("stalemate!", 600, 100);
		}
		
		this.bufferGraphics.setColor(oldColor);
		
		g.drawImage(this.offscreen, 0, 0, this);
	}
	
	@Override
	public void update(Graphics g) {
		this.paint(g);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		if(x < 512) {
			if(t == null || !t.isAlive()) {
				byte posX = (byte)Math.round(x/this.tWidth);
				byte posY = (byte)Math.round(y/this.tHeight);
				if(board.state[posX][posY] != 0 && ((this.turn%2) == 0) ==
					board.state[posX][posY] > 0) {
					this.moveTo = new Vector2D(posX, posY);
					this.pieceMoving.x = ((posX-this.moveTo.x)*this.tWidth);
					this.pieceMoving.y = ((posY-this.moveTo.y)*this.tHeight);
					repaint();
				}
			}
		}
		if(this.moveTo != null) {
			this.moving = true;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		int posX = Math.round(x/this.tWidth);
		int posY = Math.round(y/this.tHeight);
		if(x > 525 && x < 625 && y > 200 && y < 400) {
			if(y < 250) {
				this.promPiece = 0;
			}
			else if(y < 300) {
				this.promPiece = 1;
			}
			else if(y < 350) {
				this.promPiece = 2;
			}
			else {
				this.promPiece = 3;
			}
		}
		if(posX >= 0 && posX < 8 && posY >= 0 && posY < 8 && this.moveTo != null) {
			this.pieceMoving.x = ((posX-this.moveTo.x)*this.tWidth);
			this.pieceMoving.y = ((posY-this.moveTo.y)*this.tHeight);
			if(!this.validateMove(this.moveTo.x, this.moveTo.y, (byte)posX, (byte)posY)) {
				this.pieceMoving.x = 0;
				this.pieceMoving.y = 0;
			}
		}
		else if(x > 525 && x < 625 && y > 450 && y < 500) {
			this.makeMove();
		}
		else {
			this.pieceMoving.x = 0;
			this.pieceMoving.y = 0;
		}
		this.moving = false;
		this.repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
	
	@Override
	protected void processMouseMotionEvent(MouseEvent e) {
		super.processMouseMotionEvent(e);
		repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		if(this.moving) {
			int movedX = x-this.last.x;
			int movedY = y-this.last.y;
			this.pieceMoving.x += movedX;
			this.pieceMoving.y += movedY;
			repaint();
		}
		this.last.x = x;
		this.last.y = y;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		this.last.x = x;
		this.last.y = y;
	}
}