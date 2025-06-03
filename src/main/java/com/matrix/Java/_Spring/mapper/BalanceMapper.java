package com.matrix.Java._Spring.mapper;

import com.matrix.Java._Spring.dto.BalanceDto;
import com.matrix.Java._Spring.model.entity.Balance;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BalanceMapper {

    BalanceDto toBalanceDto(Balance balance);
}
