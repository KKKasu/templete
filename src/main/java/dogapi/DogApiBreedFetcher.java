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
 * Implementation of BreedFetcher that retrieves sub-breeds from the Dog CEO API.
 */
public class DogApiBreedFetcher implements BreedFetcher {
    private static final String API_URL = "https://dog.ceo/api/breed/%s/list";
    private final OkHttpClient client = new OkHttpClient();

    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        String url = String.format(API_URL, breed.toLowerCase());
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            // ① 无响应体 → 直接抛异常
            if (response.body() == null) {
                throw new BreedNotFoundException(breed);
            }

            // ② 读取内容
            String body = response.body().string().trim();
            if (body.isEmpty() || !body.startsWith("{")) {
                throw new BreedNotFoundException(breed);
            }

            // ③ 尝试解析 JSON
            JSONObject json = new JSONObject(body);
            String status = json.optString("status", "error");
            if (!"success".equalsIgnoreCase(status)) {
                throw new BreedNotFoundException(breed);
            }

            // ④ 提取 message 数组
            JSONArray arr = json.optJSONArray("message");
            if (arr == null) {
                throw new BreedNotFoundException(breed);
            }

            List<String> result = new ArrayList<>();
            for (int i = 0; i < arr.length(); i++) {
                result.add(arr.optString(i));
            }

            return result;

        } catch (IOException e) {
            throw new BreedNotFoundException(breed);
        } catch (Exception e) {
            throw new BreedNotFoundException(breed);
        }
    }
}
