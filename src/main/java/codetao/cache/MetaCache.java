package codetao.cache;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.concurrent.TimeUnit;

public class MetaCache {

    private final static Cache<String, Object> metaCache = CacheBuilder.newBuilder().maximumSize(100).expireAfterWrite(1800, TimeUnit.DAYS).build();

    public static Object getMetaCache(String key){
        return metaCache.getIfPresent(key);
    }

    public static void setMetaCache(String key, Object value){
        metaCache.put(key, value);
    }
}
