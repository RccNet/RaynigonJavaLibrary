package com.raynigon.lib.compress;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.raynigon.lib.annotations.Future;
import com.raynigon.lib.io.IOUtils;

public class ZipCompressor implements Compressor {

	@Override
	@Future(Version="0.0.6")
	public void compress(File source, File destination) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	@Future(Version="0.0.6")
	public void compress(Collection<File> source, File destination) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void decompress(File source, File destination) throws IOException {
	    InputStream is = new FileInputStream(source);
	    ZipInputStream zis = new ZipInputStream(is);          
        for(ZipEntry ze = zis.getNextEntry();ze != null;ze = zis.getNextEntry()) {
            String filename = ze.getName();
            FileOutputStream fos = new FileOutputStream(new File(destination, filename));
            IOUtils.copy(zis, fos);
            zis.closeEntry();
        }
        zis.close();

	}

}
