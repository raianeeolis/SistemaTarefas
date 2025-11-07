/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ucb.estudo.dao;

import java.sql.Connection;
/**
 *
 * @author raiane.souza
 */
public interface ConexaoBD {
    Connection obterConexao() throws Exception;
    
    void fecharConexao(Connection conexao);
}
