package pt.bmaia.csv;

import com.opencsv.CSVWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import pt.bmaia.csv.timer.TaskTimer;
import pt.bmaia.csv.timer.TimingResult;
import pt.bmaia.csv.writing.delimiterappender.DelimiterAppender;
import pt.bmaia.csv.writing.delimiterappender.DelimiterAppenders;
import pt.bmaia.csv.writing.escaper.StandardEscapeChecker;
import pt.bmaia.csv.writing.recordappender.RecordAppender;
import pt.bmaia.csv.writing.recordappender.RecordAppenders;
import pt.bmaia.csv.writing.recordsappender.RecordsAppender;
import pt.bmaia.csv.writing.recordsappender.RecordsAppenders;
import pt.bmaia.csv.writing.recordsappender.Rfc4180CompliantCharSequenceArraysRecordsAppender;
import pt.bmaia.csv.writing.recordsappender.Rfc4180CompliantIterableRecordsAppender;
import pt.bmaia.csv.writing.recordsappender.Rfc4180CompliantMatrixRecordsAppender;
import pt.bmaia.csv.writing.valueappender.ValueAppender;
import pt.bmaia.csv.writing.valueappender.ValueAppenders;
import pt.bmaia.csv.writing.escaper.Escaper;
import pt.bmaia.csv.writing.escaper.Escapers;
import pt.bmaia.csv.writing.qualifierappender.QualifierAppender;
import pt.bmaia.csv.writing.qualifierappender.QualifierAppenders;
import pt.procurainterna.injection4j.module.builder.ModuleBuilder;
import pt.procurainterna.injection4j.module.builder.ModuleBuilders;
import pt.procurainterna.injection4j.provider.Provider;
import pt.procurainterna.injection4j.provider.Providers;
import pt.procurainterna.lang.iterators.Iterators;
import pt.procurainterna.lang.text.appendable.Appendables;
import pt.procurainterna.lang.text.charsoutput.CharsOutput;
import pt.procurainterna.lang.text.charsoutput.CharsOutputs;
import pt.procurainterna.lang.text.charsoutput.writer.CharsOutputWriter;

public class WritingBenchmark {

  private static final List<List<String>> ITERABLE_RECORDS = records();
  private static final List<String[]> ARRAY_RECORDS =
      ITERABLE_RECORDS.stream().map(list -> list.toArray(String[]::new))
          .collect(Collectors.toList());

  private static final String[][] MATRIX_RECORDS = ARRAY_RECORDS.toArray(String[][]::new);
  private static final int LINES_COUNT = ITERABLE_RECORDS.size();

  public static void main(String[] args) {
    try {
      Thread.sleep(2_000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    final RecordsAppender<String[][]> matrixAppender = new Rfc4180CompliantMatrixRecordsAppender();
    final Runnable straightCutMatrixTask = () -> {
      matrixAppender.writeTo(MATRIX_RECORDS, charsOutput());
    };
    final RecordsAppender<? super Iterator<CharSequence[]>> arrayAppender =
        new Rfc4180CompliantCharSequenceArraysRecordsAppender();
    final Runnable straightCutArrayTask = () -> {
      arrayAppender.writeTo(Iterators.simplifyType(ARRAY_RECORDS.iterator()), charsOutput());
    };

    final RecordsAppender<Iterator<Iterable<String>>> iterableAppender =
        new Rfc4180CompliantIterableRecordsAppender();
    final Runnable straightCutListTask = () -> {
      iterableAppender.writeTo(Iterators.simplifyType(ITERABLE_RECORDS.iterator()), charsOutput());
    };
    final RecordsAppender<char[][][]> charMatrixAppender =
        RecordsAppenders.rfc4180CompliantCharMatrixAppender();
    final char[][][] charMatrix = ARRAY_RECORDS.stream()
        .map(values -> Arrays.stream(values).map(String::toCharArray).toArray(char[][]::new))
        .toArray(char[][][]::new);
    final Runnable straightCutCharMatrixTask = () -> {
      charMatrixAppender.writeTo(charMatrix, charsOutput());
    };

    final List<Pair<String, Runnable>> tasks = new ArrayList<>(30);

    tasks.add(pair("straightcut - matrix of strings", straightCutMatrixTask));
    tasks.add(pair("straightcut - list of arrays", straightCutArrayTask));
    tasks.add(pair("straightcut - list of lists", straightCutListTask));
    tasks.add(pair("straightcut - matrix of chars", straightCutCharMatrixTask));

    System.out.println();

    final StrategyInserter charEscaper = builder -> builder.addValue(Escaper.class,
        Escapers.fromChar('"'));
    final StrategyInserter charArrayEscaper = builder -> builder.addValue(Escaper.class,
        Escapers.fromCharArray("\"".toCharArray()));

    final List<Pair<String, StrategyInserter>> escapers =
        List.of(pair("char escaper", charEscaper), pair("char array escaper", charArrayEscaper));

    final StrategyInserter charQualifierAppender =
        builder -> builder.addValue(QualifierAppender.class, QualifierAppenders.fromChar('"'));
    final StrategyInserter charArrayQualifierAppender =
        builder -> builder.addValue(QualifierAppender.class,
            QualifierAppenders.fromCharArray("\"".toCharArray()));

    final List<Pair<String, StrategyInserter>> qualifierAppenders =
        List.of(pair("char qualifier appender", charQualifierAppender),
            pair("char array qualifier appender", charArrayQualifierAppender));

    final StrategyInserter alwaysQualifiedValueAppender =
        builder -> builder.addInvocation(ValueAppender.class, ValueAppenders::alwaysQualified,
            Escaper.class, QualifierAppender.class);
    final StrategyInserter qualifiedWhenNeededValueAppender =
        builder -> builder.addInvocation(ValueAppender.class, ValueAppenders::qualifiedIfEscaped,
            Escaper.class, QualifierAppender.class);

    final List<Pair<String, StrategyInserter>> valueAppenders =
        List.of(pair("always qualified value appender", alwaysQualifiedValueAppender),
            pair("qualified when needed", qualifiedWhenNeededValueAppender));

    final StrategyInserter charValueDelimiterAppender =
        builder -> builder.addValue(DelimiterAppender.class, DelimiterAppenders.fromChar(';'));
    final StrategyInserter charArrayValueDelimiterAppender =
        builder -> builder.addValue(DelimiterAppender.class,
            DelimiterAppenders.fromCharArray(new char[]{';'}));

    final List<Pair<String, StrategyInserter>> valueDelimiterAppenders =
        List.of(pair("char value delimiter appender", charValueDelimiterAppender),
            pair("char array value delimiter", charArrayValueDelimiterAppender));

    final ModuleBuilder contextBuilder = ModuleBuilders.map();

    final Map<String, StrategyInserter> inserters = new LinkedHashMap<>();

    escapers.forEach(escaper -> {
      inserters.put(escaper.left, escaper.right);

      qualifierAppenders.forEach(qualifierAppender -> {
        inserters.put(qualifierAppender.left, qualifierAppender.right);

        valueAppenders.forEach(valueAppender -> {
          inserters.put(valueAppender.left, valueAppender.right);

          valueDelimiterAppenders.forEach(valueDelimiterAppender -> {
            inserters.put(valueDelimiterAppender.left, valueDelimiterAppender.right);

            contextBuilder.addInvocation(RecordAppender.class, RecordAppenders::iterableBased,
                ValueAppender.class, DelimiterAppender.class);

            final StringBuilder nameBuilder = new StringBuilder();
            inserters.forEach((key, value) -> {
              nameBuilder.append(key);
              nameBuilder.append(" | ");
              value.register(contextBuilder);
            });
            nameBuilder.setLength(nameBuilder.length() - 3);

            final Provider instanceProvider =
                Providers.recursive(contextBuilder.build());

            final RecordAppender<List<String>> recordAppender =
                instanceProvider.provide(RecordAppender.class);

            final DelimiterAppender recordDelimiterAppender = DelimiterAppenders.optimize("\r\n");

            final RecordsAppender<Iterator<List<String>>> iterableRecordsAppender =
                RecordsAppenders.iteratorBased(recordAppender, recordDelimiterAppender);

            final Runnable task = () -> {
              final CharsOutput charsOutput = charsOutput();
              iterableRecordsAppender.writeTo(ITERABLE_RECORDS.iterator(), charsOutput);
            };
            tasks.add(pair(nameBuilder.toString(), task));

            inserters.remove(valueDelimiterAppender.left);
          });

          inserters.remove(valueAppender.left);
        });

        inserters.remove(qualifierAppender.left);
      });

      inserters.remove(escaper.left);
    });

    System.out.println();

    final Runnable openCsvTask = () -> {
      try (CSVWriter writer = new CSVWriter(writer())) {
        writer.writeAll(ARRAY_RECORDS);

      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    };

    final CSVFormat csvFormat = CSVFormat.RFC4180.withQuoteMode(QuoteMode.MINIMAL);
    final Runnable apacheCsvTask = () -> {
      try (final CSVPrinter csvPrinter = csvFormat.print(appendable())) {

        for (List<String> iterableRecord : ITERABLE_RECORDS) {
          csvPrinter.printRecord(iterableRecord);
        }

      } catch (final IOException e) {
        throw new RuntimeException(e);
      }
    };


    final Runnable apacheArrayCsvTask = () -> {
      try (final CSVPrinter csvPrinter = csvFormat.print(appendable())) {

        for (String[] arrayRecord : ARRAY_RECORDS) {
          csvPrinter.printRecord((Object[]) arrayRecord);
        }

      } catch (final IOException e) {
        throw new RuntimeException(e);
      }
    };

    tasks.add(pair("apache csv - User guide example", apacheCsvTask));
    tasks.add(pair("apache csv - Array records adapted user guide example", apacheArrayCsvTask));
    tasks.add(pair("opencsv - GitHub example", openCsvTask));

    final int rounds = 25;
    final List<TimingResult> results =
        tasks.stream().map(pair -> TaskTimer.timeTask(pair.left, pair.right, rounds))
            .sorted(Comparator.comparingLong(result -> result.milliseconds))
            .collect(Collectors.toList());

    final DecimalFormat decimalFormat =
        (DecimalFormat) DecimalFormat.getInstance(Locale.forLanguageTag("pt-PT"));
    decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
    decimalFormat.setDecimalSeparatorAlwaysShown(true);
    decimalFormat.setMaximumFractionDigits(3);
    decimalFormat.setMinimumFractionDigits(3);

    final int totalLines = LINES_COUNT * rounds;
    System.out.println("Total lines: " + totalLines);
    results.forEach(result -> {
      final double lineFrequency = ((double) totalLines) / (result.milliseconds / 1_000.0);

      System.out.println(
          result.milliseconds + "ms (" + decimalFormat.format(lineFrequency) + "lines/s): "
              + result.name);
    });
  }

  private static Appendable appendable() {
    return Appendables.fromCharsOutput(charsOutput());
  }

  private static CharsOutput charsOutput() {
    return CharsOutputs.noOp();
  }

  private static Writer writer() {
    return new CharsOutputWriter(charsOutput());
  }

  private static List<List<String>> records() {
    return new LargeRecordCollection().get();
  }

  private static <L, R> Pair<L, R> pair(final L left, final R right) {
    return new Pair<>(left, right);
  }

  static class Pair<L, R> {

    public final L left;
    public final R right;

    Pair(final L left, final R right) {
      this.left = left;
      this.right = right;
    }
  }

  private static interface StrategyInserter {

    void register(final ModuleBuilder typedStrategiesStoreBuilder);

  }

  private static class DeferredStrategyInserter {

    private final Consumer<? super ModuleBuilder> consumer;

    private DeferredStrategyInserter(final Consumer<? super ModuleBuilder> consumer) {
      this.consumer = consumer;
    }

    public void register(final ModuleBuilder typedStrategiesStoreBuilder) {
      consumer.accept(typedStrategiesStoreBuilder);
    }

  }

}
