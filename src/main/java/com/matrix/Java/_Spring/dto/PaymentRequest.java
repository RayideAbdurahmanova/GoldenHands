package com.matrix.Java._Spring.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {
    @JsonProperty("order_id")
    private Integer orderId;
    @JsonProperty("amount")
    private BigDecimal amount;
    @JsonProperty("customer_id")
    private Integer customerId;


}
