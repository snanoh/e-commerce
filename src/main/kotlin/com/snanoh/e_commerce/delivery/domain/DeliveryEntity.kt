package com.snanoh.e_commerce.delivery.domain

import jakarta.persistence.*

enum class DeliveryStatus {
    PREPARING, // 배송 준비중
    SHIPPED,   // 배송중
    DELIVERED, // 배송 완료
    CANCELLED  // 배송 취소
}

@Entity
@Table(name = "deliveries")
class DeliveryEntity(
    // 수령인 이름
    @Column(nullable = false)
    var recipientName: String,

    // 수령인 연락처
    @Column(nullable = false)
    var recipientPhone: String,

    // 배송 주소
    @Column(nullable = false)
    var address: String,

    // 운송장 번호 (발송 전엔 없을 수 있으므로 nullable)
    var trackingNumber: String? = null,

    // 배송 상태
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: DeliveryStatus = DeliveryStatus.PREPARING,

    // 차후에 주문 연동 시 주문 ID 혹은 주문 엔티티와의 매핑 추가
    // val orderId: Long,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val deliveryId: Long = 0L
)
