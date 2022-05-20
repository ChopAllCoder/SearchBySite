package controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author Meng Ling'en
 * @create 2022-03-19-1:33
 *  程序主入口
 **/
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
class HelloWorldMainApplication {

    public static void main(String[] args) throws Exception {

        SpringApplication.run(HelloWorldMainApplication.class,args);
    }
}
