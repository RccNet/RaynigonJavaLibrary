package test.raynigon.lib.compress;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.raynigon.lib.compress.ZipCompressor;

public class TestZipCompressor {

	private static final int FILE_AMOUNT = 100;
	private static final int MAX_FILE_SIZE = 0xFFFF;
	private static final int MIN_FILE_SIZE = 0x10;
	
	private long fileSize;
	
	private File folder;

	//@Before
	public void setUp() throws Exception {
		Random r = new Random();
		folder = new File("tmp/ziptest/");
		folder.mkdirs();
		fileSize = 0;
		for(int i=0;i<FILE_AMOUNT;i++){
			byte[] data = new byte[r.nextInt(MAX_FILE_SIZE-MIN_FILE_SIZE)+MIN_FILE_SIZE];
			r.nextBytes(data);
			File file = new File(folder, "ABC"+Math.abs(r.nextLong())+"DEF.tmp");
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(data);
			fos.close();
			fileSize += file.length();
		}
	}

	//@After
	public void tearDown() throws Exception {
		delete(folder);
	}

	//@Test
	public void CompressFolderTest() throws IOException {
		File destination = new File(folder, "data.zip");
		destination.delete();
		ZipCompressor comp = new ZipCompressor();
		comp.compress(folder, destination);
		checkZipFile(destination);
	}
	

	//@Test
	public void CompressCollectionTest() throws IOException {
		File destination = new File(folder, "data.zip");
		destination.delete();
		ZipCompressor comp = new ZipCompressor();
		comp.compress(Arrays.asList(folder.listFiles()), destination);
		checkZipFile(destination);
	}
	
	//@Test
	public void DecompressTest() throws IOException {
		File source = new File(folder, "dataToUnzip.zip");
		File destination = new File(folder, "unzip");
		destination.mkdirs();
		ZipCompressor comp = new ZipCompressor();
		comp.compress(folder, source);
		comp.decompress(source, destination);
		assertTrue(destination.exists());
	}

	private void checkZipFile(File file) {
		assertTrue(file.exists());
		assertTrue(file.length()>fileSize/1000);
		assertTrue(file.length()<fileSize*2);
	}
	
	private void delete(File f) throws IOException {
		if (f.isDirectory()) {
			for (File c : f.listFiles())
				delete(c);
		}
		if (!f.delete()){
			throw new FileNotFoundException("Failed to delete file: " + f);
		}
	}
}
