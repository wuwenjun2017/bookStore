package com.example.bookstore.controller;

import com.example.bookstore.common.CommonResult;
import com.example.bookstore.dto.CartDetail;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Cart;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cart")
public class ShoppingCart {
    @Autowired
    CartRepository cartRepository;
    @Autowired
    BookRepository bookRepository;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult create(@RequestParam Long bookId, @RequestParam Integer quantity) {

        Cart cart;
        Optional<Cart> optional = cartRepository.findAllByBookId(bookId);
        if (optional.isPresent()) { // already exist is cart
            cart = optional.get();
            cart.setQuantity(cart.getQuantity() + quantity);
        } else {
            Optional<Book> book = bookRepository.findById(bookId);
            if (book.isEmpty()) {
                return CommonResult.failed("This book doesn't exist, please add book first!");
            }
            cart = new Cart(null, 1L, bookId, book.get().getTitle(), book.get().getPrice(), quantity);
        }
        cartRepository.save(cart);
        return CommonResult.success(cart);
    }

    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<Cart>> getList() {
        List<Cart> list =  cartRepository.findAll();
        return CommonResult.success(list);
    }

    @RequestMapping(value = "/calculate", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<BigDecimal> totalPrice() {
        List<CartDetail> list =  cartRepository.findCartByBookId();
        Optional<BigDecimal> result= Optional.of(list.stream().map(p -> p.getPrice().multiply(new BigDecimal(p.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));
//        Optional<BigDecimal> result= list.stream().reduce((e1, e2) ->
//                e1.getPrice().multiply(new BigDecimal(e1.getQuantity())).add(e2.getPrice().multiply(new BigDecimal(e2.getQuantity()))))
        return CommonResult.success(result.get());
    }

}
