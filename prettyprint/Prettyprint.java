
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class Prettyprint 
{
	//let indent be with 5 spaces
    static String indent="     ";
    
 public static void main(String[] args) throws Exception 
 {
	 //read json file using json parser
     Object obj = new JSONParser().parse(new FileReader("json.json"));
      
     // typecasting obj to JSONObject
     JSONObject jo = (JSONObject) obj;
     //System.out.println(a);
     
     // print json object
     printobject(jo,"");
 }
 
 //to print the json object
 public static void printobject(JSONObject jo,String ind){
	 System.out.println(ind+"{");
     Iterator<Map.Entry> itr1 = jo.entrySet().iterator();
     while (itr1.hasNext()) {
         Map.Entry pair = itr1.next();
         
         //checking whether value is json object or not
         if(jo.get(pair.getKey()).getClass().getName().equals("org.json.simple.JSONObject"))
         { 
        	 System.out.print(indent+ind+ "\"" + pair.getKey() + "\"" + " : " );
        	 System.out.println();
        	 printobject((JSONObject)jo.get(pair.getKey()),indent+ind);
        	 if(itr1.hasNext())
        		 System.out.print(",");
         }
         
         //checking whether value is json array or not
         else if(jo.get(pair.getKey()).getClass().getName().equals("org.json.simple.JSONArray"))
         { 
        	 System.out.print(indent+ind+ "\"" + pair.getKey() + "\"" + " : " );
        	 printArray((JSONArray)jo.get(pair.getKey()),ind+indent);
        	 if(itr1.hasNext())
        		 System.out.print(",");
         }
         
        
         //if the value is boolean or integer or anything else i.e other array and json object 
         else {
        	 if(itr1.hasNext())
         System.out.print(indent+ind+"\""+pair.getKey() + "\" : " + pair.getValue()+",");
        	 else
        		 System.out.print(indent+ind+"\""+pair.getKey() + "\" : " + pair.getValue());		 
         }
         System.out.println();
     }
     System.out.print(ind+"}");
 }
 public static void printArray(JSONArray jo,String ind){
		System.out.print("[");
		for(int i = 0 ; i < jo.size() ; i++){
			System.out.println();
			
			//checking whether value is json object or not
			if(jo.get(i).getClass().getName().equals("org.json.simple.JSONObject")){
				printobject((JSONObject) jo.get(i),ind+indent);
			}
			
			//if the value is boolean or integer or anything else i.e other array and json object 
			else
				System.out.print(ind+indent+"\""+jo.get(i) +"\"");
			if(i != jo.size() - 1)
				System.out.print(",");
		}
		System.out.println();
		System.out.print(indent + "]");
	}
}