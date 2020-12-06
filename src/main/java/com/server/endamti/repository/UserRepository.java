package com.server.endamti.repository;

import com.server.endamti.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
    boolean existsByUsername(String username);
    User findDistinctByUsername(String username);
}
