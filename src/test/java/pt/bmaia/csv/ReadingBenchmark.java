package pt.bmaia.csv;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.IOException;
import java.io.Reader;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import pt.bmaia.csv.WritingBenchmark.Pair;
import pt.bmaia.csv.reading.parsing.records.RecordsParserImpl;
import pt.bmaia.csv.timer.TaskTimer;
import pt.bmaia.csv.timer.TimingResult;
import pt.procurainterna.lang.text.charsinput.CharsInput;
import pt.procurainterna.lang.text.charsinput.CharsInputs;
import pt.procurainterna.lang.text.charsinput.reader.CharsInputReader;
import pt.procurainterna.lang.text.reader.Readers;

public class ReadingBenchmark {

  private static final Consumer<? super Object> DO_NOTHING = any -> {
  };

  private static final char[] INPUT =
      "\"aaaaaaaa\";\"\"\"\"\"\"\"\"\"\"\"\"\r\n\"\n;wqe123\";\";\n;asd\";\"\"\r\n".toCharArray();

  private static final char[] RECORDS = records();
  private static final int ROUNDS = 10_000;
  private static final int LINES_PER_FILE = 2000;


  public static void main(String[] args) {
    final RecordsParserImpl recordsParser = new RecordsParserImpl('"', ';', "\r\n".toCharArray());

    final List<Pair<String, Runnable>> tasks = new ArrayList<>();

    tasks.add(new Pair<>("apache", () -> {
      try {
        final CSVFormat format =
            CSVFormat.newFormat(';').builder().setAutoFlush(true).setQuoteMode(QuoteMode.ALL)
                .setQuote('"').setTrailingDelimiter(false).setRecordSeparator("\r\n")
                .setSkipHeaderRecord(false).build();

        format.parse(reader()).getRecords();

      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }));

    tasks.add(new Pair<>("opencsv", () -> {
      try (CSVReader csvReader = new CSVReader(new CharsInputReader(charsInput()))) {
        final List<String[]> records = new ArrayList<>();
        String[] values = null;
        while ((values = csvReader.readNext()) != null) {
          records.add(values);
        }
      } catch (IOException | CsvValidationException e) {
        throw new RuntimeException(e);
      }
    }));

    tasks.add(new Pair<>("straightcut", () -> {
      final List<List<CharSequence>> records = new ArrayList<>();
      recordsParser.parse(charsInput(), records::add);
    }));

    Collections.shuffle(tasks);

    final List<TimingResult> collect =
        tasks.stream().map(task -> TaskTimer.timeTask(task.left, task.right, ROUNDS))
            .sorted(Comparator.comparingLong(result -> result.milliseconds))
            .collect(Collectors.toList());


    final DecimalFormat decimalFormat =
        (DecimalFormat) DecimalFormat.getInstance(Locale.forLanguageTag("pt-PT"));
    decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
    decimalFormat.setDecimalSeparatorAlwaysShown(true);
    decimalFormat.setMaximumFractionDigits(3);
    decimalFormat.setMinimumFractionDigits(3);

    final int totalLines = LINES_PER_FILE * ROUNDS;
    collect.forEach(result -> {
      final double lineFrequency = ((double) totalLines) / (result.milliseconds / 1_000.0);

      System.out.println(result.milliseconds + "ms (" + decimalFormat.format(lineFrequency) + " lines/s): " + result.name);
    });
  }

  private static Reader reader() {
    return Readers.fromCharsInput(charsInput());
  }

  private static CharsInput charsInput() {
    return CharsInputs.fromCharArray(RECORDS);
  }

  private static char[] records() {
    final String inputAsString = String.valueOf(INPUT);

    final StringBuilder stringBuilder = new StringBuilder();

    for (int i = 0; i < LINES_PER_FILE; i++) {
      stringBuilder.append(inputAsString);
    }

    return stringBuilder.toString().toCharArray();
  }
}
