package com.cipher.cipher;

import com.cipher.cipher.dao.SentenceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import javax.annotation.PostConstruct;

@SpringBootApplication
public class CipherApplication {

    @Autowired
    private SentenceDao dao;

    public static void main(String[] args) {
        SpringApplication.run(CipherApplication.class, args);

    }

    @PostConstruct
    public void init() {
        //dao.save(new Sentence("text", "text"));
    }

}
