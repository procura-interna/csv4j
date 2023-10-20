package pt.bmaia.csv.writing.recordsappender;

import java.util.Iterator;
import pt.bmaia.csv.writing.delimiterappender.DelimiterAppender;
import pt.bmaia.csv.writing.recordappender.RecordAppender;

public final class RecordsAppenders {

  private RecordsAppenders() {
  }

  public static <R> RecordsAppender<Iterator<R>> iteratorBased(
      final RecordAppender<? super R> recordAppender, final DelimiterAppender delimiterAppender) {
    return new IteratorRecordAppenderRecordsAppender<>(recordAppender, delimiterAppender);
  }

  public static <R> RecordsAppender<R[]> arrayBased(final RecordAppender<? super R> recordAppender,
      final DelimiterAppender delimiterAppender) {
    return new ArrayRecordAppenderRecordsAppender<>(recordAppender, delimiterAppender);
  }

  public static <R> RecordsAppender<String[][]> rfc4180CompliantMatrixAppender() {
    return new Rfc4180CompliantMatrixRecordsAppender();
  }

  public static <R> RecordsAppender<Iterator<CharSequence[]>> rfc4180CompliantArraysAppender() {
    return new Rfc4180CompliantCharSequenceArraysRecordsAppender();
  }

  public static <R> RecordsAppender<char[][][]> rfc4180CompliantCharMatrixAppender() {
    return new Rfc4180CompliantCharMatrixRecordsAppender();
  }

  /**
   * This method cannot be called directly if your iterator is of type
   * {@code Iteratpr<List<String>>}, for example. To get around this, use
   * {@link pt.bmaia.iterators.Iterators#simplifyType(Iterator) Iterators.simplifyType} to easily
   * obtain a compatible iterator.
   * <p/>
   * Example:
   * <pre>
   * RecordsAppender<Iterator<Iterable<String>>> recordsAppender = RecordsAppenders.rfc4180CompliantIterableAppender();
   *
   * Iterator&lt;List&lt;String>> listIterator = Collections.emptyList().iterator();
   *
   * // Doesn't compile
   * recordsAppender.writeTo(listIterator, null);
   *
   * // Compiles
   * recordsAppender.writeTo(Iterators.simplifyType(listIterator), null);
   * </pre>
   */
  public static <R> RecordsAppender<Iterator<Iterable<String>>> rfc4180CompliantIterableAppender() {
    return new Rfc4180CompliantIterableRecordsAppender();
  }

}
