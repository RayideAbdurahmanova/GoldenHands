package com.matrix.Java._Spring.service.impl;


import com.matrix.Java._Spring.dto.CreateWishListRequest;
import com.matrix.Java._Spring.dto.WishListDto;
import com.matrix.Java._Spring.exceptions.DataNotFoundException;
import com.matrix.Java._Spring.jwt.JwtService;
import com.matrix.Java._Spring.mapper.WishListMapper;
import com.matrix.Java._Spring.model.entity.Customer;
import com.matrix.Java._Spring.model.entity.User;
import com.matrix.Java._Spring.model.entity.WishList;
import com.matrix.Java._Spring.repository.CustomerRepository;
import com.matrix.Java._Spring.repository.UserRepository;
import com.matrix.Java._Spring.repository.WishListRepository;
import com.matrix.Java._Spring.service.WishListService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class  WishListServiceImpl implements WishListService {


    private final CustomerRepository customerRepository;
    private final WishListRepository wishListRepository;
    private final WishListMapper wishListMapper;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public List<WishListDto> getListByCustomerId(Integer customerId) {
        log.info("Starting retrieval of wish list with ID: {}", customerId);
        Customer customer=customerRepository.findById(customerId)
                .orElseThrow(()->new DataNotFoundException("Data not found"));
        List<WishListDto> wishListDtos=wishListMapper.getWishListDtoList(null);
        log.info("Finished retrieval {} wish lists", wishListDtos.size());
        return wishListDtos;
    }



    @Override
    public WishListDto create(CreateWishListRequest createWishListRequest, HttpServletRequest request) {
        log.info("Starting creating of category: {}", createWishListRequest);
        var token=request.getHeader("Authorization").substring(7).trim();
        var userId=jwtService.extractUserId(token);
        User user=userRepository.findById(userId).orElseThrow(
                ()->new DataNotFoundException("Customer not found with id: " + userId)
        );
        WishList wishList = wishListMapper.toCreateWishListRequest(createWishListRequest);
        WishList savedWishList = wishListRepository.save(wishList);
        log.info("Created wishlist with ID {} for customer ID {}", savedWishList.getId(), userId);
        WishListDto wishListDto=wishListMapper.toWishListDtoGetById(savedWishList);
        log.info("Finished creation of with list with id: {}", savedWishList.getId());
        return wishListDto;
    }

    @Override
    public WishListDto update(Integer id, CreateWishListRequest createWishListRequest,HttpServletRequest request) {
        log.info("Starting  update of wish list with ID: {} ", id);
        var token=request.getHeader("Authorization").substring(7).trim();
        var userId=jwtService.extractUserId(token);
        WishList wishList = wishListRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Wishlist not found with id: " + id));
        if(!wishList.getUser().getId().equals(userId)){
            throw new DataNotFoundException("User not found with id: " + userId);
        }
        wishListMapper.updateWishListFromRequest(createWishListRequest,wishList);
        WishList update=wishListRepository.save(wishList);
        log.info("Updated wishlist with ID {} for customer ID {}", id, userId);
        WishListDto wishListDto=wishListMapper.toWishListDtoGetById(update);
        log.info("Finished update of wish list with ID {} successfully", update.getId());
        return wishListDto;
    }

    @Override
    public void delete(Integer id,HttpServletRequest request) {
        log.info("Starting  deletion of with list with ID: {} ", id);
        var token=request.getHeader("Authorization").substring(7).trim();
        var userId=jwtService.extractUserId(token);
        WishList wishList=wishListRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Wishlist not found with id: " + id));
        if(!wishList.getUser().getId().equals(userId)){
            throw new DataNotFoundException("User not found with id: " + userId);
        }
        wishListRepository.delete(wishList);
        log.info("Deleted wishlist with ID {} for customer ID {}", id, userId);
        log.info("Finished deletion of wish list with ID: {} ", id);
    }
}
