package cn.askrosa.others;

public class NumberFormat
{
  public static String numberFormat(int number)
  {
      String numberString=String.valueOf(number);
      String numberDotString="";
      int i=numberString.length();
      while(i>0)
      {
	  if(i-3>0)
	    numberDotString=","+numberString.substring(i-3,i)+numberDotString;
	  else
	    numberDotString=numberString.substring(0,i)+numberDotString; 
	  i=i-3;
      }
      
      return numberDotString;
  }
  public static String numberFormat(long number)
  {
      String numberString=String.valueOf(number);
      String numberDotString="";
      int i=numberString.length();
      while(i>0)
      {
	  if(i-3>0)
	    numberDotString=","+numberString.substring(i-3,i)+numberDotString;
	  else
	    numberDotString=numberString.substring(0,i)+numberDotString; 
	  i=i-3;
      }
      
      return numberDotString;
  }
  public static void main(String[] args)
  {
      System.out.println(NumberFormat.numberFormat(454445124));
  }
}