package com.moa.shop.service;


import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moa.admin.dto.FrameDto;
import com.moa.entity.Artwork;
import com.moa.entity.FrameOption;
import com.moa.entity.Order;
import com.moa.entity.Order.OrderStatus;
import com.moa.entity.Order.ShippingStatus;
import com.moa.entity.OrderItem;
import com.moa.entity.User;
import com.moa.funding.dto.payment.PaymentRequest;
import com.moa.repository.ArtworkRepository;
import com.moa.repository.FrameOptionRepository;
import com.moa.repository.OrderItemRepository;
import com.moa.repository.OrderRepository;
import com.moa.repository.UserRepository;
import com.moa.shop.dto.OrderDto;
import com.moa.shop.dto.OrderItemDto;
import com.moa.shop.dto.OrderPaymentRequest;
import com.twilio.rest.api.v2010.account.call.FeedbackSummary.Status;

@Service

public class OrderPaymentServiceImpl implements OrderPaymentService {
	
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ArtworkRepository artworkRepository;

    @Autowired
    private UserRepository userRepository;
	
    @Autowired
    private FrameOptionRepository frameOptionRepository;
    
	@Override
	@Transactional
	public void processPayment(OrderPaymentRequest orderPaymentRequest, String username, OrderItem saleData) throws Exception {
		//1. 유저 확인 나중에
		
		
		
		
		
		//2. 그림 재고 확인
		Artwork artwork = artworkRepository.findById(saleData.getArtwork().getArtworkId()).orElseThrow(()->new Exception("artworkID 오류"));
		Integer saleStock = saleData.getArtwork().getStock();
		
		if (artwork.getStock() <saleStock) {
			System.out.println("판매수량보다 주문수량이 많습니다.");
			return;
		}
		//2-1.프레임 재고 확인
		FrameOption frame = frameOptionRepository.findById(saleData.getFrameOption().getFrameOptionId()).orElseThrow(()->new Exception("frameId 오류"));
		Integer saleFrameStock = saleData.getFrameOption().getStock();
		
		if(frame.getStock() < saleFrameStock ) {
			System.out.println("판매프레임보다 주문수량이 더 많습니다.");
			return;
		}
		//3.order 저장
		Order order = Order.builder()
				.user(User.builder().nickname(username).build())
				.totalAmount(orderPaymentRequest.getTotal_price())
				.paymentType(orderPaymentRequest.getPaymentType())
				.shippingStatus(ShippingStatus.NOT_SHIPPED)
				.status(OrderStatus.PENDING)
				.address(orderPaymentRequest.getBuyer_addr())
				.phoneNumber(orderPaymentRequest.getBuyer_tel())
				.name(orderPaymentRequest.getBuyer_name())
				.build();
		orderRepository.save(order);	
		
		//4. orderItem 저장
		OrderItem orderItem = OrderItem.builder()
				.order(order)
				.artwork(artwork)
				.frameOption(frame)
				.quantity(saleStock)
				.framePrice(frame.getFramePrice())
				.totalPrice(orderPaymentRequest.getTotal_price())
				.build();
		orderItemRepository.save(orderItem); 
		
		//5.그림 프레임수량 감소
		Artwork artworkDecrease = Artwork.builder()
				.artworkId(saleData.getArtwork().getArtworkId())
				.stock(artwork.getStock()-saleStock)
				.build();
		artworkRepository.save(artworkDecrease);
		
		FrameOption frameOption = FrameOption.builder()
				.frameOptionId(frame.getFrameOptionId())
				.stock(frame.getStock() - saleFrameStock)
				.build();
        frameOptionRepository.save(frameOption);
        
        

    }
        



	
}
