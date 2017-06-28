package local.tcltk;

/**
 * Created by user on 24.06.2017.
 */
public class User {
    private String vk_id;
    private int building;
    private int section;
    private int floor;
    private int flat;

    public String getVk_id() {
        return vk_id;
    }

    public void setVk_id(String vk_id) {
        this.vk_id = vk_id;
    }

    public int getBuilding() {
        return building;
    }

    public void setBuilding(int building) {
        this.building = building;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getFlat() {
        return flat;
    }

    public void setFlat(int flat) {
        this.flat = flat;
    }

    public User(String vk_id, int building, int section, int floor, int flat) {
        this.vk_id = vk_id;
        this.building = building;
        this.section = section;
        this.floor = floor;
        this.flat = flat;
    }

    @Override
    public String toString() {
        return "User{" +
                "vk_id='" + vk_id + '\'' +
                ", building=" + building +
                ", section=" + section +
                ", floor=" + floor +
                ", flat=" + flat +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return vk_id.equals(user.vk_id);
    }

    @Override
    public int hashCode() {
        return vk_id.hashCode();
    }
}
