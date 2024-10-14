package com.team2.fitinside.order.mapper;

import com.team2.fitinside.order.dto.OrderDetailResponseDto;
import com.team2.fitinside.order.dto.OrderProductResponseDto;
import com.team2.fitinside.order.dto.OrderResponseDto;
import com.team2.fitinside.order.dto.OrderUserResponseDto;
import com.team2.fitinside.order.entity.Order;
import com.team2.fitinside.order.entity.OrderProduct;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-10-11T10:12:07+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.12 (JetBrains s.r.o.)"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public OrderDetailResponseDto toOrderDetailResponseDto(Order order) {
        if ( order == null ) {
            return null;
        }

        OrderDetailResponseDto orderDetailResponseDto = new OrderDetailResponseDto();

        orderDetailResponseDto.setOrderId( order.getId() );
        orderDetailResponseDto.setOrderProducts( orderProductListToOrderProductResponseDtoList( order.getOrderProducts() ) );
        orderDetailResponseDto.setTotalPrice( order.getTotalPrice() );
        orderDetailResponseDto.setDeliveryFee( order.getDeliveryFee() );
        orderDetailResponseDto.setDeliveryAddress( order.getDeliveryAddress() );
        orderDetailResponseDto.setDeliveryReceiver( order.getDeliveryReceiver() );
        orderDetailResponseDto.setDeliveryPhone( order.getDeliveryPhone() );
        orderDetailResponseDto.setCreatedAt( order.getCreatedAt() );

        orderDetailResponseDto.setOrderStatus( order.getOrderStatus().getDisplayName() );

        return orderDetailResponseDto;
    }

    @Override
    public OrderProductResponseDto toOrderProductResponseDto(OrderProduct orderProduct) {
        if ( orderProduct == null ) {
            return null;
        }

        OrderProductResponseDto orderProductResponseDto = new OrderProductResponseDto();

        orderProductResponseDto.setProductId( orderProduct.getId() );
        orderProductResponseDto.setOrderProductName( orderProduct.getOrderProductName() );
        orderProductResponseDto.setOrderProductPrice( orderProduct.getOrderProductPrice() );
        orderProductResponseDto.setCount( orderProduct.getCount() );

        return orderProductResponseDto;
    }

    @Override
    public OrderResponseDto toOrderResponseDto(Order order) {
        if ( order == null ) {
            return null;
        }

        OrderResponseDto orderResponseDto = new OrderResponseDto();

        orderResponseDto.setOrderId( order.getId() );
        if ( order.getOrderStatus() != null ) {
            orderResponseDto.setOrderStatus( order.getOrderStatus().name() );
        }
        orderResponseDto.setTotalPrice( order.getTotalPrice() );
        orderResponseDto.setDeliveryAddress( order.getDeliveryAddress() );
        orderResponseDto.setCreatedAt( order.getCreatedAt() );

        return orderResponseDto;
    }

    @Override
    public List<OrderResponseDto> toOrderResponseDtoList(List<Order> orders) {
        if ( orders == null ) {
            return null;
        }

        List<OrderResponseDto> list = new ArrayList<OrderResponseDto>( orders.size() );
        for ( Order order : orders ) {
            list.add( toOrderResponseDto( order ) );
        }

        return list;
    }

    @Override
    public List<OrderUserResponseDto> toOrderUserResponseDtoList(List<Order> orders) {
        if ( orders == null ) {
            return null;
        }

        List<OrderUserResponseDto> list = new ArrayList<OrderUserResponseDto>( orders.size() );
        for ( Order order : orders ) {
            list.add( toOrderUserResponseDto( order ) );
        }

        return list;
    }

    @Override
    public OrderUserResponseDto toOrderUserResponseDto(Order order) {
        if ( order == null ) {
            return null;
        }

        OrderUserResponseDto orderUserResponseDto = new OrderUserResponseDto();

        orderUserResponseDto.setOrderId( order.getId() );
        orderUserResponseDto.setProductNames( mapProductNames( order.getOrderProducts() ) );
        if ( order.getOrderStatus() != null ) {
            orderUserResponseDto.setOrderStatus( order.getOrderStatus().name() );
        }
        orderUserResponseDto.setTotalPrice( order.getTotalPrice() );
        orderUserResponseDto.setDeliveryAddress( order.getDeliveryAddress() );
        orderUserResponseDto.setCreatedAt( order.getCreatedAt() );

        return orderUserResponseDto;
    }

    protected List<OrderProductResponseDto> orderProductListToOrderProductResponseDtoList(List<OrderProduct> list) {
        if ( list == null ) {
            return null;
        }

        List<OrderProductResponseDto> list1 = new ArrayList<OrderProductResponseDto>( list.size() );
        for ( OrderProduct orderProduct : list ) {
            list1.add( toOrderProductResponseDto( orderProduct ) );
        }

        return list1;
    }
}
