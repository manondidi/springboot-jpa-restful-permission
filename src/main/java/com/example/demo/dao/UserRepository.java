package com.example.demo.dao;

import com.example.demo.pojo.po.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByUserNameOrMailOrTel(String userName, String mail, String tel);
}
