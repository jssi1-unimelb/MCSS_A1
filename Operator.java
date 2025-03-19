public class Operator extends Thread {
    private Elevator elevator;

    public Operator(Elevator elevator) {
        this.elevator = elevator;
    }

    public void run() {
        while(true) {
            elevator.move();
        }
    }
}
