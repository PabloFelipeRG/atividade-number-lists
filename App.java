import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class App {
    private static final ExecutorService executor = Executors.newWorkStealingPool(5);
    private static final Random random = new Random();

    public static void main(String[] args) throws InterruptedException {
        var futures = new HashSet<Future<List<Integer>>>();
        futures.add(generateRandomNumbersList());
        futures.add(generateRandomNumbersList());
        futures.add(generateRandomNumbersList());
        futures.add(generateRandomNumbersList());
        futures.add(generateRandomNumbersList());

        var list = futures.stream()
            .map(future -> {
                try {
                    return future.get();
                }
                catch (Exception e) {
                    throw new IllegalStateException(e);
                }
            })
            .collect(Collectors.flatMapping(
                    Collection::stream,
                    Collectors.toList()
            ));

        System.out.print(list);
        executor.shutdown();
    }

    private static Future<List<Integer>> generateRandomNumbersList() {
        var result = new ArrayList<Integer>();

        return executor.submit(() -> {
            for (int i = 0; i < 5; i++) {
                Integer number = random.nextInt();
                System.out.println(number);
                result.add(number);
            }

            return result;
        });
    }
}
