package university;
import facilities.*;
import facilities.buildings.*;
import java.util.*;

public class University {

  private int reputation;
  private float budget;
  private final Estate estate = new Estate();
  private final HumanResource humanResource = new HumanResource();


  public University(int funding){
    this.budget = funding;
  }


  public Facility build(String type, String name){
    Facility newFacility = estate.addFacility(type, name);
    if (newFacility instanceof Hall newHall) {
      budget -= 100;
      reputation += 100;
      return newHall;
    } else if (newFacility instanceof Lab newLab) {
      budget -= 300;
      reputation += 100;
      return newLab;
    } else if (newFacility instanceof Theatre newTheatre) {
      budget -= 200;
      reputation += 100;
      return newTheatre;
    }
    return null;
  }

  public void upgrade(Building building) throws Exception{
    if(!Arrays.asList(estate.getFacilities()).contains(building)){
      throw new Exception("This building cannot be upgraded as it is not currently part of the university");
    }

    else if(building.getLevel() == building.getMaxLevel()){
      throw new Exception("This building cannot be upgraded as it is already at the maximum level.");
    }

    else {
      budget -= building.getUpgradeCost();
      building.increaseLevel();
      reputation += 50;
    }
  }

  public float getBudget(){
    return budget;
  }

  public int getReputation(){
    return reputation;
  }


  public void addBudget(float budget) {
    this.budget += budget;
  }

  public void subBudget(float budget) {
    this.budget -= budget;
  }

  public void subReputation(int reputation) {
    this.reputation -= reputation;
  }


  public Estate getEstate() {
    return estate;
  }

  public HumanResource getHumanResource() {
    return humanResource;
  }


  public int getNumberOfStudents(){
    return estate.getNumberOfStudents();
  }

  public int getNumberOfStaff(){
    return humanResource.getStaffSize();
  }

  public Iterator<Staff> getStaff(){
    return  humanResource.getStaff();
  }


  /*
  This method below converts the array of facilities to an array of building.
  Used in the EcsSim when upgrading the buildings.
   */
  public Building[] getBuildings(){
    ArrayList<Building> buildingArrayList = new ArrayList<>();
    for(Facility facility: estate.getFacilities()){
      if(facility instanceof Building building){
        buildingArrayList.add(building);
      }
    }
    Building[] array = buildingArrayList.toArray(new Building[buildingArrayList.size()]);
    return array;

  }

  /*
  The method below return a number
  1 for hall.
  2 for lab.
  3 for theatre.

  This is based on which facility has the least capacity. This is then used in  the EcsSim to determine  which facility to build.
  THis allows for exponential growth in students, therefore maximising the universities profits.
   */
  public int getNeededFacility(){
    int hallCount = 0;
    int labCount = 0;
    int theatreCount = 0;

    for(Building building: this.getBuildings()){
      if (building instanceof Hall){
        hallCount += building.getCapacity();
      }
      if (building instanceof Lab){
        labCount += building.getCapacity();
      }
      if (building instanceof Theatre){
        theatreCount += building.getCapacity();
      }
    }

    int i = Math.min(hallCount, Math.min(labCount, theatreCount));

    int j = 0;

    if(hallCount == i){
      j = 1;
    }else if(theatreCount == i){
      j = 3;
    }else if(labCount == i){
      j = 2;
    }


    return j;
  }
}
