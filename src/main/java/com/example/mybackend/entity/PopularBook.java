package com.example.mybackend.entity;

public class PopularBook {
    private String isbn;
    private Integer number;

    public PopularBook(String isbn, Integer number) {
        this.isbn = isbn;
        this.number = number;
    }
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
