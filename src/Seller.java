import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Seller {
    private Shop shop;
    private ReentrantLock lock;
    private Condition condition;

    public Seller(Shop shop) {
        this.shop = shop;
        this.lock = new ReentrantLock(true);
        this.condition = this.lock.newCondition();
    }

    public void receiveAuto(int i) {
        try {
            lock.lock();
            Thread.sleep(i);
            shop.getAutos().add(new Auto());
            System.out.println("Производитель " + Thread.currentThread().getName() + " выпустил 1 авто");
            condition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public Auto sellAuto(int i) {
        try {
            lock.lock();
            Thread.sleep(i);
            System.out.println(Thread.currentThread().getName() + " зашел в автосалон");
            while (shop.getAutos().size() == 0) {
                System.out.println("Машин нет");
                condition.await();
            }
            System.out.println(Thread.currentThread().getName() + " уехал на новеньком авто");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return shop.getAutos().remove(0);
    }

}
