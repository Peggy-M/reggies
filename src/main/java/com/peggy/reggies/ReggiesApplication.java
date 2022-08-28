package com.peggy.reggies;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

import java.util.concurrent.BlockingDeque;

@SpringBootApplication
@Slf4j
@ServletComponentScan
public class ReggiesApplication {

    public static void main(String[] args) {
        log.info("项目已经开始启动");
        SpringApplication.run(ReggiesApplication.class, args);
    }

}
