public class Station {
    protected int id;
    public boolean hasGem;
    protected Cart cart;

    public Station(int id) {
        this.id = id;
        this.hasGem = false;
    }

    public void cartArrive(Cart c) {
        this.cart = c;
    }

    public void cartDepart() {
        this.cart = null;
    }

    public void loadCart(Cart c) {
        if(hasGem) {
            c.gems++;
            this.hasGem = false;
        }
    }
}
