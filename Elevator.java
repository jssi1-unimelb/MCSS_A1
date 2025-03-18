public class Elevator extends Station {

    protected Cart cart;
    private boolean atTop; // Assume elevator starts at the top

    public Elevator() {
        super(0); // Elevator has a station id 0
        this.atTop = true;
    }

    public synchronized void move() {
        try {
            wait(Params.operatorPause());
        } catch (InterruptedException e) {
            // Error Handling Code
        }
        this.atTop = !this.atTop;
    }

    public synchronized void arrive(Cart cart) {
        this.cart = cart;
    }

    public synchronized Cart depart() {
        Cart temp = this.cart;
        this.cart = null; // Remove cart from elevator
        return temp;
    }
}
