package com.example.legacy;

/**
 * 레거시 코드: 순환 의존성
 * OrderProcessor와 서로 참조
 */
public class PaymentService {
    private OrderProcessor orderProcessor; // 순환 의존성!
    
    public void setOrderProcessor(OrderProcessor processor) {
        this.orderProcessor = processor;
    }
    
    public boolean processPayment(String userId, double amount) {
        // 간단한 결제 로직 (실제로는 복잡할 것)
        if (amount > 1000) {
            // 큰 금액은 검증 필요
            if (orderProcessor != null) {
                orderProcessor.onPaymentFailed("HIGH_AMOUNT_CHECK");
            }
            return false;
        }
        
        // 결제 처리 (하드코딩된 로직)
        System.out.println("결제 처리: " + userId + ", 금액: " + amount);
        return true;
    }
    
    public void refundPayment(String userId, double amount) {
        System.out.println("환불 처리: " + userId + ", 금액: " + amount);
        // OrderProcessor에 알림 (순환 의존성)
        if (orderProcessor != null) {
            orderProcessor.onPaymentFailed("REFUND_" + userId);
        }
    }
}

