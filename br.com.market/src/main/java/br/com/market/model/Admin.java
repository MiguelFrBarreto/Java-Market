package br.com.market.model;

import br.com.market.enums.Position;

public class Admin extends User{
    
    public Admin(){
    }

    public Admin(String name, String password, Long id) {
        super(name, password, id, Position.ADMIN);
    }
}
