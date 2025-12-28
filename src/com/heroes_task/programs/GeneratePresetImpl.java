package com.heroes_task.programs;

import com.battle.heroes.army.programs.GeneratePreset;
import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import java.util.*;

public class GeneratePresetImpl implements GeneratePreset {

    private static final int MAX_UNITS_PER_TYPE = 11;
    private static final int MAX_POINTS = 1500;
    private static final int MAX_TOTAL_UNITS = 20; // Ограничим общее количество

    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {
        System.out.println("[GeneratePresetImpl] generate() called with maxPoints=" + maxPoints);

        Army army = new Army();
        List<Unit> selectedUnits = new ArrayList<>();

        if (unitList == null || unitList.isEmpty()) {
            System.out.println("[WARN] Unit list is empty");
            army.setUnits(selectedUnits);
            army.setPoints(0);
            return army;
        }

        System.out.println("Available unit types: " + unitList.size());

        // Показываем доступные юниты
        for (int i = 0; i < unitList.size(); i++) {
            Unit unit = unitList.get(i);
            System.out.println("Unit " + i + ": " + unit.getUnitType() +
                    ", cost=" + unit.getCost() + ", attack=" + unit.getBaseAttack());
        }

        int remainingPoints = maxPoints;
        int unitId = 0;

        // Сортируем по эффективности (атака/стоимость)
        List<Unit> sortedByEfficiency = new ArrayList<>(unitList);
        sortedByEfficiency.sort((u1, u2) -> {
            double eff1 = (double) u1.getBaseAttack() / u1.getCost();
            double eff2 = (double) u2.getBaseAttack() / u2.getCost();
            return Double.compare(eff2, eff1); // по убыванию
        });

        // Берем самых эффективных юнитов
        for (Unit unitTemplate : sortedByEfficiency) {
            if (remainingPoints <= 0 || selectedUnits.size() >= MAX_TOTAL_UNITS) break;

            String type = unitTemplate.getUnitType();
            int cost = unitTemplate.getCost();

            // Сколько можем взять этого типа
            int maxOfThisType = Math.min(
                    MAX_UNITS_PER_TYPE,
                    Math.min(
                            remainingPoints / cost,
                            MAX_TOTAL_UNITS - selectedUnits.size()
                    )
            );

            if (maxOfThisType <= 0) continue;

            System.out.println("Adding " + maxOfThisType + " of type " + type + " (cost " + cost + " each)");

            for (int i = 0; i < maxOfThisType; i++) {
                Unit newUnit = new Unit(
                        type + "_" + unitId++, // name
                        type,                  // unitType
                        unitTemplate.getHealth(), // health
                        unitTemplate.getBaseAttack(), // baseAttack
                        cost,                  // cost
                        unitTemplate.getAttackType(), // attackType
                        new HashMap<>(),       // attackBonuses
                        new HashMap<>(),       // defenceBonuses
                        0,                     // xCoordinate
                        0                      // yCoordinate
                );

                newUnit.setProgram(unitTemplate.getProgram());
                newUnit.setAlive(true);
                newUnit.setxCoordinate(0);
                newUnit.setyCoordinate(0);

                selectedUnits.add(newUnit);
                remainingPoints -= cost;

                if (selectedUnits.size() >= MAX_TOTAL_UNITS) break;
            }
        }

        army.setUnits(selectedUnits);
        army.setPoints(maxPoints - remainingPoints);

        System.out.println("Generated army: " + selectedUnits.size() + " units, " +
                (maxPoints - remainingPoints) + " points used, " + remainingPoints + " points remaining");

        // Проверяем что все юниты имеют правильные типы
        Map<String, Integer> typeCount = new HashMap<>();
        for (Unit unit : selectedUnits) {
            String type = unit.getUnitType();
            typeCount.put(type, typeCount.getOrDefault(type, 0) + 1);
        }
        System.out.println("Unit type distribution: " + typeCount);

        return army;
    }
}