package ir.ac.kntu.services;

import ir.ac.kntu.model.Cell;
import ir.ac.kntu.model.Digger;
import ir.ac.kntu.model.Direction;
import ir.ac.kntu.model.Enemy;
import javafx.geometry.Point2D;

import java.util.*;

public class EnemyAIService {
    Random random;

    public EnemyAIService() {
        random = new Random();
    }

    public Direction getNextEnemyDirection(Enemy enemy) {
        Direction direction;
        direction = getEscapingDirection(enemy);
        if (direction != null) {
            return direction;
        }
        direction = getNextEnemyDirectionByDigger(enemy);
        if (direction != null) {
            return direction;
        }
        return getNextEnemyDirectionRandomly(enemy);
    }

    private boolean isDiggerInDirection(Enemy enemy, Direction direction) {
        int x = enemy.getGridX();
        int y = enemy.getGridY();
        while (true) {
            Point2D nextPoint = enemy.getNextPoint(x, y, 1, direction);
            x = (int) nextPoint.getX();
            y = (int) nextPoint.getY();
            if (!enemy.getMap().isGridCoordinateInMap(x, y)) {
                return false;
            }
            Cell cell = enemy.getMap().getCell(x, y);
            if (cell.hasBlock()) {
                return false;
            }
            if (cell.hasObjectType(Digger.class)) {
                return true;
            }
        }
    }

    private Direction getNextEnemyDirectionByDigger(Enemy enemy) {
        return Arrays.stream(Direction.values()).filter(direction -> isDiggerInDirection(enemy, direction))
                .findFirst().orElse(null);
    }

    private Direction getNextEnemyDirectionRandomly(Enemy enemy) {
        List<Direction> directions = new ArrayList<>(Arrays.asList(Direction.values()));
        if (enemy.getDirection() != null) {
            directions.remove(Direction.reverseDirection(enemy.getDirection()));
        }
        Iterator<Direction> iterator = directions.iterator();
        while (iterator.hasNext()) {
            Direction direction = iterator.next();
            Point2D nextPoint = enemy.getNextPoint(enemy.getGridX(), enemy.getGridY(), 1, direction);
            if (!enemy.canEnemyGoToCell((int) nextPoint.getX(), (int) nextPoint.getY())) {
                iterator.remove();
            }
        }
        if (directions.isEmpty()) {
            return Direction.reverseDirection(enemy.getDirection());
        }
        int randIndex = random.nextInt(directions.size());
        return directions.get(randIndex);
    }

    private Direction getEscapingDirection(Enemy enemy) {
        int x = enemy.getGridX();
        int y = enemy.getGridY();
        if (!enemy.getMap().canEnemiesEscape() || y != 0) {
            return null;
        }
        while (true) {
            Point2D nextPoint = enemy.getNextPoint(x, y, 1, Direction.LEFT);
            x = (int) nextPoint.getX();
            y = (int) nextPoint.getY();
            if (!enemy.getMap().isGridCoordinateInMap(x, y)) {
                return null;
            }
            Cell cell = enemy.getMap().getCell(x, y);
            if (cell.hasBlock()) {
                return null;
            }
            if (x == 0) {
                return Direction.LEFT;
            }
        }
    }
}
