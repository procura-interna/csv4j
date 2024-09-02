package pt.bmaia.csv.reading.parsing.records;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import pt.procurainterna.lang.text.charsinput.CharsInput;

public class RecordsParserImpl implements RecordParser<List<CharSequence>> {

  private final char valueQualifier;
  private final char valueSeparator;
  private final char[] recordSeparator;

  public RecordsParserImpl(final char valueQualifier, final char valueSeparator,
      final char[] recordSeparator) {
    this.valueQualifier = valueQualifier;
    this.valueSeparator = valueSeparator;
    this.recordSeparator = recordSeparator;
  }

  @Override
  public void parse(final CharsInput charsInput,
      final Consumer<? super List<CharSequence>> recordsConsumer) {

    List<CharSequence> record = new ArrayList<>();
    readRecord(charsInput, recordsConsumer, record);

    if (!record.isEmpty()) {
      recordsConsumer.accept(record);
    }
  }

  private void readRecord(final CharsInput charsInput,
      final Consumer<? super List<CharSequence>> recordsConsumer, List<CharSequence> record) {
    while (!charsInput.isDone()) {
      final boolean[] endRecordFlagCache = new boolean[] {false};

      final CharSequence value = readValue(charsInput, endRecordFlagCache);
      record.add(value);

      if (endRecordFlagCache[0]) {
        recordsConsumer.accept(record);
        record = new ArrayList<>(record.size());
      }
    }
  }

  private CharSequence readValue(final CharsInput charsInput, final boolean[] endRecordFlagCache) {
    final char firstChar = charsInput.readChar();
    if (firstChar == valueQualifier) {
      // Qualified value
      return readQualifiedValue(charsInput, endRecordFlagCache);

    } else {
      // Unqualified value
      return readUnqualifiedValue(charsInput, firstChar, endRecordFlagCache);
    }
  }

  private CharSequence readQualifiedValue(final CharsInput charsInput,
      final boolean[] endRecordFlagCache) {
    // Need to unescape valueQualifier
    // Ends with valuequalifier-valueseparator or valuequalifier-recordseparator

    final StringBuilder value = new StringBuilder();

    while (!charsInput.isDone()) {
      final char currentChar = charsInput.readChar();

      if (currentChar != valueQualifier) {
        value.append(currentChar);

      } else {
        // check if is last or if escapes other qualifier

        if (charsInput.isDone()) {
          // was last qualifier; done.
          break;

        } else {
          final char followingChar = charsInput.readChar();
          if (followingChar == valueQualifier) {
            value.append(valueQualifier);

          } else if (followingChar == valueSeparator) {
            // Value done
            break;

          } else if (followingChar == recordSeparator[0]) {
            // Check if matches whole separator; fail if it doesn't

            for (int i = 1, limit = recordSeparator.length; i < limit; i++) {
              final char characterToCheck = charsInput.readChar();

              if (characterToCheck != recordSeparator[i]) {
                final StringBuilder errorMessageBuilder = new StringBuilder();
                errorMessageBuilder.append("unexpected character sequence after value qualifier: ");
                errorMessageBuilder.append(recordSeparator, 0, i);
                errorMessageBuilder.append(characterToCheck);
                errorMessageBuilder.append(". Expected: ");
                errorMessageBuilder.append(recordSeparator);

                throw new RuntimeException(errorMessageBuilder.toString());
              }
            }

            endRecordFlagCache[0] = true;
            break;

          } else {
            // Fail
            throw new RuntimeException("unexpected character after qualifier: " + followingChar);
          }
        }
      }
    }

    return value;
  }

  private StringBuilder readUnqualifiedValue(final CharsInput charsInput, final char firstChar,
      final boolean[] endRecordFlagCache) {
    // Ends with valueseparator or recordseparator
    // Fails on valuequalifier

    final StringBuilder value = new StringBuilder();

    char currentChar = firstChar;
    while (true) {
      if (currentChar == valueSeparator) {
        break;

      } else if (currentChar == recordSeparator[0]) {
        boolean matchesRecordSeparator = true;

        char lastChar;
        int lastIndex = 1;
        for (int limit = recordSeparator.length; lastIndex < limit; lastIndex++) {
          lastChar = charsInput.readChar();

          if (lastChar != recordSeparator[lastIndex]) {
            matchesRecordSeparator = false;
            break;
          }
        }

        if (matchesRecordSeparator) {
          endRecordFlagCache[0] = true;
          break;
        }

        value.append(recordSeparator, 0, lastIndex);

      } else {
        value.append(currentChar);
      }

      if (charsInput.isDone()) {
        break;
      }

      currentChar = charsInput.readChar();
    }

    return value;
  }
}
