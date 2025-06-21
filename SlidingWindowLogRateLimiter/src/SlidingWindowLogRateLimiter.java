import java.util.concurrent.ConcurrentHashMap;

class SlidingWindowLogRateLimiter {
    private final ConcurrentHashMap<String, SlidingLog> userLogs = new ConcurrentHashMap<>();
    private final int limit;
    private final long windowSizeMillis;

    public SlidingWindowLogRateLimiter(int limit, long windowSizeMillis) {
        this.limit = limit;
        this.windowSizeMillis = windowSizeMillis;
    }

    public boolean allowRequest(String userId) {
        SlidingLog log = userLogs.get(userId);
        if (log == null) {
            log = new SlidingLog(limit, windowSizeMillis);
            SlidingLog existing = userLogs.putIfAbsent(userId, log);
            if (existing != null) {
                log = existing; // another thread created it
            }
        }
        return log.allowRequest();
    }
}
