package pt.bmaia.csv;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class LargeRecordCollection {

  public List<List<String>> get() {
    final InputStream resourceAsStream = fileInputStream();

    final Scanner scanner = new Scanner(
        new BufferedReader(new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8)));

    final List<List<String>> records = new ArrayList<>();

    int count = 0;
    while (scanner.hasNextLine()) {
      final String line = scanner.nextLine();
      count++;

      final String[] values = line.split(",");

      if (count % 3 == 0) {
        values[1] += "\"";
      }

      records.add(Arrays.asList(values));
    }

    final List<List<String>> result = new ArrayList<>();
    result.addAll(records);
    result.addAll(records);
    result.addAll(records);

    return result;
  }

  private static InputStream fileInputStream() {
    return LargeRecordCollection.class.getClassLoader().getResourceAsStream("filtered.csv");
  }

}
