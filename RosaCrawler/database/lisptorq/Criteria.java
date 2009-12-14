package database;
/**
  * Autogenerated by Lisptorq 0.1.4 
  * from default-criteria.template
*/

import java.sql.*;
import java.util.HashMap;
import java.util.ArrayList;

public class Criteria{ 

    private Criterion _head = null;  //the main criterion
    private String    _query = null; //the cached query
    private boolean   _distinct = false;
    private ArrayList   _orderBy = null;

    public Criteria add(String field, long value){
        return add(field,value,EQUAL);
    }
    public Criteria add(String field, long value, String comparison){
        return add(field,escape(value),comparison,false);
    }

    public Criteria add(String field, double value){
        return add(field,value,EQUAL);
    }
    public Criteria add(String field, double value, String comparison){
        return add(field,escape(value),comparison,false);
    }

    public Criteria add(String field, Object value){
        return add(field,value,EQUAL);
    }

    public Criteria add(String field, Object value, String comparison){
        return add(field,escape(value),comparison,false);
    }

    public Criteria addJoin(String field1, String field2){
        return add(field1,field2,EQUAL,true);
    }

    public Criteria compareWithColumn(String field1, String field2, String comparison){
        return add(field1,field2,comparison,true);
    }

    public Criteria add(String field, Object value, String comparison,boolean isValueAColumn){
        return add(new Criterion(field,value,comparison,isValueAColumn,true)); 
    }

    public Criteria add(Criterion criterion){
        _head = (null == _head)?  criterion : _head.and(criterion);
        _query = null;
        return this;    
    }

    public Criteria setDistinct(){
        _distinct = true;
        return this;
    }

    public Criteria addAscendingOrder(String column){
        return addOrderBy(column + ASCENDING);
    }
    
    public Criteria addDescendingOrder(String column){
        return addOrderBy(column + DESCENDING);
    }

    protected  Criteria addOrderBy(String order){
        if(null == _orderBy) _orderBy = new ArrayList();        
        if(!_orderBy.contains(order)) _orderBy.add(order);
        return this;  
    }

    public void clear(){
        _head = null;
        _query = null;
        _distinct = false;
        if(_orderBy != null) _orderBy.clear();  
    }

    public boolean isEmpty(){
        return (_head == null);
    }
    
    public static class Criterion{

        private String field;
        private Object value;
        private String comparison = EQUAL;
        private boolean isValueAColumn = false;

        private ArrayList conjunctions = null;
        private ArrayList conjunctionTypes = null;

        //------------ used internally only -----------

        private Criterion(String f, Object v, String c, boolean b, boolean escaped){
            field = f;   value = v;  comparison = c;  isValueAColumn = b;
        }

        //------------ object --------------

        public Criterion(String f, Object v, String c, boolean b){
            field = f;   
            isValueAColumn = b;
            value = (b || c == CUSTOM)? pad(v) : escape(v); 
        }

        public Criterion(String f, Object v, String c){
            field = f;   value = (c == CUSTOM)? pad(v) : escape(v); comparison = c;
        }

        public Criterion(String f, Object v){
            field = f;   value = escape(v); 
        }

        public Criterion(String f, Object v, boolean b){
            field = f;   value = (b)? pad(v) : escape(v); isValueAColumn = b;
        }

        //------------- double --------------
    
        public Criterion(String f, double v, String c){
            this(f,v); comparison = c;
        }

        public Criterion(String f, double v){
            field = f;   value = escape(v); 
        }

        //------------- long --------------
    
        public Criterion(String f, long v, String c){
            this(f,v); comparison = c;
        }

        public Criterion(String f, long v){
            field = f;   value = escape(v); 
        }

        //------------ for joins  between columns -----------

        public static Criterion join(String col1, String col2){
            return new Criterion(col1,col2,EQUAL,true);
        }

        public String getTable(){
            return field.substring(0,field.indexOf('.'));
        }    

        public String getField(){
            return field.substring(field.indexOf('.') + 1);
        }  

        public Criterion and(Criterion c){
            return addConjunction(c,AND);
        }

        public Criterion or(Criterion c){
            return addConjunction(c,OR);
        }

        private Criterion addConjunction(Criterion c, String type){
            if(null == conjunctions) conjunctions = new ArrayList();
            if(null == conjunctionTypes) conjunctionTypes = new ArrayList();
    
            conjunctions.add(c);
            conjunctionTypes.add(type); 

            return this;
        }

        private String pad(Object v){
            String s = v.toString();
            StringBuffer sb = new StringBuffer(2 + s.length());
            return sb.append(" ").append(s).append(" ").toString();
        }

    }


    public static final String CUSTOM  = "custom";

    /*********************************************
         CUSTOMIZATIONS MAY BE NEEDED BELOW! 
    *********************************************/


    public static final String EQUAL = "=";
    public static final String NOT_EQUAL = "<>";
    public static final String GREATER_EQUAL = ">=";
    public static final String GREATER = ">";
    public static final String LESS_EQUAL = "<=";
    public static final String LESS = "<";
    public static final String LIKE = " like ";
    public static final String IN = " IN ";
    public static final String NOT_IN = " NOT IN ";

    protected static final String JOIN = " JOIN ";
    protected static final String WHERE = " WHERE ";
    protected static final String AND = " AND ";
    protected static final String OR = " OR ";
    protected static final String FROM = " FROM ";
    protected static final String SELECT = " SELECT ";
    protected static final String DEL = "DELETE FROM ";
    protected static final String DISTINCT = " DISTINCT ";

    protected static final String ORDER_BY = " ORDER BY ";
    protected static final String ASCENDING = " ASC ";
    protected static final String DESCENDING = " DESC ";


    public  static String escape(double d){
        return d + "";
    }

    public  static String escape(long l){
        return l + "";
    }

    public static String escape(Object o){
        if (null == o) return " NULL ";
        return "'" + o.toString().replaceAll("'","\\\\'") + "'";
    }

    private StringBuffer getTables(String leadTable,StringBuffer sb){

        if(null==_head) return sb;               
        ArrayList tables = collectTables(_head,new ArrayList());
        Object[] objs = tables.toArray();
        for(int i = objs.length - 1; i >= 0; i--){
            String s = objs[i].toString();   
            if(!s.equals(leadTable))sb.append(",").append(s);
        }
        return sb;
    }

    private ArrayList collectTables(Criterion c, ArrayList h){
        if(null == c) return h;
        String t = c.getTable();
        if(!h.contains(t)) h.add(t);
        if(null == c.conjunctions) return h;
        for(int i = c.conjunctions.size() - 1; i >= 0; i--){
            collectTables((Criterion)c.conjunctions.get(i),h);   
        } 
        return h;
    }

    private void putOrderBy(StringBuffer sb){

        if(null==_orderBy || _orderBy.size() < 1) return;  
        Object[] objs = _orderBy.toArray(); 
        sb.append(ORDER_BY);                    
        int N = objs.length -1 ;
        for(int i = 0 ; i <= N; i++){
            sb.append(objs[i]);
            if(i!= N) sb.append(","); 
        }        
    }

    public String selectSQL(String selectItems, String leadTable){
        if(null == _query){     
            StringBuffer buffer = new StringBuffer(SELECT);
            if(_distinct) buffer.append(DISTINCT);
            buffer.append(selectItems);
            buffer.append(FROM);
            buffer.append(leadTable);
            buffer = getTables(leadTable,buffer);
            if(_head != null){
                buffer.append(WHERE);  
                buffer.append(toString(_head, new StringBuffer()));  
            }

            putOrderBy(buffer);
                
            _query = buffer.toString();   
        }
        return _query;
    }


    public String deleteSQL(String leadTable){
        if(_head == null) return null;
        StringBuffer buffer = new StringBuffer(DEL);
        buffer.append(leadTable);
        buffer.append(WHERE);  
        buffer.append(toString(_head, new StringBuffer()));  
        return buffer.toString();
    }

    private StringBuffer toString(Criterion c,StringBuffer buffer){
        if (null == c.conjunctions){    
            return getString(c,buffer);
        }else{

            buffer.append("(");
            buffer = getString(c,buffer); //print this criterion

            String prevConjunctionType = (String) c.conjunctionTypes.get(0);
            int N = c.conjunctionTypes.size();
            for(int i = 0; i < N; i++){
                String cType = (String) c.conjunctionTypes.get(i);
                if(cType != prevConjunctionType){
                    StringBuffer sb = new StringBuffer(buffer.length() + 2);
                    sb.append("(");
                    sb.append(buffer);
                    buffer = sb.append(")");                    
                }        
                buffer.append(cType);
                buffer = toString((Criterion) c.conjunctions.get(i),buffer);
                prevConjunctionType = cType;    
            }    
            buffer.append(")");
        }
        return buffer;
    }

    private StringBuffer getString(Criterion c,StringBuffer buffer){
        buffer.append(c.field);
            if(CUSTOM == c.comparison){
            buffer.append(c.value);                
        }else{
            buffer.append(c.comparison);
            buffer.append(c.value);
        } 
        return buffer; 
    }
 
}
