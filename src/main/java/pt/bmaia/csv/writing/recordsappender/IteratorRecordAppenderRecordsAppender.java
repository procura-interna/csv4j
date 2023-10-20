package pt.bmaia.csv.writing.recordsappender;

import java.util.Iterator;
import pt.bmaia.csv.writing.delimiterappender.DelimiterAppender;
import pt.bmaia.csv.writing.recordappender.RecordAppender;
import pt.procurainterna.text.charsoutput.CharsOutput;

public class IteratorRecordAppenderRecordsAppender<R, D extends Iterator<R>>
    implements RecordsAppender<D> {

  private final RecordAppender<? super R> recordAppender;
  private final DelimiterAppender recordDelimiterAppender;

  public IteratorRecordAppenderRecordsAppender(final RecordAppender<? super R> recordAppender,
      final DelimiterAppender recordDelimiterAppender) {
    this.recordAppender = recordAppender;
    this.recordDelimiterAppender = recordDelimiterAppender;
  }

  @Override
  public void writeTo(final D dataRecords, final CharsOutput charsOutput) {
    while (dataRecords.hasNext()) {
      recordAppender.writeTo(dataRecords.next(), charsOutput);

      if (dataRecords.hasNext()) {
        recordDelimiterAppender.writeTo(charsOutput);
      }
    }
  }

}
