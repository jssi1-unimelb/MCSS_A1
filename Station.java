public class Station {
    protected int id;
    private boolean hasGem;
    protected Cart cart;

    public Station(int id) {
        this.id = id;
        this.hasGem = false;
        this.cart = null;
    }

    public synchronized void mine() {
        while(hasGem) { // Miner thread only runs when there is no gem in station
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            wait(Params.MINING_TIME); // Miner take time to mine
            this.hasGem = true;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // Unloads the station gem onto the cart
    public synchronized void unloadGem() {
        // Pause thread if:
        //      1. Station has no cart
        //      2. The cart has already collected a gem
        while(cart == null || cart.gems != this.id) {
            try {
//                System.out.println("Station " + id + " waiting for a cart to load gem, miner put to sleep");
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        cart.gems++;
        this.hasGem = false;
        System.out.println(cart + " loaded with a gem");
        notifyAll(); // wakes up engine
    }

    // Cart arrives at station
    public synchronized void engineArrive(Cart c) {
        while(cart != null) { // Station is occupied
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        this.cart = c;
        notifyAll();
    }

    // Transfers cart departs the station
    public synchronized Cart engineDepart() {
        // station must have a cart and cart must get a gem from the station
        while(this.cart == null || this.cart.gems != id + 1) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        Cart tmp = this.cart;
        this.cart = null;
        notifyAll();
        return tmp;
    }

}
