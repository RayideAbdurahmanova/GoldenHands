package com.matrix.Java._Spring.service;

import com.matrix.Java._Spring.dto.BalanceDto;

public interface BalanceService {

     BalanceDto getBalanceByUserId(Integer userId);
}
