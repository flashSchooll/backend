package com.flashcard.service;

import com.flashcard.controller.payment.request.PaymentCreateRequest;
import com.flashcard.model.Payment;
import com.flashcard.model.User;
import com.flashcard.repository.PaymentRepository;
import com.flashcard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;

    @Transactional
    public Payment createPayment(PaymentCreateRequest paymentCreateRequest) {
        User user = userRepository.findById(paymentCreateRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));

        Payment payment=new Payment();
        payment.setUser(user);
        payment.setPaymentDate(paymentCreateRequest.getPaymentDate());
        payment.setDeleted(false);
        payment.setPaymentType(paymentCreateRequest.getPaymentType());
        payment.setExpireDate(paymentCreateRequest.getExpireDate());

        return paymentRepository.save(payment);
    }

    public List<Payment> getUserPayments(Long userId) {
        return paymentRepository.findByUserId(userId);
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}