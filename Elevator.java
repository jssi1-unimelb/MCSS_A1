public class Elevator extends Station {

    private Cart cart;

    public Elevator() {
        super(0); // Elevator has a station id 0
    }

    public void arrive(Cart cart) {
        this.cart = cart;
    }

    public Cart depart() {
        Cart temp = this.cart;
        this.cart = null; // Remove cart from elevator
        return temp;
    }
}
