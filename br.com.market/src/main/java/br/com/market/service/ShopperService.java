package br.com.market.service;

import br.com.market.model.Product;
import br.com.market.model.Shopper;
import br.com.market.repository.ProductRepository;

public class ShopperService {
    ProductRepository pr = new ProductRepository();

    public Shopper removeProduct(Product procuredProduct, Integer quantity, Shopper shopper){
        if(!pr.hasProduct(procuredProduct.getName())){
            return null;
        }

        boolean isEqual = false;
        Product equalProduct = null;

        for (Product p : shopper.getCart()) {
            if (p.getName().equalsIgnoreCase(procuredProduct.getName())) {
                equalProduct = p;
                isEqual = true;
            }
        }

        if (isEqual) {
            if (equalProduct.getQuantity() <= quantity) {
                pr.addQuantity(equalProduct.getName(), equalProduct.getQuantity());
                shopper.removeProduct(equalProduct);
            } else if (equalProduct.getQuantity() > quantity) {
                equalProduct.setQuantity(equalProduct.getQuantity() - quantity);
                pr.addQuantity(equalProduct.getName(), quantity);
            }
        }
        return shopper;
    }

    public Shopper addProduct(Product product, int quantity, Shopper shopper) {
        if(!pr.hasProduct(product.getName())){
            return null;
        }

        product.setQuantity(pr.removeQuantity(product.getName(), quantity));
        shopper.addProduct(product);

        return shopper;
    }
}
