package com.example.legacy;

public class Main {
    public static void main(String[] args) {
        // 모든 것을 직접 연결하는 스파게티 코드
        OrderProcessor processor = new OrderProcessor();
        processor.processOrder("ORDER-001", "USER-123", 100.0);
    }
}

