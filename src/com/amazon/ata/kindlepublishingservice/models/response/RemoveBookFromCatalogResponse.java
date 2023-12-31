package com.amazon.ata.kindlepublishingservice.models.response;

import com.amazon.ata.kindlepublishingservice.models.Book;

import java.util.Objects;

public class RemoveBookFromCatalogResponse {

    private Book book;

    public RemoveBookFromCatalogResponse(Book book) {
        this.book = book;
    }


    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(getBook());
    }
    public RemoveBookFromCatalogResponse(Builder builder) {
        this.book = builder.book;
    }

    public static Builder builder() {return new Builder();}

    public static final class Builder {
        private Book book;

        private Builder() {

        }

        public Builder withBook(Book bookToUse) {
            this.book = bookToUse;
            return this;
        }
        public RemoveBookFromCatalogResponse build() { return new RemoveBookFromCatalogResponse(this);}
    }



}
