package logic;

public class Move {
	public byte type;
	public Vector2D from;
	public Vector2D to;
	public Move(Vector2D from, Vector2D to, byte type) {
		this.from = from;
		this.to = to;
		this.type = type;
	}
}