package com.sayan.userauth.entities;

import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.Generated;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "user")
public class User {
    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private Long id;
    @Column(
            name = "username",
            nullable = false,
            updatable = false,
            unique = true
    )
    private String username;
    @Column(
            name = "password",
            nullable = false,
            length = 61
    )
    private String password;
    @Column(name = "role")
    private String role;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(
            name = "email",
            nullable = false,
            unique = true
    )
    @NotNull
    private String email;
    @Column(
            name = "phone",
            nullable = false,
            unique = true
    )
    private String phone;
}
