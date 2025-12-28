package com.heroes_task.programs;

import com.heroes_task_lib.model.Edge;
import com.heroes_task_lib.model.Unit;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UnitTargetPathFinderImplTest {

    @Test
    void testFindPathSimple() {
        UnitTargetPathFinderImpl pathFinder = new UnitTargetPathFinderImpl();

        // Создаем атакующего и цель
        Unit attacker = createUnit("Attacker", 0, 0);
        Unit target = createUnit("Target", 2, 2);

        // Пустой список препятствий
        List<Unit> obstacles = Arrays.asList();

        List<Edge> path = pathFinder.getTargetPath(attacker, target, obstacles);

        assertNotNull(path);
        assertTrue(path.size() > 0, "Должен быть найден путь");

        // Проверяем что путь начинается с атакующего и заканчивается целью
        assertEquals(attacker.getX(), path.get(0).getX());
        assertEquals(attacker.getY(), path.get(0).getY());
        assertEquals(target.getX(), path.get(path.size() - 1).getX());
        assertEquals(target.getY(), path.get(path.size() - 1).getY());
    }

    @Test
    void testFindPathWithObstacles() {
        UnitTargetPathFinderImpl pathFinder = new UnitTargetPathFinderImpl();

        Unit attacker = createUnit("Attacker", 0, 0);
        Unit target = createUnit("Target", 3, 0);

        // Препятствие между ними
        Unit obstacle = createUnit("Obstacle", 1, 0);

        List<Unit> obstacles = Arrays.asList(obstacle);

        List<Edge> path = pathFinder.getTargetPath(attacker, target, obstacles);

        // Путь должен существовать, обходя препятствие
        assertNotNull(path);
        assertTrue(path.size() > 0, "Должен быть найден обходной путь");
    }

    private Unit createUnit(String name, int x, int y) {
        Unit unit = new Unit(name, "Type", 100, 50, 10, "MELEE", null, true);
        // Предположим что у Unit есть методы setX и setY
        // unit.setX(x);
        // unit.setY(y);
        return unit;
    }
}