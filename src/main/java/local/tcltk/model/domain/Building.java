package local.tcltk.model.domain;

import java.util.Arrays;
import java.util.Collections;

public class Building implements Comparable<Building> {
    // creating once, not changing anymore
    final int value;                      // number of this building
    final int sectionsCount;              // count sections in this building
    final Integer[] floorsCountBySection; // floors by section
    final int maxFlatsByFloor;            // max count of flats by floor
//    int[] flatsCountBySectionFloor; // flats by section floor

    final int maxFloor;                   // max floor in this building


    public int getMaxFlatsByFloor() {
        return maxFlatsByFloor;
    }

    public int getValue() {
        return value;
    }

    public int getSectionsCount() {
        return sectionsCount;
    }

    public Integer[] getFloorsCountBySection() {
        return floorsCountBySection;
    }

    public int getMaxFloor() {
        return maxFloor;
    }

    /**
     *
     * @param value - building number
     * @param sectionsCount - count of sections in this building
     * @param floorsCountBySection - count of floors in each section
     * @param maxFlatsByFloor - max flats per floor
     */
    public Building(int value, int sectionsCount, Integer[] floorsCountBySection, int maxFlatsByFloor) {
        this.value = value;
        this.sectionsCount = sectionsCount;
        this.floorsCountBySection = floorsCountBySection;
        this.maxFloor = Collections.max(Arrays.asList(floorsCountBySection));
        this.maxFlatsByFloor = maxFlatsByFloor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Building building = (Building) o;

        return value == building.value;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public String toString() {
        return "Building{" +
                "value=" + value +
                ", sectionsCount=" + sectionsCount +
                ", floorsCountBySection=" + Arrays.toString(floorsCountBySection) +
                ", maxFloor=" + maxFloor +
                '}';
    }

    @Override
    public int compareTo(Building o) {
        return value - o.value;
    }
}

