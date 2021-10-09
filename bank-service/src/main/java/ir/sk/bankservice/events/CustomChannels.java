package ir.sk.bankservice.events;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author <a href="kayvanfar.sj@gmail.com">Saeed Kayvanfar</a> on 3/26/2020.
 */
public interface CustomChannels {
    @Input("inboundTransaction")
    SubscribableChannel transactions();
}
