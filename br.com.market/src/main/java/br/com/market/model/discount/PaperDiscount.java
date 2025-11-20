package br.com.market.model.discount;

import br.com.market.enums.PaymentOption;

public class PaperDiscount implements Discount{
    PaymentOption paymentOption = PaymentOption.PAPER;
    
    @Override
    public Double discountCalculation(Double amount){
        return amount -= amount * 0.05;
    }

    @Override
    public PaymentOption getPaymentOption(){
        return paymentOption;
    }
}
