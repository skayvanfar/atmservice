package ir.sk.atmservice.service;

import ir.sk.atmservice.events.source.SimpleSourceBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by sad.kayvanfar on 10/9/2021.
 */
@Service
@Slf4j
public class TransactionService {

    @Autowired
    SimpleSourceBean simpleSourceBean;

    public void withdraw(String accountNumber, @RequestParam Double amount) {
        simpleSourceBean.publishTransaction("WITHDRAW", accountNumber, amount);
    }

    public void deposit(String accountNumber, @RequestParam Double amount) {
        simpleSourceBean.publishTransaction("DEPOSIT", accountNumber, amount);
    }

}
