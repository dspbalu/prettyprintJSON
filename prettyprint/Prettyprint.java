package edyst;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class Prettyprint 
{
	//let indent be with 2 spaces
    static String indent="  ";
    static boolean replace=false,compact=false;
   static ArrayList<String> place=new ArrayList<String>();
   
 public static void main(String[] args) throws Exception 
 {
	 Object obj=null;String file;
	 if(args.length!=0) {
		 for(int i=0;i<args.length;i++) {
		 if(args[i].contains("--replace"))
			 replace=true;
		 if(args[i].contains("--compact"))
			 compact=true;
		 if(args[i].contains("--indent")){
			int a=Integer.parseInt(args[i].substring(args[i].indexOf('=')+1, args[i].length()));
			indent="";
			for(int j=0;j<a;j++)
				indent+=" ";
		 }		 
		 if(args[i].contains("--from-file")){
			 file=args[i].substring(args[i].indexOf('=')+1, args[i].length());
			 obj = new JSONParser().parse(new FileReader(file));
		 }
		 }
	// System.out.println("cmd");
	 }
	 else {
     System.out.println("please give path to read a file in command line");
     System.exit(0);
	 }
      
     // typecasting obj to JSONObject
     JSONObject jo = (JSONObject) obj;
     //System.out.println(a);
     if(compact)
     compact(jo);
     // print json object
     printobject(jo,"");
     if(replace)
     replace(args[0]);
 }
 
 public static void replace(String file) throws Exception {
 	FileWriter fileWriter = new FileWriter(file);
 	for(String a:place) {
 		fileWriter.write(a+"\n");
 		
 	}
 	fileWriter.close();
 }
 
 public static void compact(JSONObject jo){
 	System.out.print("{");
 	Iterator<Map.Entry> itr1 = jo.entrySet().iterator();
     while (itr1.hasNext()) {
         Map.Entry pair = itr1.next();
         if(jo.get(pair.getKey()).getClass().getName().equals("java.lang.String")) {
        	 if(itr1.hasNext()) {
        	 System.out.print("\""+pair.getKey()+"\": \""+pair.getValue()+"\",");
        	 }
        	 else
        		 System.out.print("\""+pair.getKey()+"\": \""+pair.getValue()+"\"");
         }
         else {
         	System.out.print("\""+pair.getKey()+"\":"+pair.getValue());
         if(itr1.hasNext())
         	System.out.print(",");
     }
     }
     System.out.print("}");
     System.out.println();
 }
 //to print the json object
 public static void printobject(JSONObject jo,String ind){
	 System.out.println("{");
	 place.add(ind+"{");
     Iterator<Map.Entry> itr1 = jo.entrySet().iterator();
     while (itr1.hasNext()) {
         Map.Entry pair = itr1.next();
         
         //checking whether value is json object or not
         if(jo.get(pair.getKey()).getClass().getName().equals("org.json.simple.JSONObject"))
         { 
        	 System.out.print(indent+ind+ "\"" + pair.getKey() + "\"" + " : " );
        	 place.add(indent+ind+ "\"" + pair.getKey() + "\"" + " : ");
        	 printobject((JSONObject)jo.get(pair.getKey()),indent+ind);
        	 if(itr1.hasNext()) {
        		 String x=place.get(place.size()-1);
        		place.set(place.size()-1,x+",");
        		 System.out.print(",");
        	 }
         }
         
         //checking whether value is json array or not
         else if(jo.get(pair.getKey()).getClass().getName().equals("org.json.simple.JSONArray"))
         { 
        	 System.out.print(indent+ind+ "\"" + pair.getKey() + "\"" + " : " );
        	 place.add(indent+ind+ "\"" + pair.getKey() + "\"" + " : ");
        	 
        	 printArray((JSONArray)jo.get(pair.getKey()),ind+indent);
        	 if(itr1.hasNext()) {
        		 String x=place.get(place.size()-1);
         		place.set(place.size()-1,x+",");
         		 System.out.print(",");
         	 }
         }
         
        
         //if the value is boolean or integer or anything else i.e other array and json object 
         else {
        	 if(itr1.hasNext()) {
         System.out.print(indent+ind+"\""+pair.getKey() + "\" : \"" + pair.getValue()+"\",");
         place.add(indent+ind+"\""+pair.getKey() + "\" : \"" + pair.getValue()+"\",");
        	 }
        	 else {
        		 System.out.print(indent+ind+"\""+pair.getKey() + "\" : \"" + pair.getValue()+"\"");
        		 place.add(indent+ind+"\""+pair.getKey() + "\" : \"" + pair.getValue()+"\""); 
        	 }
         }
         System.out.println();
     }
     System.out.print(ind+"}");
     place.add(ind+"}");
 }
 public static void printArray(JSONArray jo,String ind){
		System.out.print("[");
		String x=place.get(place.size()-1);
		place.set(place.size()-1,x+"[");
		for(int i = 0 ; i < jo.size() ; i++){
			System.out.println();
			
			//checking whether value is json object or not
			if(jo.get(i).getClass().getName().equals("org.json.simple.JSONObject")){
				System.out.print(ind+indent);
				printobject((JSONObject) jo.get(i),ind+indent);
			}
			
			//if the value is boolean or integer or anything else i.e other array and json object 
			else {
				System.out.print(ind+indent+"\""+jo.get(i) +"\"");
				place.add(ind+indent+"\""+jo.get(i) +"\"");
			}
			if(i != jo.size() - 1) {
				System.out.print(",");
				x=place.get(place.size()-1);
				place.set(place.size()-1,x+",");
			}
		}
		System.out.println();
		System.out.print(indent + "]");
		place.add(indent+"]");
	}
}