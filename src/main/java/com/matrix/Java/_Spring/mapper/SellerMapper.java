package com.matrix.Java._Spring.mapper;

import com.matrix.Java._Spring.dto.SellerDto;
import com.matrix.Java._Spring.model.entity.Seller;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SellerMapper {
    List<SellerDto> mapToSellerDtoList(List<Seller> seller);
}
