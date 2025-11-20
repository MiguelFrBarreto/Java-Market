package br.com.market.model.discount;

import br.com.market.enums.PaymentOption;

public class DigitalDiscount implements Discount{
    PaymentOption paymentOption = PaymentOption.DIGITAL_PAYMENT;

    @Override
    public Double discountCalculation(Double amount){
        return amount -= amount * 0.03;
    }
    @Override
    public PaymentOption getPaymentOption(){
        return paymentOption;
    }
}
