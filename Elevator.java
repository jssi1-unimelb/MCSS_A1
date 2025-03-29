public class Elevator extends StationTemplate {

    private boolean atTop; // Assume elevator starts at the top

    public Elevator() {
        super(-1); // Elevator has a station id 0
        atTop = true;
    }

    public synchronized void move() {
        if(cart == null) { // Elevator empty
            try {
                wait(Params.operatorPause()); // Move at random interval
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else { // Elevator not empty
            while(cart != null && ((atTop && cart.gems == Params.STATIONS) || (!atTop && cart.gems == 0))) {
                try {
                    wait();

                    // When woken up, the elevator will be empty
                    try {
                        wait(Params.operatorPause()); // Move at random interval
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
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

            if(cart == null) {
                System.out.println("elevator " + movement + " (empty)");
            } else {
                System.out.println("elevator " + movement + " with " + cart);
            }


            wait(Params.ELEVATOR_TIME); // Elevator takes time to move
            atTop = !atTop; // Change location
            notifyAll(); // wake up engine waiting to deliver cart
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized Cart cartDepart() {
        Cart tmp = cart;
        cart = null;
        notifyAll();
        return tmp;
    }

    // Called by Consumer and Producer
    public synchronized Cart depart() {
        // Elevator is empty or at the bottom or cart is empty
        while(cart == null || !atTop || cart.gems == 0){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return cartDepart();
    }

    // Called by Consumer and Producer
    public synchronized void arrive(Cart c) {
        // Elevator is not empty or at the bottom
        while(cart != null || !atTop) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        cart = c;
        notifyAll();
    }

    // Used by the "engine" class
    public synchronized Cart stationDepart() {
        // Elevator is empty or at the top or cart is full
        while(cart == null || atTop || cart.gems == Params.STATIONS) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return cartDepart();
    }

    // Used by the "engine" class
    public synchronized void stationArrive(Cart c) {
        // Elevator is not empty or at the top
        while(cart != null || atTop) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        cart = c;
        notifyAll();
    }
}
