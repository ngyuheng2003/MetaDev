package com.metadev.connect.Repository;

import com.metadev.connect.Controller.User;

import java.util.Optional;

public interface UserRepository {
    void save(User user);
    Optional<User> findByUsername(String username);
}

