package classes;

//import java.util.ArrayList;
import java.lang.Math;

public class ChowLiu {
	
	private static double Pr(DataSet T, int i, int xi) {
		return T.Count(i, xi)/T.getM();
	}
	private static double Pr(DataSet T, int i, int j, int xi, int xj) {
		return T.Count(i, j, xi, xj)/T.getM();
	}
	
	
	//Função da informação mútua
	private static double I(DataSet T, int i, int j) {
		double res = 0;
		int Di = T.getSupdom(i); int Dj = T.getSupdom(j);
		for (int xi = 0; xi <= Di; xi++) {
			for (int xj = 0; xj <= Dj; xj++) {
				double pr = Pr(T, i,j,xi,xj);
				if (pr!=0) {
					res = res + pr*Math.log(pr/(Pr(T,i,xi)*Pr(T,j,xj)));
				}
			}
		}
		return res;
	}
	
	
	public static Tree chowliu(DataSet T) {
		int n = T.getVeclen();
		WeightedGraph G = new WeightedGraph(n);
		for (int i = 0; i < n-1; i++) {
			for (int j = i+1; j < n; j++) {
				double peso = I(T,i,j);
				G.Add(i,j,peso);
			}
		}
		//final do ciclo: temos grafo completo pesado. falta apenas MST
		return G.MST();
	}
	

	public static void main(String[] args) {
		
		DataSet T = new DataSet("bcancer.csv");
		DataSet F0 = T.Fiber(0);
		DataSet F1 = T.Fiber(1);
		System.out.println(chowliu(F0));
		System.out.println(chowliu(F1));
		
	}

}
