package com.matrix.Java._Spring.mapper;

import com.matrix.Java._Spring.dto.CreatePaymentRequest;
import com.matrix.Java._Spring.dto.PaymentDto;
import com.matrix.Java._Spring.model.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel="spring")
public interface PaymentMapper {


    List<PaymentDto> getPaymentDtoList(List<Payment> payments);

    PaymentDto toPaymentDtoGetById(Payment payments);

    Payment toCreatePaymentRequest(CreatePaymentRequest createPaymentRequest);

    @Mapping(target="paymentId", ignore=true)
    void updatePaymentFromDto(CreatePaymentRequest request, @MappingTarget Payment payment);

}
