package classes;

import java.io.Serializable;
import java.util.ArrayList;


/*Count é uma classe com um atributo C que guarda as contagens todas necessárias, e tem funções
relevantes que convém guardar nesta classe para não sobrecarregar a classe dataset
*/

public class Count implements Serializable {
	private static final long serialVersionUID = 1L;
	
	ArrayList<ArrayList<ArrayList<KeyVal>>> C;
	
	//Construtor
	Count(int dim) { // dim é número de variáveis (Cria array dim*dim*0)
		this.C = new ArrayList<ArrayList<ArrayList<KeyVal>>>();
		for (int i = 0; i < dim; i++) {
			ArrayList<ArrayList<KeyVal>> aux2 = new ArrayList<ArrayList<KeyVal>>(dim); //ArrayList de tamanho dim com dim ArrayLists vazias de KeyVals
			for (int j = 0; j < dim; j++) {
				ArrayList<KeyVal> aux = new ArrayList<KeyVal>(); //ArrayList vazia de KeyVal's
				aux2.add(aux);
			}
			C.add(aux2);
		}
		
		// Notas sobre construtor: necessário criar novo aux a cada iterada, cc java ia considerar
		// que todas as ArrayLists de aux2 eram as mesmas (por ex adicionar KeyVal na posição 0,0
		// ia adicionar em 0,j para todo o j no range a considerar)
		
	}

	
	//dá a "celula" com arraylist de valores que i e j tomam em simultaneo, bem como a sua contagem
	private ArrayList<KeyVal> countCell(int i, int j) {
		return C.get(i).get(j);
	}
	
	//auxiliar: so para comparar vetores
	private static boolean isEqual(int[] u, int[] v) { 
		return (u[0]==v[0] && u[1]==v[1]);
	}
	
	//auxiliar: ver se u < v (ordem do dicionário)
	private static boolean isLesser(int[] u, int[] v) { 
		return (u[0] == v[0] && u[1] < v[1]) || (u[0] < v[0]);
	}
	
	
	//recebe (i,j),(xi,xj) e adiciona à contagem (i<=j necessário!)
	//Para adicionar à contagem i e xi, basta usar como input (i,i) , (xi,xi)
	//A "célula" (i,j) dos pares (xi,xj) fica ordenada com a ordem do dicionário, para facilitar a pesquisa na função getCount (mais abaixo)
	private void Add(int[] vars, int[] vals) { 
		ArrayList<KeyVal> cell = countCell(vars[0],vars[1]);
		int l = cell.size();
		if (l == 0) {
			KeyVal k = new KeyVal(vals[0], vals[1]);
			cell.add(k);
		} 
		else {
			int i = 0;
			while (i < l && isLesser(cell.get(i).getKey(), vals)) {
				i++;
			}
			if (i < l && isEqual(vals, cell.get(i).getKey()) ) {
				cell.get(i).Add();
			} 
			else {
				KeyVal k = new KeyVal(vals[0], vals[1]);
				cell.add(i, k);
			}
		}
	}
	
	
	//devolve array de 2 listas, uma de pares de vars, uma de pares de vals (todos os pares (i,j) e respetivo (xi,xj) dado um vetor)
	//Nota: também se devolve, para cada variável i,  (i,i) (xi,xi) de modo a adicionar à contagem uma ocorrência do valor xi à variável i 
	private ArrayList<ArrayList<int[]>> allPairs(int[] vec) { //note-se que nos pares, i<=j sempre
		int len = vec.length;
		ArrayList<int[]> vars = new ArrayList<int []>();
		ArrayList<int[]> vals = new ArrayList<int []>();
		for (int i = 0; i < len; i++) {
			for (int j = i; j < len; j++) {
				int[] var = new int[2]; int[] val = new int[2];
				var[0] = i; var[1] = j; val[0] = vec[i]; val[1] = vec[j];
				vars.add(var); vals.add(val);
			}
		}
		ArrayList<ArrayList<int[]>> res = new ArrayList<ArrayList<int[]>>();
		res.add(vars); res.add(vals);
		return res;
		
	}
	
	public void AddVec(int[] vec) { //adiciona cada par de variaveis/valores em vec à contagem, e cada var/val singular tb
		ArrayList<ArrayList<int[]>> AllPairs = allPairs(vec);
		ArrayList<int[]> vars = AllPairs.get(0);
		ArrayList<int[]> vals = AllPairs.get(1);
		int size = vars.size(); //( = vals.size() )
		for (int i = 0; i < size; i++) {
			this.Add(vars.get(i), vals.get(i));
		}
	}
	
	
	//Dá o nº de ocorrências em simultâneo dos valores (xi,xj) para as variáveis (i,j)
	public double getCount(int i, int j, int xi, int xj) { //i<=j de preferência
		if (i > j) {
			return getCount(j, i, xj, xi);
		} else {
			int[] x = new int[2];
			x[0]=xi; x[1]=xj;
			double res = 0;
			int k = 0;
			ArrayList<KeyVal> cell = countCell(i,j); int l = cell.size();
			while (k < l && isLesser(cell.get(k).getKey(), x)) {
				k++;
			}
			if (k < l && isEqual(cell.get(k).getKey(), x)) {
				res = cell.get(k).getVal();
			}
			return res;
		}
	}
	
	public double getCount(int i, int xi) { //contagem de uma so variavel
		return getCount(i,i,xi,xi);
	}
	
	
	public String toString() {
		String res = "";
		for (int i = 0; i<C.size(); i++) {
			for (int j = 0; j<C.get(i).size(); j++) {
				for (int k = 0; k<C.get(i).get(j).size(); k++) {
					res = res + " " + C.get(i).get(j).get(k);
				}
				res = res + " || ";
			}
			res = res + "\n";
		}
		return res;
	}
	
	public static void main(String[] args) {
		
		Count c = new Count(3);
		
		
		int[] vec = new int[3];
		vec[0] = 1; vec[1] = 2; vec[2] = 3;
		c.AddVec(vec);
		
		int[] vec1 = new int[3];
		vec1[0] = 1; vec1[1] = 0; vec1[2] = 2;
		c.AddVec(vec1);
		
		int[] vec2 = new int[3];
		vec2[0] = 0; vec2[1] = 0; vec2[2] = 3;
		c.AddVec(vec2);
		
		System.out.println(c);
		System.out.println(c.getCount(0, 1));
		System.out.println(c.getCount(1, 1));
		System.out.println(c.getCount(0,2,1,3));
		
	}

}

