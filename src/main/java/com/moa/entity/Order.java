package com.moa.entity;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import com.moa.entity.User.ApprovalStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name="`order`")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "username", nullable = false)
    private User user;

    @Column(nullable = false)
    private Long totalAmount;

    
    private Timestamp paymentDate;
    private String paymentType;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    private ShippingStatus shippingStatus;

    private String address;
    private String phoneNumber;
    private String name;
    
    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems;
    
    
    @PrePersist
	protected void onCreate() {
		this.paymentDate = new Timestamp(System.currentTimeMillis());
	}

    public enum OrderStatus { PENDING, COMPLETED }
    public enum ShippingStatus { NOT_SHIPPED, SHIPPING, DELIVERED }

}
