package com.team2.fitinside.address.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressResponseDto {

    private String receiver;
    private String phone;
    private String postalCode;
    private String deliveryAddress;
    private String detailedAddress;
    private String memo;

}
