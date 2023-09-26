package Test;

import java.util.ArrayList;

import classes.*;


//Accuracy (total): percentagem total de classificações corretas (corretas/m*100)
//Precision: para cada classe c, de entre todas as vezes que classificámos c, a percentagem de classificações corretas (em que a classe real era c)
//Recall: para cada classe c, (nº de vezes que classificámos c quando era suposto)/(nº de ocorrências reais da classe c)

//precision alta para classe c indica que se se classificou c, é mais confiável que esteja certo
//recall alta para classe c indica que o modelo é bom a encontrar ocorrências de c - se a classe real for c, é mais provável que o classificador classifique c.

public class LeaveOneOut {
	
	private double accuracy;
	private double[] precision;
	private double[] recall; 
	
	
	public String toString() {
		return "Accuracy: " + accuracy + "\nPrecision: " + vec_to_str(precision) + "\nRecall: " + vec_to_str(recall);
	}
	
	//usada no toString()
	public static String vec_to_str(double[] vec) {
		String res = ""+vec[0];
		for (int i = 1; i<vec.length; i++) {
			res = res + ", " + vec[i];
		}
		return "[" + res + "]";
	}
	
	//construtor
	LeaveOneOut(String csvFile) {
		
		DataSet dataset = new DataSet(csvFile);
		
		int numClasses = dataset.numberOfClasses();
		System.out.println("nº de Classes = " + numClasses);
		
		this.precision = new double[numClasses];
		this.recall = new double[numClasses]; 
		
		
		int m = dataset.getM();
		double M = (double)m;
		System.out.println("Dimensão da amostra (m) = " + m);
		
		int n = dataset.getVeclen();
		System.out.println("Tamanho de cada observação (n) = " + n);
		
		
		int[][] vals = new int[m][n]; //preciso de remover a classe de cada vetor do datalist então crio array auxiliar
		
		ArrayList<int[]> datalist = dataset.getDataList(); //guardar dataList
		
		double[] correctClassifications = new double[numClasses]; //entrada c é nº de vezes que se classificou c quando era suposto fazê-lo
		double[] realClassCount = new double[numClasses]; //entrada c é nº de ocorrências reais da classe c
		double[] allClassifications = new double[numClasses]; //entrada c é nº de vezes que classificámos c no total
		
		
		for (int k = 0; k < m; k++) { //percorrer cada vetor do dataset, criar classificador C2 a partir de dataset sem essse vetor, e classificar o proprio vetor vals[k] com C2
			
			int[] aux = datalist.get(k);
			
			int realClass = aux[n-1]; //guardar a classe real do vetor
			
			realClassCount[realClass]++;
			
			vals[k] = DataSet.popclass(aux); //vai-se criando array de vetores do dataset sem a classe, para serem classificados
			
			DataSet T = new DataSet(csvFile, k); //criar dataSet sem o k-ésimo vetor (criou-se novo construtor no dataset para isto)
			
			T.supdomRefresh(vals[k]); //evita-se o caso de vals[k] ser o unico vetor em que certa variável toma o valor máximo do seu domínio (essencial pois sem isto dava erro para um dos datasets - ver nota no fim)
			
			
			
			int numClasses2 = 0; //para o caso improvável do vetor a retirar ser o único que toma um certo valor da classe (pois neste caso criavamos fibra vazia e MRFT dava erro)
			int[] countclasses = new int[numClasses]; //entrada c tem contagem da classe c no dataset T
			for (int i = 0; i < numClasses; i++) {
				double contagem = T.Count(n-1, i);
				countclasses[i] = (int)contagem;
				if (contagem > 0) {numClasses2++;}
			}
			
			DataSet[] fibers2 = new DataSet[numClasses2];
			MRFT[] mrfts2 = new MRFT[numClasses2];
			int[] freqs2 = new int[numClasses2];
			
			int c = 0; 
			
			while (c < numClasses) {
				if (countclasses[c]!=0) { //(se a classe ocorre no novo dataset)
					fibers2[c] = T.Fiber(c);
					mrfts2[c] = new MRFT(fibers2[c]);
					freqs2[c] = fibers2[c].getM();
				}
				c++;
			}
			
			Classifier C2 = new Classifier(mrfts2, freqs2);
			
			int classification = C2.Classify(vals[k]);
			
			if (classification == realClass) {
				correctClassifications[realClass]++;
				accuracy++;
			}
			
			allClassifications[classification]++;
			
		}
		
		
		accuracy = accuracy/M*100;
		
		for (int c = 0; c < numClasses; c++) {
			this.recall[c] = 100*correctClassifications[c]/realClassCount[c];
			this.precision[c] = 100*correctClassifications[c]/allClassifications[c];
		}
		
		
	}
	
	public static void main(String[] args) {
		
		
		
		LeaveOneOut test6 = new LeaveOneOut("thyroid.csv");
		System.out.println(test6);
		
	}

}


