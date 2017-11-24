package gen;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class IOUtil {

	public static void writeDaoFileToDesk(byte[] data,String fileName)throws Exception{
		String path=	System.getProperty("user.dir")+"/dataaccess";
		File file = new File(path,fileName + ".java");
		if(!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
		System.out.println("file.getAbsolutePath()---> " + file.getAbsolutePath());
		OutputStream os = new FileOutputStream(file);
		os.write(data,0,data.length);
		os.close();
	}
	public static void writeDaoGenFileToDesk(byte[] data,String fileName)throws Exception{
		String path=	System.getProperty("user.dir")+"/dataaccess_gen";
		File file = new File(path,fileName + ".java");
		if(!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
		System.out.println("file.getAbsolutePath()---> " + file.getAbsolutePath());
		OutputStream os = new FileOutputStream(file);
		os.write(data,0,data.length);
		os.close();
	}

	public static void writeIOSHFileToDesk(byte[] data,String fileName)throws Exception{		
		String path=	System.getProperty("user.dir")+"/ios";
		File file = new File(path,fileName + ".h");
		if(!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
		System.out.println("file.getAbsolutePath()---> " + file.getAbsolutePath());
		OutputStream os = new FileOutputStream(file);
		os.write(data,0,data.length);
		os.close();
	}
	public static void writeIOSMFileToDesk(byte[] data,String fileName)throws Exception{		
		String path=	System.getProperty("user.dir")+"/ios";
		File file = new File(path,fileName + ".m");
		if(!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
		System.out.println("file.getAbsolutePath()---> " + file.getAbsolutePath());
		OutputStream os = new FileOutputStream(file);
		os.write(data,0,data.length);
		os.close();
	}
}

