package com.modulus.qbar.core.interpreter;

import java.io.IOException;
import java.io.InputStream;

public class SourceReader implements QBInterpreterReader {

	@Override
	public QBInterpreter readFromStream(InputStream in) throws IOException {
		return QBInterpreter.readInterpreterFromSource(in);
	}

}
