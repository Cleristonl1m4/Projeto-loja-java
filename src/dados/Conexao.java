package dados;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    private static Connection con = null;
    private static String USER = "sa";
    private static String PASS = "";
    private static final String URL = "jdbc:h2:mem:loja";

    private Conexao(){}

    public static Connection getConnection() throws SQLException {
        if(con == null){
            con = DriverManager.getConnection(URL, USER, PASS);
            JOptionPane.showMessageDialog(null,"Conexão estabelecida com sucesso");
        }
        return con;
    }
}
