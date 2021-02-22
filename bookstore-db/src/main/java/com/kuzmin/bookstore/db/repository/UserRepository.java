package com.kuzmin.bookstore.db.repository;

import com.kuzmin.bookstore.db.domain.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, ObjectId> {
    User findByUsername(String username);
}
