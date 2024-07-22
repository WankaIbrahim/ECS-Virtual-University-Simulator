package university;

import facilities.*;
import facilities.buildings.*;
import java.util.*;

public class Estate {

  private final ArrayList<Facility> facilities = new ArrayList<>();


  public Facility[] getFacilities() {
    return facilities.toArray(new Facility[0]);
  }

  public Facility addFacility(String type, String name) {
    if (type.equalsIgnoreCase("hall")) {
      Facility myHall = new Hall(name);
      facilities.add(myHall);
      return myHall;
    } else if (type.equalsIgnoreCase("lab")) {
      Facility myLab = new Lab(name);
      facilities.add(myLab);
      return myLab;
    } else if (type.equalsIgnoreCase("theatre")) {
      Facility myTheatre = new Theatre(name);
      facilities.add(myTheatre);
      return myTheatre;
    } else {
      return null;
    }
  }

  public float getMaintenanceCost() {

    float annualMaintenanceCost = 0.0f;

    for (Facility facility : getFacilities()) {

      if (facility instanceof Building myBuilding) {
        float annualBuildingCost = myBuilding.getCapacity() * 0.1f;
        annualMaintenanceCost += annualBuildingCost;
      }
    }

    return annualMaintenanceCost;
  }

  public int getNumberOfStudents() {

    int totalHallsCapacity = 0;
    int totalLabsCapacity = 0;
    int totalTheatresCapacity = 0;

    for (Facility facility : getFacilities()) {
      if (facility instanceof Hall) {
        totalHallsCapacity += ((Hall) facility).getCapacity();
      } else if (facility instanceof Lab) {
        totalLabsCapacity += ((Lab) facility).getCapacity();
      } else if (facility instanceof Theatre) {
        totalTheatresCapacity += ((Theatre) facility).getCapacity();
      }
    }

    return Math.min(totalHallsCapacity, Math.min(totalLabsCapacity, totalTheatresCapacity));
  }
}
