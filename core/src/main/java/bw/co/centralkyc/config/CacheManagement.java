package bw.co.centralkyc.config;

import java.util.Map;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
public class CacheManagement {

    private final CacheManager cacheManager;

    public CacheManagement(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void clearAllCaches() {
        cacheManager.getCacheNames().forEach(cacheName -> cacheManager.getCache(cacheName).clear());
    }

    public void evict(String cacheName) {
        cacheManager.getCache(cacheName).clear();
    }

    public void evict(String cacheName, Object key) {
        cacheManager.getCache(cacheName).evict(key);
    }

    public void evict(Map<String, Object> cacheNameAndKeys) {

        cacheNameAndKeys.forEach((cacheName, key) -> {

            if (key != null) {
                cacheManager.getCache(cacheName).evict(key);
            } else {

                cacheManager.getCache(cacheName).clear();
            }

        });
    }

    public void put(String cacheName, Object key, Object value) {
        cacheManager.getCache(cacheName).put(key, value);
    }
}
