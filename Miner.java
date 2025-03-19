public class Miner extends Thread {
    public Station station;

    public Miner(Station station) {
        this.station = station;
    }

    public void run() {
        while(true) {
            station.mine();
            station.unloadGem();
        }
    }
}
