package classes;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class DataSet implements Serializable {
	    private static final long serialVersionUID = 1L;
	    
	    //atributos
		private ArrayList<int []> dataList;
		private Count count; //guarda a contagem do nª de ocorrências de valores para variaveis/pares de variaveis
		private int veclen; //tamanho dos vetores do dataList
		private int m; //dimensão do dataList (numero de observações)
		private int[] supdom; //vetor onde entrada i é valor máximo tomado pela variável i
		
		//Construtor da classe:
		public DataSet(String csvFile)  {
			this.dataList = new ArrayList<int []>();
			String line;
			BufferedReader br;
			
				try {
					br = new BufferedReader(new FileReader(csvFile));
					boolean inicio = true; //no inicio criar count vazio c/ dimensões desejadas
					this.m = 0;
					while((line = br.readLine()) != null) {
						int[] v = convert(line);
						dataList.add(v);
						m++;
						if (inicio) {
							this.veclen = v.length;
							this.count = new Count(veclen);
							this.supdom = new int[veclen];
							inicio = false;
						}
						this.count.AddVec(v);
						this.supdomRefresh(v);
					}
						br.close();
						
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
		}
		
		//Construtor auxiliar para o teste Leave One Out (constrói dataset da mesma maneira mas deixa de fora a k-ésima observação)
		public DataSet(String csvFile, int k)  {
			this.dataList = new ArrayList<int []>();
			String line;
			BufferedReader br;
			
				try {
					int counter = 0;
					br = new BufferedReader(new FileReader(csvFile));
					boolean inicio = true; //no inicio criar count vazio c/ dimensões desejadas
					this.m = 0;
					while((line = br.readLine()) != null) {
						if (k!=counter) {
							int[] v = convert(line);
							dataList.add(v);
							m++;
							if (inicio) {
								this.veclen = v.length;
								this.count = new Count(veclen);
								this.supdom = new int[veclen];
								inicio = false;
							}
							this.count.AddVec(v);
							this.supdomRefresh(v);
						}
						counter++;
					}
						br.close();
						
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
		}
		
		//construtor auxiliar para o método Fiber (cria DataSet vazio)
		DataSet(DataSet T) {
			int dim = T.veclen-1;
			this.dataList = new ArrayList<int[] >();
			this.count = new Count(dim); //dim é tamanho dos vetores de dados que vou ter no DataSet
			this.veclen = dim;
			this.m = 0;
			this.supdom = popclass(T.supdom); //domínio não muda de fibra para fibra
		}
		 
		//usa-se no LeaveOneOut
		public ArrayList<int[]> getDataList() {
			return dataList;
		}
		
		public int getVeclen() {
			return veclen;
		}
		
		public int getM() {
			return m;
		}
		
		public int getSupdom(int i) {
			return supdom[i];
		}
		

		@Override
		public String toString() {
			String s="[";
			if (dataList.size()>0) s+=Arrays.toString(dataList.get(0));
			for (int i=1; i<dataList.size();i++)
				s+= "\n" + Arrays.toString(dataList.get(i));
			s+="]";
				
			return "Size= " + dataList.size() + "\nDataset = " + s;
		}
		
		//função auxiliar usada no construtor:
		public static int [] convert (String line) {
		String cvsSplitBy = ",";
		String[] strings     = line.split(cvsSplitBy);
		int[] stringToIntVec = new int[strings.length];
		for (int i = 0; i < strings.length; i++)
			stringToIntVec[i] = Integer.parseInt(strings[i].trim()); //trim() para retirar espaços em branco
		return stringToIntVec;
		}
		
		//auxiliar usada no Fiber
		private void Add(int[] vector) {
			dataList.add(vector);
			count.AddVec(vector);
			m++;
		}
		
		//auxiliar para Fiber (remove o elemento da classe de cada vetor deixando so elementos do dominio)
		//public pois usa-se no LeaveOneOut
		public static int[] popclass(int[] vec) {
			int l = vec.length - 1;
			int[] res = new int[l];
			for (int i = 0; i < l; i++) {
				res[i] = vec[i];
			}
			return res;
		}
		
		
		public DataSet Fiber(int classe) {
			int dls = this.m;
			int vlen = this.veclen;
			DataSet res = new DataSet(this);
			for (int i = 0; i < dls; i++) {
				int[] dado = dataList.get(i);
				if (dado[vlen-1] == classe) {
					res.Add(popclass(dado));
				}
			}
			return res;
		}
		

		public double Count(int i , int j, int xi, int xj) {
			return this.count.getCount(i, j, xi, xj);
		}
		public double Count(int i , int xi) {
			return this.count.getCount(i, xi);
		}
		
		//atualiza supdom aquando da adição de novo vetor ao DataSet
		//só usamos no LeaveOneOut
		public void supdomRefresh(int[] vec) {
			for (int i = 0; i < vec.length; i++) {
				if (vec[i] > supdom[i]) supdom[i] = vec[i];
			}
		}
		
		//da numero de classes do dataset (Não é necessária mas facilita o uso do código)
		public int numberOfClasses() {
			return supdom[veclen-1]+1;
		}
		
		
	
		public static void main(String[] args) {
				DataSet d = new DataSet("bcancer.csv");
				
				System.out.println(d);
				System.out.println(d.Fiber(0));
				
				System.out.println(d.Fiber(0).Count(0, 1));
				
				System.out.println(d.Fiber(0).Count(0,1,1,0));
				System.out.println(d.Fiber(0).Count(0,1,0,1));
				
				System.out.println(d.Fiber(0).Count(0, 0));
				
				
				System.out.println(d.numberOfClasses());
				
				
				DataSet s = new DataSet("letter.csv");
				System.out.println(s.Fiber(0));
				
		}
}


