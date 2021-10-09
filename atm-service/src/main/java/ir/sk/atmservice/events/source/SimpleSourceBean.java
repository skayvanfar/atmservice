package ir.sk.atmservice.events.source;

import ir.sk.atmservice.events.model.TransactionModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * The message publication code
 * @author <a href="kayvanfar.sj@gmail.com">Saeed Kayvanfar</a> on 3/25/2020.
 */
@Component
@Slf4j
public class SimpleSourceBean {

    private Source source;

    @Autowired
    public SimpleSourceBean(Source source){
        this.source = source;
    }

    public void publishTransaction(String action, String accountNumber, double amount){
        log.debug("Sending Kafka message {} for Account Id: {}", action, accountNumber);
        TransactionModel change =  new TransactionModel(
                TransactionModel.class.getTypeName(),
                action,
                accountNumber,
                amount); // The message to be published is a Java POJO

        source.output().send(MessageBuilder.withPayload(change).build());
    }
}
