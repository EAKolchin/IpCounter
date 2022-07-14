import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final long READ_LENGTH = 3221225472L; // TODO
    public static void main(String[] args) throws IOException, InterruptedException {
        long startTime = System.currentTimeMillis();
        File file = new File("C:\\Projects\\ip_addresses");
        long fileSize = file.length();
        long taskCount = fileSize / READ_LENGTH + 1;
        UniqueIpArray counter = new UniqueIpArray();
        ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        for (int i = 0; i < taskCount; i++) {
            service.execute(new Task(i * READ_LENGTH,
                    (i + 1) * READ_LENGTH,
                    new RandomAccessFile(file, "r"),
                    counter));
        }
        service.shutdown();
        service.awaitTermination(30, TimeUnit.MINUTES);

        //new Task(0, fileSize / 100, new RandomAccessFile(file, "r"), counter).run();

        System.out.println("Unique IP address: " + counter.getCount());
        System.out.println("Spent time: "+ (System.currentTimeMillis() - startTime) / 1000 + " sec");
    }
}
