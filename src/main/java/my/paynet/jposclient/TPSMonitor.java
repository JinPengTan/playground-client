//package my.paynet.jposclient;
//
//import java.util.concurrent.atomic.AtomicLong;
//import my.paynet.jposclient.listener.response.ExternalServerResponseListener;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//@Component
//public class TPSMonitor {
//
//    private final AtomicLong lastCheckedCount = new AtomicLong(0);
//    private final AtomicLong lastCheckedTime = new AtomicLong(System.currentTimeMillis());
//
//    @Scheduled(fixedRate = 5000) // Run every 60 seconds
//    public void calculateAndLogTPS() {
//        long currentCount = ExternalServerResponseListener.getCurrentResponse();
//        long currentTime = System.currentTimeMillis();
//
//        long countDiff = currentCount - lastCheckedCount.getAndSet(currentCount);
//        long timeDiffSeconds = (currentTime - lastCheckedTime.getAndSet(currentTime)) / 1000;
//
//        if (timeDiffSeconds > 0) { // Avoid division by zero
//            double tps = (double) countDiff / timeDiffSeconds;
//            System.out.printf("Current TPS: %.2f (Responses: %d, Time: %d seconds)%n", tps, countDiff, timeDiffSeconds);
//        } else {
//            System.out.println("Not enough time has passed to calculate TPS.");
//        }
//    }
//}
