package JunkFood;

import java.util.ArrayList;
import java.util.Scanner;

class Espiral{
	String nome;
	int qtd;
	float preço;
	
	public Espiral(String nome, int qtd, float preço) {
		this.nome = nome;
		this.qtd = qtd;
		this.preço = preço;
	}
	
	public String toString() {
		return "[" + this.nome + " : " + this.qtd + " U " + ":" + this.preço + " RS" + "]";
	}
}

class Maquina{
	float saldo;
	float lucro;
	ArrayList<Espiral> espirais;
	int espira;
	int max;
	
	public Maquina(int qtdEspirais, int maxProdutos){
		this.espirais = new ArrayList<Espiral>();
		for(int i = 0; i < qtdEspirais; i++)
			this.espirais.add(new Espiral("-", 0, 0));
	}
	
	public boolean alterarEspiral(int indice, String nome, int qtd,float preço) {	
		if(indice > (espirais.size() - 1)) {
			throw new RuntimeException("Indice não existe");	
		}
		this.espirais.get(indice).nome = nome;
		this.espirais.get(indice).qtd = qtd;
		this.espirais.get(indice).preço = preço;
		return true;
	}
	
	public boolean remover(int indice) {
		if(indice > (espirais.size() - 1)) {
			throw new RuntimeException("Indice não existe");
		}
		this.espirais.get(indice).nome = "-";
		this.espirais.get(indice).qtd = 0;
		this.espirais.get(indice).preço = 0;
		return true;
	}
	public boolean inserirDinheiro(float value) {
		saldo += value;
		return true;
	}
	public float getSaldo() {
		return saldo;
	}
	public float pedirTroco() {
		float troco = saldo;
		saldo = troco - saldo;
		return troco;
	}
	public String toString() {
		String saida = "";
		for(int i = 0; i < espirais.size(); i++)
			saida += i + " " + espirais.get(i) + "\n";
		return saida;
	}
	public boolean vender(int indice) {
		if(indice > (espirais.size() - 1)) {
			throw new RuntimeException("Indice não existe");
		}
		for(int i = 0; i < espirais.size(); i++) { 
			Espiral es = espirais.get(i);
			if(es == null){
				throw new RuntimeException("Espiral sem produtos");
			}
			else if(es == espirais.get(indice)) {
				if(es.qtd > 0 ) {
				if(saldo >= es.preço) {
					es.qtd = es.qtd - 1;
					saldo = saldo - es.preço;
					System.out.println("Comprou um " + es.nome + ". saldo: " +  saldo);
					return true;
				}
				else if(saldo < es.preço) {
					throw new RuntimeException("Saldo insuficiente");
				}
				}
				else 
					throw new RuntimeException("Produto não disponível ou saldo insuficiente");
			}
			}

		
	return false;
}
}

class Controller{
    Maquina maq;
    static final int DEFAULT_ESPIRAIS = 3;
    static final int DEFAULT_MAX = 5;
    public Controller() {
        maq = new Maquina(DEFAULT_ESPIRAIS, DEFAULT_MAX);
    }
    
    private float toFloat(String s) {
        return Float.parseFloat(s);
    }
    
    public String oracle(String line){
        String ui[] = line.split(" ");

        if(ui[0].equals("help"))
            return "show, init _espirais _maximo, set _indice _nome _qtd __preço, remover _indice, dinheiro _valor, saldo _valor, troco";
        else if(ui[0].equals("init"))
            maq = new Maquina(Integer.parseInt(ui[1]), Integer.parseInt(ui[2]));
        else if(ui[0].equals("show"))
            return "Saldo: " + maq.getSaldo() + "\n" + maq;
        else if(ui[0].equals("set"))
        	maq.alterarEspiral(Integer.parseInt(ui[1]), (ui[2]), Integer.parseInt(ui[3]), Float.parseFloat(ui[4]));
        else if(ui[0].equals("remover"))
        	maq.remover(Integer.parseInt(ui[1]));
        else if(ui[0].equals("dinheiro"))
        	maq.inserirDinheiro(Float.parseFloat(ui[1]));
        else if(ui[0].equals("saldo"))
        	return "" + maq.getSaldo();
        else if(ui[0].equals("troco"))
        	return "Você recebeu " + maq.pedirTroco();
        else if(ui[0].equals("comprar")) {
        	maq.vender(Integer.parseInt(ui[1]));
        }
        else
            return "comando invalido";
        return "done";
    }
}

public class IO {
	    static Scanner scan = new Scanner(System.in);
	    
	    static private String tab(String text){
	        return "  " + String.join("\n  ", text.split("\n"));
	    }
	    
	    public static void main(String[] args) {
	        Controller cont = new Controller();
	        System.out.println("Digite um comando ou help:");
	        while(true){
	            String line = scan.nextLine();
	            try {
	                System.out.println(tab(cont.oracle(line)));
	            }catch(Exception e) {
	                System.out.println(tab(e.getMessage()));
	            }
	        }
	    }
	}