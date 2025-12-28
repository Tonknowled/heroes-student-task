package com.heroes_task.programs;

import com.battle.heroes.army.programs.GeneratePreset;
import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;

import java.util.*;

class GeneratePresetImplTest {

    @Test
    void testGenerateArmyWithinPointsLimit() {
        GeneratePresetImpl generator = new GeneratePresetImpl();

        // Создаем тестовых юнитов
        List<Unit> unitList = Arrays.asList(
                createUnit("Archer", 100, 50, 10),
                createUnit("Knight", 150, 75, 15),
                createUnit("Swordsman", 120, 60, 12),
                createUnit("Pikeman", 80, 40, 8)
        );

        Army army = generator.generate(unitList, 1500);

        assertNotNull(army);
        assertTrue(army.getPoints() <= 1500);
        assertTrue(army.getUnits().size() > 0);
    }

    @Test
    void testMax11UnitsPerType() {
        GeneratePresetImpl generator = new GeneratePresetImpl();

        // Дешевый юнит
        List<Unit> unitList = Arrays.asList(
                createUnit("Archer", 50, 25, 5) // 5 очков за юнита
        );

        Army army = generator.generate(unitList, 1000);

        // Максимум 11 юнитов * 5 очков = 55 очков
        // Должно быть не больше 11 юнитов
        long archerCount = army.getUnits().stream()
                .filter(u -> u.getUnitType().equals("Archer"))
                .count();

        assertTrue(archerCount <= 11);
    }

    private Unit createUnit(String type, int health, int attack, int cost) {
        return new Unit(
                type,
                type,
                health,
                attack,
                cost,
                "RANGED",
                null,
                true
        );
    }
}