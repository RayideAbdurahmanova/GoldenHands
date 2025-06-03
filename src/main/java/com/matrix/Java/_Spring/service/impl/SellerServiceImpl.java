package com.matrix.Java._Spring.service.impl;

import com.matrix.Java._Spring.dto.SellerDto;
import com.matrix.Java._Spring.mapper.SellerMapper;
import com.matrix.Java._Spring.repository.SellerRepository;
import com.matrix.Java._Spring.service.SellerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class  SellerServiceImpl  implements SellerService {

    private final SellerRepository sellerRepository;
    private final SellerMapper sellerMapper;

    @Override
    public List<SellerDto> getSellers() {
        log.info("Starting retrieval of sellers");
        var seller=sellerRepository.findAll();
        List<SellerDto> sellerDtos=sellerMapper.mapToSellerDtoList(seller);
        log.info("Finished retrieval {} sellers", sellerDtos.size());
        return sellerDtos;
    }
}
