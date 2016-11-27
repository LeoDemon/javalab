package com.ufree.learn;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;

/**
 * 使用javassit动态生成一个java类
 * 
 * @author Demon.Lee
 * @version 1.0
 * @date 2016.10.13
 * 
 */
public class LearnJavassist {
	private static Map<String, Object> newClassMap = new HashMap<String, Object>();

	public void GenNewClass(String className, List<String> fieldList)
			throws Exception {

		CtClass newClass = null;

		if (null != newClassMap && 0 < newClassMap.size()) {
			if (null != newClassMap.get(className)) {
				System.out.println("[" + className + "] has been generated!");
				return;
			}
		}
		// ClassPool：CtClass对象的容器
		ClassPool pool = ClassPool.getDefault();

		// 通过ClassPool生成一个public新类
		newClass = pool.makeClass("com.ufree.vo." + className);

		for (int f = 0; f < fieldList.size(); f++) {
			String filedName = fieldList.get(f);

			// 创建属性filedName
			CtField cfd = CtField.make("private String " + filedName + ";",
					newClass);
			newClass.addField(cfd);

			// 创建getfiledName方法
			CtMethod getMethod = CtMethod.make("public String get" + filedName
					+ "(){return this." + filedName + ";}", newClass);
			newClass.addMethod(getMethod);

			// 创建setfiledName方法
			CtMethod setMethod = CtMethod.make("public String set" + filedName
					+ "(String " + filedName + "){return this." + filedName
					+ "=" + filedName + ";}", newClass);
			newClass.addMethod(setMethod);
		}

		newClassMap.put(className, newClass);

		return;
	}

	public void testNewClass(String className, List<String> fieldList)
			throws Exception {
		CtClass ctClass = (CtClass) newClassMap.get(className);

		// 为了验证效果，下面使用反射执行方法printInfo
		Class<?> clazz = ctClass.toClass();
		Object obj = clazz.newInstance();

		int size = fieldList.size();
		for (int k = 0; k < size; k++) {
			String fieldName = fieldList.get(k);

			// invoke setField(String) function
			Method setMethod = clazz.getDeclaredMethod("set" + fieldName,
					String.class);
			setMethod.invoke(obj, "testtt_" + k);
			// invoke getfield() function
			String fieldValue = (String) obj.getClass()
					.getMethod("get" + fieldName, new Class[] {})
					.invoke(obj, new Object[] {});
			System.out.println(fieldName + "==" + fieldValue);
		}
	}

	public void testGenNewClass() throws Exception {

		// ClassPool：CtClass对象的容器
		ClassPool pool = ClassPool.getDefault();

		// 通过ClassPool生成一个public新类Emp.java
		CtClass ctClass = pool.makeClass("com.ufree.vo.Pd_userprc_info");

		// 添加字段
		// 首先添加字段private String prodprcins_id
		CtField prodprcins_id = new CtField(
				pool.getCtClass("java.lang.String"), "prodprcins_id", ctClass);
		prodprcins_id.setModifiers(Modifier.PRIVATE);
		ctClass.addField(prodprcins_id);

		// 其次添加字段privtae long id_no
		CtField id_no = new CtField(pool.getCtClass("long"), "id_no", ctClass);
		id_no.setModifiers(Modifier.PRIVATE);
		ctClass.addField(id_no);

		// 为字段prodprcins_id和id_no添加getXXX和setXXX方法
		ctClass.addMethod(CtNewMethod.getter("getProdprcins_id", prodprcins_id));
		ctClass.addMethod(CtNewMethod.setter("setProdprcins_id", prodprcins_id));
		ctClass.addMethod(CtNewMethod.getter("getId_no", id_no));
		ctClass.addMethod(CtNewMethod.setter("setId_no", id_no));

		/*
		 * // 添加构造函数 CtConstructor ctConstructor = new CtConstructor(new
		 * CtClass[] {}, ctClass); // 为构造函数设置函数体 StringBuffer buffer = new
		 * StringBuffer();
		 * buffer.append("{\n").append("prodprcins_id=\"abc12396owkdd\";\n")
		 * .append("id_no=12358965895L;\n}");
		 * ctConstructor.setBody(buffer.toString()); // 把构造函数添加到新的类中
		 * ctClass.addConstructor(ctConstructor);
		 */

		// 添加自定义方法
		CtMethod ctMethod = new CtMethod(CtClass.voidType, "printInfo",
				new CtClass[] {}, ctClass);
		// 为自定义方法设置修饰符
		ctMethod.setModifiers(Modifier.PUBLIC);
		// 为自定义方法设置函数体
		StringBuffer buffer2 = new StringBuffer();
		buffer2.append(
				"{\nSystem.out.println(\"***********begin printInfo!***********\");\n")
				.append("System.out.println(\"prodprcins_id==\"+prodprcins_id);\n")
				.append("System.out.println(\"id_no==\"+id_no);\n")
				.append("System.out.println(\"***********end printInfo!***********\");\n")
				.append("}");
		ctMethod.setBody(buffer2.toString());
		ctClass.addMethod(ctMethod);

		// 为了验证效果，下面使用反射执行方法printInfo
		Class<?> clazz = ctClass.toClass();
		Object obj = clazz.newInstance();

		// invoke printInfo() function
		obj.getClass().getMethod("printInfo", new Class[] {})
				.invoke(obj, new Object[] {});

		// invoke setProdprcins_id(String) function
		Method setProdprcins_idMethod = clazz.getDeclaredMethod(
				"setProdprcins_id", String.class);
		setProdprcins_idMethod.invoke(obj, "aaaaaaaaaaaaaaaaaaaaaaa");
		// invoke getProdprcins_id() function
		String proprcId = (String) obj.getClass()
				.getMethod("getProdprcins_id", new Class[] {})
				.invoke(obj, new Object[] {});
		System.out.println("proprcId==" + proprcId);

		// invoke setId_no(long) function
		Method setId_noMethod = clazz.getDeclaredMethod("setId_no", long.class);
		setId_noMethod.invoke(obj, 111111111122222222L);
		// invoke getId_no() function
		long idNo = (long) obj.getClass().getMethod("getId_no", new Class[] {})
				.invoke(obj, new Object[] {});
		System.out.println("idNo==" + idNo);
		// invoke printInfo() function
		obj.getClass().getMethod("printInfo", new Class[] {})
				.invoke(obj, new Object[] {});

		// 把生成的class文件写入文件
		byte[] byteArr = ctClass.toBytecode();
		FileOutputStream fos = new FileOutputStream(new File(
				"./file/Pd_userprc_info.class"));
		fos.write(byteArr);
		fos.close();
	}

	public static void main(String[] args) throws Exception {
		LearnJavassist gncbj = new LearnJavassist();
		System.out.println("++++++++++++++++++++++++++++++++++++");
		List<String> fieldList = new ArrayList<String>();
		fieldList.add("BOOK_ID");
		fieldList.add("BOOK_NAME");
		fieldList.add("PURCHASE_TIME");
		fieldList.add("BOOK_STATE");
		fieldList.add("BOOK_BORROW_ID");
		fieldList.add("BOOK_BORROW_TIME");

		gncbj.GenNewClass("BookInfo", fieldList);
		gncbj.testNewClass("BookInfo", fieldList);
		System.out.println("------------------------------------");
	}
}
