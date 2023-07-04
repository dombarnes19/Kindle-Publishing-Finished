package com.amazon.ata.kindlepublishingservice.dao;

import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;
import com.amazon.ata.kindlepublishingservice.exceptions.BookNotFoundException;
import com.amazon.ata.kindlepublishingservice.models.Book;
import com.amazon.ata.kindlepublishingservice.models.response.RemoveBookFromCatalogResponse;
import com.amazon.ata.kindlepublishingservice.publishing.KindleFormattedBook;
import com.amazon.ata.kindlepublishingservice.utils.KindlePublishingUtils;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;
import javax.inject.Inject;

public class CatalogDao {

    private final DynamoDBMapper dynamoDbMapper;

    /**
     * Instantiates a new CatalogDao object.
     *
     * @param dynamoDbMapper The {@link DynamoDBMapper} used to interact with the catalog table.
     */
    @Inject
    public CatalogDao(DynamoDBMapper dynamoDbMapper) {
        this.dynamoDbMapper = dynamoDbMapper;
    }

    /**
     * Returns the latest version of the book from the catalog corresponding to the specified book id.
     * Throws a BookNotFoundException if the latest version is not active or no version is found.
     * @param bookId Id associated with the book.
     * @return The corresponding CatalogItem from the catalog table.
     */
    public CatalogItemVersion getBookFromCatalog(String bookId) {
        CatalogItemVersion book = getLatestVersionOfBook(bookId);

        if (book == null || book.isInactive()) {
            throw new BookNotFoundException(String.format("No book found for id: %s", bookId));
        }

        return book;
    }

    // Returns null if no version exists for the provided bookId
    private CatalogItemVersion getLatestVersionOfBook(String bookId) {
        CatalogItemVersion book = new CatalogItemVersion();
        book.setBookId(bookId);

        DynamoDBQueryExpression<CatalogItemVersion> queryExpression = new DynamoDBQueryExpression()
            .withHashKeyValues(book)
            .withScanIndexForward(false)
            .withLimit(1);

        List<CatalogItemVersion> results = dynamoDbMapper.query(CatalogItemVersion.class, queryExpression);
        if (results.isEmpty()) {
            return null;
        }
        return results.get(0);
    }
    public Book delete(String bookId) {
        CatalogItemVersion latest = getLatestVersionOfBook(bookId);
        if (latest == null) {
            throw new BookNotFoundException("noo Book found ");
        } else {
            latest.setInactive(true);
            dynamoDbMapper.save(latest);
            return new Book(bookId, latest.getTitle(), latest.getAuthor(), latest.getText(), latest.getGenre().toString(), latest.getVersion());
        }
    }
    public void validateBookExists(String bookId) {
        if(getLatestVersionOfBook(bookId) == null) {
            throw new BookNotFoundException("no book found");
        }
    }
    public void addBookToCatalog(KindleFormattedBook book) {
        CatalogItemVersion item = new CatalogItemVersion();
        item.setBookId(KindlePublishingUtils.generateBookId());
        item.setVersion(1);
        item.setTitle(book.getTitle());
        item.setAuthor(book.getAuthor());
        item.setText(book.getText());
        item.setGenre(book.getGenre());
        dynamoDbMapper.save(item);
    }
    public CatalogItemVersion createOrUpdateBook(KindleFormattedBook book) throws BookNotFoundException {
        if (book.getBookId() == null) {
            String bookId = KindlePublishingUtils.generateBookId();
            CatalogItemVersion newBook = new CatalogItemVersion();
            newBook.setBookId(bookId);
            newBook.setVersion(1);
            newBook.setTitle(book.getTitle());
            newBook.setGenre(book.getGenre());
            newBook.setAuthor(book.getAuthor());
            newBook.setText(book.getText());
            dynamoDbMapper.save(newBook);
            return getLatestVersionOfBook(newBook.getBookId());
        }

        validateBookExists(book.getBookId());
        CatalogItemVersion latest = getLatestVersionOfBook(book.getBookId());
        latest.setVersion(latest.getVersion() + 1);
        delete(latest.getBookId());
        dynamoDbMapper.save(latest);
        return getLatestVersionOfBook(latest.getBookId());
    }
}

