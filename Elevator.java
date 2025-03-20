import static java.lang.Thread.currentThread;

public class Elevator extends Station {

    private boolean atTop; // Assume elevator starts at the top

    public Elevator() {
        super(-1); // Elevator has a station id 0
        this.atTop = true;
    }

    public synchronized void move() {
        if(cart == null) { // Empty Lift
            while(cart == null) {
                try {
                    wait(Params.operatorPause());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        } else { // Not empty lift
            while(cart != null && ((!atTop && cart.gems == 0) || (atTop && cart.gems == Params.STATIONS))) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        try {
            // Get direction
            String movement = "ascends";
            if(atTop) {
                movement = "descends";
            }

            if(this.cart == null) {
                System.out.println("elevator " + movement + " (empty)");
            } else {
                System.out.println("elevator " + movement + " with " + cart);
            }
            wait(Params.ELEVATOR_TIME);
            this.atTop = !atTop;
            notifyAll(); // wake up engine waiting to deliver cart
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // Used by the "consumer" class to take the full cart from the lift
    public synchronized Cart depart() {
        // Elevator must have a cart
        // Elevator must be on top
        // Cart must be full
        while(this.cart == null || !atTop || this.cart.gems == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

//        System.out.println("depart() called");


        Cart tmp = this.cart;
        this.cart = null;
        notifyAll();
        return tmp;
    }

    // Used by the "engine" class to take the empty cart from the lift
    public synchronized Cart engineDepart() {
        // Elevator must have a cart
        // Elevator must be on bottom
        // Cart must be empty
        while(this.cart == null || atTop || this.cart.gems == Params.STATIONS) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // System.out.println("engineDepart() called");

        Cart tmp = this.cart;
        this.cart = null;
        notifyAll();
        return tmp;
    }

    public synchronized void arrive(Cart c) {
        while(!atTop) {
           try {
               wait();
           } catch (InterruptedException e) {
               throw new RuntimeException(e);
           }
        }

//        System.out.println("arrive() called");

        super.engineArrive(c);
        notifyAll();
    }

    public @Override synchronized void engineArrive(Cart c) {
        while(atTop) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

//        System.out.println("engineArrive() called");


        super.engineArrive(c);
        notifyAll();
    }
}
