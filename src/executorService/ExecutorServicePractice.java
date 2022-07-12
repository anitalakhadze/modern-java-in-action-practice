package executorService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ExecutorServicePractice {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
//        testScheduledExecutorService();
//        testExecutorServiceWithExecute();
//        testExecutorServiceWithSubmit();
//        testExecutorServiceWithInvokeAny();
//        testExecutorServiceWithInvokeAll();
    }

    public static void testScheduledExecutorService() throws InterruptedException {
        // Creating an instance of SingleThreadScheduledExecutor
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        // Creating a runnable task
        AtomicInteger counter = new AtomicInteger();
        Runnable runnableTask = () -> {
            try {
                TimeUnit.SECONDS.sleep(3);
                System.out.println("Counter is: " + counter);
                counter.getAndIncrement();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        // Assigning the above runnable task to the ScheduledExecutorService instance
        ScheduledFuture<?> scheduledFuture = executorService.scheduleAtFixedRate(runnableTask, 0, 5, TimeUnit.SECONDS);

        // Assigning a stopping condition to the executor service instance
        while (true) {
            Thread.sleep(1000);
            if (counter.get() >= 5) {
                executorService.shutdown();
                break;
            }
        }

        // for debugging the scheduled future above
        System.out.println(scheduledFuture.isDone() ? "ScheduledExecutorService is done." : "ScheduledExecutorService is not done.");
    }

    public static void testExecutorServiceWithExecute() throws InterruptedException {
        // Creating a new ExecutorService instance
        ExecutorService executorService = Executors.newFixedThreadPool(1);

        // Creating a runnable task
        AtomicInteger counter = new AtomicInteger();
        Runnable runnableTask = () -> {
            try {
                TimeUnit.SECONDS.sleep(3);
                System.out.println("Counter is: " + counter);
                counter.getAndIncrement();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        // Assigning a runnable task to the service (execute() is a void)
        executorService.execute(runnableTask);

        // Giving enough time to program to execute the runnable task before logging
        Thread.sleep(5000);

        // correct way of shutting down the service
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executorService.shutdown();
            }
        } catch (Exception e) {
            executorService.shutdownNow();
        }

        // logging the final result
        System.out.println("Counter now is: " + counter.get());
    }

    public static void testExecutorServiceWithSubmit() throws InterruptedException, ExecutionException {
        // Creating a new ExecutorService instance
        ExecutorService executorService = Executors.newFixedThreadPool(1);

        // Creating a new callable task
        Callable<String> callableTask = () -> {
            TimeUnit.SECONDS.sleep(3);
            return "Callable task's execution";
        };

        // Giving program enough time to execute the task
        Thread.sleep(5000);

        Future<String> future = executorService.submit(callableTask);

        // future.get() blocks until the result is available
        String s = future.get();

        // correct way of shutting down the service - if shutDown() is not called, executorService.isDone() will be false
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executorService.shutdown();
            }
        } catch (Exception e) {
            executorService.shutdownNow();
        }

        // Printing out the result of the future
        System.out.println("The callable task result: " + s);
    }

    public static void testExecutorServiceWithInvokeAny() {
        // Creating a new ExecutorService instance
        ExecutorService executorService = Executors.newFixedThreadPool(1);

        // Creating a new callable task
        Callable<String> callableTask = () -> {
            TimeUnit.SECONDS.sleep(3);
            return "Callable task's execution";
        };

        // Initializing a list of callable tasks
        List<Callable<String>> callableTasks = new ArrayList<>();
        callableTasks.add(callableTask);
        callableTasks.add(callableTask);
        callableTasks.add(callableTask);

        // Executing invokeAny() on the executor service
        String invokeAnyResult = "";
        try {
            invokeAnyResult = executorService.invokeAny(callableTasks);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // correct way of shutting down the service - if shutDown() is not called, executorService.isDone() will be false
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executorService.shutdown();
            }
        } catch (Exception e) {
            executorService.shutdownNow();
        }

        // Printing out the result of the execution and the number of the completed tasks
        System.out.println("Executor service return statement is: " + invokeAnyResult);
        System.out.println("Completed task count is: " + ((ThreadPoolExecutor) executorService).getCompletedTaskCount());
    }

    public static void testExecutorServiceWithInvokeAll() throws ExecutionException, InterruptedException {
        // Creating a new ExecutorService instance
        ExecutorService executorService = Executors.newFixedThreadPool(1);

        // Creating a new callable task
        Callable<String> callableTask = () -> {
            TimeUnit.SECONDS.sleep(3);
            return "Callable task's execution";
        };

        // Initializing a list of callable tasks
        List<Callable<String>> callableTasks = new ArrayList<>();
        callableTasks.add(callableTask);
        callableTasks.add(callableTask);
        callableTasks.add(callableTask);

        // Executing invokeAny() on the executor service
        List<Future<String>> futures = new ArrayList<>();
        try {
            futures = executorService.invokeAll(callableTasks);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // correct way of shutting down the service - if shutDown() is not called, executorService.isDone() will be false
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executorService.shutdown();
            }
        } catch (Exception e) {
            executorService.shutdownNow();
        }

        // Printing out the result of the execution and the number of the completed tasks
        for (int i = 0; i < futures.size(); i++) {
            System.out.println("Executor service return statement for future #" + i + " is: " + futures.get(i).get());
        }
        System.out.println("Completed task count is: " + ((ThreadPoolExecutor) executorService).getCompletedTaskCount());
    }
}
