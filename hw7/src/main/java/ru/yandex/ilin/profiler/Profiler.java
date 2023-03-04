package ru.yandex.ilin.profiler;

import org.aspectj.lang.Signature;

import java.io.PrintStream;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.Map;

public class Profiler {
    private static final Profiler PROFILER_INSTANCE = new Profiler();
    private String packagePrefix = null;
    private final Map<String, DoubleSummaryStatistics> statistics = new HashMap<>();

    public static Profiler getInstance() {
        return PROFILER_INSTANCE;
    }

    public void register(Signature signature, long executionTime) {
        String path = signature.getDeclaringTypeName() + "." + signature.getName();
        statistics.computeIfAbsent(path, __ -> new DoubleSummaryStatistics()).accept(0.000001 * executionTime);
    }

    public void printStats(PrintStream printStream) {
        Tree tree = new Tree("Profiling");
        statistics.forEach((key, value) -> tree.addElement(
            key,
            value.toString().replace(DoubleSummaryStatistics.class.getSimpleName(), "")
        ));
        tree.print(printStream);
    }

    public String getPackagePrefix() {
        return packagePrefix;
    }

    public void setPackagePrefix(String packagePrefix) {
        this.packagePrefix = packagePrefix;
    }
}
