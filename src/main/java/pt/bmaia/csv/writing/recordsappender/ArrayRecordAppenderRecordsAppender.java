package pt.bmaia.csv.writing.recordsappender;

import pt.bmaia.csv.writing.delimiterappender.DelimiterAppender;
import pt.bmaia.csv.writing.recordappender.RecordAppender;
import pt.procurainterna.text.charsoutput.CharsOutput;

public class ArrayRecordAppenderRecordsAppender<R> implements RecordsAppender<R[]> {

  private final RecordAppender<? super R> recordAppender;
  private final DelimiterAppender recordDelimiterAppender;

  public ArrayRecordAppenderRecordsAppender(final RecordAppender<? super R> recordAppender,
      final DelimiterAppender recordDelimiterAppender) {
    this.recordAppender = recordAppender;
    this.recordDelimiterAppender = recordDelimiterAppender;
  }

  @Override
  public void writeTo(final R[] dataRecords, final CharsOutput charsOutput) {
    final int limit = dataRecords.length - 1;

    for (int i = 0 ; i < limit; i++) {
      recordAppender.writeTo(dataRecords[i], charsOutput);

      recordDelimiterAppender.writeTo(charsOutput);
    }

    recordAppender.writeTo(dataRecords[limit], charsOutput);
  }

}
