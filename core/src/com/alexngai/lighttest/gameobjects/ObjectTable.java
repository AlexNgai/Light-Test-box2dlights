package com.alexngai.lighttest.gameobjects;

import java.util.Hashtable;

import com.badlogic.gdx.Gdx;

public class ObjectTable {

	private static Hashtable typeTable = new Hashtable();
	private static Hashtable objectTable = new Hashtable();

	public static final int wallType = 1;
	public static final int playerTypeA = 2;
	public static final int playerTypeB = 3;

	
	public static void addObjectType(int hashcode, int type){
		Integer code = new Integer(hashcode);
		typeTable.put(code, type);
		//Gdx.app.log("ObjTable", "put " + hashcode + ", " + type);
	}
	
	public static void addObject(int hashcode, int type, Object object){
		Integer code = new Integer(hashcode);
		typeTable.put(code, type);
		objectTable.put(code, object);
		//Gdx.app.log("ObjTable", "put " + hashcode + ", " + type);
	}
	
	public static int getType(int hashcode){
		Integer code = new Integer(hashcode);
		if (typeTable.containsKey(hashcode)){
			int type = (Integer)typeTable.get(code);
			//Gdx.app.log("ObjTable", "get " + hashcode + ", " + type);
			return type;
		}
		return -1;
	}
	
	public static Object getObject(int hashcode){
		Integer code = new Integer(hashcode);
		if (objectTable.containsKey(hashcode)){
			return objectTable.get(code);
		}
		return null;
	}
}
