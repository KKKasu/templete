package dogapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * BreedFetcher implementation that relies on the dog.ceo API.
 * Reports any failure as a BreedNotFoundException, as required by the interface.
 */
public class DogApiBreedFetcher implements BreedFetcher {
    private final OkHttpClient client = new OkHttpClient();

    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        // Construct API URL (must be lowercase)
        String url = "https://dog.ceo/api/breed/" + breed.toLowerCase() + "/list";
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            // If network or response body is invalid
            if (response.body() == null) {
                throw new BreedNotFoundException(breed);
            }

            String body = response.body().string();
            JSONObject json = new JSONObject(body);

            // and {"status":"error", "message":"Breed not found"} for invalid ones
            if (!"success".equalsIgnoreCase(json.optString("status"))) {
                throw new BreedNotFoundException(breed);
            }

            JSONArray message = json.optJSONArray("message");
            if (message == null) {
                throw new BreedNotFoundException(breed);
            }

            List<String> subBreeds = new ArrayList<>();
            for (int i = 0; i < message.length(); i++) {
                subBreeds.add(message.getString(i));
            }

            return subBreeds;

        } catch (IOException e) {
            throw new BreedNotFoundException(breed);
        }
    }
}
