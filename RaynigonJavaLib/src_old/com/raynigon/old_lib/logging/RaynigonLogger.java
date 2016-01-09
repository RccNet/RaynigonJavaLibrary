package com.raynigon.old_lib.logging;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/** The RaynigonLogger which logs everything into the standard output and error streams
 * @author Simon Schneider
 */
public class RaynigonLogger {

	private FileOutputStream fos = null;
	private SimpleDateFormat sdf = null;
	private RaynigonFrameManager rfm = null;
	
	private int exception_logging_type = 0;//TODO made a new constructor where this attribute can be set
	
	private PrintStream std_out = null;
	private PrintStream err_out = null;
	private boolean printing_console = true;
	
	private ByteArrayOutputStream tmp_buffer_std = null;
	private ByteArrayOutputStream tmp_buffer_err = null;
	private int logging_type = 0;
	
	/**Creates a new RaynigonLogger
	 */
	public RaynigonLogger(){
		sdf = new SimpleDateFormat("dd.MM.yyy-HH:mm:ss");
		std_out = System.out;
		err_out = System.err;
		tmp_buffer_std = new ByteArrayOutputStream();
		tmp_buffer_err = new ByteArrayOutputStream();
	}
	
	/**Creates a new RaynigonLogger
	 * @param f							a Logging file in which the logging is done
	 * @throws FileNotFoundException	is thrown if the File wasn't found
	 */
	public RaynigonLogger(File f) throws FileNotFoundException{
		this();
		fos = new FileOutputStream(f);
	}
	
	/**Creates a new RaynigonLogger
	 * @param trfm	A RaynigonFrameManager which manages a Normal or a Fatal Exception
	 */
	public RaynigonLogger(RaynigonFrameManager trfm){
		this();
		rfm = trfm;
	}
	
	/**Creates a new RaynigonLogger.java
	 * @param f							a Logging file in which the logging is done
	 * @param trfm						A RaynigonFrameManager which manages a Normal or a Fatal Exception
	 * @throws FileNotFoundException	is thrown if the File wasn't found
	 */
	public RaynigonLogger(File f,RaynigonFrameManager trfm) throws FileNotFoundException{
		this(f);
		rfm = trfm;
	}
	
	
	private void toLog(RaynigonLoggingType rlt, String text){
		String logtext = "["+sdf.format(new Date())+"] "+rlt.getText()+" ";
		logtext = logtext + text+"\n";
		if(fos!=null){
			try {
				fos.write(logtext.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(rlt==RaynigonLoggingType.Error){
			err_out.print(logtext);
		}else{
			std_out.print(logtext);
		}
	}
	

	private void printException(Exception e){
		switch(exception_logging_type){
		case 0:
			logDebugException(e);
		break;
		case 1:
			logOnlyMessage(e);
		break;
		case 2:
			logOnlyCode(e);
		break;
		}
	}
	
	private void logDebugException(Exception e){
		if(e.getMessage()!=null){
			toLog(RaynigonLoggingType.Error, e.getClass().getName()+":"+e.getMessage());
		}else{
			toLog(RaynigonLoggingType.Error, e.getClass().getName());
		}
		printStackTrace(e);
		Throwable cause = e.getCause();
		while(cause!=null){
			if(cause.getMessage()!=null){
				toLog(RaynigonLoggingType.Error, "Caused by:"+cause.getClass().getName()+":"+cause.getMessage());
			}else{
				toLog(RaynigonLoggingType.Error, "Caused by:"+cause.getClass().getName());
			}
			printStackTrace(cause);
			cause = cause.getCause();
		}
	}
	
	private void printStackTrace(Throwable cause) {
		for(StackTraceElement st : cause.getStackTrace()){
			String ltext = st.getFileName()+":"+String.valueOf(st.getLineNumber());
			if(st.getLineNumber()<0){
				ltext = "Unknown Source";
			}
			toLog(RaynigonLoggingType.Error, "\tat "+st.getClassName()+"."+st.getMethodName()+"("+ltext+")");
		}
	}

	
	private void logOnlyMessage(Exception e){
		toLog(RaynigonLoggingType.Error, e.getClass().getName()+":"+e.getMessage());
	}
	
	private void logOnlyCode(Exception e){
		toLog(RaynigonLoggingType.Error, "Exception:"+e.getMessage());
	}
	
	/**Logs the Exception and Pauses all frames
	 * @param id	The Error Id
	 * @param e		The Exception which should be Logged
	 */
	public void throwNormalException(int id, Exception e){
		printException(e);
		if(rfm!=null){
			rfm.pauseAll(id,e.getMessage());
		}
	}
	
	/**Logs the Exception and Shutdown the whole Process
	 * @param id	The Error Id
	 * @param e		The Exception which should be Logged
	 */
	public void throwFatalException(int id, Exception e){
		startPrintingToConsole();
		printException(e);
		printErrorText("FatalException was thrown and catched, please contact the System Administrator");
		if(rfm!=null){
			rfm.stopAll(id,e.getMessage());
		}else{
			System.exit(id);
		}
	}
	
	
	/**Logs the Exception and Pauses all frames
	 * @param e		The Exception which should be Logged
	 */
	public void throwNormalException(Exception e){
		throwNormalException(-1,e);
	}
	
	/**Logs the Exception and Shutdown the whole Process
	 * @param e		The Exception which should be Logged
	 */
	public void throwFatalException(Exception e){
		throwFatalException(-1,e);
	}
	
	/**Returns the standard PrintStream
	 * @return	the standard PrintStream
	 */
	public PrintStream getStandardPrintStream(){
		return std_out;
	}
	
	/**Returns the Error PrintStream
	 * @return	the Error PrintStream
	 */
	public PrintStream getErrorPrintStream(){
		return err_out;
	}

	//TODO make this Method Protected and Access only from an LoggerMaster
	public void stopPrintingToConsole() {
		if(printing_console){
			std_out = new PrintStream(tmp_buffer_std);
			err_out = new PrintStream(tmp_buffer_err);
			printing_console = false;
		}
	}

	//TODO make this Method Protected and Access only from an LoggerMaster
	public void startPrintingToConsole() {
		if(!printing_console){
			std_out = System.out;
			err_out = System.err;
			String tmp_std = tmp_buffer_std.toString();
			String tmp_err = tmp_buffer_err.toString();
			tmp_buffer_std.reset();
			tmp_buffer_err.reset();
			std_out.print(tmp_std);
			err_out.print(tmp_err);
			printing_console = true;
		}
	}
	
	public void printDebugText(String string) {
		if(isDebugMode()){
			toLog(RaynigonLoggingType.Debug, string);
		}
	}
	
	public void printInfoText(String string) {
		toLog(RaynigonLoggingType.Info, string);
	}
	
	public void printStandartText(String string) {
		toLog(RaynigonLoggingType.Standard, string);
	}

	public void printWarningText(String string) {
		toLog(RaynigonLoggingType.Warning, string);
	}
	
	public void printErrorText(String string) {
		toLog(RaynigonLoggingType.Error, string);
	}

	//TODO make this Method Protected and Access only from an LoggerMaster
	public void saveAndClose() {
		if(fos!=null){
			try{
				fos.flush();
				fos.close();
			}catch(IOException e){
				e.printStackTrace(err_out);
			}
		}
	}
	
	public boolean isDebugMode() {
		return logging_type <=0;
	}
}
