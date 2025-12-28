package com.heroes_task.programs;

import com.battle.heroes.army.programs.SimulateBattle;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import java.util.*;

public class SimulateBattleImpl implements SimulateBattle {

    // Все поля которые могут требоваться через reflection
    public PrintBattleLog printBattleLog;
    public Object allyArmy;  // Возможно нужно
    public Object enemyArmy; // Возможно нужно
    public int simSpeed;     // Возможно нужно

    @Override
    public void simulate(Army playerArmy, Army computerArmy) throws InterruptedException {
        System.out.println("=== SIMULATE BATTLE STARTED ===");

        try {
            // Собираем всех юнитов
            List<Unit> allUnits = new ArrayList<>();

            if (playerArmy != null && playerArmy.getUnits() != null) {
                allUnits.addAll(playerArmy.getUnits());
                System.out.println("Player units added: " + playerArmy.getUnits().size());
            }

            if (computerArmy != null && computerArmy.getUnits() != null) {
                allUnits.addAll(computerArmy.getUnits());
                System.out.println("Computer units added: " + computerArmy.getUnits().size());
            }

            System.out.println("Total units in battle: " + allUnits.size());

            // Удаляем неживых
            allUnits.removeIf(unit -> unit == null || !unit.isAlive());
            System.out.println("Alive units: " + allUnits.size());

            // Сортируем по атаке (по убыванию)
            allUnits.sort((u1, u2) -> Integer.compare(u2.getBaseAttack(), u1.getBaseAttack()));

            // Симулируем несколько раундов
            int round = 1;
            boolean battleEnded = false;

            while (!battleEnded && round <= 10) { // Максимум 10 раундов
                System.out.println("--- Round " + round + " ---");

                for (Unit unit : allUnits) {
                    if (!unit.isAlive()) continue;

                    try {
                        // Юнит пытается атаковать
                        if (unit.getProgram() != null) {
                            Unit target = unit.getProgram().attack();

                            if (target != null && target.isAlive() && printBattleLog != null) {
                                // Логируем атаку
                                printBattleLog.printBattleLog(unit, target);
                                System.out.println(unit.getName() + " attacks " + target.getName());
                            }
                        }

                        // Небольшая задержка для наглядности
                        if (simSpeed > 0) {
                            Thread.sleep(simSpeed);
                        } else {
                            Thread.sleep(100);
                        }

                    } catch (Exception e) {
                        System.out.println("Error in unit turn: " + e.getMessage());
                    }
                }

                // Удаляем погибших
                allUnits.removeIf(unit -> !unit.isAlive());

                // Проверяем окончание боя
                long playerAlive = allUnits.stream()
                        .filter(u -> playerArmy.getUnits() != null && playerArmy.getUnits().contains(u))
                        .count();

                long computerAlive = allUnits.stream()
                        .filter(u -> computerArmy.getUnits() != null && computerArmy.getUnits().contains(u))
                        .count();

                System.out.println("Player alive: " + playerAlive + ", Computer alive: " + computerAlive);

                if (playerAlive == 0 || computerAlive == 0) {
                    battleEnded = true;
                    System.out.println("Battle ended! Winner: " + (playerAlive > 0 ? "Player" : "Computer"));
                }

                round++;
            }

        } catch (Exception e) {
            System.out.println("Error in battle simulation: " + e);
            e.printStackTrace();
        }

        System.out.println("=== SIMULATE BATTLE FINISHED ===");
    }
}