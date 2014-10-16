package misc;

import java.util.ArrayList;

public class BestOrder {
	public static void main(String[] args) {
		ArrayList<Integer> pos1 = putPlyEfficient(2);
		ArrayList<Integer> pos2 = putPlyEfficient(4);
		for(int i = 0; i < pos1.size(); i++) {
			if(pos1.get(i) == pos2.get(i)) {
				System.out.println("at point["+i+"]: "+pos1.get(i));
			}
		}
	}
	public static void moveTo(ArrayList<Integer> pos, int from, int to) {
		int ans = pos.remove(from);
		pos.add(to, ans);
	}
	public static ArrayList<Integer> putPlyEfficient(int ply) {
		ArrayList<Integer> pos = new ArrayList<Integer>(64);
		switch(ply) {
		case 3:
			for(int y = 0; y < 8; y++) {
				for(int x = 0; x < 8; x++) {
					pos.add(x+(y*8));
				}
			}
			BestOrder.moveTo(pos, 12, 0);
			break;
		case 4:
			pos.add(3);		pos.add(11);	pos.add(12);	pos.add(28);
			pos.add(2);		pos.add(5);		pos.add(25);	pos.add(27);
			pos.add(26);	pos.add(30);	pos.add(18);	pos.add(1);
			pos.add(0);		pos.add(4);		pos.add(6);		pos.add(7);
			pos.add(8);		pos.add(9);		pos.add(10);	pos.add(13);
			pos.add(14);	pos.add(15);	pos.add(16);	pos.add(17);
			pos.add(19);	pos.add(20);	pos.add(21);	pos.add(22);
			pos.add(23);	pos.add(24);	pos.add(29);	pos.add(31);
			pos.add(33);	pos.add(35);	pos.add(36);	pos.add(59);
			pos.add(39);	pos.add(34);	pos.add(40);	pos.add(52);
			pos.add(41);	pos.add(32);	pos.add(42);	pos.add(38);
			pos.add(43);	pos.add(37);	pos.add(44);	pos.add(45);
			pos.add(46);	pos.add(47);	pos.add(48);	pos.add(49);
			pos.add(50);	pos.add(51);	pos.add(53);	pos.add(54);
			pos.add(55);	pos.add(56);	pos.add(57);	pos.add(58);
			pos.add(60);	pos.add(61);	pos.add(62);	pos.add(63);
			break;
		case 5:
			pos.add(12);	pos.add(61);	pos.add(58);	pos.add(5);
			pos.add(15);	pos.add(31);	pos.add(28);	pos.add(27);
			pos.add(23);	pos.add(16);	pos.add(59);	pos.add(24);
			pos.add(29);	pos.add(30);	pos.add(26);	pos.add(25);
			pos.add(21);	pos.add(10);	pos.add(56);	pos.add(13);
			pos.add(18);	pos.add(41);	pos.add(2);		pos.add(0);
			pos.add(3);		pos.add(37);	pos.add(4);		pos.add(22);
			pos.add(34);	pos.add(7);		pos.add(8);		pos.add(11);
			pos.add(6);		pos.add(14);	pos.add(9);		pos.add(17);
			pos.add(1);		pos.add(35);	pos.add(38);	pos.add(19);
			pos.add(20);	pos.add(32);	pos.add(33);	pos.add(36);
			pos.add(45);	pos.add(39);	pos.add(40);	pos.add(54);
			pos.add(42);	pos.add(49);	pos.add(43);	pos.add(50);
			pos.add(46);	pos.add(47);	pos.add(52);	pos.add(51);
			pos.add(63);	pos.add(44);	pos.add(48);	pos.add(55);
			pos.add(53);	pos.add(57);	pos.add(60);	pos.add(62);
			break;
		case 6:
			pos.add(12);	pos.add(3);		pos.add(55);	pos.add(39);
			pos.add(28);	pos.add(17);	pos.add(30);	pos.add(21);
			pos.add(36);	pos.add(38);	pos.add(33);	pos.add(5);
			pos.add(29);	pos.add(23);	pos.add(18);	pos.add(27);
			pos.add(26);	pos.add(40);	pos.add(16);	pos.add(9);
			pos.add(35);	pos.add(52);	pos.add(25);	pos.add(20);
			pos.add(2);		pos.add(14);	pos.add(22);	pos.add(0);
			pos.add(31);	pos.add(7);		pos.add(63);	pos.add(24);
			pos.add(6);		pos.add(10);	pos.add(1);		pos.add(11);
			pos.add(47);	pos.add(19);	pos.add(32);	pos.add(13);
			pos.add(34);	pos.add(37);	pos.add(61);	pos.add(42);
			pos.add(45);	pos.add(51);	pos.add(59);	pos.add(44);
			pos.add(4);		pos.add(46);	pos.add(53);	pos.add(48);
			pos.add(43);	pos.add(56);	pos.add(49);	pos.add(15);
			pos.add(50);	pos.add(8);		pos.add(58);	pos.add(54);
			pos.add(60);	pos.add(57);	pos.add(62);	pos.add(41);
			break;
		case 7:
			pos.add(59);	pos.add(43);	pos.add(31);	pos.add(16);
			pos.add(27);	pos.add(23);	pos.add(12);	pos.add(28);
			pos.add(30);	pos.add(19);	pos.add(29);	pos.add(25);
			pos.add(61);	pos.add(35);	pos.add(38);	pos.add(26);
			pos.add(58);	pos.add(50);	pos.add(5);		pos.add(45);
			pos.add(15);	pos.add(39);	pos.add(41);	pos.add(40);
			pos.add(33);	pos.add(24);	pos.add(52);	pos.add(36);
			pos.add(42);	pos.add(47);	pos.add(11);	pos.add(2);
			pos.add(3);		pos.add(21);	pos.add(17);	pos.add(7);
			pos.add(63);	pos.add(18);	pos.add(14);	pos.add(20);
			pos.add(32);	pos.add(13);	pos.add(9);		pos.add(56);
			pos.add(0);		pos.add(10);	pos.add(37);	pos.add(1);
			pos.add(4);		pos.add(22);	pos.add(34);	pos.add(8);
			pos.add(51);	pos.add(6);		pos.add(49);	pos.add(44);
			pos.add(54);	pos.add(55);	pos.add(46);	pos.add(62);
			pos.add(60);	pos.add(48);	pos.add(57);	pos.add(53);
			break;
			default:
				for(int y = 0; y < 8; y++) {
					for(int x = 0; x < 8; x++) {
						pos.add(x+(y*8));
					}
				}
				break;
		}
		return pos;
	}
}
