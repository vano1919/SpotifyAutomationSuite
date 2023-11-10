package org.epam.apiClasses.playlistClient;

import io.restassured.response.Response;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class PlaylistClient {
    public static final int MAX_RETRIES = 20;

    public Response sendRequestWithRetry(Supplier<Response> requestSupplier, Predicate<Response> responsePredicate) {
        Response response;
        for (int retries = 0; retries < MAX_RETRIES; retries++) {
            try {
                response = requestSupplier.get();
                if (responsePredicate.test(response)) {
                    return response;
                } else {
                    System.out.println("Response did not meet the expected criteria, retrying...");
                }
            } catch (Exception e) {
                System.out.println("An exception occurred while processing the request: " + e.getMessage());
            }
        }
        throw new RuntimeException("Request failed after " + MAX_RETRIES + " retries");
    }
}

