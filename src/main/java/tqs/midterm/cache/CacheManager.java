package tqs.midterm.cache;

import java.time.LocalDateTime;
import java.util.*;

public class CacheManager {

    // Structure { url: [expire-time,object]}
    private Map<String,List<Object>> cache;
    private int hits;
    private int misses;

    public CacheManager(){
        this.cache = new HashMap<>();
        this.hits = 0;
        this.misses = 0;
    }

    public void add(String url,LocalDateTime ttl,Object obj){
        this.cache.put(url, Arrays.asList(ttl,obj));
    }

    public Object get(String url){
        List<Object> fetched = this.cache.get(url);
        if(fetched != null) {
            if (((LocalDateTime) fetched.get(0)).isAfter(LocalDateTime.now())){
                this.hits+=1;
                return fetched.get(1);
            }
            else{
                this.cache.remove(url);
                return null;
            }
        } else {
            this.misses+=1;
            return null;
        }
    }

    public boolean isEmpty(){
        return this.cache.isEmpty();
    }

    public int size(){
        return this.cache.size();
    }

    public int getHits() {
        return hits;
    }

    public int getMisses() {
        return misses;
    }
}
