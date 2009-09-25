package searcher;
/**
 * 表示一个索引节点
 * @author elegate
 *
 */
public class Node
{
    private String name;

    private String address;
    
    private int port;

    public Node(String name, String address, int port)
    {
	this.name = name;
	this.address = address;
	this.port = port;
    }

    public String getName()
    {
	return this.name;
    }

    public void setName(String name)
    {
	this.name = name;
    }

    public String getAddress()
    {
	return this.address;
    }

    public void setAddress(String address)
    {
	this.address = address;
    }

    public String toString()
    {
	return this.getClass().getName() + "[name=" + name + ",address="
		+ address + "]";
    }

    /**
     * @return the port
     */
    public int getPort()
    {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(int port)
    {
        this.port = port;
    }
}
