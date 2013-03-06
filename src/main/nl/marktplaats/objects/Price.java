package main.nl.marktplaats.objects;

public class Price {
	private float price = 0;

	public Price(int i) {
		price = i;
	}

	public Price() {
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float myPrice) {
		this.price = myPrice;
	}

}
