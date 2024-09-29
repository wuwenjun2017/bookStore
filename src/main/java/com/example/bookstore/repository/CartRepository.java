package com.example.bookstore.repository;

import com.example.bookstore.dto.CartDetail;
import com.example.bookstore.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findAllByBookId(Long bookId);

    @Query(value = "select new com.example.bookstore.dto.CartDetail(c.id, c.bookId, c.quantity, b.price) from cart c inner join book b on c.bookId = b.id")
    public List<CartDetail> findCartByBookId();
}
