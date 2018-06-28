package agência;
import java.util.ArrayList;
import java.util.Scanner;

class Agencia {
	private Repositório<Cliente> clientes;
	
	public Agencia(){
		clientes = new Repositório<Cliente>("clientes");
	}

	public boolean addCliente(String cpf){
		for(Cliente c: clientes.getAll()){
			if(c.getIdCliente().equals(cpf)){
				throw new RuntimeException("CPF já cadastrado");
			}
		}
		
		this.clientes.add(new Cliente(cpf));
		return true;
	}
	
	public boolean abrirNovaConta(String cpf){
		for(Cliente c: clientes.getAll()){
			if(c.getIdCliente().equals(cpf)){
				c.addConta(new Conta(Conta.ultIdConta++));
				return true;
			}
		}
		
		return false;
	}
	
	public Repositório<Cliente> getClientes(){
		return clientes;
	}
}
class Cliente {
	private String idCliente;
	private Repositório<Conta> contas;
	
	public Cliente(String idCliente){
		this.idCliente = idCliente;
		this.contas    = new Repositório<Conta>("contas");
		
		this.contas.add(new Conta(Conta.ultIdConta));
		Conta.ultIdConta++;
		if(contas == null){
			throw new RuntimeException("Ops, conta nula!");
		}
	}

	public boolean addConta(Conta conta){
		int qtd = 0;
		for(Conta c: contas.getAll()){
			if(c.isAtiva()){
				qtd++;
			}
		}
		
		if(qtd == 2){
			throw new RuntimeException("Limite de contas ativas estourado!");
		}
		
		this.contas.add(conta);
		return true;
	}
	
	public boolean encerrarConta(int numero){
		for(Conta c : contas.getAll()){
			if(c.getNumero() == numero){
				if(c.getSaldo() == 0){
					c.encerrar();
					return true;
				}
			}
		}
		
		return false;
	}
	
	public String getIdCliente() {
		return idCliente;
	}

	public Repositório<Conta> getContas() {
		return contas;
	}
	
	
}
class Conta {
	public static int ultIdConta = 1;
	
	private float saldo;
	private int numero;
	private Repositório<Operacao> extrato;
	private boolean ativa;
	
	public Conta(int numero){
		this.numero  = numero;
		this.saldo   = 0;
		this.extrato = new Repositório<Operacao>("extrato");
		this.ativa   = true;
	}
	
	public boolean depositar(float valor){
		if(valor <= 0){
			return false;
		}
		
		this.saldo += valor;
		this.extrato.add(new Operacao("deposito", valor, saldo));
		return true;
	}
	
	public boolean sacar(float valor){
		if(valor <= 0){
			throw new RuntimeException("Valor negativo");
		}
		
		if(valor > saldo){
			throw new RuntimeException("Tentativa de saque maior do que o saldo!");
		}
		
		this.saldo -= valor;
		this.extrato.add(new Operacao("saque", valor, saldo));
		return true;
	}
	
	public boolean transferir(Conta other, float valor){
		if(!other.isAtiva()){
			throw new RuntimeException("A conta destino está inativa");
		}
		
		if(this.sacar(valor)){
			other.depositar(valor);
			return true;
		}
		
		return false;
	}
	
	public void encerrar(){
		this.ativa = false;
	}
	
	public float getSaldo() {
		return saldo;
	}

	public int getNumero() {
		return numero;
	}

	public Repositório<Operacao> getExtrato() {
		return extrato;
	}

	public boolean isAtiva() {
		return ativa;
	}
	
	public String toString(){
		return numero + " - " + saldo + " - " + extrato + " - "+ ativa;
	}
}
class Operacao {
	private String descricao;
	private float valor;
	private float saldoParcial;
	
	public Operacao(String descricao, float valor, float saldoParcial){
		this.descricao = descricao;
		this.valor = valor;
		this.saldoParcial = saldoParcial;
	}
	
	public String getDescricao(){
		return descricao;
	}
	
	public void setDescricao(String descricao){
		this.descricao = descricao;
	}
	

	public String toString(){
		return descricao + " " + valor + " " + saldoParcial;
	}
}

class GerenciadorDeLogin{
	private Repositório<Cliente> clientes;
	private Cliente cliet;
	
	public GerenciadorDeLogin(Repositório<Cliente> clientes) {
		this.clientes = clientes;
		cliet = null;
	}
	
	void login(String idCliente){
		if(cliet != null)
			throw new RuntimeException("fail: ja existe alguem logado");
		this.cliet = clientes.get(idCliente);
	}
	
	void logout(){
		if(cliet == null)
			throw new RuntimeException("fail: ninguem logado");
		cliet = null;
	}
	
	public Cliente getCliente(){
		if(cliet == null)
			throw new RuntimeException("fail: ninguem logado");
		return cliet;
	}
}
class Controller{
	Repositório<Cliente> clientes;
	Repositório<Conta> contas;
	Repositório<Operacao> extrato;
	Agencia agencia;
	Cliente cliente;
	Conta conta;
	String idCliente;
	int numero;
	GerenciadorDeLogin gerLogin;
	
	
	
	public Controller() {
		clientes = new Repositório<Cliente>("clientes");
		contas = new Repositório<Conta>("contas");
		extrato = new Repositório<Operacao>("extrato");
		agencia = new Agencia();
		cliente = new Cliente(idCliente);
		conta = new Conta(numero);
		
		gerLogin = new GerenciadorDeLogin(clientes);
	}

    //nossa funcao oraculo que recebe uma pergunta e retorna uma resposta
    public String oracle(String line){
        String ui[] = line.split(" ");

        if(ui[0].equals("help"))
            return "addCliente _cpf, abrirConta _cpf, encerrarConta _numero \n" + 
                   "login _username , logout \n" + 
                   "sacar _valor, transferir _numero _valor depositar _valor";
        else if(ui[0].equals("addCliente")) {
        	clientes.add(ui[1], new Cliente(ui[1]));
        	return "done: conta adicionada ao cliente";
        }
        else if(ui[0].equals("abrirConta")) {
        	agencia.abrirNovaConta(ui[1]);
        	return "done: conta adicionada ao cliente";
        }	
        else if(ui[0].equals("encerrarConta"))
        	cliente.encerrarConta(Integer.parseInt(ui[1]));
        else if(ui[0].equals("depositar"))
        	conta.depositar(Float.parseFloat(ui[1]));
        else if(ui[0].equals("transferir"))
        	conta.transferir(new Conta(Integer.parseInt(ui[1])),Float.parseFloat(ui[2]));
        else if(ui[0].equals("sacar"))
        	conta.sacar(Float.parseFloat(ui[1]));
        else if(ui[0].equals("showAll")) {
        	String saida = " ";
        	for(Cliente c : clientes.getAll())
				saida += c.getIdCliente() + "\n";
			return saida;
        }
		else if(ui[0].equals("login"))
		    gerLogin.login(ui[1]);
		else if(ui[0].equals("logout"))
        	gerLogin.logout();
        
        else
            return "comando invalido";
        return "done";
    }
}

public class IO {
    //cria um objeto scan para ler strings do teclado
    static Scanner scan = new Scanner(System.in);
    
    //aplica um tab e retorna o texto tabulado com dois espaços
    static private String tab(String text){
        return "  " + String.join("\n  ", text.split("\n"));
    }
    
    public static void main(String[] args) {
        Controller cont = new Controller();
        System.out.println("Digite um comando ou help:");
        while(true){
            String line = scan.nextLine();
            try {
                //se não der problema, faz a pergunta e mostra a resposta
                System.out.println(tab(cont.oracle(line)));
            }catch(Exception e) {
                //se der problema, mostre o erro que deu
                System.out.println(tab(e.getMessage()));
            }
        }
    }
}
