package br.com.market.model;

import br.com.market.enums.Position;

public abstract class User {
    private Long id;
    private String name;
    private String password;
    private Position position;

    public User(){
    }

    public User(String name, String password, Long id, Position position) {
        this.name = name;
        this.password = password;
        this.id = id;
        this.position = position;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    
}
