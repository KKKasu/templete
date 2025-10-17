package dogapi;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class DogApiBreedFetcher implements BreedFetcher {
    private static final String API_URL = "https://dog.ceo/api/breed/%s/list";

    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        try {
            HttpClient client = HttpClient.newHttpClient();
            String url = String.format(API_URL, breed.toLowerCase());
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());
            String body = response.body();

            if (!body.contains("\"status\":\"success\"")) {
                throw new BreedNotFoundException(breed);
            }

            int start = body.indexOf('[');
            int end = body.indexOf(']');
            if (start < 0 || end < 0) {
                throw new BreedNotFoundException(breed);
            }

            String inside = body.substring(start + 1, end).trim();
            List<String> result = new ArrayList<>();
            if (!inside.isEmpty()) {
                for (String s : inside.replace("\"", "").split(",")) {
                    result.add(s.trim());
                }
            }
            return result;

        } catch (IOException | InterruptedException e) {
            throw new BreedNotFoundException(breed);
        }
    }
}
