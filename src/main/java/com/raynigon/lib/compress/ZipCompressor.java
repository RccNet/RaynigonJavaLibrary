package com.raynigon.lib.compress;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.raynigon.lib.annotations.Future;
import com.raynigon.lib.io.IOUtils;

public final class ZipCompressor implements Compressor {

	@Override
	@Future(Version="0.0.6")
	public void compress(File source, File destination) throws IOException {
		List<Pair> pairs = new ArrayList<>();
		addRecursive(pairs, source.listFiles(), source);
		compressInt(pairs, destination);
	}

	private void addRecursive(List<Pair> pairs, File[] files, File base) {
		for(File file : files){
			if(file.isDirectory()){
				addRecursive(pairs, file.listFiles(), base);
				continue;
			}
			String name = generateRelativePath(base, file);
			Pair pair = new Pair(name, file);
			pairs.add(pair);
		}
	}

	@Override
	@Future(Version="0.0.6")
	public void compress(Collection<File> source, File destination) throws IOException {
		List<Pair> pairs = new ArrayList<>(source.size());
		for(File file : source)
			pairs.add(new Pair(file.getName(), file));
		compressInt(pairs, destination);
	}
	
	@Override
	public void decompress(File source, File destination) throws IOException {
	    InputStream is = new FileInputStream(source);
	    ZipInputStream zis = new ZipInputStream(is);          
        for(ZipEntry ze = zis.getNextEntry();ze != null;ze = zis.getNextEntry()) {
            String filename = ze.getName();
            File destFile = new File(destination, filename+File.separator);
            if(!ze.isDirectory()){
            	FileOutputStream fos = new FileOutputStream(destFile);
                IOUtils.copy(zis, fos);
                fos.close();	
            }else{
            	destFile.mkdirs();            
            }
            zis.closeEntry();
        }
        zis.close();

	}

	private void compressInt(Collection<Pair> sources, File destination) throws IOException{
		FileOutputStream fos = new FileOutputStream(destination);
		ZipOutputStream zos = new ZipOutputStream(fos);
		for(Pair source : sources)
			writeEntryToZip(zos, source.name, source.file);
	    zos.close();
	    fos.close();
	}
	
	private void writeEntryToZip(ZipOutputStream zos, String name, File file) throws IOException{
		FileInputStream fis = new FileInputStream(file);
	    ZipEntry entry = new ZipEntry(name);
	    zos.putNextEntry(entry);
	    IOUtils.copy(fis, zos);
	    zos.closeEntry();
	    fis.close();
	}
	
	private String generateRelativePath(File base, File absolute){
		return base.toURI().relativize(absolute.toURI()).getPath();
	}
	
	private class Pair{
		public String name;
		public File file;
		
		public Pair(String inName, File inFile) {
			name = inName;
			file = inFile;
		}
	}
}
