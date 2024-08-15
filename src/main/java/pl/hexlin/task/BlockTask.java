package pl.hexlin.task;

import pl.hexlin.Instance;

import java.time.Duration;
import java.util.ArrayList;
import java.util.TimerTask;

public class BlockTask extends TimerTask {
    public static ArrayList<String> blockedUserIds = new ArrayList<>();
    public final Instance instance;

    public BlockTask(Instance instance) {
        this.instance = instance;
    }

    @Override
    public void run() {
        blockedUserIds.forEach(blockedUserId -> {
            if (instance.api.getUserById(blockedUserId).join().getActiveTimeout(instance.api.getServerById("1199446281840492629").get()).isEmpty()) {
                instance.api.getUserById(blockedUserId).join().timeout(instance.api.getServerById("1199446281840492629").get(), Duration.ofDays(7)).join();
            }
        });
    }
}
