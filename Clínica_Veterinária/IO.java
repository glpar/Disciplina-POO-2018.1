package Cl�nica_Veterin�ria;

import java.util.ArrayList;
import java.util.Scanner;

class Animal{
	public String nomea;
	public int idani;
	public String especie;
	public Cliente dono;
	
	public Animal(int idani, String nome, String especie, Cliente dono) {
		this.idani = idani;
		this.nomea = nome;
		this.especie = especie;
		this.dono = dono;
	}
	public String toString() {
		return "[" + this.idani + ":"+ this.nomea + ":" + this.especie + "]";
	}
}

class Cliente{
	protected String idCli;
	protected String nome;
	private Reposit�rio<Animal> animais;

	public Cliente(String idCli, String nome) {
		this.idCli = idCli;
		this.nome = nome;
		this.animais = new Reposit�rio<Animal>("animal");
	}
	public void addAni(Animal ani) {
		if(!animais.getAll().isEmpty()) {
			for(Animal an : animais.getAll())
				if(an.nomea == ani.nomea) {
					throw new RuntimeException("Animal j� existe");
				}
		}
		int idani = ++ServicoCl�nica.nextAniID;
		ani.idani = idani;
		this.animais.add(ani);
		return;
	}
	public String getIdCli() {
		return this.idCli;
	}
	
	public String getNome() {
		return this.nome;
	}

	public Reposit�rio<Animal> getAnimais() {
		return animais;
	}
	
	public String animaisCliente() {
		String osAnimais = "";
		for(Animal ani : this.animais.getAll()) {
			osAnimais += ani.toString();
		}
		return this.toString() + " " + osAnimais;
	}
	
	public String toString() {
		return "cli " + idCli + ": " + nome;
	}
	
}

class Servi�o {
	public  String idSer;
	public int valor;
	
	
	public Servi�o(String idSer,int valor) {
		this.idSer = idSer;
		this.valor = valor;
	}
	public String getidSer() {
		return this.idSer;
	}
	public float getvalor() {
		return this.valor;
	}
	public String toString() {
		return "[" + idSer + " " + valor + "] ";
	}
}

class Venda{
	public String nomea;
	public String idCli;
	public String idSer;
	public int venID;
	
	public Venda(String idCli, String nomea, String idSer) {
		this.idCli = idCli;
		this.nomea = nomea;
		this.idSer = idSer;
	}
	
	public String toString() {
		return "[" + venID + " " + idCli + " " + nomea + " " + idSer + "] ";	
	}
}
class ServicoCl�nica  {
	static int nextSerID;
	static int nextAniID;
	static int nextVenId;
	private Reposit�rio<Servi�o> servi�os;
	private Reposit�rio<Animal> animais;
	private Reposit�rio<Cliente> clientes;
	private Reposit�rio<Venda> vendas;
	private Cliente clien;
	private float saldo;
	
	public ServicoCl�nica() {
		nextAniID = 0;
		nextVenId = 0;
		servi�os = new Reposit�rio<Servi�o>("servi�o");
		animais = new Reposit�rio<Animal>("animal");
		clientes = new Reposit�rio<Cliente>("clientes");
		vendas = new Reposit�rio<Venda>("venda");
		clien = null;
		saldo = 0;
		
	}
	
	public void addCli(Cliente cliente) {
		if(!clientes.getAll().isEmpty()) {
			for(Cliente cl : clientes.getAll())
				if(cl.getIdCli().equals(cliente.getIdCli())) {
					throw new RuntimeException("Cliente j� existe");
			}
		}
		clientes.add(cliente.getIdCli(), cliente);
		return;
	}
	public void addSer(Servi�o servi�o) {
		if(!servi�os.getAll().isEmpty()) {
			for(Servi�o se : servi�os.getAll())
				if(se.getidSer().equals(servi�o.getidSer())) {
					throw new RuntimeException("Servi�o j� existe");
			}
		}
		servi�os.add(servi�o.getidSer(),servi�o);
		return;
	}
	public void addAni(String idCli, String nomea, String especie) {
		Cliente dono = null;
		if(!clientes.getAll().isEmpty())
			for(Cliente cl : clientes.getAll())
				if(cl.getIdCli().equals(idCli)) {
					dono = cl;
					break;
				}
		if(dono != null) {
			Animal novoAni = new Animal(0, nomea,especie, dono);
			dono.addAni(novoAni);
			animais.add(novoAni);
		}
		
	}
	public void vender(String idCli, String nomea,String idSer) {
		Servi�o servi�o = null;
		if(!servi�os.getAll().isEmpty()) {
			for(Servi�o se : servi�os.getAll())
				if(se.getidSer().equals(idSer)) {
					servi�o = se;
					break;
				}
			if(servi�o == null) {
				throw new RuntimeException("Servi�o n�o existe");
			}
		}
		
		if(!clientes.getAll().isEmpty()) {
			Cliente cliente = null;
			for(Cliente cl : clientes.getAll())
				if(cl.getIdCli().equals(idCli)) {
					cliente = cl;
					break;
				}
			
			if(cliente != null) {
				Animal animal = null;
				for(Animal an : cliente.getAnimais().getAll())
					if(an.nomea.equals(nomea)) {
						animal = an;
						break;
					}
				if(animal != null) {
					Venda venda = new Venda(idCli, nomea, idSer);
					venda.venID = ++ServicoCl�nica.nextVenId;
					vendas.add(venda);
					saldo += servi�o.getvalor();
				} else {
					throw new RuntimeException("Animal n�o existe");
				}	
			}else {
				throw new RuntimeException("Cliente n�o existe");
			}
		} else {
			throw new RuntimeException("N�o h� clientes no sistema");
		}
	
		return;
	
	}
	public float saldo() {
		return saldo; 
	}
	public ArrayList<Cliente> getAllCli() {
		return clientes.getAll();
		
	}
	public ArrayList<Animal> getAllAni() {
		return animais.getAll();
		
	}
	public ArrayList<Servi�o> getAllSer() {
		return servi�os.getAll();
		
	}
	public ArrayList<Venda> getAllVen() {
		return vendas.getAll();
		
	}
	public Cliente getCliente() {
		return clien;
	}
}
class Controller{
	ServicoCl�nica servc;
	
	public Controller() {
		servc = new ServicoCl�nica();
		 
	}

    public String oracle(String line){
        String ui[] = line.split(" ");

        if(ui[0].equals("help"))
            return "Adicionar: nwcli _ idCli _nome, nwani _idCli _nome _especie, nwser _idSer _valor, nwven _idCli _nome _idSer\n" + 
                   "Mostrar: laani, laser, laven, lacli, lscli _idCli, saldo\n";
        else if(ui[0].equals("nwcli")) {
        	String nome = "";
        	for(int i = 2; i < ui.length; i++)
        		nome += ui[i] + " ";
        	servc.addCli(new Cliente(ui[1], nome));
        }	
        else if(ui[0].equals("lacli")) {
        	String saida = "";
        	for(Cliente cli : servc.getAllCli())
        		saida += cli.toString() + "\n";
        	return saida;
        }
        else if(ui[0].equals("nwani")) {
        	servc.addAni(ui[1], ui[2], ui[3]);	
        }
        else if(ui[0].equals("nwser"))
        	servc.addSer(new Servi�o(ui[1], Integer.parseInt(ui[2])));	
        else if(ui[0].equals("nwven"))
        	servc.vender(ui[1], ui[2], ui[3]);	
        else if(ui[0].equals("laani")) {
        	String saida = "";
        	for(Animal ani : servc.getAllAni())
        		saida += ani.toString() + "\n";
        	return saida;
        }
        else if(ui[0].equals("laser")) {
        	String saida = "";
        	for(Servi�o ser : servc.getAllSer())
        		saida += ser.toString() + "\n";
        	return saida;
        }
        else if(ui[0].equals("laven")) {
        	String saida = "";
        	for(Venda ven : servc.getAllVen())
        		saida += ven.toString() + "\n";
        	return saida;
        }
        else if(ui[0].equals("saldo")) {
        	return "" + servc.saldo();
        }
        else if(ui[0].equals("lscli")) {
        	Cliente clienteMostrar = null;
        	for(Cliente cli : servc.getAllCli())
        		if(cli.getIdCli().equals(ui[1])) {
        			clienteMostrar = cli;
        			break;
        	}
        	if(clienteMostrar != null) {
        		return clienteMostrar.animaisCliente();
        	} else {
        		return "cliente n�o existe";
        	}
        		
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