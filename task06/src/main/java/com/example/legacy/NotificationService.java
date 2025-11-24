package com.example.legacy;

import java.util.ArrayList;
import java.util.List;

/**
 * 레거시 코드: 전역 상태, 사이드 이펙트
 */
public class NotificationService {
    // 전역 상태: 모든 알림이 저장됨
    private static List<String> notifications = new ArrayList<>();
    
    public void sendNotification(String userId, String message) {
        // 하드코딩된 출력 (로깅과 섞임)
        String notification = userId + ": " + message;
        System.out.println("[알림] " + notification);
        
        // 전역 상태 변경 (사이드 이펙트)
        notifications.add(notification);
        
        // 실제로는 이메일/SMS 전송 등 복잡한 로직
        // 하지만 테스트하기 어려움 (의존성 주입 없음)
    }
    
    // 전역 상태 접근자 (위험)
    public static List<String> getNotifications() {
        return notifications; // 방어적 복사 없음
    }
    
    public static void clearNotifications() {
        notifications.clear();
    }
}

