public class Miner extends Thread {
    public Station station;

    public Miner(Station station) {
        this.station = station;
    }

    public void run() {
        while(true) {
            station.mine();
            station.loadCart(); // Miner will not continue until it has loaded the gem into the cart
        }
    }
}
