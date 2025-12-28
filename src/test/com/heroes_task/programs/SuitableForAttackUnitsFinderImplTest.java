package com.heroes_task.programs;

import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;
import com.battle.heroes.army.Unit;

import java.util.*;

class SuitableForAttackUnitsFinderImplTest {

    @Test
    void testGetSuitableUnitsForLeftArmy() {
        SuitableForAttackUnitsFinderImpl finder = new SuitableForAttackUnitsFinderImpl();

        // Создаем тестовые ряды
        List<List<Unit>> unitsByRow = Arrays.asList(
                Arrays.asList(createUnit("U1"), createUnit("U2"), null), // Ряд 1
                Arrays.asList(null, createUnit("U3"), createUnit("U4")), // Ряд 2
                Arrays.asList(createUnit("U5"), null, null)              // Ряд 3
        );

        // Для левой армии (атакуем компьютер)
        List<Unit> result = finder.getSuitableUnits(unitsByRow, true);

        // Должны быть юниты U2, U4, U5 (первые незакрытые с конца)
        assertEquals(3, result.size());
    }

    @Test
    void testGetSuitableUnitsForRightArmy() {
        SuitableForAttackUnitsFinderImpl finder = new SuitableForAttackUnitsFinderImpl();

        List<List<Unit>> unitsByRow = Arrays.asList(
                Arrays.asList(createUnit("U1"), null, createUnit("U2")),
                Arrays.asList(null, createUnit("U3"), null),
                Arrays.asList(createUnit("U4"), createUnit("U5"), null)
        );

        // Для правой армии (атакуем игрока)
        List<Unit> result = finder.getSuitableUnits(unitsByRow, false);

        // Должны быть юниты U1, U3, U4 (первые незакрытые с начала)
        assertEquals(3, result.size());
    }

    private Unit createUnit(String name) {
        return new Unit(name, "Type", 100, 50, 10, "MELEE", null, true);
    }
}