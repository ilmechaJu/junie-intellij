package com.example.legacy;

import java.util.ArrayList;
import java.util.List;

/**
 * 레거시 코드: 거대한 클래스, 모든 책임을 가짐
 * 문제점:
 * - 단일 책임 원칙 위반
 * - 강한 결합
 * - 테스트 어려움
 * - 사이드 이펙트 많음
 */
public class OrderProcessor {
    // 전역 상태 (정적 필드)
    private static int totalOrders = 0;
    private static List<String> processedOrders = new ArrayList<>();
    
    // 강한 결합: 구체 클래스에 직접 의존
    private PaymentService paymentService;
    private InventoryService inventoryService;
    private NotificationService notificationService;
    
    public OrderProcessor() {
        // 순환 의존성 생성
        this.paymentService = new PaymentService();
        this.inventoryService = new InventoryService();
        this.notificationService = new NotificationService();
        
        // 사이드 이펙트: 다른 서비스에 자신을 주입
        paymentService.setOrderProcessor(this);
        inventoryService.setOrderProcessor(this);
    }
    
    /**
     * 모든 로직이 하나의 메서드에 있는 스파게티 코드
     */
    public boolean processOrder(String orderId, String userId, double amount) {
        // 1. 주문 검증
        if (orderId == null || orderId.isEmpty()) {
            System.out.println("주문 ID가 없습니다");
            return false;
        }
        if (amount <= 0) {
            System.out.println("금액이 잘못되었습니다");
            return false;
        }
        
        // 2. 재고 확인 (다른 서비스 호출)
        boolean inStock = inventoryService.checkStock(orderId);
        if (!inStock) {
            System.out.println("재고가 없습니다");
            notificationService.sendNotification(userId, "재고 부족");
            return false;
        }
        
        // 3. 결제 처리 (다른 서비스 호출)
        boolean paymentSuccess = paymentService.processPayment(userId, amount);
        if (!paymentSuccess) {
            System.out.println("결제 실패");
            notificationService.sendNotification(userId, "결제 실패");
            return false;
        }
        
        // 4. 재고 차감 (다른 서비스 호출)
        boolean stockUpdated = inventoryService.updateStock(orderId, -1);
        if (!stockUpdated) {
            // 결제 취소 (롤백)
            paymentService.refundPayment(userId, amount);
            System.out.println("재고 업데이트 실패 - 결제 취소");
            return false;
        }
        
        // 5. 전역 상태 변경 (사이드 이펙트)
        totalOrders++;
        processedOrders.add(orderId);
        
        // 6. 알림 전송 (다른 서비스 호출)
        notificationService.sendNotification(userId, "주문 완료: " + orderId);
        
        // 7. 로깅 (하드코딩된 출력)
        System.out.println("주문 처리 완료: " + orderId + ", 총 주문 수: " + totalOrders);
        
        return true;
    }
    
    // 전역 상태 접근자 (위험)
    public static int getTotalOrders() {
        return totalOrders;
    }
    
    public static List<String> getProcessedOrders() {
        return processedOrders; // 방어적 복사 없음
    }
    
    // 다른 서비스에서 호출하는 메서드 (순환 의존성)
    public void onPaymentFailed(String orderId) {
        System.out.println("결제 실패 알림: " + orderId);
        // 또 다른 로직...
    }
    
    public void onInventoryLow(String itemId) {
        System.out.println("재고 부족 알림: " + itemId);
        // 또 다른 로직...
    }
}

