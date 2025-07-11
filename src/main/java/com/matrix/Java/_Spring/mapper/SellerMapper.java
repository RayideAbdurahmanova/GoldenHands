package com.matrix.Java._Spring.mapper;

import com.matrix.Java._Spring.dto.SellerDto;
import com.matrix.Java._Spring.model.entity.Seller;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SellerMapper {
    List<SellerDto> mapToSellerDtoList(List<Seller> sellers);

    @Mapping(source = "user.phoneNumber", target = "phoneNumber")
    @Mapping(source = "storeName", target = "storeName")
    @Mapping(source = "businessAddress", target = "businessAddress")
    SellerDto sellerToSellerDto(Seller seller);}
