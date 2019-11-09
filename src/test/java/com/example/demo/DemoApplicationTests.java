package com.example.demo;

import com.example.demo.pojo.dto.Permission;
import com.example.demo.service.UserService;
import com.example.demo.service.impl.UserServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class DemoApplicationTests {
    @Autowired
    private UserServiceImpl userService;


    @Test
    public void test() throws JsonProcessingException {
//        List<Permission> permissionDtos = userService.getPermissions(new ArrayList<>());
//        ObjectMapper mapper = new ObjectMapper();
//        String json = mapper.writeValueAsString(permissionDtos);
//        log.info(json);
    }

}
