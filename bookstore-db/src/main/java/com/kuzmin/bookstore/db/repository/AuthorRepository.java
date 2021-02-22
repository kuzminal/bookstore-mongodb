package com.kuzmin.bookstore.db.repository;

import com.kuzmin.bookstore.db.domain.entity.Author;
import com.kuzmin.bookstore.db.domain.i18n.MultiLangDocument;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends MongoRepository<Author, ObjectId> {
    Optional<Author> findByName(MultiLangDocument name);
}
