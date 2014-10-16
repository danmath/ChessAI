package misc;

import java.util.ArrayList;

public class Permution {
	/*public static void main(String []args) {
		Permution perm = new Permution();
		for(int i = 0; i < 6; i++) {
			ArrayList<Integer> pos = new ArrayList<Integer>();
			pos.add(0);
			pos.add(1);
			pos.add(2);
			perm.getPermution(pos, i);
			System.out.print("step("+i+"):");
			for(int j = 0; j < pos.size(); j++) {
				System.out.print(pos.get(j)+", ");
			}
			System.out.println();
		}
	}*/
	public void getPermution(ArrayList<Integer> pos, int step, int size) {
		int depth = 0;
		while(size > 0) {
			swap(pos, depth, depth+(step%size));
			depth++;
			size--;
		}
	}
	public void swap(ArrayList<Integer> pos, int index1, int index2) {
		if(index1 != index2) {
			pos.set(index1, (pos.get(index1)+pos.get(index2)));
			pos.set(index2, (pos.get(index1)-pos.get(index2)));
			pos.set(index1, (pos.get(index1)-pos.get(index2)));
		}
	}
}