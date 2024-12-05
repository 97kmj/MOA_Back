package com.moa.shop.service;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moa.entity.Artwork;
import com.moa.entity.FrameOption;
import com.moa.entity.Order;
import com.moa.entity.Order.OrderStatus;
import com.moa.entity.Order.ShippingStatus;
import com.moa.entity.OrderItem;
import com.moa.entity.User;
import com.moa.repository.ArtworkRepository;
import com.moa.repository.FrameOptionRepository;
import com.moa.repository.OrderItemRepository;
import com.moa.repository.OrderRepository;
import com.moa.repository.UserRepository;
import com.moa.shop.dto.OrderDto;
import com.moa.shop.dto.OrderItemDto;
import com.moa.shop.dto.OrderPaymentRequest;

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
	public void processPayment(OrderPaymentRequest orderPaymentRequest, String username, List<OrderItemDto> saleData) throws Exception {
		//1. 유저 확인 나중에		
		
		//2. 그림 재고 확인
		Artwork artwork = artworkRepository.findById(saleData.get(0).getArtworkId()).orElseThrow(()->new Exception("artworkID 오류"));
		if (artwork.getStock() < 1) {
			System.out.println("판매수량보다 주문수량이 많습니다.");
			return;
		}
		System.out.println("-------------------------");
		System.out.println(orderPaymentRequest);
		//3.order 저장
		Order order = Order.builder()
				.user(User.builder().username(username).build())
				.totalAmount(orderPaymentRequest.getAmount())
				.paymentType(orderPaymentRequest.getPaymentType())
				.shippingStatus(ShippingStatus.NOT_SHIPPED)
				.status(OrderStatus.PENDING)
				.address(orderPaymentRequest.getBuyerAddr())
				.phoneNumber(orderPaymentRequest.getBuyerTel())
				.name(orderPaymentRequest.getBuyerName())
				.build();
		orderRepository.save(order);	
		
		for (int i = 0; i < saleData.size(); i++) {
		//2-1.프레임 재고 확인
		OrderItemDto item = saleData.get(i);
		
		Integer saleFrameStock = item.getFrameOptionId() != null ? 1 : 0; 
        FrameOption frame = item.getFrameOptionId() != null ? frameOptionRepository.findById(item.getFrameOptionId())
                .orElseThrow(() -> new Exception("Frame ID not found for item ")) : null;
        
        if (frame != null && frame.getStock() < saleFrameStock) {
            System.out.println("판매프레임보다 주문수량이 더 많습니다.");
            return;
        }
		
		//4. orderItem 저장
		OrderItem orderItem = OrderItem.builder()
				.order(order)
				.artwork(artwork)
				.frameOption(frame)
				.quantity(1)
				.price(artwork.getPrice())
				.framePrice(frame != null ? frame.getFramePrice() : 0)
				.totalPrice(orderPaymentRequest.getAmount())
				.build();
		orderItemRepository.save(orderItem); 
		
		if (frame != null) {
//			FrameOption frameOption = FrameOption.builder()
//					.frameOptionId(frame.getFrameOptionId())
//					.stock(frame.getStock() - saleFrameStock )
//					.build();
			frame.setStock(frame.getStock() - saleFrameStock);
	        frameOptionRepository.save(frame);
			}
		}
        
		
		//5.그림 프레임수량 감소
//		Artwork artworkDecrease = Artwork.builder()
//				.artworkId(artwork.getArtworkId())
//				.stock(artwork.getStock()-saleData.size())
//				.build();
		artwork.setStock(artwork.getStock()-saleData.size());
		artworkRepository.save(artwork);

    }
        



	
}
