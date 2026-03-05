package com.flashcard.controller.payment.response;

import com.flashcard.model.Payment;
import com.flashcard.model.enums.PaymentType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class PaymentDto {

    private final Long id;
    private final BigDecimal amount; // Ödeme tutarı
    private final String transactionId; // Ödeme sağlayıcısından dönen kod
    private final LocalDateTime paymentDate;
    private final LocalDateTime expireDate; // Abonelik bitiş tarihi
    private final PaymentType paymentType;
    private final Long userId;

    public PaymentDto(Payment payment) {
        this.id = payment.getId();
        this.amount = payment.getAmount();
        this.transactionId = payment.getTransactionId();
        this.paymentDate = payment.getPaymentDate();
        this.expireDate = payment.getExpireDate();
        this.paymentType = payment.getPaymentType();
        this.userId = payment.getUser().getId();
    }
}
