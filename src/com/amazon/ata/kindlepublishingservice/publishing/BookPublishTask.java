package com.amazon.ata.kindlepublishingservice.publishing;

import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;
import com.amazon.ata.kindlepublishingservice.dao.PublishingStatusDao;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.PublishingStatusItem;
import com.amazon.ata.kindlepublishingservice.enums.PublishingRecordStatus;
import com.amazon.ata.kindlepublishingservice.exceptions.BookNotFoundException;

import javax.inject.Inject;

public class BookPublishTask implements Runnable{

    BookPublishRequestManager manager;
    PublishingStatusDao publishingStatusDao;

    CatalogDao catalogDao;

    @Inject
    public BookPublishTask(BookPublishRequestManager bookPublishRequestManager, PublishingStatusDao publishingStatusDao, CatalogDao catalogDao) {
        this.manager = bookPublishRequestManager;
        this.publishingStatusDao = publishingStatusDao;
        this.catalogDao = catalogDao;

    }

    @Override
    public void run() {
       BookPublishRequest request =  manager.getBookPublishRequestToProcess();
       if(request == null){
           return;
       }
       publishingStatusDao.setPublishingStatus(request.getPublishingRecordId(), PublishingRecordStatus.IN_PROGRESS, request.getBookId());
       try{
       KindleFormattedBook book = KindleFormatConverter.format(request);
       CatalogItemVersion version = catalogDao.createOrUpdateBook(book);
       publishingStatusDao.setPublishingStatus(request.getPublishingRecordId(), PublishingRecordStatus.SUCCESSFUL, version.getBookId());
    } catch (BookNotFoundException e) {
           publishingStatusDao.setPublishingStatus(request.getPublishingRecordId(), PublishingRecordStatus.FAILED, request.getBookId());
       }
    }

}
