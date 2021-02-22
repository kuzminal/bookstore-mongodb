package com.kuzmin.bookstore.db.domain.i18n;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MultiLangDocument {
    public String language;
    public String text;
}
