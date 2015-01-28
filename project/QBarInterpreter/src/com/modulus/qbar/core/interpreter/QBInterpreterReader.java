package com.modulus.qbar.core.interpreter;

import java.io.InputStream;

public interface QBInterpreterReader{
	QBInterpreter readFromStream( InputStream in ) throws Exception;
}
