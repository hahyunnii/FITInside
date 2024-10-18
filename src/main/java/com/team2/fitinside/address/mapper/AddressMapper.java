package com.team2.fitinside.address.mapper;

import com.team2.fitinside.address.dto.AddressRequestDto;
import com.team2.fitinside.address.dto.AddressResponseDto;
import com.team2.fitinside.address.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    List<AddressResponseDto> toAddressResponseDtoList(List<Address> addresses);

    @Mapping(source = "id", target = "addressId")
    AddressResponseDto toAddressResponseDto(Address address);

    Address toAddress(AddressRequestDto addressRequestDto);

}
