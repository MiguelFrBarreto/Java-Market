package br.com.market.model.discount;

import br.com.market.enums.PaymentOption;

public class CardDiscount implements Discount{
    PaymentOption paymentOption = PaymentOption.CARD;

    @Override
    public Double discountCalculation(Double amount){
        return amount;
    }

    @Override
    public PaymentOption getPaymentOption(){
        return paymentOption;
    }
}
