package com.raynigon.lib.json;

import java.io.IOException;
import java.io.ObjectOutput;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import com.raynigon.lib.annotations.Future;

@Future(Version="0.1.5")
public class JSONOutputStream extends OutputStream implements ObjectOutput{

    private OutputStream os;
    private boolean first;
    private Charset charset;
    
    public JSONOutputStream(OutputStream out){
        os = out;
        first = true;
        charset = StandardCharsets.UTF_8;
    }

    @Override
    public void writeBoolean(boolean v) throws IOException{
        writeString(String.valueOf(v));
    }

    @Override
    public void writeByte(int v) throws IOException{
        writeString(String.valueOf(v));
    }

    @Override
    public void writeShort(int v) throws IOException{
        writeString(String.valueOf(v));
    }

    @Override
    public void writeChar(int v) throws IOException{
        writeString(String.valueOf(v));
    }

    @Override
    public void writeInt(int v) throws IOException{
        writeString(String.valueOf(v));
    }

    @Override
    public void writeLong(long v) throws IOException{
        writeString(String.valueOf(v));
    }

    @Override
    public void writeFloat(float v) throws IOException{
        writeString(String.valueOf(v));
    }

    @Override
    public void writeDouble(double v) throws IOException{
        writeString(String.valueOf(v));
    }

    @Override
    public void writeBytes(String s) throws IOException{
        // TODO Auto-generated method stub
        
    }

    @Override
    public void writeChars(String s) throws IOException{
        // TODO Auto-generated method stub
        
    }

    @Override
    public void writeUTF(String s) throws IOException{
        if(s==null)
            writeString("null");
        else
            writeString("\""+s+"\"");
        return;
    }

    @Override
    public void writeObject(Object obj) throws IOException{
        // TODO Auto-generated method stub
        
    }

    @Override
    public void write(int b) throws IOException{
        writeString(String.valueOf(b%256));
    }

    private void writeString(String v) throws IOException{
        StringBuilder sb = new StringBuilder();
        if(first)
            first=false;
        else
            sb.append(",\n");
        sb.append(v);
        os.write(sb.toString().getBytes(charset));
    }
}
