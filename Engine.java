public class Engine extends Thread {
    private final StationTemplate station1;
    private final StationTemplate station2;
    private StationTemplate currentStation;
    private Cart cart;

    public Engine(StationTemplate station1, StationTemplate station2) {
        this.station1 = station1;
        this.station2 = station2;
        this.currentStation = station1; // Engine always starts at "station1"
    }

    public void operate() {
        if(currentStation.id == station1.id) { // At station 1
            if(cart == null) { // Engine does not have cart
                cart = currentStation.stationDepart(); // Pick up cart from station

                if(currentStation.id != -1) {
                    System.out.println(cart + " collected from station " + currentStation.id);
                } else {
                    System.out.println(cart + " collected from elevator");
                }
            }
        }
        // If engine at station 2, it travels back to station 1 with no cart

        // Engine travels to other station
        try {
            sleep(Params.ENGINE_TIME); // Travel time
            if(currentStation.id == station1.id) {
                currentStation = station2;
            } else if(currentStation.id == station2.id) {
                currentStation = station1;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if(cart != null) { // Engine has cart
            if(currentStation.id != -1) {
                System.out.println(cart + " delivered to station " + currentStation.id);
            } else {
                System.out.println(cart + " delivered to elevator");
            }

            // Deliver cart to station
            currentStation.stationArrive(cart);
            cart = null;
        }
    }

    public void run() {
        while(true) {
            operate();
        }
    }
}
