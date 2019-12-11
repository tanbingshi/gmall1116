package com.gmall.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootTest
class GmallUserApplicationTests {

    @Autowired
    DataSource dataSource;


    void contextLoads() throws SQLException {

        Connection connection = dataSource.getConnection();
        System.out.println(connection);


    }

}
