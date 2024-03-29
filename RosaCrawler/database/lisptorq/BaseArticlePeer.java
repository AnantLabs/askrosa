package database;
/**
  * Autogenerated by Lisptorq 0.1.4 
*/
import java.sql.*;

public class BaseArticlePeer  { 

	public static void doInsert(BaseArticle args0)
	throws Exception{
        Connection connection = Database.getConnection(); 
        doSave(args0,false,connection);
        Database.release(connection);
	} 

	public static void doInsert(BaseArticle args0,Connection connection)
	throws Exception{ 
        doSave(args0,false,connection);
	} 

	public static void doUpdate(BaseArticle args0)
	throws Exception{ 	
        Connection connection = Database.getConnection(); 
        doSave(args0,true,connection);
        Database.release(connection);
    }

	public static void doUpdate(BaseArticle args0,Connection connection)
	throws Exception{ 	
        doSave(args0,true,connection);
    }

	private static void doSave(BaseArticle args0, boolean isUpdate, Connection connection)
	throws Exception{ 

        Statement statement = connection.createStatement();        
        String query = "";
        if(isUpdate){
            query = "UPDATE Article SET " + " author = " + Criteria.escape(args0.getAuthor()) + " , " + " time = " + Criteria.escape(args0.getTime()) + " , " + " title = " + Criteria.escape(args0.getTitle()) + " , " + " content = " + Criteria.escape(args0.getContent()) + " , " + " clickcount = " + Criteria.escape(args0.getClickcount()) + " , " + " ip = " + Criteria.escape(args0.getIp()) + " , " + " verify = " + Criteria.escape(args0.getVerify()) + " WHERE " + " id = " + Criteria.escape(args0.getId()) + "";
            statement.executeUpdate(query);
        }else{
            query = "INSERT INTO Article ( author , time , title , content , clickcount , ip , verify ) VALUES ( " + Criteria.escape(args0.getAuthor()) + " , " + Criteria.escape(args0.getTime()) + " , " + Criteria.escape(args0.getTitle()) + " , " + Criteria.escape(args0.getContent()) + " , " + Criteria.escape(args0.getClickcount()) + " , " + Criteria.escape(args0.getIp()) + " , " + Criteria.escape(args0.getVerify()) + " )";         
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
        statement.executeUpdate(args0.deleteSQL("ARTICLE"));
        statement.close();
    } 

	public static void doDelete(BaseArticle args0)
	throws Exception{ 
        Connection connection = Database.getConnection();
        doDelete(args0,connection);
        Database.release(connection);
    } 

	public static void doDelete(BaseArticle args0, Connection connection)
	throws Exception{ 
        Statement statement = connection.createStatement();
        statement.executeUpdate("DELETE FROM Article WHERE " + " id = " + Criteria.escape(args0.getId()) + "");
        statement.close();
        args0.saved = false; 	
    } 

	public static Scroller doSelect(Criteria args0)
	throws Exception{ 
		return doSelect(args0.selectSQL("ARTICLE.ID,ARTICLE.AUTHOR,ARTICLE.TIME,ARTICLE.TITLE,ARTICLE.CONTENT,ARTICLE.CLICKCOUNT,ARTICLE.IP,ARTICLE.VERIFY","ARTICLE"));
	} 

	public static Scroller doSelect(String sql)
	throws Exception{         
        return new  ArticleScroller(sql,false);
	} 

	public static Scroller doSelect(Criteria args0, boolean recycle)
	throws Exception{ 
		return doSelect(args0.selectSQL("ARTICLE.ID,ARTICLE.AUTHOR,ARTICLE.TIME,ARTICLE.TITLE,ARTICLE.CONTENT,ARTICLE.CLICKCOUNT,ARTICLE.IP,ARTICLE.VERIFY","ARTICLE"),recycle);
	} 

	public static Scroller doSelect(String sql, boolean recycle)
	throws Exception{         
        return new  ArticleScroller(sql,recycle);
	} 

	protected static Article getBean(ResultSet results, Article ret)
	throws Exception{ 

				ret.id=results.getInt("ID");
				ret.author=results.getString("AUTHOR");
				ret.time=results.getDate("TIME");
				ret.title=results.getString("TITLE");
				ret.content=results.getString("CONTENT");
				ret.clickcount=results.getInt("CLICKCOUNT");
				ret.ip=results.getString("IP");
				ret.verify=results.getString("VERIFY");
        
    
        ret.saved = true;        
                
        return ret;   
	} 


    private static class ArticleScroller implements Scroller{

        Statement statement;
        ResultSet results;   
        boolean recycle = false;  
        Article cache = null;    

        private ArticleScroller(String sql,boolean recycle) throws Exception{

            Connection connection = Database.getConnection();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            results = statement.executeQuery(sql);
            Database.release(connection);
            this.recycle = recycle;
            
        }

        public Object next(){        
            try{     
                cache = (recycle && cache != null)?  BaseArticlePeer.getBean(results, cache) : 
                                                     BaseArticlePeer.getBean(results, new Article());
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
                cache = (recycle && cache != null)?  BaseArticlePeer.getBean(results, cache) : 
                                                     BaseArticlePeer.getBean(results, new Article());
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
