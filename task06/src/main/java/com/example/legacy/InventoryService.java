package com.example.legacy;

import java.util.HashMap;
import java.util.Map;

/**
 * 레거시 코드: 순환 의존성, 전역 상태
 */
public class InventoryService {
    private OrderProcessor orderProcessor; // 순환 의존성!
    private static Map<String, Integer> stock = new HashMap<>(); // 전역 상태
    
    static {
        // 초기 재고 데이터
        stock.put("ITEM-001", 10);
        stock.put("ITEM-002", 5);
        stock.put("ITEM-003", 0);
    }
    
    public void setOrderProcessor(OrderProcessor processor) {
        this.orderProcessor = processor;
    }
    
    public boolean checkStock(String itemId) {
        // 재고 확인
        int currentStock = stock.getOrDefault(itemId, 0);
        
        if (currentStock <= 0) {
            // OrderProcessor에 알림 (순환 의존성)
            if (orderProcessor != null) {
                orderProcessor.onInventoryLow(itemId);
            }
            return false;
        }
        
        return true;
    }
    
    public boolean updateStock(String itemId, int quantity) {
        // 재고 업데이트
        int currentStock = stock.getOrDefault(itemId, 0);
        int newStock = currentStock + quantity;
        
        if (newStock < 0) {
            return false;
        }
        
        stock.put(itemId, newStock);
        
        // 재고가 부족하면 알림 (순환 의존성)
        if (newStock < 3 && orderProcessor != null) {
            orderProcessor.onInventoryLow(itemId);
        }
        
        return true;
    }
    
    // 전역 상태 직접 접근 (위험)
    public static Map<String, Integer> getStock() {
        return stock; // 방어적 복사 없음
    }
}

