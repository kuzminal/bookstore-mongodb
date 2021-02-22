package com.kuzmin.bookstore.api.service;

import com.kuzmin.bookstore.db.domain.entity.Author;
import com.kuzmin.bookstore.db.domain.enums.ContentType;
import com.kuzmin.bookstore.db.domain.i18n.MultiLangDocument;
import com.kuzmin.bookstore.db.repository.AuthorRepository;
import com.kuzmin.bookstore.api.model.AuthorCommonDTO;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.kuzmin.bookstore.api.util.CommonUtils.getLocalizedValue;
import static com.kuzmin.bookstore.api.util.CommonUtils.makeDataUrl;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final MongoTemplate mongoTemplate;
    private final GridFsOperations gridFsOperations;

    public AuthorService(AuthorRepository authorRepository,
                         MongoTemplate mongoTemplate,
                         GridFsOperations gridFsOperations) {
        this.authorRepository = authorRepository;
        this.mongoTemplate = mongoTemplate;
        this.gridFsOperations = gridFsOperations;
    }

    @Cacheable("authors")
    public AuthorCommonDTO getAuthorById(ObjectId id, Locale locale) {
        Optional<Author> author = authorRepository.findById(id);
        return author.map(value -> mapToCommonDTO(value, locale)).orElseGet(AuthorCommonDTO::new);
    }

    @Cacheable("authors")
    public List<AuthorCommonDTO> getAllAuthors(Locale locale) {
        List<Author> authors = authorRepository.findAll();
        return authors.stream()
                .map(author -> this.mapToCommonDTO(author, locale))
                .collect(Collectors.toList());
    }

    @Cacheable("authors")
    public Author getAuthorByName(MultiLangDocument name) {
        return authorRepository.findByName(name).orElse(null);
    }

    @CachePut("authors")
    public Author saveAuthor(Author author) {
        return authorRepository.save(author);
    }

    @CacheEvict("authors")
    public void deleteAllAuthors() {
        authorRepository.deleteAll();
    }

    @Cacheable("authors")
    public List<Author> fullTextSearchAuthor(String searchCriteria) {
        TextCriteria criteria = TextCriteria.forDefaultLanguage()
                .matching(searchCriteria)
                .caseSensitive(false);

        Query query = TextQuery.queryText(criteria)
                .sortByScore()
                .with(PageRequest.of(0, 5));
        return mongoTemplate.find(query, Author.class);
    }

    public String saveAuthorIcon() throws IOException {
        Document metaData = new Document();
        metaData.put("user", "alex");
        metaData.put("_contentType", ContentType.PNG.getContentType());
        ClassLoader classLoader = AuthorService.class.getClassLoader();
        String id;
        try (InputStream inputStream = classLoader.getResourceAsStream("images/author_photo.png")) {
            id = gridFsOperations.store(inputStream, "author_photo.png", metaData).toString();
        }
        return id;
    }

    public String getAuthorIcon(String id) {
        byte[] bytes;
        String contentType = "";
        String dataUrl = "";
        GridFSFile gridFsFile = gridFsOperations.findOne(new Query(Criteria.where("_id").is(id)));
        if (gridFsFile != null) {
            GridFsResource gridFsResource = gridFsOperations.getResource(gridFsFile);
            contentType = gridFsResource.getContentType();
            try (InputStream inputStream = gridFsResource.getContent()) {
                bytes = inputStream.readAllBytes();
                dataUrl = makeDataUrl(contentType, bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return dataUrl;
    }

    private AuthorCommonDTO mapToCommonDTO(Author author, Locale locale) {
        AuthorCommonDTO authorDTO = new AuthorCommonDTO();
        if (author != null) {
            authorDTO.setId(author.getId().toString());
            authorDTO.setName(getLocalizedValue(author.getName(), locale));
            authorDTO.setAge(author.getAge());
            authorDTO.setImgId(author.getImgId());
            String img = getAuthorIcon(author.getImgId());
            authorDTO.setImgData(img);
        }
        return authorDTO;
    }
}
