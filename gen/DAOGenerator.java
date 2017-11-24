package gen;

import java.lang.reflect.Field;

import cn.dadaclub.reflect.IgnoreField;

public class DAOGenerator {
	/********************** D T O **********************/
	@SuppressWarnings("rawtypes")
	public static void GenIOS_H(Class clazz) throws Exception{
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("#import <Foundation/Foundation.h>");
		sBuilder.append("\n\n");
		sBuilder.append("@interface " + clazz.getSimpleName() + ": NSObject");
		sBuilder.append("\n");
		Field[]fs=  clazz.getDeclaredFields();
		//@property (nonatomic, strong)NSString *phoneNo;
		for(Field f:fs){
			String name = f.getName();
			String type =f.getType().getSimpleName();
			if(type.equals("String"))
			{
				sBuilder.append("@property (nonatomic, strong)NSString *" +name + ";");
			}
			else if(type.equals("int"))
			{
				sBuilder.append("@property (nonatomic, assign)NSInteger " +name + ";");
			}
			else if(type.equals("float"))
			{
				sBuilder.append("@property (nonatomic, assign)CGFloat " +name + ";");
			}	
			else if(type.equals("boolean"))
			{
				sBuilder.append("@property (nonatomic, assign)NSInteger " +name + ";");
			}			
			else if(type.equals("Timestamp"))
			{
				sBuilder.append("@property (nonatomic, assign)NSInteger " +name + ";");
			}			
			else{
				sBuilder.append("@property (nonatomic, strong)"+type+" *" +name + ";");
			}
			sBuilder.append("\n");
		}		
		sBuilder.append("@end\n");		
		IOUtil.writeIOSHFileToDesk(sBuilder.toString().getBytes(),clazz.getSimpleName());
	}
	@SuppressWarnings("rawtypes")
	public static void GenIOS_M(Class clazz) throws Exception{
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("#import \""+clazz.getSimpleName()+".h\"");
		sBuilder.append("\n\n");
		sBuilder.append("@implementation " + clazz.getSimpleName());
		sBuilder.append("\n\n");			
		sBuilder.append("@end\n");
		IOUtil.writeIOSMFileToDesk(sBuilder.toString().getBytes(),clazz.getSimpleName());
	}
	/********************** D A O **********************/
	@SuppressWarnings("rawtypes")
	public static void Generate(Class clazz) throws Exception{
		StringBuilder sBuilder = new StringBuilder();
		Field[]fs=  clazz.getDeclaredFields();
		boolean hasTimestamp=false;
		for(Field f:fs){	 	 
			hasTimestamp =f.getType().getSimpleName().equals("Timestamp");	
			 if(hasTimestamp)break;
		}
		
		sBuilder.append("package cn.dadaclub.dataaccess.gen;");
		sBuilder.append("\n\n");
		sBuilder.append("import " + clazz.getPackage().getName() + "."+ clazz.getSimpleName() + ";");
		sBuilder.append("\n");
		if(hasTimestamp)
		sBuilder.append("import java.sql.Timestamp;\n");
		sBuilder.append("import java.util.List;\n");
		sBuilder.append("import org.springframework.jdbc.core.BeanPropertyRowMapper;\n");
		sBuilder.append("import org.springframework.jdbc.core.JdbcTemplate;\n\n");
		sBuilder.append("public class " + clazz.getSimpleName() + "BaseDao");
		sBuilder.append("\n");
		sBuilder.append("{");
		sBuilder.append("\n");
		sBuilder.append("	/********************** 模 板 对 象 **********************/\n");
		sBuilder.append("	protected JdbcTemplate jdbcTemplate;\n");		 	
		sBuilder.append("	public void setJdbcTemplate(JdbcTemplate jdbcTemplate)\n");
		sBuilder.append("	{\n");
		sBuilder.append("		this.jdbcTemplate = jdbcTemplate;\n");
		sBuilder.append("	}");
		sBuilder.append("\n\n");
		sBuilder.append("	/********************** 增 加 修 改 **********************/\n");
		GenerateInsert(clazz, sBuilder);
		GenerateUpdate(clazz, sBuilder);
		sBuilder.append("	/********************** 对 象 删 除 **********************/\n");
		GenerateDelete(clazz, sBuilder);
		for(Field f:fs){ 
			String name = f.getName(); // 获取属性的名字
			String type =f.getType().getSimpleName();
			 	if(type.equals("List"))continue;
			GenerateDeleteBy(name,type,clazz, sBuilder);
		}
		sBuilder.append("	/********************** 对 象 查 询 **********************/\n");
		for(Field f:fs){ 
			String name = f.getName(); // 获取属性的名字
			String type =f.getType().getSimpleName();
		 	if(type.equals("List"))continue;
			GenerateModelBy(name,type,clazz, sBuilder);
		}
		
		sBuilder.append("	/********************** 查      询 **********************/\n");
		GenerateQueryList(clazz, sBuilder);
		for(Field f:fs){		 
			String name = f.getName(); // 获取属性的名字
			String type =f.getType().getSimpleName(); 	
			if(type.equals("List"))continue;
			GenerateQueryListBy(name,type,clazz, sBuilder);
		}
		
		sBuilder.append("	/********************** 条       数 **********************/\n"); 
		GenerateQueryCount(clazz, sBuilder);
		for(Field f:fs){		 
			String name = f.getName(); // 获取属性的名字
			String type =f.getType().getSimpleName(); 
			
			if(type.equals("List"))continue;
			GenerateQueryCountBy(name,type,clazz, sBuilder);
		}
		
		sBuilder.append("	/********************** 分 页 查 询 **********************/\n");
		GenerateQueryListPage(clazz, sBuilder);
		for(Field f:fs){	 
			String name = f.getName(); // 获取属性的名字
			String type =f.getType().getSimpleName();	
			if(type.equals("List"))continue;
			GenerateQueryListPageBy(name,type,clazz, sBuilder);
		}
		
		sBuilder.append("}");
		sBuilder.append("\n\n");

		IOUtil.writeDaoGenFileToDesk(sBuilder.toString().getBytes(),clazz.getSimpleName()+"BaseDao");
		StringBuilder daoBuilder = new StringBuilder();
		daoBuilder.append("package cn.dadaclub.dataaccess;");
		daoBuilder.append("\n\n");
		daoBuilder.append("import " + clazz.getPackage().getName() + "."+ clazz.getSimpleName() + ";");
		daoBuilder.append("\n");
		daoBuilder.append("public class " + clazz.getSimpleName() + "Dao extends " + clazz.getSimpleName() + "BaseDao");
		daoBuilder.append("\n");
		daoBuilder.append("{");
		daoBuilder.append("\n");
		daoBuilder.append("\n");
		daoBuilder.append("}");
		daoBuilder.append("\n");
		IOUtil.writeDaoFileToDesk(daoBuilder.toString().getBytes(),clazz.getSimpleName()+"Dao");
		System.out.println("=====================");
		System.out.println(sBuilder.toString());
	}

	//插入方法
	
	@SuppressWarnings("rawtypes")
	protected static void GenerateInsert(Class clazz, StringBuilder sBuilder) {
		sBuilder.append("	public int Insert" + clazz.getSimpleName() + "("+clazz.getSimpleName()+ " " + clazz.getSimpleName().toLowerCase() +"){");
		sBuilder.append("\n");
		Field[]fs=  clazz.getDeclaredFields();
		String params="";
		String values="";
		String objectfields="";
		String object= clazz.getSimpleName().toLowerCase();
		for(Field f:fs){
			if(f.getType().getSimpleName().equals("List"))continue;
			IgnoreField ignoreField = f.getAnnotation(IgnoreField.class);
			if(ignoreField!=null)continue;
			
			params= params+f.getName()+",";
			values=values+"?"+",";
			String name = f.getName(); // 获取属性的名字
			name = name.substring(0, 1).toUpperCase() + name.substring(1);			
			objectfields=objectfields+object+".get"+name+"(),";
		}
		try{ 
		params=params.substring(0, params.length()-1);
		values=values.substring(0, values.length()-1);
		objectfields=objectfields.substring(0, objectfields.length()-1);
		}
		catch(Exception ex)
		{
			System.out.println( clazz.getSimpleName() +"异常");
			ex.printStackTrace();
		}
		String line1="		String SQL = \"insert into "+clazz.getSimpleName().toLowerCase()+" ("+params+") values ("+values+")\";\n";
		sBuilder.append(line1);
		String line2="		return jdbcTemplate.update(SQL, "+objectfields+");\n";
		sBuilder.append(line2);				
		sBuilder.append("	}");
		sBuilder.append("\n\n");
	}

	@SuppressWarnings("rawtypes")
	protected static void GenerateDelete(Class clazz, StringBuilder sBuilder) {
		sBuilder.append("	public int Delete" + clazz.getSimpleName() + "("+clazz.getSimpleName()+ " " + clazz.getSimpleName().toLowerCase() +"){");
		sBuilder.append("\n");
		sBuilder.append("		String sql = \"delete from "+clazz.getSimpleName().toLowerCase()+" where Id =?\";\n");
		sBuilder.append("		return jdbcTemplate.update(sql, new Object[]{"+clazz.getSimpleName().toLowerCase()+".getId()});\n");
		sBuilder.append("	}");
		sBuilder.append("\n\n");
	}

	@SuppressWarnings("rawtypes")
	protected static void GenerateDeleteBy(String fname,String type,Class clazz, StringBuilder sBuilder) {
		String upperfname = fname.substring(0, 1).toUpperCase() + fname.substring(1);
		sBuilder.append("	public int Delete"+ clazz.getSimpleName()+"By"+upperfname + "("+type+" "+fname+"){");
		sBuilder.append("\n");
		sBuilder.append("		String sql = \"delete from "+clazz.getSimpleName().toLowerCase()+" where "+upperfname + " =?\";\n");
		sBuilder.append("		return jdbcTemplate.update(sql, new Object[]{"+fname+"});\n");
		sBuilder.append("	}");
		sBuilder.append("\n\n");
	}
	//更新
	@SuppressWarnings("rawtypes")
	protected static void GenerateUpdate(Class clazz, StringBuilder sBuilder) {
		sBuilder.append("	public int Update" + clazz.getSimpleName() + "("+clazz.getSimpleName()+ " " + clazz.getSimpleName().toLowerCase() +"){");
		sBuilder.append("\n");
		Field[]fs=  clazz.getDeclaredFields();
		String params="";
		String values="";
		String objectfields="";
		String object= clazz.getSimpleName().toLowerCase();
		for(Field f:fs){
			if(f.getType().getSimpleName().equals("List"))continue;
			IgnoreField ignoreField = f.getAnnotation(IgnoreField.class);
			if(ignoreField!=null)continue;
			params= params+f.getName()+" = ?,";
			values=values+"?"+",";
			String name = f.getName(); // 获取属性的名字
			name = name.substring(0, 1).toUpperCase() + name.substring(1);			
			objectfields=objectfields+object+".get"+name+"(),";
		}
		params=params.substring(0, params.length()-1);
		values=values.substring(0, values.length()-1);
		objectfields=objectfields.substring(0, objectfields.length()-1);
		String line1="		String SQL = \"update "+clazz.getSimpleName()+" set "+params+" where id = ?\";\n";
		sBuilder.append(line1);
		String line2="		return jdbcTemplate.update(SQL, "+objectfields+", " + clazz.getSimpleName().toLowerCase() +".getId());\n";
		sBuilder.append(line2);	     	   	      
		sBuilder.append("	}");
		sBuilder.append("\n\n");
	}


	@SuppressWarnings("rawtypes")
	protected static void GenerateModel(Class clazz, StringBuilder sBuilder) {
		sBuilder.append("	@SuppressWarnings({ \"rawtypes\", \"unchecked\" })\n");
		sBuilder.append("	public "+ clazz.getSimpleName() +" Query" + clazz.getSimpleName() + "ById(String id){");
		sBuilder.append("\n");
		Field[]fs=  clazz.getDeclaredFields();
		String params="";
		String values="";
		String objectfields="";
		String object= clazz.getSimpleName().toLowerCase();
		for(Field f:fs){
			if(f.getType().getSimpleName().equals("List"))continue;
			IgnoreField ignoreField = f.getAnnotation(IgnoreField.class);
			if(ignoreField!=null)continue;
			params= params+f.getName()+",";
			values=values+"?"+",";
			String name = f.getName(); // 获取属性的名字
			name = name.substring(0, 1).toUpperCase() + name.substring(1);		
			objectfields=objectfields+object+".get"+name+"(),";
		}
		params=params.substring(0, params.length()-1);
		values=values.substring(0, values.length()-1);
		objectfields=objectfields.substring(0, objectfields.length()-1);

		sBuilder.append("		String sql = \"SELECT  "+params+" FROM "+clazz.getSimpleName()+" where Id=?\";\n");
		sBuilder.append("		List<"+clazz.getSimpleName()+"> l = jdbcTemplate.query(sql,new Object[] { id },new BeanPropertyRowMapper("+clazz.getSimpleName()+".class));\n");

		//return l.isEmpty() ? null : l.get(0);
		sBuilder.append("		return l.isEmpty() ? null : l.get(0);\n");
		sBuilder.append("	}");
		sBuilder.append("\n\n");
	}
	@SuppressWarnings("rawtypes")
	protected static void GenerateModelBy(String fname,String type,Class clazz, StringBuilder sBuilder) {
		String upperfname = fname.substring(0, 1).toUpperCase() + fname.substring(1);
		sBuilder.append("	@SuppressWarnings({ \"rawtypes\", \"unchecked\" })\n");
		sBuilder.append("	public "+ clazz.getSimpleName() +" Query" + clazz.getSimpleName() + "By"+upperfname+"("+type+" "+fname+"){");
		sBuilder.append("\n");
		Field[]fs=  clazz.getDeclaredFields();
		String params="";
		String values="";
		String objectfields="";
		String object= clazz.getSimpleName().toLowerCase();
		for(Field f:fs){
			if(f.getType().getSimpleName().equals("List"))continue;
			IgnoreField ignoreField = f.getAnnotation(IgnoreField.class);
			if(ignoreField!=null)continue;
			params= params+f.getName()+",";
			values=values+"?"+",";
			String name = f.getName(); // 获取属性的名字
			name = name.substring(0, 1).toUpperCase() + name.substring(1);		
			objectfields=objectfields+object+".get"+name+"(),";
		}
		params=params.substring(0, params.length()-1);
		values=values.substring(0, values.length()-1);
		objectfields=objectfields.substring(0, objectfields.length()-1);

		sBuilder.append("		String sql = \"SELECT  "+params+" FROM "+clazz.getSimpleName()+" where "+upperfname+"=?\";\n");
		sBuilder.append("		List<"+clazz.getSimpleName()+"> l = jdbcTemplate.query(sql,new Object[] { "+fname+" },new BeanPropertyRowMapper("+clazz.getSimpleName()+".class));\n");

		//return l.isEmpty() ? null : l.get(0);
		sBuilder.append("		return l.isEmpty() ? null : l.get(0);\n");
		sBuilder.append("	}");
		sBuilder.append("\n\n");
	}
	@SuppressWarnings("rawtypes")
	protected static void GenerateQueryList(Class clazz, StringBuilder sBuilder) {
		sBuilder.append("	@SuppressWarnings({ \"rawtypes\", \"unchecked\" })\n");
		sBuilder.append("	public List<"+ clazz.getSimpleName() +"> Query" + clazz.getSimpleName() + "List(){");
		sBuilder.append("\n");
		Field[]fs=  clazz.getDeclaredFields();
		String params="";
		String values="";
		String objectfields="";
		String object= clazz.getSimpleName().toLowerCase();
		for(Field f:fs){
			if(f.getType().getSimpleName().equals("List"))continue;
			IgnoreField ignoreField = f.getAnnotation(IgnoreField.class);
			if(ignoreField!=null)continue;
			params= params+f.getName()+",";
			values=values+"?"+",";
			String name = f.getName(); // 获取属性的名字
			name = name.substring(0, 1).toUpperCase() + name.substring(1);	
			objectfields=objectfields+object+".get"+name+"(),";
		}
		params=params.substring(0, params.length()-1);
		values=values.substring(0, values.length()-1);
		objectfields=objectfields.substring(0, objectfields.length()-1);

		sBuilder.append("		String sql = \"SELECT  "+params+" FROM "+clazz.getSimpleName()+" ORDER BY lastupdate DESC\";\n");
		sBuilder.append("		List<"+clazz.getSimpleName()+"> l = jdbcTemplate.query(sql, new BeanPropertyRowMapper("+clazz.getSimpleName()+".class));\n");
		sBuilder.append("		return l;\n");		
		sBuilder.append("	}");
		sBuilder.append("\n\n");
	}
	@SuppressWarnings("rawtypes")
	protected static void GenerateQueryListBy(String fname,String type,Class clazz, StringBuilder sBuilder) {
		String upperfname = fname.substring(0, 1).toUpperCase() + fname.substring(1);
		sBuilder.append("	@SuppressWarnings({ \"rawtypes\", \"unchecked\" })\n");
		sBuilder.append("	public List<"+ clazz.getSimpleName() +"> Query" + clazz.getSimpleName() + "ListBy"+upperfname+"("+type+" "+fname+"){");
		sBuilder.append("\n");
		 
		Field[]fs=  clazz.getDeclaredFields();
		String params="";
		String values="";
		String objectfields="";
		String object= clazz.getSimpleName().toLowerCase();
		for(Field f:fs){
			if(f.getType().getSimpleName().equals("List"))continue;
			IgnoreField ignoreField = f.getAnnotation(IgnoreField.class);
			if(ignoreField!=null)continue;
			params= params+f.getName()+",";
			values=values+"?"+",";
			String name = f.getName(); // 获取属性的名字
			name = name.substring(0, 1).toUpperCase() + name.substring(1);	
			objectfields=objectfields+object+".get"+name+"(),";
		}
		params=params.substring(0, params.length()-1);
		values=values.substring(0, values.length()-1);
		objectfields=objectfields.substring(0, objectfields.length()-1);

		sBuilder.append("		String sql = \"SELECT  "+params+" FROM "+clazz.getSimpleName()+" where "+upperfname+"=? ORDER BY lastupdate DESC \";\n");
		sBuilder.append("		List<"+clazz.getSimpleName()+"> l = jdbcTemplate.query(sql,new Object[] {"+fname+"},new BeanPropertyRowMapper("+clazz.getSimpleName()+".class));\n");
		sBuilder.append("		return l;\n");		
		sBuilder.append("	}");
		sBuilder.append("\n\n");
	}
	@SuppressWarnings("rawtypes")
	protected static void GenerateQueryListPage(Class clazz, StringBuilder sBuilder) {
//		int pageindex,int pagesize){
//			int start=pageindex*pagesize;
//			int end=pageindex*pagesize+pagesize;
		sBuilder.append("	@SuppressWarnings({ \"rawtypes\", \"unchecked\" })\n");
		sBuilder.append("	public List<"+ clazz.getSimpleName() +"> Query" + clazz.getSimpleName() + "ListPage(int pageindex,int pagesize){");
		sBuilder.append("\n");
		sBuilder.append("int start=pageindex*pagesize;");
		sBuilder.append("\n");
		sBuilder.append("int end=pageindex*pagesize+pagesize;");
		sBuilder.append("\n");
		Field[]fs=  clazz.getDeclaredFields();
		String params="";
		String values="";
		String objectfields="";
		String object= clazz.getSimpleName().toLowerCase();
		for(Field f:fs){
			if(f.getType().getSimpleName().equals("List"))continue;
			IgnoreField ignoreField = f.getAnnotation(IgnoreField.class);
			if(ignoreField!=null)continue;
			params= params+f.getName()+",";
			values=values+"?"+",";
			String name = f.getName(); // 获取属性的名字
			name = name.substring(0, 1).toUpperCase() + name.substring(1);	
			objectfields=objectfields+object+".get"+name+"(),";
		}
		params=params.substring(0, params.length()-1);
		values=values.substring(0, values.length()-1);
		objectfields=objectfields.substring(0, objectfields.length()-1);

		sBuilder.append("		String sql = \"SELECT  "+params+" FROM "+clazz.getSimpleName()+" ORDER BY lastupdate DESC limit ?,?\";\n");
		sBuilder.append("		List<"+clazz.getSimpleName()+"> l = jdbcTemplate.query(sql,new Object[] {start,end },new BeanPropertyRowMapper("+clazz.getSimpleName()+".class));\n");
		sBuilder.append("		return l;\n");		
		sBuilder.append("	}");
		sBuilder.append("\n\n");
	}
	@SuppressWarnings("rawtypes")
	protected static void GenerateQueryListPageBy(String fname,String type,Class clazz, StringBuilder sBuilder) {
//		int pageindex,int pagesize){
//			int start=pageindex*pagesize;
//			int end=pageindex*pagesize+pagesize;
		String upperfname = fname.substring(0, 1).toUpperCase() + fname.substring(1);
		sBuilder.append("	@SuppressWarnings({ \"rawtypes\", \"unchecked\" })\n");
		sBuilder.append("	public List<"+ clazz.getSimpleName() +"> Query" + clazz.getSimpleName() + "ListPageBy"+upperfname+"("+type+" "+fname+",int pageindex,int pagesize){");
		sBuilder.append("\n");
		sBuilder.append("		int start=pageindex*pagesize;");
		sBuilder.append("\n");
		sBuilder.append("		int end=pageindex*pagesize+pagesize;");
		sBuilder.append("\n");
		Field[]fs=  clazz.getDeclaredFields();
		String params="";
		String values="";
		String objectfields="";
		String object= clazz.getSimpleName().toLowerCase();
		for(Field f:fs){
			if(f.getType().getSimpleName().equals("List"))continue;
			IgnoreField ignoreField = f.getAnnotation(IgnoreField.class);
			if(ignoreField!=null)continue;
			params= params+f.getName()+",";
			values=values+"?"+",";
			String name = f.getName(); // 获取属性的名字
			name = name.substring(0, 1).toUpperCase() + name.substring(1);	
			objectfields=objectfields+object+".get"+name+"(),";
		}
		params=params.substring(0, params.length()-1);
		values=values.substring(0, values.length()-1);
		objectfields=objectfields.substring(0, objectfields.length()-1);

		sBuilder.append("		String sql = \"SELECT  "+params+" FROM "+clazz.getSimpleName()+" where "+upperfname+"=? ORDER BY lastupdate DESC limit ?,?\";\n");
		sBuilder.append("		List<"+clazz.getSimpleName()+"> l = jdbcTemplate.query(sql,new Object[] {"+fname+",start,end },new BeanPropertyRowMapper("+clazz.getSimpleName()+".class));\n");
		sBuilder.append("		return l;\n");		
		sBuilder.append("	}");
		sBuilder.append("\n\n");
	}

	@SuppressWarnings("rawtypes")
	protected static void GenerateQueryCountBy(String fname,String type,Class clazz, StringBuilder sBuilder) {
		String upperfname = fname.substring(0, 1).toUpperCase() + fname.substring(1);
		//sBuilder.append("	@SuppressWarnings({ \"rawtypes\", \"unchecked\" })\n");
		sBuilder.append("	public int Query" + clazz.getSimpleName() + "CountBy"+upperfname+"("+type+" "+fname+"){");
		sBuilder.append("\n");
		sBuilder.append("		String sql = \"SELECT  count(*)  FROM "+clazz.getSimpleName()+" where "+upperfname+"=?\";\n");
		sBuilder.append("		int count = jdbcTemplate.queryForObject(sql,new Object[] {"+fname+"},Integer.class);\n");
		sBuilder.append("		return count;\n");		
		sBuilder.append("	}");
		sBuilder.append("\n\n");
	}
	@SuppressWarnings("rawtypes")
	protected static void GenerateQueryCount(Class clazz, StringBuilder sBuilder) {
		//String upperfname = fname.substring(0, 1).toUpperCase() + fname.substring(1);
		//sBuilder.append("	@SuppressWarnings({ \"rawtypes\", \"unchecked\" })\n");
		sBuilder.append("	public int Query" + clazz.getSimpleName() + "Count(){");
		sBuilder.append("\n");
		sBuilder.append("		String sql = \"SELECT  count(*)  FROM "+clazz.getSimpleName()+"\";\n");
		sBuilder.append("		int count = jdbcTemplate.queryForObject(sql,Integer.class);\n");
		sBuilder.append("		return count;\n");		
		sBuilder.append("	}");
		sBuilder.append("\n\n");
	}
}

