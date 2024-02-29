package com.example.mybackend.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "image")
public class BookIcon {
    @Id
    private String isbn;

    private String iconBase64;

    public BookIcon() {}
    public BookIcon(String isbn, String iconBase64) {
        this.iconBase64 = iconBase64;
        this.isbn = isbn;
    }

    public String getIconBase64() {
        return iconBase64;
    }

    public void setIconBase64(String iconBase64) {
        this.iconBase64 = iconBase64;
    }
}
