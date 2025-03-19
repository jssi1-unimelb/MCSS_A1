public class Engine extends Thread {
    private final Station station1;
    private final Station station2;
    private Station currentStation;
    private Cart cart;

    public Engine(Station station1, Station station2) {
        this.station1 = station1;
        this.station2 = station2;
        this.currentStation = station1; // Engine always starts at "station1"
    }

    public void operate() {
        if(this.currentStation.id == this.station1.id) { // At station 1
            if(this.cart == null) { // Engine does not have cart
                this.cart = this.currentStation.unloadCart(); // Pick up cart from station
            }
        }

        // Engine travels to other station
        try {
            sleep(Params.ENGINE_TIME); // Travel time
            if(this.currentStation.id == this.station1.id) {
                this.currentStation = this.station2;
            } else if(this.currentStation.id == this.station2.id) {
                this.currentStation = this.station1;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Offload cart to the station
        this.currentStation.loadCart(cart);
        this.cart = null;
    }

    public void run() {
        while(true) {
            operate();
        }
    }
}
