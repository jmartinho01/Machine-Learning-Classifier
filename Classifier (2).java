package classes;


import java.io.Serializable;

public class Classifier implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//Atributos:
	private MRFT[] MRFTS;
	private int[] freq;
	
	//construtor
	public Classifier(MRFT[] M, int[] f) {
		MRFTS = M;
		freq = f;
	}
	
	//Devolve a mensagem apresentada na aplicação de classificação
	public String strClassify(int[] vals) {
		String res = "Probabilidades de cada classe (arredondadas às unidades):\n";
		int numclasses = freq.length;
		int m = 0;
		for (int j = 0; j < numclasses; j++) {
			m = m + freq[j];
		}
		int c = 0;
		double [] probs = new double [numclasses]; // probs[c] = Pr(vals[0],..., vals[n], c)
		double sum = 0; //soma dos valores de probs, para a normalização dps
		for (int i = 0; i < numclasses; i++) {
			double Pc = (double)freq[i]/(double)m; //Pr(C=c)
			MRFT mrft = MRFTS[i];
			double P = Pc*mrft.Prob(vals);
			probs[i] = P;
			sum = sum + P;
			if (P > probs[c]) {c = i;}
		}
		for (int i = 0; i < numclasses; i++) {
			double p = (probs[i]/sum)*100;
			res = res + "Classe " + i + ": " + (int) (p + 0.5) + "%\n";
		}
		return res + "A classe mais provável é a " + c;
	}
	
	//Precisamos de função que devolve um inteiro (classe) para o LeaveOneOut
	public int Classify(int[] vals) {
		int numclasses = freq.length;
		int m = 0;
		for (int j = 0; j < numclasses; j++) {
			m = m + freq[j];
		}
		int c = 0;
		double [] probs = new double [numclasses]; // probs[c] = Pr(vals[0],..., vals[n], c)
		double sum = 0; //soma dos valores de probs, para a normalização dps
		for (int i = 0; i < numclasses; i++) {
			double Pc = (double)freq[i]/(double)m; //Pr(C=i)
			MRFT mrft = MRFTS[i];
			double P = Pc*mrft.Prob(vals);
			probs[i] = P;
			sum = sum + P;
			if (P > probs[c]) {c = i;}
		}
		return c;
	}
	
	//auxiliar no toString
	private static String vec_to_str(int[] vec) {
		String res = "";
		for (int i = 0; i<vec.length; i++) {
			res = res + " " + vec[i];
		}
		return "[" + res + "]";
	}
	
	//quando printamos um classifier, vemos só freq pq o array de mrfts é grande
	public String toString() {
		return "frequências = " + vec_to_str(freq);
	}
	
	public static void main(String[] args) {
		
		DataSet T = new DataSet("bcancer.csv");
		
		int numClasses = T.numberOfClasses();
		
		DataSet[] fibers = new DataSet[numClasses];
		MRFT[] mrfts = new MRFT[numClasses];
		int[] freqs = new int[numClasses];
		
		
		for (int c = 0; c < numClasses; c++) {
			fibers[c] = T.Fiber(c);
			mrfts[c] = new MRFT(fibers[c]);
			freqs[c] = fibers[c].getM();
		}
		
		Classifier C = new Classifier(mrfts, freqs);
		
		int[] vals = {1,0,2,2,2,0,1,2,1,1};
		int[] vals2 = {0,0,0,0,0,0,0,0,0,0};
		
		System.out.println("Deve dar prob. alta de ter cancro:");
		System.out.println(C.strClassify(vals));
		System.out.println();
		System.out.println("Deve dar prob. baixa de ter cancro:");
		System.out.println(C.strClassify(vals2));
		System.out.println();
		
		
		
	}
	

}