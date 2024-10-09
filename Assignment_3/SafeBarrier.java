//Implementation of a basic Barrier class (skeleton)
//Mandatory assignment 3
//Course 02158 Concurrent Programming, DTU, Fall 2023

//Hans Henrik Lovengreen     Oct 26, 2023

class SafeBarrier extends Barrier {
    
    int arrived = 0;
    boolean active = false;
    boolean pass = false;
    
   
    public SafeBarrier(CarDisplayI cd) {
        super(cd);
    }

    @Override
    public synchronized void sync(int no) throws InterruptedException {

        if (!active) return;

        if (pass){  //for those who looped around faster then the rest leaving
            wait();
        }
        
        arrived++;
        System.out.println(String.format("Arival-- arrived: %d car no: %d ",arrived,no));
        
        if (arrived == 9 ){//add threshhold
            pass = true;
            notifyAll();
            arrived--;
//            if (arrived == 0){    < -------
//                    pass = false;     < -------
//                    notifyAll();      < -------
//            }
        }

        while (!pass) { 
            wait();   
//            if(arrived >= threshold )  pass = true;  < -------
            if(pass) {
                arrived--;
                System.out.println(String.format("Exit-- arrived: %d car no: %d ",arrived,no));
            }
            
            if (arrived == 0){
                pass = false;
                notifyAll();
                break;
            }
        }     
    }

    @Override
    public synchronized void on() {
        active = true;
        arrived = 0;
        pass = false;
    }

    @Override
    public synchronized void off() {
        active = false;
        pass = true;
        notifyAll();

        
        
    }


    @Override
    // Here (ab)used for emulation of spurious wakeups
    public synchronized void set(int k) {
        for (int i = 0; i < k; i++) { notify(); }
    }    
    

}
