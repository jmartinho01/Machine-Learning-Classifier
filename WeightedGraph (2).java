package classes;

import java.io.Serializable;
import java.util.LinkedList;



public class WeightedGraph implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int dim; //dimensão (útil no MST() para definir árvore com a mesma dimensão sem ter de percorrer am para calcular)
	private double [][] am; //adjacency matrix
	
	public WeightedGraph(int d) { //no grafo vazio entradas são -1 para nos salvaguardar no caso de ter vértices adjacentes c/ peso 0
		
		dim = d;
		am = new double [dim][dim];
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				am[i][j] = -1;
			}
		}
	}
	
	public void Add(int i, int j, double w) {
		am[i][j] = w;
		am[j][i] = w;
	}
	
	
	public Tree MST() {
		Tree mst = new Tree(dim);
		//uso de LinkedList devido à sua eficiência para manuseamento (adicionar e apagar elementos)
		LinkedList<Integer> in = new LinkedList<Integer>(); //lista com capacidade para tds os nós, começa com o primeiro nó da árvore
		in.add(0);
		LinkedList<Integer> not_in = new LinkedList<Integer>();
		for (int j = 1; j < dim; j++) {not_in.add(j);} 
		while (not_in.size() != 0) {
			double cost = -1;
			int o = 0; //não interessa valor inicial
			int d = 0;
			for (int i = 0; i < in.size(); i++) {
				for (int j = 0; j < not_in.size(); j++) { //neste ciclo vejo a aresta de maior peso do nó i
					double w = am[in.get(i)][not_in.get(j)];
					if  (w > cost) {cost = w;o = in.get(i); d = not_in.get(j);}
				}
			}
			in.add(d);
			mst.Add(o, d); //Note-se que 0 é sempre o nó inicial (sem pai), por construção
			not_in.removeFirstOccurrence(d); 
		}
		return mst;
	}
	
	
	//auxiliar para toString()
	public static String matrix_to_str(double[][] A) {
		String res = new String();
		for (int i=0; i<A.length; i++) {
			for (int j=0; j<A[i].length; j++) {
				if (j==0) {
					res = res + "\n" +
							A[i][j];
				} else {
					res = res +"   " + A[i][j];
				}
			}
		}
		return res;
	}
	
	public String toString() {
		return "dim: " + dim + "\n" + matrix_to_str(am);
	}
	
	public static void main(String[] args) {
		
		WeightedGraph G = new WeightedGraph(4);
		G.Add(0, 1, 2);
		G.Add(0, 2, 1);
		G.Add(2, 1, 0.5);
		G.Add(3, 1, 0.5);
		G.Add(3, 2, 1);
		System.out.println(G);
		System.out.println();
		
		Tree t = G.MST();
		System.out.println(t);
		System.out.println(t.getDad(1));
		
	}
	
}


