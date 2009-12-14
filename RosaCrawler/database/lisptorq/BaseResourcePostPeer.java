package database;
/**
  * Autogenerated by Lisptorq 0.1.4 
*/
import java.sql.*;

public class BaseResourcePostPeer  { 

	public static void doInsert(BaseResourcePost args0)
	throws Exception{
        Connection connection = Database.getConnection(); 
        doSave(args0,false,connection);
        Database.release(connection);
	} 

	public static void doInsert(BaseResourcePost args0,Connection connection)
	throws Exception{ 
        doSave(args0,false,connection);
	} 

	public static void doUpdate(BaseResourcePost args0)
	throws Exception{ 	
        Connection connection = Database.getConnection(); 
        doSave(args0,true,connection);
        Database.release(connection);
    }

	public static void doUpdate(BaseResourcePost args0,Connection connection)
	throws Exception{ 	
        doSave(args0,true,connection);
    }

	private static void doSave(BaseResourcePost args0, boolean isUpdate, Connection connection)
	throws Exception{ 

        Statement statement = connection.createStatement();        
        String query = "";
        if(isUpdate){
            query = "UPDATE ResourcePost SET " + " nickname = " + Criteria.escape(args0.getNickname()) + " , " + " resourcename = " + Criteria.escape(args0.getResourcename()) + " , " + " address = " + Criteria.escape(args0.getAddress()) + " , " + " time = " + Criteria.escape(args0.getTime()) + " , " + " port = " + Criteria.escape(args0.getPort()) + " , " + " username = " + Criteria.escape(args0.getUsername()) + " , " + " passwd = " + Criteria.escape(args0.getPasswd()) + " WHERE " + " id = " + Criteria.escape(args0.getId()) + "";
            statement.executeUpdate(query);
        }else{
            query = "INSERT INTO ResourcePost ( nickname , resourcename , address , time , port , username , passwd ) VALUES ( " + Criteria.escape(args0.getNickname()) + " , " + Criteria.escape(args0.getResourcename()) + " , " + Criteria.escape(args0.getAddress()) + " , " + Criteria.escape(args0.getTime()) + " , " + Criteria.escape(args0.getPort()) + " , " + Criteria.escape(args0.getUsername()) + " , " + Criteria.escape(args0.getPasswd()) + " )";         
            statement.execute(query,Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = statement.getGeneratedKeys();
            if(rs.next()){
                args0.id=rs.getInt(1);
            }
        }   
        statement.close();  
        args0.saved = true;      
	}
 
	public static void doDelete(Criteria args0)
	throws Exception{ 
        Connection connection = Database.getConnection();
        doDelete(args0,connection);
        Database.release(connection);
    } 

	public static void doDelete(Criteria args0, Connection connection)
	throws Exception{ 
        Statement statement = connection.createStatement();
        statement.executeUpdate(args0.deleteSQL("RESOURCEPOST"));
        statement.close();
    } 

	public static void doDelete(BaseResourcePost args0)
	throws Exception{ 
        Connection connection = Database.getConnection();
        doDelete(args0,connection);
        Database.release(connection);
    } 

	public static void doDelete(BaseResourcePost args0, Connection connection)
	throws Exception{ 
        Statement statement = connection.createStatement();
        statement.executeUpdate("DELETE FROM ResourcePost WHERE " + " id = " + Criteria.escape(args0.getId()) + "");
        statement.close();
        args0.saved = false; 	
    } 

	public static Scroller doSelect(Criteria args0)
	throws Exception{ 
		return doSelect(args0.selectSQL("RESOURCEPOST.ID,RESOURCEPOST.NICKNAME,RESOURCEPOST.RESOURCENAME,RESOURCEPOST.ADDRESS,RESOURCEPOST.TIME,RESOURCEPOST.PORT,RESOURCEPOST.USERNAME,RESOURCEPOST.PASSWD","RESOURCEPOST"));
	} 

	public static Scroller doSelect(String sql)
	throws Exception{         
        return new  ResourcePostScroller(sql,false);
	} 

	public static Scroller doSelect(Criteria args0, boolean recycle)
	throws Exception{ 
		return doSelect(args0.selectSQL("RESOURCEPOST.ID,RESOURCEPOST.NICKNAME,RESOURCEPOST.RESOURCENAME,RESOURCEPOST.ADDRESS,RESOURCEPOST.TIME,RESOURCEPOST.PORT,RESOURCEPOST.USERNAME,RESOURCEPOST.PASSWD","RESOURCEPOST"),recycle);
	} 

	public static Scroller doSelect(String sql, boolean recycle)
	throws Exception{         
        return new  ResourcePostScroller(sql,recycle);
	} 

	protected static ResourcePost getBean(ResultSet results, ResourcePost ret)
	throws Exception{ 

				ret.id=results.getInt("ID");
				ret.nickname=results.getString("NICKNAME");
				ret.resourcename=results.getString("RESOURCENAME");
				ret.address=results.getString("ADDRESS");
				ret.time=results.getDate("TIME");
				ret.port=results.getInt("PORT");
				ret.username=results.getString("USERNAME");
				ret.passwd=results.getString("PASSWD");
        
    
        ret.saved = true;        
                
        return ret;   
	} 


    private static class ResourcePostScroller implements Scroller{

        Statement statement;
        ResultSet results;   
        boolean recycle = false;  
        ResourcePost cache = null;    

        private ResourcePostScroller(String sql,boolean recycle) throws Exception{

            Connection connection = Database.getConnection();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            results = statement.executeQuery(sql);
            Database.release(connection);
            this.recycle = recycle;
            
        }

        public Object next(){        
            try{     
                cache = (recycle && cache != null)?  BaseResourcePostPeer.getBean(results, cache) : 
                                                     BaseResourcePostPeer.getBean(results, new ResourcePost());
                return cache;
            }catch(Exception ignore){
                return null;
            }
        }

        public boolean hasNext(){
            try{
                if(results.next()){
                    return true;
                }else{
                    return false;   
                }
            }catch(Exception ignore){
                return false;
            }
        }

        public Object previous(){        
            try{     
                cache = (recycle && cache != null)?  BaseResourcePostPeer.getBean(results, cache) : 
                                                     BaseResourcePostPeer.getBean(results, new ResourcePost());
                return cache;
            }catch(Exception ignore){
                return null;
            }
        }

        public boolean hasPrevious(){
            try{
                if(results.previous()){
                    return true;
                }else{
                    return false;   
                }
            }catch(Exception ignore){
                return false;
            }
        }

        public void absolute(int position){
            try{results.absolute(position);}catch(Exception ignore){}    
        }

        public void remove() throws UnsupportedOperationException{ throw new UnsupportedOperationException(); }
        public int  size()   throws UnsupportedOperationException{ throw new UnsupportedOperationException(); }

        protected void finalize() throws Throwable{
            try{statement.close();}catch(Exception ignore){}
        }

    }


}
