package com.amazon.ata.kindlepublishingservice.activity;

import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;
import com.amazon.ata.kindlepublishingservice.exceptions.BookNotFoundException;
import com.amazon.ata.kindlepublishingservice.models.requests.RemoveBookFromCatalogRequest;
import com.amazon.ata.kindlepublishingservice.models.response.RemoveBookFromCatalogResponse;
import com.amazonaws.services.lambda.runtime.Context;

import javax.inject.Inject;

public class RemoveBookFromCatalogActivity {
    private CatalogDao catalogDao;

    @Inject
    public RemoveBookFromCatalogActivity(CatalogDao catalogDao) {
        this.catalogDao = catalogDao;
    }
    public RemoveBookFromCatalogResponse execute(RemoveBookFromCatalogRequest removeBookFromCatalogRequest) {
        if(!removeBookFromCatalogRequest.getBookId().equals(catalogDao.getBookFromCatalog(removeBookFromCatalogRequest.getBookId()))
                || catalogDao.getBookFromCatalog(removeBookFromCatalogRequest.getBookId()) == null) {
            throw new BookNotFoundException("no book found with given exception");
        }
        else catalogDao.delete(removeBookFromCatalogRequest.getBookId());
        return RemoveBookFromCatalogResponse.builder().build();
    }
}
