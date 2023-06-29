package com.amazon.ata.kindlepublishingservice.publishing;

import java.util.Queue;


public class BookPublishRequestManager {
    Queue<BookPublishRequest> publishRequests;

    public BookPublishRequestManager(Queue<BookPublishRequest> publishRequests){
        this.publishRequests = publishRequests;
    }
    public BookPublishRequestManager(){}

    public void addBookPublishRequest(BookPublishRequest bookPublishRequest) {
        publishRequests.add(bookPublishRequest);
    }
    public BookPublishRequest getBookPublishRequestToProcess() {
        if(publishRequests.isEmpty()){
            return null;
        }
        return publishRequests.remove();
    }

}
