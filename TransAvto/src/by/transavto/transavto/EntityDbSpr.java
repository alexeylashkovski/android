package by.transavto.transavto;

public class EntityDbSpr {
	
	protected int id;
	protected String data;
	
	public EntityDbSpr(){
		setId(0);
		setData("");
	}
	public EntityDbSpr(int id, String data) {
		setId(id);
		setData(data);
	}
	public void setId(int id){
		this.id = id;
	}
	public int getId() {
		return this.id;
	}
	public void setData(String data){
		this.data = data;
	}
	public String getData(){
		return this.data;
	}

}
