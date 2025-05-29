package modelo.dao;

import dados.Conexao;
import modelo.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDao implements Dao<Produto> {
    private Connection con = null;

    public ProdutoDao() throws SQLException {
        con = Conexao.getConnection();
        String sqlCreateTable = "CREATE TABLE produto(id INT PRIMARY KEY AUTO_INCREMENT, titulo VARCHAR(100), preco DECIMAL(10,2), quantidade INT);";
        PreparedStatement stmt = con.prepareStatement(sqlCreateTable);

        stmt.execute();
    }

    @Override
    public void salvar(Produto produto) throws SQLException {
        con = Conexao.getConnection();
        String sqlInsert = "INSERT INTO produto(titulo, preco, quantidade) VALUES (?, ?, ?);";
        PreparedStatement stmt = con.prepareStatement(sqlInsert);
        stmt.setString(1, produto.getTitulo());
        stmt.setDouble(2, produto.getPreco());
        stmt.setInt(3, produto.getQuantidade());

        stmt.execute();
    }

    @Override
    public List<Produto> listar() throws SQLException {
        con = Conexao.getConnection();
        String sqlSelect = "SELECT * FROM produto";
        List<Produto> produtos = new ArrayList<>();

        PreparedStatement stmt = con.prepareStatement(sqlSelect);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Produto p = new Produto();
            p.setId(rs.getInt("Id"));
            p.setTitulo(rs.getString("Titulo"));
            p.setPreco(rs.getDouble("Preco"));
            p.setQuantidade(rs.getInt("Quantidade"));

            produtos.add(p);
        }
        return produtos;
    }

    @Override
    public void alterar(int id, Produto produto) throws SQLException {
        con = Conexao.getConnection();
        String sqlUpdate = "UPDATE produto SET titulo = ?, preco = ?, quantidade  = ? WHERE id = ?;";
        PreparedStatement stmt = con.prepareStatement(sqlUpdate);
        stmt.setString(1, produto.getTitulo());
        stmt.setDouble(2, produto.getPreco());
        stmt.setInt(3, produto.getQuantidade());
        stmt.setInt(4, produto.getId());
        stmt.execute();
    }

    @Override
    public void deletar(int id) throws SQLException {
        con = Conexao.getConnection();
        String sqlDelete = "DELETE FROM produto WHERE id = ?";
        PreparedStatement stmt = con.prepareStatement(sqlDelete);
        stmt.setInt(1, id);
        stmt.execute();

    }

}
