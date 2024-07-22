package facilities.buildings;

import facilities.Facility;

public class Theatre extends Facility implements Building {

  private int level;

  private final int maxLevel = 6;


  public Theatre(String name){
    super(name);
    this.level = 1;
  }


  public int getLevel(){
    return level;
  }

  public void increaseLevel(){
    if(level != maxLevel) level++;
  }

  public int getUpgradeCost() {
    int upgradeCost;
    int baseBuildingCost = 200;
    if(level != maxLevel) upgradeCost = baseBuildingCost * (level + 1);
    else upgradeCost = -1;
    return upgradeCost;
  }

  public int getCapacity() {
    int baseBuildingCapacity = 10;
    int buildingCapacity;
    if(level!=1){
      buildingCapacity = (int) (baseBuildingCapacity * Math.pow(2, level-1));
    }
    else{
      buildingCapacity = baseBuildingCapacity;
    }

    return buildingCapacity;
  }


  public int getMaxLevel(){
    return maxLevel;
  }
}
