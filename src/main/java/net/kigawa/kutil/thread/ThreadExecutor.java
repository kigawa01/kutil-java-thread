package net.kigawa.kutil.thread;

import net.kigawa.kutil.kutil.interfaces.LoggerInterface;
import net.kigawa.kutil.kutil.interfaces.Module;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadExecutor implements Module {
    private static final Map<String, ThreadExecutor> map = new LinkedHashMap<>();
    private final LoggerInterface logger;
    private final String name;
    private ExecutorService service;

    public ThreadExecutor(String name, LoggerInterface logger) {
        this.logger = logger;
        this.name = name;
    }

    public static ThreadExecutor getInstance(String name) {
        return map.get(name);
    }

    @Override
    public synchronized void enable() {
        logger.info("enable thread executor");

        if (name != null)
            if (map.containsKey(name)) {
                logger.warning(name + " is already enable!");
                return;
            } else map.put(name, this);

        service = Executors.newCachedThreadPool();
    }

    @Override
    public synchronized void disable() {
        logger.info("disable thread executor");

        service.shutdown();

        if (name != null) map.remove(name);
    }

    public void execute(Runnable runnable) {
        service.execute(runnable);
    }

    public ExecutorService getService() {
        return service;
    }
}
