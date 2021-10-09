package ir.sk.bankservice;

import ir.sk.bankservice.events.models.TransactionModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

@SpringBootApplication
@EnableBinding(Sink.class)
@Slf4j
public class AtmBankApplication {
    public static void main(String[] args) {
        SpringApplication.run(AtmBankApplication.class, args);
    }

    @StreamListener(Sink.INPUT)
    public void loggerSink(TransactionModel transactionModel) {
        log.debug("Received an event for transactionModel id {}", transactionModel.getAccountNumber());
    }
}
