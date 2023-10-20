package pt.bmaia.csv.writing.recordappender;

import java.util.Iterator;
import pt.bmaia.csv.format.FormatSequencesConfiguration;
import pt.bmaia.csv.writing.delimiterappender.DelimiterAppender;
import pt.bmaia.csv.writing.delimiterappender.DelimiterAppenders;
import pt.bmaia.csv.writing.valueappender.ValueAppender;
import pt.bmaia.csv.writing.valueappender.ValueAppenders;

public final class RecordAppenders {

  private RecordAppenders() {
  }

  public static <C extends CharSequence, I extends Iterable<C>> RecordAppender<I> iterableBased(
      final ValueAppender valueAppender, final DelimiterAppender valueDelimiter) {

    return iterableAppender(valueAppender, valueDelimiter);
  }

  public static <C extends CharSequence, I extends Iterator<C>> RecordAppender<I> iteratorBased(
      final ValueAppender valueAppender, final DelimiterAppender valueDelimiter) {

    return new CharSequenceIteratorRecordAppender<>(valueAppender, valueDelimiter);
  }

  public static <C extends CharSequence> RecordAppender<C[]> arrayBased(
      final ValueAppender valueAppender, final DelimiterAppender valueDelimiter) {

    return arrayAppender(valueAppender, valueDelimiter);
  }

  private static ValueAppender valueAppender(final CharSequence textQualifier) {
    return ValueAppenders.alwaysQualified(textQualifier);
  }

  private static <C extends CharSequence, I extends Iterable<C>> RecordAppender<I> iterableAppender(
      final FormatSequencesConfiguration formatSequencesConfiguration) {
    final ValueAppender valueConverter =
        valueAppender(formatSequencesConfiguration.valueDelimiter());

    final DelimiterAppender valueSeparatorDelimiterAppender =
        DelimiterAppenders.optimize(formatSequencesConfiguration.valueSeparator());

    return iterableAppender(valueConverter, valueSeparatorDelimiterAppender);
  }

  private static <C extends CharSequence, I extends Iterable<C>> RecordAppender<I> iterableAppender(
      final ValueAppender valueConverter, final DelimiterAppender valueSeparatorDelimiterAppender) {
    return new CharSequenceIterableRecordAppender<>(valueConverter, valueSeparatorDelimiterAppender);
  }

  private static <C extends CharSequence> RecordAppender<C[]> arrayAppender(
      final ValueAppender valueConverter, final DelimiterAppender valueSeparatorDelimiterAppender) {
    return new CharSequenceArrayRecordAppender<>(valueConverter, valueSeparatorDelimiterAppender);
  }

  private static <C extends CharSequence> RecordAppender<C[]> arrayAppender(
      final FormatSequencesConfiguration formatSequencesConfiguration) {
    final ValueAppender valueConverter =
        valueAppender(formatSequencesConfiguration.valueDelimiter());

    final DelimiterAppender valueSeparatorDelimiterAppender =
        DelimiterAppenders.optimize(formatSequencesConfiguration.valueSeparator());

    return new CharSequenceArrayRecordAppender<>(valueConverter, valueSeparatorDelimiterAppender);
  }

}
