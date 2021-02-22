package com.kuzmin.bookstore.api.model;

import lombok.Data;

@Data
public class AuthorCommonDTO {
    private String id;
    private String name;
    private int age;
    private String imgId;
    private String imgData;
}
