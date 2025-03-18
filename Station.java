public class Station {
    protected int id;
    public boolean hasGem;
    protected Cart cart;

    public Station(int id) {
        this.id = id;
        this.hasGem = false;
    }

    public synchronized void cartArrive(Cart c) {
        while(cart != null) {
            try {
                wait();
            } catch (InterruptedException e) {
                // Do some error handling
            }
        }
        this.cart = c;
    }

    public synchronized void cartDepart() {
        this.cart = null;
        notifyAll();
    }

    public synchronized void loadCart(Cart c) {
        if(hasGem) {
            c.gems++;
            this.hasGem = false;
        }
    }
}
