package pt.bmaia.csv.writing.recordappender;

import java.util.Iterator;
import pt.bmaia.csv.writing.delimiterappender.DelimiterAppender;
import pt.bmaia.csv.writing.valueappender.ValueAppender;
import pt.procurainterna.text.charsoutput.CharsOutput;

public class CharSequenceIterableRecordAppender<C extends CharSequence, I extends Iterable<C>>
    implements RecordAppender<I> {

  protected final ValueAppender<? super CharSequence> valueAppender;
  protected final DelimiterAppender valueSeparatorDelimiterAppender;

  public CharSequenceIterableRecordAppender(final ValueAppender<? super CharSequence> valueAppender,
      final DelimiterAppender valueSeparatorDelimiterAppender) {
    this.valueAppender = valueAppender;
    this.valueSeparatorDelimiterAppender = valueSeparatorDelimiterAppender;
  }

  @Override
  public void writeTo(final I record, final CharsOutput appendable) {
    final Iterator<C> iterator = record.iterator();

    while (iterator.hasNext()) {
      final C charSequence = iterator.next();
      valueAppender.append(appendable, charSequence);

      if (iterator.hasNext()) {
        valueSeparatorDelimiterAppender.writeTo(appendable);
      }
    }
  }

}
