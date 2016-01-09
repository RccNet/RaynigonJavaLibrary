package com.raynigon.old_lib.files.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import com.raynigon.old_lib.files.helpers.Line;
import com.raynigon.old_lib.files.helpers.RayHeader;
import com.raynigon.old_lib.files.helpers.Line.LineType;

/**
 * @author Simon Schneider
 *
 */
public class RaynigonKeyValueFile extends RaynigonFile {

	List<Line> lines = null;
	
	public RaynigonKeyValueFile(InputStream tis) {
		super(tis, null, null);
		lines = new LinkedList<Line>();
	}
	
	public RaynigonKeyValueFile(InputStream tis, OutputStream tos) {
		super(tis,tos,null);
		lines = new LinkedList<Line>();
	}
	
	public RaynigonKeyValueFile(File f) throws FileNotFoundException {
		super(f);
		lines = new LinkedList<Line>();
	}
	
	public RaynigonKeyValueFile(InputStream tis, byte[] key) {
		super(tis, null, key);
		lines = new LinkedList<Line>();
	}
	
	public RaynigonKeyValueFile(InputStream tis, OutputStream tos, byte[] tkey) {
		super(tis, tos, tkey);
		lines = new LinkedList<Line>();
	}
	
	public RaynigonKeyValueFile(File f, byte[] key) throws FileNotFoundException {
		super(f, key);
		lines = new LinkedList<Line>();
	}
	
	public RaynigonKeyValueFile(RayHeader header, File f) throws FileNotFoundException {
		super(header, f);
		lines = new LinkedList<Line>();
	}

	public RaynigonKeyValueFile(RayHeader header, File f, byte[] key) throws FileNotFoundException {
		super(header, f, key);
		lines = new LinkedList<Line>();
	}

	public RaynigonKeyValueFile(RayHeader header, File f, FileInputStream fis) throws FileNotFoundException {
		super(header, f, fis);
		lines = new LinkedList<Line>();
	}

	public RaynigonKeyValueFile(RayHeader header, File f, FileInputStream fis,
			byte[] key) throws FileNotFoundException {
		super(header, f, fis, key);
		lines = new LinkedList<Line>();
	}

	@Override
	protected void parseData(InputStream is) throws Exception {
		Scanner sc = new Scanner(is);
		String line = null;
		boolean comment_section = false;
		while(sc.hasNextLine()){
			line = sc.nextLine();
			if(line.startsWith("//")){
				addLine(new Line(line, LineType.Comment));
			}else if(line.startsWith("/*") && line.endsWith("*/")){
				addLine(new Line(line, LineType.Comment));
			}else if(line.startsWith("/*")){
				addLine(new Line(line, LineType.Comment));
				comment_section = true;
			}else if(comment_section && line.endsWith("*/")){
				addLine(new Line(line, LineType.Comment));
				comment_section = false;
			}else if(comment_section){
				addLine(new Line(line, LineType.Comment));
			}else if(line.contains("=")){
				String[] pair = line.split("=");
				addLine(new Line(pair[0], pair[1], LineType.KeyValuePair));
			}
		}
		sc.close();
	}

	@Override
	protected void writeData(OutputStream os) throws Exception {
		for(Line l : lines){
			os.write(l.toString().getBytes());
		}
	}
	
	public void addLine(Line line){
		boolean add = true;
		for(int i=0;i<lines.size();i++){
			if(lines.get(i).getKey()!=null && line.getKey()!=null){
				if(lines.get(i).getKey().equals(line.getKey())){
					add = false;
					i = lines.size();
				}
			}
		}
		if(add){
			lines.add(line);
		}
	}
	
	public void removeAllLines(){
		lines.clear();
	}
	
	@Override
	public String toString() {
		String out = "";
		for(Line line : lines){
			out = out+line.toString();
		}
		return out;
	}

}
