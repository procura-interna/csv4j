package pt.bmaia.csv.writing.serializing.outputstream;

import pt.procurainterna.io.bytesoutput.BytesOutput;

public interface RecordsToBytesOutput<D> {

  void writeInto(D records, BytesOutput bytesOutput);

}
