package be.ysebie.guust.tamperdetectorapp;

import be.ysebie.guust.tamperdetector.TamperDetector;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class BpApp {


    public static void main(String[] args) throws IOException, InterruptedException {
        Instant start1 = Instant.now();

        String filePath = args[0];

        File file = new File(filePath);
        File[] directories = file.listFiles((current, name) -> new File(current, name).isDirectory());

        List<DataSet> dataSets = new ArrayList<>();
        assert directories != null;
        for (File directory : directories) {
            DataSet dataSet = new DataSet();
            dataSet.name = directory.getName();
            dataSet.pdfs = getPdfs(directory.getAbsolutePath());
            dataSets.add(dataSet);
        }

        for (DataSet dataSet : dataSets) {
            System.out.println("[APP] " + dataSet.name + " has " + dataSet.pdfs.size() + " pdfs");

            for (int i = 0; i < Math.min(10, dataSets.size()); i++) {
                System.out.println("[APP] \t\t" + dataSet.pdfs.get(i));
            }
        }
        Instant endReadingDirs = Instant.now();
        System.out.println("[APP] Reading dirs took: " + Duration.between(start1, endReadingDirs).toString());
        List<ExecutionProfile> profiles = ExecutionProfile.getAllProfiles();
        for (ExecutionProfile profile : profiles) {
            executeProfile(dataSets, profile);
        }
    }

    public static void executeProfile(List<DataSet> dataSets, ExecutionProfile profile) throws InterruptedException {
        System.out.println("[APP] Executing profile: " + profile.getTitle());
        for (DataSet dataSet : dataSets) {
            AtomicInteger counterBenign = new AtomicInteger(0);
            AtomicInteger counterErrorBenign = new AtomicInteger(0);

            List<Callable<Object>> tasks = new ArrayList<>();

            List<String> pdfs = dataSet.pdfs;
            for (int i = 0; i < pdfs.size(); i++) {
                String pdfPath = pdfs.get(i);
                int finalI = i;
                tasks.add(() -> {
                    System.out.printf("[LOG] (%d/%d) %s %s\n", finalI + 1, pdfs.size(), profile.getTitle(), pdfPath);
                    try {
                        byte[] pdf = Files.readAllBytes(Paths.get(pdfPath));
                        AtomicBoolean isTampered = new AtomicBoolean(false);
                        TamperDetector detector = new TamperDetector(pdf, profile, c -> true, result -> {
                            if (result.isTamperingDetected()) {
                                isTampered.set(true);
                            }
                        });
                        detector.analyze();
                        if (isTampered.get()) {
                            counterBenign.incrementAndGet();
                        }
                    } catch (Exception e) {
                        counterErrorBenign.incrementAndGet();
                    }
                    return null;
                });
            }
            ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(16);
            List<Future<Object>> answers = executor.invokeAll(tasks);
            System.out.println(
                    "[APP] Dataset: " + dataSet.name + " " + counterBenign.get() + " / " + dataSet.pdfs.size()
                            + " errors: " + counterErrorBenign.get());
        }
    }

    public static List<String> getPdfs(String path) throws IOException {
        List<String> pdfs = new ArrayList<>();
        try (Stream<Path> stream = Files.walk(Paths.get(path))) {
            stream.filter(Files::isRegularFile).forEach(f -> {
                if (f.getFileName().toString().length() == "0cef22da73ecf5054ee9160d75117b51e69d1186".length()) {
                    if (f.toAbsolutePath().toString().contains("Malicious")) {
                        pdfs.add(f.toAbsolutePath().toString());
                    }
                }
                if (f.getFileName().toString().toUpperCase().endsWith("PDF")) {
                    pdfs.add(f.toAbsolutePath().toString());
                }
            });
        }
        return pdfs;
    }

    static class DataSet {
        String name;
        List<String> pdfs;
    }


}
