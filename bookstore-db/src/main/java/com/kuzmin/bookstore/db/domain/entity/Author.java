package com.kuzmin.bookstore.db.domain.entity;

import com.kuzmin.bookstore.db.domain.i18n.MultiLangDocument;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Set;

@Document(collection = "authors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Author implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private ObjectId id;
    @TextIndexed
    private Set<MultiLangDocument> name;
    private int age;
    private String imgId;
}
