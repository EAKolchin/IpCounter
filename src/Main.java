import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final long READ_LENGTH = 30485760; // TODO
    public static void main(String[] args) throws IOException, InterruptedException {
        long startTime = System.currentTimeMillis();
        File file = new File("C:\\Projects\\ip_addresses");
        long fileSize = file.length();
        long taskCount = 5;//fileSize / READ_LENGTH + 1;
        ExecutorService service = Executors.newFixedThreadPool(5);
        UniqueIpArray counter = new UniqueIpArray();
        for (int i = 0; i < taskCount; i++)
        {
            service.execute(new Task(i * READ_LENGTH,
                    (i + 1) * READ_LENGTH,
                    new RandomAccessFile(file, "r"),
                    counter));
        }
        service.shutdown();
        service.awaitTermination(10, TimeUnit.MINUTES); // TODO
        System.out.print("Unique IP address: " + counter.getCount() + "\n");
        System.out.print("Spent time: "+ (System.currentTimeMillis() - startTime) / 1000 + "seconds\n");
    }
}
