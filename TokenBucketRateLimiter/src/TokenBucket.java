import java.util.concurrent.locks.ReentrantLock;

class TokenBucket {
    private final int capacity;
    private double tokens;
    private final double refillRate;
    private long lastRefillTime;
    private final ReentrantLock lock = new ReentrantLock();

    public TokenBucket(int capacity, double refillRatePerSecond) {
        this.capacity = capacity;
        this.tokens = capacity;
        this.refillRate = refillRatePerSecond / 1000.0; // convert to tokens/ms
        this.lastRefillTime = System.currentTimeMillis();
    }

    public boolean allowRequest() {
        lock.lock();
        try {
            long now = System.currentTimeMillis();
            refillTokens(now);

            if (tokens >= 1) {
                tokens -= 1;
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    private void refillTokens(long currentTime) {
        long elapsed = currentTime - lastRefillTime;
        double tokensToAdd = elapsed * refillRate;
        tokens = Math.min(capacity, tokens + tokensToAdd);
        lastRefillTime = currentTime;
    }
}
