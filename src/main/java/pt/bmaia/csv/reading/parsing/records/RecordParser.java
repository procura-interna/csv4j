package pt.bmaia.csv.reading.parsing.records;

import java.util.function.Consumer;
import pt.procurainterna.text.charsinput.CharsInput;

public interface RecordParser<R> {

  void parse(final CharsInput charsInput, final Consumer<? super R> recordsConsumer);

}
