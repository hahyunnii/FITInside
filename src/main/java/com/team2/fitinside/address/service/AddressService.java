package com.team2.fitinside.address.service;

import com.team2.fitinside.address.dto.AddressRequestDto;
import com.team2.fitinside.address.dto.AddressResponseDto;
import com.team2.fitinside.address.entity.Address;
import com.team2.fitinside.address.mapper.AddressMapper;
import com.team2.fitinside.address.repository.AddressRepository;
import com.team2.fitinside.config.SecurityUtil;
import com.team2.fitinside.global.exception.CustomException;
import com.team2.fitinside.member.entity.Member;
import com.team2.fitinside.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.team2.fitinside.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final SecurityUtil securityUtil;
    private final AddressMapper addressMapper;
    private final AddressRepository addressRepository;
    private final MemberRepository memberRepository;

    public List<AddressResponseDto> findAllAddresses(){

        Long loginMemberId = securityUtil.getCurrentMemberId();
        List<Address> addresses = addressRepository.findAllByMemberId(loginMemberId);

        return addressMapper.toAddressResponseDtoList(addresses);
    }

    public AddressResponseDto findAddress(Long addressId) {

        Address address = addressRepository.findByIdAndIsDeletedFalse(addressId)
                .orElseThrow(() -> new CustomException(ADDRESS_NOT_FOUND));

        checkAuthorization(address);
        return addressMapper.toAddressResponseDto(address);
    }

    @Transactional
    public AddressResponseDto createAddress(AddressRequestDto request) {

        Long loginMemberId = securityUtil.getCurrentMemberId();
        Member findMember = memberRepository.findById(loginMemberId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        // 배송지는 최대 5개까지 저장
        List<Address> addresses = addressRepository.findAllByMemberId(loginMemberId);
        if(addresses.size() >= 5){
            throw new CustomException(EXCEEDED_MAX_ADDRESS_LIMIT);
        }

        Address address = addressMapper.toAddress(request);
        address.setMember(findMember);

        Address createdAddress = addressRepository.save(address);
        return addressMapper.toAddressResponseDto(createdAddress);

    }

    @Transactional
    public AddressResponseDto updateAddress(Long addressId, AddressRequestDto request) {

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new CustomException(ADDRESS_NOT_FOUND));

        checkAuthorization(address);
        address.updateAddress(request);
        return addressMapper.toAddressResponseDto(address);
    }

    @Transactional
    public void deleteAddress(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new CustomException(ADDRESS_NOT_FOUND));

        checkAuthorization(address);
        address.deleteAddress();
    }

    private void checkAuthorization(Address address){
        Long loginMemberId = securityUtil.getCurrentMemberId();
        if(!loginMemberId.equals(address.getMember().getId())){
            throw new CustomException(USER_NOT_AUTHORIZED);
        }
    }
}
