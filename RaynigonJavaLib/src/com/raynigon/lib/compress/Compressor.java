package com.raynigon.lib.compress;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

/** Generated on 01.03.2016
 * @author Simon Schneider
 *
 */
public interface Compressor {

	void compress(File source, File destination) throws IOException;
	void compress(Collection<File> source, File destination) throws IOException;
	void decompress(File source, File destination) throws IOException;
}
