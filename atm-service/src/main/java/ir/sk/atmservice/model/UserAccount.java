package ir.sk.atmservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserAccount {

    private String id;
    private String name;
    private String accountNumber;
    private String pin;
    private Double balance;
}
