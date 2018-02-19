// Testing

class SimpleTesting implements ISimpleTask{
    private int i;
    public SimpleTesting(int i){
        this.i = i;
    }
    @Override
    public void run() {
        System.out.println(i);
    }
    public static void main(String args[]){
        // Initialize thread pool
        SimpleThreadPool pool = new SimpleThreadPool();
//        pool.start();
        // Create 20 tasks
        for(int i = 1; i<=20; i++){
            pool.addTask(new SimpleTesting(i));
        }
        pool.stop();
        pool.start();
        pool.stop();
    }
}