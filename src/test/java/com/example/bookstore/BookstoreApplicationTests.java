package com.example.bookstore;

import com.example.bookstore.common.CommonResult;
import com.example.bookstore.controller.BookManagement;
import com.example.bookstore.controller.ShoppingCart;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Cart;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.CartRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookstoreApplicationTests {

    @Test
    void contextLoads() {
    }
    @Autowired
    BookManagement management;
    @Autowired
    ShoppingCart shoppingCart;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    CartRepository cartRepository;

    @Test
    void management() {
        Book book = new Book(null, "相对论", "物理类", new BigDecimal(45.1), "爱因斯坦");
        List<Book> books = new ArrayList();
        books.add(book);
        CommonResult<List<Book>> result1 = management.getList();
        Optional<Book> o1 = result1.getData().stream().filter(e -> "相对论".equals(e.getTitle())).findAny();
        assertFalse(o1.isPresent());

        management.create(books);
        CommonResult<List<Book>> result2 = management.getList();
        Optional<Book> o2 = result2.getData().stream().filter(e -> "相对论".equals(e.getTitle())).findAny();
        assertTrue(o2.isPresent());
    }

    @Test
    void shoppingCartAdd() {
        List<Book> resultdata = addBooks();
        Long id = resultdata.get(0).getId();

        CommonResult<List<Cart>> exist1 = shoppingCart.getList();

        Optional<Cart> o1 = exist1.getData().stream().filter(e -> e.getId().equals(id)).findFirst();
        int quantity = 0;
        if (o1.isPresent()) {
            quantity = o1.get().getQuantity();
        }
        shoppingCart.create(id, 2);

        CommonResult<List<Cart>> exist2 = shoppingCart.getList();
        Optional<Cart> o2 = exist2.getData().stream().filter(e -> e.getId().equals(id)).findFirst();
        assertTrue(o2.isPresent() && o2.get().getQuantity().equals(quantity+2));
    }

    @Test
    void shoppingCartGetPrice() {
        List<Book> resultdata = addBooks();
        Set<Long> ids = resultdata.stream().map(e->e.getId()).collect(Collectors.toSet());
        ids.forEach(id->shoppingCart.create(id, 2));

        Optional<BigDecimal> priceResult= Optional.of(resultdata.stream().map(p -> p.getPrice().multiply(new BigDecimal(2)))
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        CommonResult<BigDecimal> commonResult = shoppingCart.totalPrice();

        assertTrue(priceResult.get().equals(commonResult.getData()));
    }

    List<Book> addBooks() {
        Book book1 = new Book(null, "相对论", "物理类", new BigDecimal(45.1), "爱因斯坦");
        Book book2 = new Book(null, "资本论", "经济类", new BigDecimal(30), "马克思");
        List<Book> books = new ArrayList();
        books.add(book1);
        books.add(book2);
        CommonResult result = management.create(books);

        return (List<Book>)result.getData();
    }

}
