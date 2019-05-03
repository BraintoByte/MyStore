package MyProject.MyStore.BusinessData;

public class StoreItem {
	
	private String name;
	private Double price;
	private String type;
	
	
	public Double getPrice() {
		return price;
	}
	
	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setType(String type) {
		this.type = type;
	}
}