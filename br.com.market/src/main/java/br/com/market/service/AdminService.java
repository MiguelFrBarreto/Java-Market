package br.com.market.service;

import br.com.market.repository.ProductRepository;

public class AdminService {
    ProductRepository pr = new ProductRepository();

    public void addProduct(String name, Double price, Integer quantity) {
        pr.addProduct(name, price, quantity);
    }

    public void removeProduct(String name) {
        pr.removeProduct(name);
    }

    public void addQuantity(String name, Integer quantity){
        pr.addQuantity(name, quantity);
    }

    public void removeQuantity(String name, Integer quantity){
        pr.removeQuantity(name, quantity);
    }
}
