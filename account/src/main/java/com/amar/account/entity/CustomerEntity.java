package com.amar.account.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "Customer") // It has to be mentioned as the Class name is not same as Table Name
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString
public class CustomerEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO,generator="native")
    @GenericGenerator(name = "native",strategy = "native")
    private Long customerId;

    @Column(name = "name")
    private String name;


    private String email;

    @Column(name = "mobile_number")
    private String mobileNumber;

}
