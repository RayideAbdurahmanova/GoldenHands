package com.matrix.Java._Spring.client;

import com.matrix.Java._Spring.dto.PaymentRequest;
import com.matrix.Java._Spring.enums.PaymentStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "wallet-service", url = "http://localhost:8080/payment")
public interface WalletClient {

    @PostMapping("pay")
    ResponseEntity<PaymentStatus> pay(@RequestHeader("Authorization") String authorization,
                                      @RequestBody PaymentRequest paymentRequest);
}
