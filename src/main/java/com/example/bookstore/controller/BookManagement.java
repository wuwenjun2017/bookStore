package com.example.bookstore.controller;

import com.example.bookstore.common.CommonResult;
import com.example.bookstore.common.ResultCode;
import com.example.bookstore.entity.Book;
import com.example.bookstore.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/book")
public class BookManagement {
    @Autowired
    BookRepository bookRepository;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult create(@RequestBody List<Book> books) {
        if (books.size() == 0) {
            return CommonResult.failed(ResultCode.VALIDATE_FAILED, "there is no book to be added!");
        }
        if (books.stream().anyMatch(e -> e.getTitle()==null || e.getTitle().isBlank())) {
            return CommonResult.failed(ResultCode.VALIDATE_FAILED, "title can't be empty!");
        }
        Set<String> set = books.stream().map(e -> e.getTitle()).collect(Collectors.toSet());
        List<Book> exist = bookRepository.findAllByTitles(set);
        if (!exist.isEmpty()) {
            return CommonResult.failed(exist.get(0).title + " is duplicated!");
        }
        try {
            books.stream().forEach(e -> e.setPrice(e.getPrice().setScale(2, RoundingMode.DOWN)));
            List<Book> list = bookRepository.saveAllAndFlush(books);
            if (list.size() > 0) {
                return CommonResult.success(list);
            }
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }

        return CommonResult.failed();
    }

    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<Book>> getList() {
        List<Book> list =  bookRepository.findAll();
        return CommonResult.success(list);
    }
}
