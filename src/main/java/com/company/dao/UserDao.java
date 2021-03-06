package com.company.dao;

import com.company.pojo.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends CrudRepository<User, Long> {
    User findByUsername(String username);
    User findByUsernameAndPassword(String username, String password);
}
