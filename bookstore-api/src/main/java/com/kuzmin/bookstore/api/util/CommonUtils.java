package com.kuzmin.bookstore.api.util;

import com.kuzmin.bookstore.db.domain.i18n.MultiLangDocument;

import java.util.Base64;
import java.util.Locale;
import java.util.Set;

public class CommonUtils {
    public static String makeDataUrl(String contentType, byte[] content) {
        String dataUrl = "data:";
        byte[] encodedBytes = Base64.getEncoder().encode(content);
        dataUrl = dataUrl + contentType + ";base64," + new String(encodedBytes);
        return dataUrl;
    }

    public static String getLocalizedValue(Set<MultiLangDocument> multiLangDocument, Locale locale) {
        MultiLangDocument doc = multiLangDocument.stream()
                .filter(m -> m.getLanguage().equals(locale.getLanguage()))
                .findFirst().orElse(new MultiLangDocument());
        return doc.getText();
    }
}
