package com.heroes_task.programs;


import com.battle.heroes.army.programs.SimulateBattle;
import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;

import java.util.*;


class SimulateBattleImplTest {

    @Test
    void testSimulateBattleBasic() throws InterruptedException {
        SimulateBattleImpl simulator = new SimulateBattleImpl();

        // Создаем простые армии
        Army playerArmy = new Army();
        Army computerArmy = new Army();

        // Добавляем по одному юниту в каждую армию
        playerArmy.setUnits(Arrays.asList(
                createUnit("PlayerUnit", 100, 50, true)
        ));
        computerArmy.setUnits(Arrays.asList(
                createUnit("ComputerUnit", 100, 50, true)
        ));

        // Запускаем симуляцию
        simulator.simulate(playerArmy, computerArmy);

        // Проверяем что хотя бы одна армия проиграла
        boolean playerHasAlive = playerArmy.getUnits().stream().anyMatch(Unit::isAlive);
        boolean computerHasAlive = computerArmy.getUnits().stream().anyMatch(Unit::isAlive);

        assertFalse(playerHasAlive && computerHasAlive,
                "После боя должна остаться только одна армия");
    }

    private Unit createUnit(String name, int health, int attack, boolean alive) {
        Unit unit = new Unit(name, "Type", health, attack, 10, "MELEE", null, alive);
        return unit;
    }
}