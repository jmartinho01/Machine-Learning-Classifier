package classes;

import java.io.Serializable;

//Classe usada na Classe Count


//identificamos cada (xi,xj) e a sua contagem c como um objeto KeyVal onde o key=(xi,xj) e val=c
//Classe útil para guardar logo a contagem de cada par como um double, e é mais elegante que vetor de 3 inteiros
public class KeyVal implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int[] key;
	private double val;
	
	//construtor
	KeyVal(int xi, int xj) {
		super();
		this.key = new int[2];
		key[0] = xi; key[1] = xj;
		this.val = 1;
	}
	
	public void Add() {
		val++;
	}
	
	//getters
	public int[] getKey() {
		return key;
	}
	
	public double getVal() {
		return val;
	}
	
	public String toString() {
		return "[" + key[0]+ "," + key[1]+"]" + " " + val + " ";
	}
}
