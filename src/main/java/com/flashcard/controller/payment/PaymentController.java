package com.flashcard.controller.payment;

import com.flashcard.controller.payment.request.PaymentCreateRequest;
import com.flashcard.controller.payment.response.PaymentDto;
import com.flashcard.model.Payment;
import com.flashcard.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/user")
    public ResponseEntity<Payment> savePayment(@RequestBody PaymentCreateRequest paymentCreateRequest) {
        return ResponseEntity.ok(paymentService.createPayment(paymentCreateRequest));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentDto>> getPaymentsByUser(@PathVariable Long userId) {
        List<PaymentDto> payments = paymentService.getUserPayments(userId).stream().map(PaymentDto::new).toList();
        return ResponseEntity.ok(payments);
    }

    @GetMapping
    public ResponseEntity<List<PaymentDto>> getAll() {
        List<PaymentDto> payments = paymentService.getAllPayments().stream().map(PaymentDto::new).toList();
        return ResponseEntity.ok(payments);
    }
}