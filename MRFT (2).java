package classes;
import java.io.Serializable;
import java.util.ArrayList;

public class MRFT implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private ArrayList<double[][]> phistodos;
	private Tree tree;
	
	public MRFT(DataSet dataset){ //vamos guardar a matriz dos phis para cada aresta
		
		double delta = 0.2;
		
		this.tree = ChowLiu.chowliu(dataset); //de facto não é necessário construtor receber tree

		this.phistodos = new ArrayList<double[][]>();
		
		int m = dataset.getM();
		boolean especial = true;
		
		for (int d = 1; d < tree.getDim(); d++) { //percorrer arestas dirigidas (ignorar 0 pois n tem pai)
		
			
			int o = tree.getDad(d);  // estamos a estudar a aresta do vértice o para o vértice d, e vamos criar a matriz dos phis a,b
			
			int Do = dataset.getSupdom(o);
			int Dd = dataset.getSupdom(d);
			
			double[][] phis = new double[Do+1][Dd+1]; //phis é a matriz dos phis para uma aresta
			
			for(int j1 = 0; j1 <= Do; j1++) { //j1 são os valores que a variável o pode tomar
				double[] v = new double [Dd+1]; // v é o vetor de phis para uma aresta e um determinado valor da variável o
				for(int j2 = 0; j2 <= Dd; j2++) { //j2 são os valores que a variável d pode tomar
					if (especial==true && o==0) {
						v[j2] = (dataset.Count(o,d, j1, j2)+delta)/(m+delta*Do*Dd);
					}else {
						v[j2] = (dataset.Count(o, d, j1, j2)+delta)/(dataset.Count(o,j1)+delta*Dd);
					}

					}
				phis[j1]=v;
				}
			
			
			phistodos.add(phis);
			if (o == 0) {especial = false;} //escolhemos como aresta especial a primeira que encontramos cuja origem seja 0
		}
	}
	
	
	//(auxiliar para Prob)
	//recebe indice k (a começar em 1) e devolve os nós da aresta k correspondente
	//Nota: o nó destino da aresta k é k, considerando as arestas numeradas de 1 a n
	private int[] getVars(int k) {
		int[] res = new int[2];
		res[0] = tree.getDad(k);
		res[1] = k;
		return res;
	}
	

	public double Prob (int[] valores) {
		double p = 1;	
		for (int k = 1; k < valores.length ; k++) { //k percorre as arestas
			
			int o = getVars(k)[0];
			int d = getVars(k)[1];
			
			int ValorOrigem = valores[o];
			int ValorDestino = valores[d];
			
			p = p*phistodos.get(k-1)[ValorOrigem][ValorDestino];
		}
		return p;
	}
	
	//auxiliar no toString()
	public static String matrix_to_str(double[][] A) {
		String res = new String();
		for (int i=0; i<A.length; i++) {
			for (int j=0; j<A[i].length; j++) {
				if (j==0) {
					res = res + "\n" +
							A[i][j];
				} else {
					res = res + "   " + A[i][j];
				}
			}
		}
		return res;
	}
	
	public String toString() {
		String res = "";
		for (int i = 0; i < phistodos.size(); i++) {
			res = res + "\n" + matrix_to_str(phistodos.get(i));
		}
		return res;
	}
	
	
	public static void main(String[] args) {
		
		
		DataSet T = new DataSet("thyroid.csv");
		DataSet F0 = T.Fiber(0);
		
		Tree tree0 = ChowLiu.chowliu(F0);
		
		System.out.println(tree0);
		
		MRFT fiber0thyroid = new MRFT(F0);
		
		int[] vals = {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4};
		
		System.out.println(fiber0thyroid.Prob(vals));
		
		
		
	}
	
}