package com.matrix.Java._Spring.client;

import com.matrix.Java._Spring.dto.PaymentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "wallet-service", url = "http://localhost:8080/payment")
public interface WalletClient {

    @PostMapping("pay")
    void pay(@RequestHeader("Authorization") String authorization,
             @RequestBody PaymentRequest paymentRequest);

}
