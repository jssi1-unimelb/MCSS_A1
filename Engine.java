public class Engine extends Thread {
    private final Station station1;
    private final Station station2;
    private Station currentStation;

    public Engine(Station station1, Station station2) {
        this.station1 = station1;
        this.station2 = station2;
    }

    // Move engine to other station
    public void travel() {
        try {
            sleep(Params.ENGINE_TIME); // Engine travels to next station
            if(this.currentStation.id == this.station1.id) {
                this.currentStation = this.station2;
            } else if(this.currentStation.id == this.station2.id) {
                this.currentStation = this.station1;
            }
        } catch (InterruptedException e) {
            // Handle Error Code
        }
    }

    public void run() {
        try
        System.out.println("Engine go NYOOOOOOOOOOOOOOOM SKRRT SKRRT SKRRT!!!");
    }
}
