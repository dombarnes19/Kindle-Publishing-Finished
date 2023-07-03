package com.amazon.ata.kindlepublishingservice.publishing;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Singleton
public class BookPublishRequestManager {
    Queue<BookPublishRequest> publishRequests;

   @Inject
    public BookPublishRequestManager(){
       publishRequests = new ConcurrentLinkedQueue<>();
   }

    public void addBookPublishRequest(BookPublishRequest bookPublishRequest) {
        publishRequests.add(bookPublishRequest);
    }
    public BookPublishRequest getBookPublishRequestToProcess() {
        return publishRequests.poll();
    }

}
