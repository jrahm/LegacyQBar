package com.modulus.qbar.core.interpreter;

import java.io.IOException;
import java.io.InputStream;

public class CompressedReader implements QBInterpreterReader {

	@Override
	public QBInterpreter readFromStream(InputStream in) throws IOException, ClassNotFoundException {
		return QBInterpreter.readInterpreterCompressed(in);
	}

}
