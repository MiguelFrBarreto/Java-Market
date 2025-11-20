package br.com.market.service;

import java.time.LocalDateTime;

import br.com.market.model.Shopper;
import br.com.market.model.discount.Discount;
import br.com.market.repository.UserRepository;

public class PaymentService {
    Discount discount;

    public PaymentService(Discount discount) {
        this.discount = discount;
    }

    public Shopper payment(Shopper user){
        UserRepository ur = new UserRepository();
        Double amount = user.getCartPrice();
        Double userMoney = user.getMoney();
        Double discountPrice = discount.discountCalculation(amount);

        user.setMoney(userMoney -= discountPrice);
        
        ur.updateUsuario(user);

        receipt(amount, discountPrice, user);
        
        return user;
    }
    
    private void receipt(Double price, Double discountedPrice, Shopper user){
        LocalDateTime date = LocalDateTime.now();
        System.out.println("Payment type: " + discount.getPaymentOption());
        System.out.println("Normal price: " + price);
        System.out.println("Discounted price: " + discountedPrice);
        System.out.println("Date: " + date);
        System.out.println("Products: ");
        user.listCart();
    }
}
