package br.com.market.db;

public class DbException extends RuntimeException{
    //EXCEÇÃO PERSONALIZADA PARA CONEXAO COM BANCO DE DADOS

    private static final long serialVersionUID = 1;
    
    public DbException(String msg){
        super(msg);
    }
}