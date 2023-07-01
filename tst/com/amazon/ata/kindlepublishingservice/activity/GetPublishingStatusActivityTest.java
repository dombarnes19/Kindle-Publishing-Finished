package com.amazon.ata.kindlepublishingservice.activity;


import com.amazon.ata.kindlepublishingservice.dagger.ApplicationComponent;
import com.amazon.ata.kindlepublishingservice.dagger.DaggerApplicationComponent;
import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;
import com.amazon.ata.kindlepublishingservice.dao.PublishingStatusDao;

import com.amazon.ata.kindlepublishingservice.models.requests.GetPublishingStatusRequest;
import com.amazon.ata.kindlepublishingservice.helpers.KindlePublishingServiceTctTestDao.PublishingRecordStatus;

import com.amazon.ata.kindlepublishingservice.models.response.GetPublishingStatusResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


public class GetPublishingStatusActivityTest {



    private static final String Status_Message = "Book published at 2020-03-02 03:05:37.456";
    private static final ApplicationComponent COMPONENT = DaggerApplicationComponent.create();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void execute_SuccessFulPublishingProcess() {
        //GIVEN
        GetPublishingStatusRequest request = GetPublishingStatusRequest.builder()
                .withPublishingRecordId("publishingstatus.bdd319cb-05eb-494b-983f-6e1b983c4c46")
                .build();

        //WHEN
        GetPublishingStatusResponse response = COMPONENT.provideGetPublishingStatusActivity().execute(request);

        //THEN
        assertNotNull(response.getPublishingStatusHistory());
        assertEquals(response.getPublishingStatusHistory().get(0).getStatus(), PublishingRecordStatus.IN_PROGRESS.toString());

    }

    @Test
    public void execute_FailedPublishingProcess() {
        //GIVEN
        GetPublishingStatusRequest request = GetPublishingStatusRequest.builder()
                .withPublishingRecordId("publishingstatus.4bd41646-b1b2-4627-8304-5180c9b54e00")
                .build();

        //WHEN
        GetPublishingStatusResponse response = COMPONENT.provideGetPublishingStatusActivity().execute(request);

        //THEN
        assertNotNull(response.getPublishingStatusHistory());
        assertEquals(response.getPublishingStatusHistory().get(0).getStatus(), PublishingRecordStatus.FAILED.toString());

    }

    @Test
    public void execute_SuccessfulNewPublishingProcess() {
        //GIVEN
        GetPublishingStatusRequest request = GetPublishingStatusRequest.builder()
                .withPublishingRecordId("publishingstatus.2bc206a1-5b41-4782-a260-976c0a291825")
                .build();

        //WHEN
        GetPublishingStatusResponse response = COMPONENT.provideGetPublishingStatusActivity().execute(request);

        //THEN
        assertNotNull(response.getPublishingStatusHistory());
        assertEquals(response.getPublishingStatusHistory().get(2).getStatus(), PublishingRecordStatus.SUCCESSFUL.toString());
        assertEquals(response.getPublishingStatusHistory().get(2).getStatusMessage(), Status_Message);

    }

}
