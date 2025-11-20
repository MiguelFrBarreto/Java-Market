package br.com.market.model;

import java.util.List;
import java.util.ArrayList;

import br.com.market.enums.Position;
import java.util.Iterator;
import br.com.market.service.ShopperService;

public class Shopper extends User {

    private Double money;
    private final List<Product> cart = new ArrayList<>();

    public Shopper() {
    }

    public Shopper(String name, String password, Long id, Double money) {
        super(name, password, id, Position.SHOPPER);
        this.money = money;
    }

    public Double getMoney() {
        return money;
    }

    public Product getProductByIndex(int index) {
        return cart.get(index);
    }

    public Product getProductByName(String name) {
        for (Product p : cart) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public void listCart() {
        for (Product p : cart) {
            if (!cart.isEmpty()) {
                System.out.println("Name: " + p.getName() + ", price: " + p.getPrice() + ", quantity: " + p.getQuantity());
            }
        }
    }

    public Double getCartPrice() {
        Double cartPrice = 0.0;
        for (Product p : cart) {
            cartPrice += p.getFinalPrice();
        }
        return cartPrice;
    }

    public void addProduct(Product product) {
        cart.add(product);
    }

    public void removeProduct(Product product) {
        Product toRemove = null;
        for (Product p : cart) {
            if (p.getName().equals(product.getName())) {
                toRemove = p;
            }
        }
        if (cart != null) {
            cart.remove(toRemove);
        }
    }

    public List<Product> getCart() {
        return cart;
    }

    public void cleanCart() {
        ShopperService ss = new ShopperService();
        Iterator<Product> iterator = cart.iterator();

        while (iterator.hasNext()) {
            Product product = iterator.next();
            ss.removeProduct(product, product.getQuantity(), this);
            iterator.remove();
        }
    }
}
