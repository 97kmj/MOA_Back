package com.moa.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moa.entity.Artwork;
import com.moa.entity.FrameOption;
import com.moa.entity.Order;
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
	public void processPayment(OrderPaymentRequest orderPaymentRequest) throws Exception {
        // 1. 사용자 정보 조회 (구매자의 이메일로 사용자 확인)
        User user = userRepository.findByEmail(orderPaymentRequest.getBuyer_email());
        if (user == null) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }
        // 2. 결제 요청에서 주문 DTO 추출
        OrderDto orderDto = orderPaymentRequest.getOrderDto();
        
        

        // 3. 프레임 옵션 재고 확인 및 감소
        for (OrderItemDto orderItemDto : orderDto.getOrderItems()) {
            Long frameOptionId = orderItemDto.getFrameOptionId();
            FrameOption frameOption = frameOptionRepository.findById(frameOptionId)
                    .orElseThrow(() -> new Exception("FrameOption not found"));

            if (frameOption.getStock() > 0) {
                // 수량에 맞게 재고 감소
                frameOption.setStock(frameOption.getStock() - orderItemDto.getQuantity());
                frameOptionRepository.save(frameOption);
            } else {
                throw new Exception("Frame option stock is 0");
            }
        }
     // 4. Artwork 재고 확인 및 감소
        for (OrderItemDto orderItemDto : orderDto.getOrderItems()) {
            Long artworkId = orderItemDto.getArtworkId();
            Artwork artwork = artworkRepository.findById(artworkId)
                    .orElseThrow(() -> new Exception("Artwork not found"));

            if (artwork.getStock() > 0) {
                // 수량에 맞게 재고 감소
                artwork.setStock(artwork.getStock() - orderItemDto.getQuantity());
                artworkRepository.save(artwork);
            } else {
                throw new Exception("Artwork stock is 0");
            }
        }

        // 5. 주문 저장
        Order order = orderDto.toOrderEntity();
        orderRepository.save(order);

        // 6. 주문 항목 저장
        for (OrderItemDto orderItemDto : orderDto.getOrderItems()) {
            OrderItem orderItem = orderItemDto.toOrderItemEntity();
            orderItem.setOrder(order); // 주문 항목에 연관된 Order 설정
            orderItemRepository.save(orderItem);
        }
        
        
        

    }
        



	
}
