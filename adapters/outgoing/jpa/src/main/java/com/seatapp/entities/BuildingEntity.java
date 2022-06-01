package com.seatapp.entities;

import com.seatapp.domain.Building;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import java.util.List;

@Entity
@Table(name = "Building")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BuildingEntity {
    /**
     * Represents the building id.
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * Represents the building name.
     */
    private String name;

    /**
     * Represents the floors in the building.
     */
    @OneToMany(cascade = CascadeType.ALL)
    private List<FloorEntity> floors;

    /**
     * This method converts a building to a buildingEntity.
     *
     * @param building the to be converted building
     * @return a seat entity
     */
    public static BuildingEntity build(final Building building) {
        return new BuildingEntity(building.getId(), building.getName(),
                building.getFloors().stream()
                        .map(FloorEntity::build)
                        .toList());
    }

    /**
     * This method converts a buildingEntity to a building.
     *
     * @return a building
     */
    public Building toBuilding() {
        return new Building(this.getId(), this.getName(),
                this.getFloors().stream()
                        .map(FloorEntity::toFloor)
                        .toList());
    }
}
