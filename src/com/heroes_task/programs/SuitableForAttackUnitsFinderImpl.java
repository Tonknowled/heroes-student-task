package com.heroes_task.programs;

import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;
import com.battle.heroes.army.Unit;
import java.util.*;

public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {

    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {
        List<Unit> result = new ArrayList<>();

        if (unitsByRow == null) {
            return result;
        }

        for (List<Unit> row : unitsByRow) {
            if (row == null || row.isEmpty()) {
                continue;
            }

            if (isLeftArmyTarget) {
                // Для левой армии: ищем с конца
                for (int i = row.size() - 1; i >= 0; i--) {
                    Unit unit = row.get(i);
                    if (unit != null && unit.isAlive()) {
                        result.add(unit);
                        break;
                    }
                }
            } else {
                // Для правой армии: ищем с начала
                for (int i = 0; i < row.size(); i++) {
                    Unit unit = row.get(i);
                    if (unit != null && unit.isAlive()) {
                        result.add(unit);
                        break;
                    }
                }
            }
        }

        return result;
    }
}