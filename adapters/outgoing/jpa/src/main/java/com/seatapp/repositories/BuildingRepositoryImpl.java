package com.seatapp.repositories;

import com.seatapp.domain.Building;
import com.seatapp.domain.Floor;
import com.seatapp.domain.Point;
import com.seatapp.domain.Seat;
import com.seatapp.entities.BuildingEntity;
import com.seatapp.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class BuildingRepositoryImpl implements BuildingRepository {
    /**
     * Loading in the data for an island of 2 rows or columns.
     */
    private static final int ISLAND_ROW_COLUMN_SEAT2 = 2;

    /**
     * Loading in the data for an island of 3 rows or columns.
     */
    private static final int ISLAND_ROW_COLUMN_SEAT3 = 3;

    /**
     * Loading in the data for seats with a start coordinate of 5 cm.
     */
    private static final int START_COORDINATE5 = 5;

    /**
     * Loading in the data for seats with a start coordinate of 100 cm.
     */
    private static final int START_COORDINATE100 = 100;

    /**
     * Loading in the data for seats with a start coordinate of 500 cm.
     */
    private static final int START_COORDINATE500 = 500;

    /**
     * Loading in the data for seats with a start coordinate of 605 cm.
     */
    private static final int START_COORDINATE605 = 605;

    /**
     * Loading in the data for seats with a start coordinate of 900 cm.
     */
    private static final int START_COORDINATE900 = 900;

    /**
     * Loading in the data for seats with a start coordinate of 995 cm.
     */
    private static final int START_COORDINATE995 = 995;

    /**
     * Loading in the data for seats with a start coordinate of 1005 cm.
     */
    private static final int START_COORDINATE1005 = 1005;

    /**
     * Loading in the data for seats with a start coordinate of 1300 cm.
     */
    private static final int START_COORDINATE1300 = 1300;

    /**
     * Loading in the data for seats with a start coordinate of 1405 cm.
     */
    private static final int START_COORDINATE1405 = 1405;

    /**
     * Loading in the data for seats with a start coordinate of 1805 cm.
     */
    private static final int START_COORDINATE1805 = 1805;

    /**
     * Loading in the data for seats with a width or height of 100 cm.
     */
    private static final int WIDTH_HEIGHT100 = 100;

    /**
     * Loading in the data for seats with a width or height of 200 cm.
     */
    private static final int WIDTH_HEIGHT200 = 200;

    /**
     * Loading in the data for seats with a width or height of 1000 cm.
     */
    private static final int WIDTH_HEIGHT1000 = 1000;

    /**
     * Loading in the data for seats with a width or height of 1600 cm.
     */
    private static final int WIDTH_HEIGHT1600 = 1600;

    /**
     * Loading in the data for seats with a width or height of 1800 cm.
     */
    private static final int WIDTH_HEIGHT1800 = 1800;

    /**
     * Loading in the data for seats with a width or height of 2100 cm.
     */
    private static final int WIDTH_HEIGHT2100 = 2100;

    /**
     * Represents the building repository.
     */
    private final BuildingRepositoryJpa repository;

    /**
     * Creates the BuildingRepositoryImpl.
     *
     * @param repository the repository.
     */
    @Autowired
    public BuildingRepositoryImpl(
            final BuildingRepositoryJpa repository) {
        this.repository = repository;
        initEntities();
    }

    /**
     * Initialize the static floorplan.
     */
    private void initEntities() {
        List<Seat> seats = new ArrayList<>();
        int count = 1;
        for (int rowIndex = 0; rowIndex < ISLAND_ROW_COLUMN_SEAT2; rowIndex++) {
            for (int columnIndex = 0; columnIndex < ISLAND_ROW_COLUMN_SEAT2;
                 columnIndex++) {
                seats.add(new Seat("A" + count,
                        START_COORDINATE100 + (WIDTH_HEIGHT100 * rowIndex),
                        START_COORDINATE5 + (WIDTH_HEIGHT200 * columnIndex),
                        WIDTH_HEIGHT100, WIDTH_HEIGHT200));
                seats.add(new Seat("B" + count,
                        START_COORDINATE500 + (WIDTH_HEIGHT100 * rowIndex),
                        START_COORDINATE5 + (WIDTH_HEIGHT200 * columnIndex),
                        WIDTH_HEIGHT100, WIDTH_HEIGHT200));
                seats.add(new Seat("C" + count,
                        START_COORDINATE900 + (WIDTH_HEIGHT100 * rowIndex),
                        START_COORDINATE5 + (WIDTH_HEIGHT200 * columnIndex),
                        WIDTH_HEIGHT100, WIDTH_HEIGHT200));
                seats.add(new Seat("D" + count,
                        START_COORDINATE1300 + (WIDTH_HEIGHT100 * rowIndex),
                        START_COORDINATE5 + (WIDTH_HEIGHT200 * columnIndex),
                        WIDTH_HEIGHT100, WIDTH_HEIGHT200));
                count++;
            }
        }

        count = 1;
        for (int rowIndex = 0; rowIndex < ISLAND_ROW_COLUMN_SEAT3; rowIndex++) {
            for (int columnIndex = 0; columnIndex < ISLAND_ROW_COLUMN_SEAT2;
                 columnIndex++) {
                seats.add(new Seat("E" + count,
                        START_COORDINATE5 + (WIDTH_HEIGHT200 * rowIndex),
                        START_COORDINATE605 + (WIDTH_HEIGHT100 * columnIndex),
                        WIDTH_HEIGHT200, WIDTH_HEIGHT100));
                seats.add(new Seat("F" + count,
                        START_COORDINATE995 + (WIDTH_HEIGHT200 * rowIndex),
                        START_COORDINATE605 + (WIDTH_HEIGHT100 * columnIndex),
                        WIDTH_HEIGHT200, WIDTH_HEIGHT100));
                seats.add(new Seat("G" + count,
                        START_COORDINATE5 + (WIDTH_HEIGHT200 * rowIndex),
                        START_COORDINATE1005 + (WIDTH_HEIGHT100 * columnIndex),
                        WIDTH_HEIGHT200, WIDTH_HEIGHT100));
                seats.add(new Seat("H" + count,
                        START_COORDINATE995 + (WIDTH_HEIGHT200 * rowIndex),
                        START_COORDINATE1005 + (WIDTH_HEIGHT100 * columnIndex),
                        WIDTH_HEIGHT200, WIDTH_HEIGHT100));
                seats.add(new Seat("I" + count,
                        START_COORDINATE5 + (WIDTH_HEIGHT200 * rowIndex),
                        START_COORDINATE1405 + (WIDTH_HEIGHT100 * columnIndex),
                        WIDTH_HEIGHT200, WIDTH_HEIGHT100));
                seats.add(new Seat("J" + count,
                        START_COORDINATE995 + (WIDTH_HEIGHT200 * rowIndex),
                        START_COORDINATE1405 + (WIDTH_HEIGHT100 * columnIndex),
                        WIDTH_HEIGHT200, WIDTH_HEIGHT100));
                seats.add(new Seat("K" + count,
                        START_COORDINATE5 + (WIDTH_HEIGHT200 * rowIndex),
                        START_COORDINATE1805 + (WIDTH_HEIGHT100 * columnIndex),
                        WIDTH_HEIGHT200, WIDTH_HEIGHT100));
                count++;
            }
        }

        List<Point> points = new ArrayList<>();
        points.add(new Point(0, 0));
        points.add(new Point(WIDTH_HEIGHT1600, 0));
        points.add(new Point(WIDTH_HEIGHT1600, WIDTH_HEIGHT1800));
        points.add(new Point(WIDTH_HEIGHT1000, WIDTH_HEIGHT1800));
        points.add(new Point(WIDTH_HEIGHT1000, WIDTH_HEIGHT2100));
        points.add(new Point(0, WIDTH_HEIGHT2100));
        points.add(new Point(0, 0));

        Floor floor = new Floor(1L, "1", points, seats);

        save(new Building(1L, "Xplore Kontich", List.of(floor)));
    }

    /**
     * Saving the building in the database.
     *
     * @param building the building you want to save
     * @return The updated building due to
     * the changes from the database
     */
    @Override
    public Building save(final Building building) {
        BuildingEntity buildingEntity = BuildingEntity.build(building);
        buildingEntity = repository.save(buildingEntity);

        return buildingEntity.toBuilding();
    }

    /**
     * Get all the buildings from the database.
     *
     * @return all the buildings from the database
     * in a list
     */
    @Override
    public List<Building> findAll() {
        List<BuildingEntity> entities = repository.findAll();

        return entities.stream().map(BuildingEntity::toBuilding)
                .toList();
    }

    /**
     * Tries to find the building with the given id.
     *
     * @param buildingId the id of the building you want to find
     * @return the building with the given id.
     */
    @Override
    public Building findById(final Long buildingId) {
        Optional<BuildingEntity> optionalEntity =
                repository.findById(buildingId);
        return optionalEntity
                .map(BuildingEntity::toBuilding)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "No building with this id."));
    }

    /**
     * Deletes all the buildings.
     */
    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
}
