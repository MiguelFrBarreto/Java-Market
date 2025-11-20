package br.com.market.service;

import java.util.List;

import br.com.market. model.Product;
import br.com.market.repository.ProductRepository;

public class ProductService {

    ProductRepository pr = new ProductRepository();

    public List<Product> getProducts() {
        return pr.getProducts();
    }

    public void listProducts() {
        for (Product p : getProducts()) {
            System.out.println("Name: " + p.getName() + ", price: "
                    + p.getPrice() + ", quantity: "
                    + p.getQuantity());
        }
    }

    public Product getProductByName(String name) {
        if (pr.hasProduct(name)) {
            return pr.getProductByName(name);
        } else {
            return null;
        }
    }
}
