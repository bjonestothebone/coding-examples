import java.io.*;
import java.util.ArrayList;

public class test {
	public static ArrayList<String> holder = new ArrayList<String>();
	public static ArrayList<String> holder2 = new ArrayList<String>();
	String s= "";
  public static void main(String[] args) throws IOException {
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in)); 
    String s= "";
    System.out.println(foo(s));
    
    
  }//main
  
  
  public static String foo (String s){
	  String[] temp = s.split("|"); 
	  for (int i = 0; i<temp.length; i++){
          String test = temp[i].replaceAll("[\\-\\+\\.\\^:,\"\'?]","").toLowerCase();
          boolean tz = helper(temp, test);
          if(tz) {
              holder.add(temp[i]);
              holder2.add(test);
          }
      }
      return holder.toString();
      
  }
  public static boolean helper(String [] temp, String test){
      for(int x = 0; x<temp.length; x++){
          String test2 = temp[x].replaceAll("[\\-\\+\\.\\^:,\"\'?]","").toLowerCase();
          if(test2.equals(test)){
              if(holder2.contains(test2)) {
                  return false;
              }else{
                  continue;
              }
          } 
          if(test2.contains(test))
              return false;
      }
      return true;
  
  }//helper()
 
}//test

