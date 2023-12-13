package com.group20.dentanoid.DatabaseManagement;
import org.junit.jupiter.api.Test;
import com.group20.dentanoid.DatabaseManagement.Schemas.Clinic.ClinicSchema;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bson.Document;

class PayloadParserTest {

    @Test
    void convertPayloadToDocument() {
        String expectedRequestID = UUID.randomUUID().toString();

        String payload = PayloadParser.createJSONPayload(new HashMap<>() {{
            put("requestID", expectedRequestID);
        }});

        Document payloadDoc = PayloadParser.convertPayloadToDocument(payload, new ClinicSchema());
        assertEquals(expectedRequestID, payloadDoc.get("requestID").toString());
    }

    @Test
    void queryBySchema() {
    }
}