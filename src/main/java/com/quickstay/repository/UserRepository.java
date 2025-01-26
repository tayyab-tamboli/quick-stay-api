package com.quickstay.repository;


import com.quickstay.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

    Optional<User> findByUsernameAndPasswordAndActive(String username, String password, boolean active);
}
