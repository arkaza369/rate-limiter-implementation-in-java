import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(10, 5); // 5 tokens/sec

        for (int i = 0; i < 100; i++) {
            executor.submit(() -> {
                if (limiter.allowRequest("User_Arkaza")) {
                    System.out.println("Allowed: " + Thread.currentThread().getName());
                } else {
                    System.out.println("Denied: " + Thread.currentThread().getName());
                }
            });
        }
        executor.shutdown();
//        without concurrency check
//        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(10, 5); // 5 tokens/sec
//        if (limiter.allowRequest("User_Arkaza")) {
//            System.out.println("Allowed");
//        } else {
//            System.out.println("Denied");
//        }

    }
}