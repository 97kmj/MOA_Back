package com.moa.shop.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.moa.entity.Artwork;
import com.moa.entity.Artwork.SaleStatus;
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
import com.moa.shop.dto.OrderItemDto;
import com.moa.shop.dto.OrderPaymentRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartOrderServiceImpl implements CartOrderService {
	private final ArtworkRepository artworkRepository;
	private final FrameOptionRepository frameOptionRepository;
	private final OrderRepository orderRepository;
	private final OrderItemRepository orderItemRepository;
	private final UserRepository userRepository;
	
	
	@Override
	public void checkStock(List<OrderItemDto> saleData) throws Exception {
		  // 작품별로 주문 수량 계산
	    Map<Long, Long> artworkCountMap = saleData.stream()
	        .collect(Collectors.groupingBy(
	            OrderItemDto::getArtworkId, 
	            Collectors.counting()
	        ));
	    
	    // 프레임 옵션별로 주문 수량 계산
	    Map<Long, Long> frameCountMap = saleData.stream()
	        .filter(item -> item.getFrameOptionId() != null) // 프레임 옵션이 존재하는 경우만
	        .collect(Collectors.groupingBy(
	            OrderItemDto::getFrameOptionId,
	            Collectors.counting()
	        ));

	    // 1. 각 작품의 재고 확인
	    for (Map.Entry<Long, Long> entry : artworkCountMap.entrySet()) {
	        Long artworkId = entry.getKey();
	        Long requiredStock = entry.getValue();

	        Artwork artwork = artworkRepository.findById(artworkId)
	            .orElseThrow(() -> new Exception("Artwork ID " + artworkId + " not found."));

	        if (artwork.getStock() < requiredStock) {
	            throw new Exception("Artwork ID " + artworkId + " has insufficient stock. Required: " + requiredStock + ", Available: " + artwork.getStock());
	        }
	    }

	    // 2. 각 프레임 옵션의 재고 확인
	    for (Map.Entry<Long, Long> entry : frameCountMap.entrySet()) {
	        Long frameOptionId = entry.getKey();
	        Long requiredStock = entry.getValue();

	        FrameOption frameOption = frameOptionRepository.findById(frameOptionId)
	            .orElseThrow(() -> new Exception("Frame Option ID " + frameOptionId + " not found."));

	        if (frameOption.getStock() < requiredStock) {
	            throw new Exception("Frame Option ID " + frameOptionId + " has insufficient stock. Required: " + requiredStock + ", Available: " + frameOption.getStock());
	        }
	    }
	}
	
	@Override
	@Transactional
	public void processPayment(OrderPaymentRequest orderPaymentRequest, String username, List<OrderItemDto> saleData) throws Exception {
	    // 1. 사용자 정보 확인은 추후 구현 예정

	    // 2. 작품 재고 검증 및 감소
	    validateAndDecreaseArtworkStock(saleData);

	    // 3. 주문 저장
	    Order order = createOrder(orderPaymentRequest, username);

	    // 4. 주문 항목 저장 및 프레임 재고 감소
	    saveOrderItemsAndDecreaseFrameStock(saleData, order);
	}

	/**
	 * 작품 재고 검증 및 감소
	 */
	private void validateAndDecreaseArtworkStock(List<OrderItemDto> saleData) throws Exception {
	    if (saleData.isEmpty()) {
	        throw new IllegalArgumentException("주문 데이터가 비어 있습니다.");
	    }

	    // 작품 ID 기준으로 그룹화하여 재고 확인
	    Map<Long, Long> artworkOrderCounts = saleData.stream()
	        .collect(Collectors.groupingBy(OrderItemDto::getArtworkId, Collectors.counting()));

	    for (Map.Entry<Long, Long> entry : artworkOrderCounts.entrySet()) {
	        Long artworkId = entry.getKey();
	        Long orderCount = entry.getValue();

	        Artwork artwork = artworkRepository.findById(artworkId)
	            .orElseThrow(() -> new Exception("Artwork ID " + artworkId + " not found."));

	        int updatedStock = artwork.getStock() - orderCount.intValue();

	        if (updatedStock < 0) {
	            throw new IllegalStateException("판매 수량이 재고를 초과했습니다. Artwork ID: " + artworkId);
	        }

	        artwork.setStock(updatedStock);
	        artwork.setSaleStatus(updatedStock == 0 ? SaleStatus.SOLD_OUT : SaleStatus.AVAILABLE);
	        artworkRepository.save(artwork);
	    }
	}

	/**
	 * 주문 저장
	 */
	private Order createOrder(OrderPaymentRequest orderPaymentRequest, String username) throws Exception {
	    Order order = Order.builder()
	        .user(userRepository.findById(username).orElseThrow(()->new Exception("username 오류")))
	        .totalAmount(orderPaymentRequest.getAmount())
	        .shippingStatus(ShippingStatus.NOT_SHIPPED)
	        .status(OrderStatus.PENDING)
	        .address(orderPaymentRequest.getBuyerAddr())
	        .phoneNumber(orderPaymentRequest.getBuyerTel())
	        .name(orderPaymentRequest.getBuyerName())
	        .build();
	    return orderRepository.save(order);
	}

	/**
	 * 주문 항목 저장 및 프레임 재고 감소
	 */
	private void saveOrderItemsAndDecreaseFrameStock(List<OrderItemDto> saleData, Order order) throws Exception {
	    for (OrderItemDto item : saleData) {
	        FrameOption frameOption = null;

	        // 프레임 옵션이 있는 경우 재고 확인 및 감소
	        if (item.getFrameOptionId() != null) {
	            frameOption = frameOptionRepository.findById(item.getFrameOptionId())
	                .orElseThrow(() -> new Exception("Frame Option ID " + item.getFrameOptionId() + " not found."));

	            if (frameOption.getStock() <= 0) {
	                throw new IllegalStateException("Frame Option ID " + frameOption.getFrameOptionId() + " has insufficient stock.");
	            }

	            frameOption.setStock(frameOption.getStock() - 1);
	            frameOptionRepository.save(frameOption);
	        }

	        // 주문 항목 저장
	        OrderItem orderItem = OrderItem.builder()
	            .order(order)
	            .artwork(artworkRepository.findById(item.getArtworkId())
	                .orElseThrow(() -> new Exception("Artwork ID " + item.getArtworkId() + " not found.")))
	            .frameOption(frameOption)
	            .quantity(1)
	            .price(item.getPrice())
	            .framePrice(item.getFrameprice())
	            .totalPrice(item.getPrice() + item.getFrameprice())
	            .build();

	        orderItemRepository.save(orderItem);
	    }
	}
		
	
}
