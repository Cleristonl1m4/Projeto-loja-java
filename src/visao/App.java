package visao;

import dados.Conexao;
import modelo.Produto;
import modelo.dao.ProdutoDao;
import org.h2.server.Service;
import org.h2.tools.Server;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class App {
    public static ProdutoDao produtoDao = null;
    public static void main(String[] args) throws SQLException {
        Server.createWebServer("-web","-webAllowOthers","-webPort","8082").start();
        System.out.println("Console h2 acessivel em: htt://localhost:8082");

        produtoDao = new ProdutoDao();

        int op;

        do{
            Scanner scanner = new Scanner(System.in);
            System.out.println("==================================");
            System.out.println("SISTEMA - GESTAO DE PRODUTOS");
            System.out.println("1: Cadastrar Produto");
            System.out.println("2: Lista Produtos");
            System.out.println("3: Alterar Produto");
            System.out.println("4: Deletar Produto");
            System.out.println("5: Sair");
            System.out.println("==================================");
            System.out.print("Opção: ");
            op = scanner.nextInt();


            switch (op){
                case 1:{
                    cadrastarProduto(scanner, produtoDao);
                    break;
                }
                case 2:
                {
                    listarProduto(produtoDao);
                    break;
                }
                case 3:
                {
                    alterarProduto(scanner,produtoDao);
                    break;
                }
                case 4:
                {
                    deletarProduto(produtoDao,scanner);
                    break;
                }
                case 5:
                {
                    JOptionPane.showMessageDialog(null,"Saindo do Sistema");
                }
            }
        }while (op != 5);

    }

    public static void cadrastarProduto(Scanner scanner, ProdutoDao produtoDao) throws SQLException {
        Produto produto = new Produto();
        System.out.println("Digite o nome do produto: ");
        scanner.nextLine();
        produto.setTitulo(scanner.nextLine());
        System.out.println("Digite o preco do produto: ");
        produto.setPreco(scanner.nextDouble());
        System.out.println("Digite a quantidade: ");
        produto.setQuantidade(scanner.nextInt());

        produtoDao.salvar(produto);
        JOptionPane.showMessageDialog(null,"Produto salvo com sucesso!");
    }

    public static void listarProduto(ProdutoDao produtoDao) throws SQLException{
        for (Produto p: produtoDao.listar()){
            System.out.println("Id: "+p.getId());
            System.out.println("Nome: "+p.getTitulo());
            System.out.println("Preço: "+p.getPreco());
            System.out.println("Quantidade em estoque: "+p.getQuantidade());
            System.out.println("\n-----------------------------------------\n");
        }
    }

    public static void deletarProduto(ProdutoDao produtoDao, Scanner scanner) throws SQLException{
        System.out.println("Digite o ID do produto que deseja excluir: ");
        int id = scanner.nextInt();
        produtoDao.deletar(id);
        JOptionPane.showMessageDialog(null,"Produto removido com sucesso!");
    }

    public static void alterarProduto(Scanner scanner, ProdutoDao produtoDao) throws SQLException{
        System.out.println("Digite o ID do Produto que deseja alterar: ");
        int id = scanner.nextInt();
        Produto produto = new Produto();
        System.out.println("Digite o nome do produto: ");
        scanner.nextLine();
        produto.setTitulo(scanner.nextLine());
        System.out.println("Digite o preco do produto: ");
        produto.setPreco(scanner.nextDouble());
        System.out.println("Digite a quantidade: ");
        produto.setQuantidade(scanner.nextInt());

        produtoDao.alterar(id, produto);

        JOptionPane.showMessageDialog(null,"Produto alterado com Sucesso!");
    }
}
