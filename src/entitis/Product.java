package entitis;

public class Product {
	int id_product;
	String name;
	String description;
	Float price;
	int stock;
	Boolean shippingIncluded;
	
	//idProduct
	public int getId_product() {
		return id_product;
	}
	public void setId_product(int id_product) {
		this.id_product = id_product;
	}
	//name
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	//description
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	//price
	public Float getPrice() {
		return price;
	}
	public void setPrice(Float price) {
		this.price = price;
	}
	//stock
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	//ShippingIncluded
	public Boolean getShippingIncluded() {
		return shippingIncluded;
	}
	public void setShippingIncluded(Boolean shippingIncluded) {
		this.shippingIncluded = shippingIncluded;
	}
	
	public Product(){
	}

	public Product(int id_product, String name, String Descripcion, Float price, int stock, Boolean shippingIncluded){
		setId_product(id_product);
		setName(name);
		setDescription(description);
		setPrice(price);
		setStock(stock);
		setShippingIncluded(shippingIncluded);
	}
	
}
