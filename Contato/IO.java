package Contato;

import java.util.ArrayList;
import java.util.Scanner;

class Contato {
	String nome;
	ArrayList<Telefone> telefones;
	
	public Contato(String nome) {
		this.nome = nome;
		this.telefones = new ArrayList<Telefone>();
	}
	public String toString() {
		return "" + this.nome + "" + this.telefones;
	}
	public boolean addFone(Telefone telefone) {
		for(Telefone tel : this.telefones)
			if(tel.foneid.equals(telefone.foneid)) {
				throw new RuntimeException("Fone id ja existe");
			}
		this.telefones.add(new Telefone(telefone.foneid,telefone.numero));
		return true;
	}
	public boolean rmFone(String foneid) {
		for(int i = 0; i < this.telefones.size(); i++)
			if(this.telefones.get(i).foneid.equals(foneid)) {
				telefones.remove(i);
				return true;
			}
	return false;
	}   
	
	
}

class Telefone{
	String foneid;
	int numero;
	
	public Telefone(String foneid, int numero) {
		this.foneid = foneid;
		this.numero = numero;
	}
	
	public String toString() {
		return "" + this.foneid + " " + this.numero;
	}
}

class Controller {
	Contato contato;
	
	public Controller() {
		contato = new Contato(null);
	}

	public String oracle(String line){
		String ui[] = line.split(" ");
		
		if(ui[0].equals("help"))
			return "show, init _nome, add _id _numero, rm _id";
		else if(ui[0].equals("init"))
			contato = new Contato(ui[1]);
		else if(ui[0].equals("show"))
			return "" + contato;
		else if(ui[0].equals("add"))
			 contato.addFone(new Telefone(ui[1], Integer.parseInt(ui[2])));
		else if(ui[0].equals("rm")) {
			 contato.rmFone(ui[1]);
		}
		else
			return "comando invalido";
		return "done";
	}
}

public class IO {
	static Scanner scan = new Scanner(System.in);

	 static String tab(String text){
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
