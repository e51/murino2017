package local.tcltk.model.domain;

import org.apache.log4j.Logger;

import static local.tcltk.Constants.STRUCTURE;

/**
 * Created by user on 24.06.2017.
 */

public class User {
    private static final Logger logger = Logger.getLogger(User.class);

    // DB stored fields
    private long vk_id;
    private int building;
    private int section;
    private int floor;
    private int flat;
    private int updates;

    // other fields
    private String vkFirstName;
    private String vkLastName;
    private String vkPhoto;
    private String vkPhoto200;
    private String vkPhoto100;
    private String vkPhoto50;
    private String token;
//    private boolean mobileUser;
    private int appVersion;

    private boolean useFlat;

    public int getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(int appVersion) {
        this.appVersion = appVersion;
    }

    public String getVkPhoto200() {
        return vkPhoto200;
    }

    public void setVkPhoto200(String vkPhoto200) {
        this.vkPhoto200 = vkPhoto200;
    }

    public String getVkPhoto100() {
        return vkPhoto100;
    }

    public void setVkPhoto100(String vkPhoto100) {
        this.vkPhoto100 = vkPhoto100;
    }

    public String getVkPhoto50() {
        return vkPhoto50;
    }

    public void setVkPhoto50(String vkPhoto50) {
        this.vkPhoto50 = vkPhoto50;
    }

//    public boolean isMobileUser() {
//        return mobileUser;
//    }

//    public void setMobileUser(boolean mobileUser) {
//        this.mobileUser = mobileUser;
//    }

    public boolean isUseFlat() {
        return useFlat;
    }

    public void setUseFlat(boolean useFlat) {
        this.useFlat = useFlat;
    }

    public int getUpdates() {
        return updates;
    }

    public void setUpdates(int updates) {
        this.updates = updates;
    }

    public String getVkFirstName() {
        return vkFirstName;
    }

    public void setVkFirstName(String vkFirstName) {
        this.vkFirstName = vkFirstName;
    }

    public String getVkLastName() {
        return vkLastName;
    }

    public void setVkLastName(String vkLastName) {
        this.vkLastName = vkLastName;
    }

    public String getVkPhoto() {
        return vkPhoto;
    }

    public void setVkPhoto(String vkPhoto) {
        this.vkPhoto = vkPhoto;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getVk_id() {
        return vk_id;
    }

    public void setVk_id(long vk_id) {
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

    public User(long vk_id, int building, int section, int floor, int flat, int updates) {
        this.vk_id = vk_id;
        this.building = building;
        this.section = section;
        this.floor = floor;
        this.flat = flat;
        this.updates = updates;
    }

    @Override
    public String toString() {
        return "User{" +
                "vk_id='" + vk_id + '\'' +
                ", vkLastName=" + vkLastName +
                ", vkFirstName=" + vkFirstName +
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

        return vk_id == user.vk_id;
    }

    @Override
    public int hashCode() {
        return (int) vk_id;
    }

    public boolean isValid() {
        boolean result = true;

        if (!STRUCTURE.keySet().contains(new Integer(building))) {
            logger.error(String.format("[isValid] INVALID building. No such building (building=%d)", building));
            return false;
        }

        if (section < 1 || section > STRUCTURE.get(new Integer(building)).getSectionsCount()) {
            logger.error(String.format("[isValid] INVALID section. No such section (building=%d, section=%d)", building, section));
            return false;
//            result = false;
        }

        try {
            if (floor < 1 || floor > STRUCTURE.get(new Integer(building)).getFloorsCountBySection()[section - 1]) {
                logger.error(String.format("[isValid] INVALID floor. No such floor (building=%d, section=%d, floor=%d)", building, section, floor));
                return false;
//                result = false;
            }
        } catch (Exception e) {
            // In case of ArrayIndexOutOfBounds exception
//            e.printStackTrace();
            logger.error(String.format("[isValid] INVALID floor. Got %s for: (building=%d, section=%d, floor=%d)", e.getClass().getSimpleName(), building, section, floor));
            return false;
//            result = false;
        }

        return result;
    }
}
