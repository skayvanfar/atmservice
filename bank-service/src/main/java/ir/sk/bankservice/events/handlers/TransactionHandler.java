package ir.sk.bankservice.events.handlers;

import ir.sk.bankservice.events.models.TransactionModel;
import ir.sk.bankservice.service.UserAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;

@Slf4j
public class TransactionHandler {

    @Autowired
    private UserAccountService userAccountService;

    @StreamListener("inboundOrgChanges")
    public void loggerSink(TransactionModel transactionModel) {
        log.debug("Received a message of type " + transactionModel.getType());
        switch(transactionModel.getAction()){
            case "WITHDRAW":
                log.debug("Received a GET event from the atm service for Transaction id {}", transactionModel.getAccountNumber());
                userAccountService.withdraw(transactionModel.getAccountNumber(), transactionModel.getAmount());
                break;
            case "DEPOSIT":
                log.debug("Received a SAVE event from the atm service for Transaction id {}", transactionModel.getAccountNumber());
                userAccountService.deposit(transactionModel.getAccountNumber(), transactionModel.getAmount());
                break;
        }
    }
}
