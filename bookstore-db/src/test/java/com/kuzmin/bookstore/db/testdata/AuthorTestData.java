package com.kuzmin.bookstore.db.testdata;

import com.kuzmin.bookstore.db.domain.entity.Author;
import com.kuzmin.bookstore.db.domain.i18n.MultiLangDocument;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

public class AuthorTestData {
    public static Binary getPhoto() throws IOException {
        Binary img;
        ClassLoader classLoader = AuthorTestData.class.getClassLoader();
        try (InputStream file = classLoader.getResourceAsStream("images/author_photo.png")) {
            assert file != null;
            img = new Binary(BsonBinarySubType.BINARY, file.readAllBytes());
        }
        return img;
    }

    public static Author getAuthor1() throws IOException {
        return new Author(
                new ObjectId(),
                Set.of(new MultiLangDocument("ru", "Алексей Кузьмин"),
                new MultiLangDocument("en", "Aleksey Kuzmin")),
                20, "");
    }
}
