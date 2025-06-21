import java.util.concurrent.ConcurrentHashMap;

class TokenBucketRateLimiter {
    private final ConcurrentHashMap<String, TokenBucket> userBuckets = new ConcurrentHashMap<>();
    private final int capacity;
    private final double refillRatePerSecond;

    public TokenBucketRateLimiter(int capacity, double refillRatePerSecond) {
        this.capacity = capacity;
        this.refillRatePerSecond = refillRatePerSecond;
    }

    public boolean allowRequest(String userId) {
        TokenBucket bucket = userBuckets.get(userId);
        if (bucket == null) {
            bucket = new TokenBucket(capacity, refillRatePerSecond);
            TokenBucket existing = userBuckets.putIfAbsent(userId, bucket);
            if (existing != null) {
                bucket = existing; // another thread beat us to it
            }
        }
        return bucket.allowRequest();
    }
}
