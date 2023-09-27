package classes;

import java.io.Serializable;

public class Tree implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int[] list;
	private int dim;
	
	Tree(int dim) {
		this.list = new int[dim];
		for (int i = 0; i<dim; i++) {
			list[i] = -1; //pai do nó origem é -1 para distinguir dos outros
		}
		this.dim = dim;
	}
	
	//auxiliar para método toString()
	private static String vec_to_str(int[] vec) {
		String res = "";
		for (int i = 0; i<vec.length; i++) {
			res = res + " " + vec[i];
		}
		return "[" + res + "]";
	}
	
	public String toString() {
		return "dimensão: "+ dim + "\nárvore: "+vec_to_str(list);
	}
	
	public void Add(int o, int d) {
		list[d] = o;
	}
	
	public int getDim() {
		return dim;
	}
	
	public int getDad(int d) {
		return list[d];
	}
	
	
}