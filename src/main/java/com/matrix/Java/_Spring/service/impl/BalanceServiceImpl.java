package com.matrix.Java._Spring.service.impl;

import com.matrix.Java._Spring.dto.BalanceDto;
import com.matrix.Java._Spring.exceptions.DataNotFoundException;
import com.matrix.Java._Spring.mapper.BalanceMapper;
import com.matrix.Java._Spring.model.entity.Balance;
import com.matrix.Java._Spring.model.entity.User;
import com.matrix.Java._Spring.repository.BalanceRepository;
import com.matrix.Java._Spring.repository.UserRepository;
import com.matrix.Java._Spring.service.BalanceService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BalanceServiceImpl implements BalanceService {

    private static final Log log = LogFactory.getLog(BalanceServiceImpl.class);
    private final BalanceRepository balanceRepository;
    private final UserRepository userRepository;
    private final BalanceMapper balanceMapper;

    @Override
    public BalanceDto getBalanceByUserId(Integer userId) {
        log.info("Starting retrieval of balance with id: {}" + userId);
        User user =userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found"));
         Balance balance=balanceRepository.findByUser(user);
         BalanceDto balanceDto=balanceMapper.toBalanceDto(balance);
         log.info("Finished retrieval of balance  with ID : {} successfully" + userId);
         return balanceDto;
    }
}
