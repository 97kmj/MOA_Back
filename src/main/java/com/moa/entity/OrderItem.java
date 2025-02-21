package com.moa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class OrderItem {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "artwork_id", nullable = false)
    private Artwork artwork;

    @ManyToOne
    @JoinColumn(name = "frame_option_id")
    private FrameOption frameOption;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Long price;
    
    private Long framePrice;
    private Long totalPrice;
    
    @Enumerated(EnumType.STRING)
    private ShippingStatus shippingStatus;
    public enum ShippingStatus { WAITING, INSPECTION, DELIVERED, DEFECTIVE } // 작가 대기중, 검수중, 발송완료, 검수 불량

}
