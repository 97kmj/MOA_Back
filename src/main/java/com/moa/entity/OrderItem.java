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

import com.moa.entity.Order.ShippingStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
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
    public enum ShippingStatus { NOT_SHIPPED, SHIPPING, DELIVERED }

}
