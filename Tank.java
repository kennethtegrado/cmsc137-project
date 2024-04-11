public class Tank {
    private int health;
    private int damage;

    public Tank() {
        this.health = 300;
        this.damage = 10;
    }

    public int getHealth() {
        return health;
    }

    public int getDamage() {
        return damage;
    }

    public Bullet fireBullet() {
        //test value
        return new Bullet(10); 
    }
}
