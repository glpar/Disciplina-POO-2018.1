package Agiota;

import java.util.ArrayList;
import java.util.Scanner;

class Sistema{
    public float qtddin;
    ArrayList<Cliente> clientes;
    ArrayList<Transacao> trans;
    public float saldo = 0;
    public int id = 0;

    public Sistema(float qtddin){
        this.qtddin = qtddin;
        clientes = new ArrayList<Cliente>();
        saldo += qtddin;
        trans = new ArrayList<Transacao>(); 
    }
    
    public ArrayList<Transacao> getTrans(){
    	return trans;
    	
    }
    
    public void nwcli(String clienteid, String nome){
        for(Cliente cli : this.clientes){
            if(cli.clienteid.equals(clienteid)){
                throw new RuntimeException(" fail: cliente ja existe");
            }
        }
        clientes.add(new Cliente(clienteid, nome));
    }
    
    public void Cadastrans(String clienteid , float valor){
        if(valor <= saldo){
            saldo += valor;
            trans.add(new Transacao(clienteid,valor,id));
            id ++ ;
        }
        else{
            throw new RuntimeException ("fail: fundos insuficientes");  
        }
    }
    
    public void emprestar(String clienteid, float valor){
        for(Cliente cli: clientes){
        	if(valor <= saldo) {
        		if(cli.clienteid.equals(clienteid)){
        			cli.saldo += valor * (-1);
        			this.Cadastrans(clienteid, valor * (-1));
        			return;
            }
        	}
        	else if(valor > saldo) {
        		throw new RuntimeException ("Saldo Insuficiente!");
        	}
        }
        throw new RuntimeException ("Cliente não existe!");
    }

    public String toString(){
        return "done: sistema iniciado com: " +saldo+ " "+ clientes.toString();
    }
    
    public String getAllCli(){
        String lacli = " ";
        for(int i = 0; i < clientes.size(); i++){
            lacli += "" + this.clientes.get(i).Saldo();
        }
        return lacli; 
    }  
    
    public void lscli(String clienteid){
        int i = 0;
        for(Cliente cli : clientes){
            if(cli.clienteid.equals(clienteid)){
                System.out.println(cli.Saldo());
                while (trans.get(i).clienteid.equals(clienteid)){
                    System.out.println(trans.get(i).toString());
                i++;
                }
            }
         return;
        }
       throw new RuntimeException("Cliente não existe");
    }
    
    public void matartrans(String clienteid) {
        for(int i = 0; i < trans.size(); i++){
        	if(trans.get(i).clienteid.equals(clienteid)) {
        		trans.remove(i);
        		return;
        	}
        }
    }
    
    public void matar(String clienteid) {
        for( int i = 0; i < clientes.size(); i++) {
        	if(clientes.get(i).clienteid.equals(clienteid)) {
                clientes.remove(i);
                matartrans(clienteid);
                return;
            }
        }
    }
    
    public boolean receber(String clienteid, float valor){
        for(Cliente cli : clientes) {
        	if(cli.clienteid.equals(clienteid)) {
        		if (cli.saldo < 0 && valor * (-1) >= cli.saldo) {
        			cli.saldo = cli.saldo + valor;
        			qtddin = qtddin + valor;
                    this.trans.add(new Transacao(clienteid, valor, id));
                    id++;
                    return true;
                    
                }
        		else if(valor > cli.saldo)
        		throw new RuntimeException("fail: valor maior que divida");
        	}	
        }
        throw new RuntimeException("Cliente não encontrado!");
    }
}

class Transacao {
    public String clienteid;
    public float deve;
    public int id = 0;
    public float total = 0;
    
    public Transacao(String clienteid, float value, int id) {
        this.clienteid = clienteid;
        this.deve = value;
        this.id = id;
        this.total = deve + total;
    }
    public float getdeve() {
    	return deve;
    }
    
    public String toString(){
    	return "id: " + id + " [" + clienteid + " " + deve + "]";
    }      
}

 class Cliente{
     public String clienteid;
     public String nome;
     public boolean isAlive = true;
     public float saldo;
    
    public Cliente (String clienteid, String nome){
        this.clienteid = clienteid;
        this.nome = nome;
        saldo = 0;
    }

    public String Saldo(){
        return clienteid + ":" + nome + ":" + saldo  + " ";
    }
    
 }
 
   class Controller {
		Sistema sistema;
		Transacao trans;
		public int ide = 0;
		public int idr = 0;
		
		public Controller() {
			sistema = new Sistema(0);
			trans = new Transacao(null,0,0);
	}

		public String oracle(String line){
			String ui[] = line.split(" ");

			if(ui[0].equals("help"))
				return "show, init _dinheiro, nwcli _apelido _nome, emprestar _apelido _valor \n" +
				"matar _apelido, receber _apelido _valor \n"+
				"latran, lacli, lscli";
			else if(ui[0].equals("init")) {
				sistema = new Sistema(Float.parseFloat(ui[1]));
				return "done: sistema iniciado com " + ui[1] + " reais";
			}
			else if(ui[0].equals("show"))
				return "" + sistema;
			else if(ui[0].equals("nwcli")) {
				String nome = "";
        		for(int i = 2; i < ui.length; i++)
        			nome += ui[i] + " ";
				sistema.nwcli(ui[1],nome);
			}
			else if(ui[0].equals("matar")) {
			    sistema.matar(ui[1]);
			}
			else if(ui[0].equals("emprestar")) {
				sistema.emprestar(ui[1],Float.parseFloat(ui[2]));
				ide++;
				return "id: " + (ide - 1) + " [" + ui[1] + " " + Float.parseFloat(ui[2]) * (-1) + "]";
				
			}
			else if(ui[0].equals("receber")) {
				sistema.receber(ui[1],Float.parseFloat(ui[2]));
				idr++;
				return "id: " + (idr - 1) + " [" + ui[1] + " " + Float.parseFloat(ui[2]) + "]";
			}
			else if(ui[0].equals("latran")) {
				return "" + sistema.getTrans();
			}	
			else if(ui[0].equals("lacli")) {
		    	return ""+ sistema.getAllCli();
			}
			else if(ui[0].equals("lscli")) {
		        sistema.lscli(ui[1]);
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