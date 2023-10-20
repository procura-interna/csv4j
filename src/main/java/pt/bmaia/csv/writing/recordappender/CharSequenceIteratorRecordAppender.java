package pt.bmaia.csv.writing.recordappender;

import java.util.Iterator;
import pt.bmaia.csv.writing.delimiterappender.DelimiterAppender;
import pt.bmaia.csv.writing.valueappender.ValueAppender;
import pt.procurainterna.text.charsoutput.CharsOutput;

public class CharSequenceIteratorRecordAppender<C extends CharSequence, I extends Iterator<C>> implements RecordAppender<I> {

  private final ValueAppender<? super CharSequence> valueAppender;
  private final DelimiterAppender valueSeparatorDelimiterAppender;

  public CharSequenceIteratorRecordAppender(final ValueAppender<? super CharSequence> valueAppender,
      final DelimiterAppender valueSeparatorDelimiterAppender) {
    this.valueAppender = valueAppender;
    this.valueSeparatorDelimiterAppender = valueSeparatorDelimiterAppender;
  }

  @Override
  public void writeTo(final I record, final CharsOutput appendable) {
    while (record.hasNext()) {
      final C charSequence = record.next();
      valueAppender.append(appendable, charSequence);

      if (record.hasNext()) {
        valueSeparatorDelimiterAppender.writeTo(appendable);
      }
    }
  }

}
