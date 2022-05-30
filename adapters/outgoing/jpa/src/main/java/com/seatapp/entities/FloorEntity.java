package com.seatapp.entities;

import com.seatapp.domain.Floor;
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
import javax.persistence.ElementCollection;
import java.util.List;

@Entity
@Table(name = "Floor")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FloorEntity {
    /**
     * Represents the floor id.
     */
    @Id
    @GeneratedValue
    private long id;

    /**
     * Represents the floor name.
     */
    private String name;

    /**
     * Represents the points for drawing the walls of the floor in a svg.
     */
    @ElementCollection
    private List<PointEntity> points;

    /**
     * Represents the seats on the floor.
     */
    @OneToMany(cascade = CascadeType.ALL)
    private List<SeatEntity> seats;

    /**
     * This method converts a floor to a floorEntity.
     *
     * @param floor the to be converted floor
     * @return a seat entity
     */
    public static FloorEntity build(final Floor floor) {
        return new FloorEntity(floor.getId(), floor.getName(),
                floor.getPoints().stream()
                        .map(PointEntity::build)
                        .toList(),
                floor.getSeats().stream()
                        .map(SeatEntity::build)
                        .toList());
    }

    /**
     * This method converts a floorEntity to a floor.
     *
     * @return a floor
     */
    public Floor toFloor() {
        return new Floor(this.getId(), this.getName(),
                this.getPoints().stream()
                        .map(PointEntity::toPoint)
                        .toList(),
                this.getSeats().stream()
                        .map(SeatEntity::toSeat)
                        .toList());
    }
}
