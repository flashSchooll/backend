package com.flashcard.controller.payment.request;

import com.flashcard.model.enums.PaymentType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class PaymentCreateRequest {
    private Long userId;
    private PaymentType paymentType;
    private LocalDateTime paymentDate;
    private LocalDateTime expireDate;
}
