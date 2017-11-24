package gen;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Gen {

	public static List<String> getClassName(String packageName) {  
		String filePath = ClassLoader.getSystemResource("").getPath() + packageName.replace(".", "/");  
		List<String> fileNames = getClassName(filePath, null);  
		return fileNames;  
	}  

	private static List<String> getClassName(String filePath, List<String> className) {  
		List<String> myClassName = new ArrayList<String>();  
		File file = new File(filePath);  
		File[] childFiles = file.listFiles();  
		for (File childFile : childFiles) {  
			if (childFile.isDirectory()) {  
				myClassName.addAll(getClassName(childFile.getPath(), myClassName));  
			} else {  
				String childFilePath = childFile.getPath();  
				childFilePath = childFilePath.substring(childFilePath.indexOf("/bin/")+5, childFilePath.lastIndexOf("."));  
				childFilePath = childFilePath.replace("/", ".");  
				myClassName.add(childFilePath);  
			}  
		}  

		return myClassName;  
	} 

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
			String packageName = "cn.dadaclub.DTO";  
			//生成iOS .h .m
			List<String> classNames = getClassName(packageName);  
			for (String className : classNames) {  
				System.out.println(className);  		            
				DAOGenerator.GenIOS_H(Class.forName(className));
				DAOGenerator.GenIOS_M(Class.forName(className));
			}
			//生成 DAO
			String packageName2 = "cn.dadaclub.domainmodel";     
			List<String> classNames2 = getClassName(packageName2);  
			for (String className : classNames2) {  
				System.out.println(className);  		            
				DAOGenerator.Generate(Class.forName(className));

			}		

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  

	}

}
