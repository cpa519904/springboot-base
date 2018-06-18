package com.company.pojo.entity;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "user")
public class User extends BaseModel {
    private String username;
    private String password;
    private String role;
}
