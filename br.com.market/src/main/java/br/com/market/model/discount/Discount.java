package br.com.market.model.discount;

import br.com.market.enums.PaymentOption;

public interface Discount {
    public Double discountCalculation(Double amount);
    public PaymentOption getPaymentOption();
}
