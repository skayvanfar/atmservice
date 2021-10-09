package ir.sk.atmservice.events.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * @author <a href="kayvanfar.sj@gmail.com">Saeed Kayvanfar</a> on 3/25/2020.
 */
@Data
@RequiredArgsConstructor
public class TransactionModel {
    private String type;
    /**
     * This is the action that triggered the event. I’ve included the action in
     * the message to give the message consumer more context on how it should process
     * an event
     */
    private String action;

    /**
     * This is the accountNumber associated with the event
     */
    private String accountNumber;

    private double amount;


    public TransactionModel(String type, String action, String accountNumber, double amount) {
        super();
        this.type   = type;
        this.action = action;
        this.accountNumber = accountNumber;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "TransactionModel [type=" + type +
                ", action=" + action +
                ", accountNumber="  + accountNumber +
                ", amount=" + amount + "]";
    }
}
