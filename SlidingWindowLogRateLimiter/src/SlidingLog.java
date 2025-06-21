import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

class SlidingLog {
    private final Deque<Long> timestamps = new LinkedList<>();
    private final int limit;
    private final long windowSizeMillis;
    private final ReentrantLock lock = new ReentrantLock();

    public SlidingLog(int limit, long windowSizeMillis) {
        this.limit = limit;
        this.windowSizeMillis = windowSizeMillis;
    }

    public boolean allowRequest() {
        long now = System.currentTimeMillis();
        lock.lock();
        try {
            cleanup(now);
            if (timestamps.size() < limit) {
                timestamps.addLast(now);
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    private void cleanup(long now) {
        while (!timestamps.isEmpty() && now - timestamps.peekFirst() >= windowSizeMillis) {
            timestamps.pollFirst();
        }
    }
}
