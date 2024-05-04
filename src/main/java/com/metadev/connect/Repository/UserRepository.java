package com.metadev.connect.Repository;

import com.metadev.connect.Entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository {


    public int registerNewUser(String username, String email, String password);

    public List<String> findUserByUsername(String username) throws InterruptedException;
    public List<String> findUserUsernameById(Long user_id) throws InterruptedException;

    public List<String> findUserByEmail(String email);
    public List<User> findUserInfoByUsername(String username);

    public boolean loginUserByEmail(String email, String password);

    public boolean loginUserByUsername(String username, String password);
}

