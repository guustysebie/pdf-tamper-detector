package be.ysebie.guust.tamperdetectorapp;

import be.ysebie.guust.tamperdetector.AnalysisResult;
import be.ysebie.guust.tamperdetector.IResultAggregator;
import be.ysebie.guust.tamperdetector.results.Result;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Aggregator implements IResultAggregator {

    private final String outputFolder;

    private final List<Result> results = new ArrayList<>();
    private final HashMap<String, int[]> counters = new HashMap<>();


    private int totalTamperingDetected = 0;
    private int totalNoTamperingDetected = 0;

    public Aggregator(String outputFolder) {
        this.outputFolder = outputFolder;
    }

    @Override
    public void onNewResult(Result result) {
        if (result.isTamperingDetected()) {
            results.add(result);
        }
        String indicatorName = result.getIndicator().getClass().getSimpleName();
        if (!counters.containsKey(indicatorName)) {
            int[] ctrs = new int[4];
            ctrs[AnalysisResult.NO_TAMPERING.ordinal()] = 0;
            ctrs[AnalysisResult.NO_ANALYSIS_DONE.ordinal()] = 0;
            ctrs[AnalysisResult.TAMPERING_DETECTED.ordinal()] = 0;
            ctrs[AnalysisResult.UNDETERMINED.ordinal()] = 0;
            counters.put(indicatorName, ctrs);
        }
        counters.get(indicatorName)[result.getAnalysisResult().ordinal()]++;
    }

    public void cleanForNewDocument() {
        this.results.clear();
    }


    public void reportAnalysis(String absolutePath, String filename) throws IOException {
        if (this.results.isEmpty()) {
            System.out.println("No tampering detected in: " + absolutePath);
            this.totalNoTamperingDetected++;
            return;
        }
        this.totalTamperingDetected++;
        File file = new File(outputFolder + File.separator + filename + "-report.txt");
        System.out.printf("Tampering detected in: %s for more info check: %s \n", filename, file.getAbsolutePath());
        if (file.exists()) {
            System.err.println("File already exists: " + file.getAbsolutePath());
            return;
        } else {
            file.createNewFile();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Document: ").append(absolutePath).append("\n");
        sb.append("-----------------RUN SUMMARY------------------\n");
        for (Result result : this.results) {
            sb.append(result.format().replace("\t","\t\t")).append("\n");
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(sb.toString());

    }

    public void printResults() {
        System.out.println("Results:");

        System.out.println("-----------------RUN SUMMARY------------------");
        System.out.println("Amount of documents processed: " + (this.totalTamperingDetected + this.totalNoTamperingDetected));
        System.out.println("Amount of tampering detected: " + this.totalNoTamperingDetected);

        System.out.println();
        System.out.println("Indicators that got triggered: ");
        System.out.printf("%-30s %15s %10s \n", "Indicator", "No tampering", "Tampering");
        this.counters.forEach((type, ints) -> {
            System.out.printf("%-35s %10d %10d \n", type.replaceAll("Indicator", ""),
                    ints[AnalysisResult.NO_TAMPERING.ordinal()], ints[AnalysisResult.TAMPERING_DETECTED.ordinal()]);
        });
    }
}
