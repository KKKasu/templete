package dogapi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CachingBreedFetcher implements BreedFetcher {
    private final BreedFetcher delegate;
    private final Map<String, List<String>> cache = new HashMap<>();
    private int callsMade = 0; // ← 新增：记录调用次数

    public CachingBreedFetcher(BreedFetcher delegate) {
        this.delegate = delegate;
    }

    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        String key = breed.toLowerCase();

        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        callsMade++;

        try {
            List<String> result = delegate.getSubBreeds(breed);
            cache.put(key, result);  // 只缓存成功结果
            return result;
        } catch (BreedNotFoundException e) {
            // 不缓存失败
            throw e;
        }
    }

    public int getCallsMade() {
        return callsMade;
    }
}
