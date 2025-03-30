public class Station extends StationTemplate {
    protected int id;
    private boolean cartHasBeenLoaded;
    protected Cart cart;

    public Station(int id) {
        super(id);
        this.cartHasBeenLoaded = false;
    }

    public synchronized void mine() {
        // Station has no gem
        try {
            wait(Params.MINING_TIME); // Miner take time to mine

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // Unloads the station gem onto the cart
    public synchronized void loadCart() {
        while(cart == null || cartHasBeenLoaded) { // Station has no cart or cart is already loaded
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // Load cart
        cart.gems++;
        cartHasBeenLoaded = true;
        System.out.println(cart + " loaded with a gem");
        notifyAll(); // wakes up engine
    }

    // Cart arrives at station
    public @Override synchronized void stationArrive(Cart c) {
        while(cart != null) { // Station is occupied
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // Cart arrives at station
        cart = c;
        cartHasBeenLoaded = false; // Cart needs to be loaded
        notifyAll();
    }

    // Cart departs the station
    public @Override synchronized Cart stationDepart() {
        while(cart == null || !cartHasBeenLoaded) { // Station isn't occupied or cart hasn't been loaded
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Cart tmp = cart;
        cart = null;
        notifyAll();
        return tmp;
    }

}
