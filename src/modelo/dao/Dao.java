package modelo.dao;

import java.sql.SQLException;
import java.util.List;

public interface Dao <T>{
    void salvar(T objeto ) throws SQLException;

    List<T> listar()throws SQLException;

    void alterar(int id, T objeto)throws SQLException;

    void deletar(int id)throws SQLException;
}

