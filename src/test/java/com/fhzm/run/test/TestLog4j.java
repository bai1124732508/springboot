package com.fhzm.run.test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestLog4j {

    public static void main(String[] args) throws Exception {
        Logger logger = LoggerFactory.getLogger(TestLog4j.class);
        logger.info("冲突测试");
        logger.info("Hello World");
        logger.info("冲突测试");
        logger.info("冲突测试2222");
        logger.info("冲突测试3333");
        logger.info("冲突测试44444");
        logger.info("冲突测试55555");
        logger.info("冲突测试66666");
        logger.info("冲突测试66666");
    }

}
