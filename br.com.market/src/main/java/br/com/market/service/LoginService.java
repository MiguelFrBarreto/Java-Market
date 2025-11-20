package br.com.market.service;

import br.com.market.model.User;
import br.com.market.repository.LoginRepository;
import br.com.market.repository.UserRepository;

public class LoginService {
    LoginRepository lr = new LoginRepository();
    UserRepository ur = new UserRepository();

    public User login(String name, String password){
        if(ur.hasUser(name) == false){
            return null;
        }

        for(User user: ur.getUsers()){
            if(user.getName().equals(name) && user.getPassword().equals(password)){
                return user;  
            }
        }
        return null;
    }

    public void createAccount(String name, String password){
        lr.createAccount(name, password);
    }
}
