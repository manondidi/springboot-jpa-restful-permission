package com.example.demo.service.impl;

import com.example.demo.dao.UserRepository;
import com.example.demo.expection.BusinessException;
import com.example.demo.expection.BusinessExceptionEnum;
import com.example.demo.pojo.dto.Token;
import com.example.demo.pojo.dto.User;
import com.example.demo.service.UserService;
import com.github.dozermapper.core.Mapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
@Getter
@Setter
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Resource
    private Mapper dozerMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public Token login(String user, String password) {
        com.example.demo.pojo.po.User userPo = userRepository.findUserByUserNameOrMailOrTel(user, user, user);
        if (userPo == null || !userPo.getPassword().equals(password)) {
            throw new BusinessException(BusinessExceptionEnum.LOGIN_FAIL);
        }
        Token token = Token.generateToken();
        stringRedisTemplate.opsForValue().set(token.getToken(), userPo.getId().toString(), 7, TimeUnit.DAYS);
        stringRedisTemplate.opsForSet().add(userPo.getId().toString(), token.getToken());
        return token;
    }

    @Override
    public void unLogin(String userId, String token) {
        stringRedisTemplate.opsForValue().set(token, null);
        stringRedisTemplate.opsForSet().remove(userId, token);
    }

    @Override
    public User findUserByToken(String token) {
        String userId = stringRedisTemplate.opsForValue().get(token);
        if (userId == null) {
            throw new BusinessException(BusinessExceptionEnum.USER_UNLOGIN);
        }
        com.example.demo.pojo.po.User userPo = userRepository.findById(Long.parseLong(userId)).get();
        if (userPo == null) {
            throw new BusinessException(BusinessExceptionEnum.USER_UNLOGIN);
        }
        return dozerMapper.map(userPo, User.class);
    }


}
