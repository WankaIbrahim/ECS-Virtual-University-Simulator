import facilities.buildings.*;
import java.io.*;
import java.util.*;
import university.*;
import university.University;

public class EcsSim {
  private final University university;
  private final ArrayList<Staff> staffMarket = new ArrayList<>();
  ArrayList<String> hallNames = new ArrayList<>();
  ArrayList<String> labNames = new ArrayList<>();
  ArrayList<String> theatreNames = new ArrayList<>();

  /*
  The constructor for the EcsSim.

  Takes the name of the staff configuration file and the universities budget as parameters.
  Fills the staff market using the staff configuration file, creates a university using the initial funding and fills in the arraylists for facility names.
   */
  public EcsSim(String staffFile, int initialFunding){
    fillStaffMarket(staffFile);
    university = new University(initialFunding);
    filNames();
  }

  /*
  This method handles the simulation of the university.
   */
  public void simulate(){
    construction();

    collectTuition();

    hireStaff();

    instructStudents();

    pay();

    increaseYearsOfTeaching();

    deductReputation();

    staffLeaving();

    replenishStamina();
  }

  /*
  Handles simulation for multiple years.
   */
  private void simulate(int years){
    for(int i = 0; i < years; i++){
      simulate();
      System.out.println("End of year " + (i+1) + "\n\n");
    }
  }

  /*
  This method handles the filling of the staff market from the staff configuration file (Staff.txt)

  It creates a buffered reader which reads a line and assigns it to the String line.
  The line is then checked to ensure it is not null.
  The line is then split using the regex "\\(" and added to an array of type String. THis ensures the line is split into the name and skill respectively.
  The first element of the array is the trimmed to remove whitespace and assigned to a String variable name.
  The second element of the array is then parsed, trimmed and all non-digit characters are removed and assigned to an int skill.
  A staff object is then created using the name and skill as parameters.
  The staff is then added to the staff market.

   */
  private void fillStaffMarket(String staffFile){

    try (BufferedReader reader = new BufferedReader(new FileReader(staffFile))) {

      String line;

      while((line = reader.readLine()) != null){

        String[] staffArray = line.split("\\(");

        String name = staffArray[0].trim();
        int skill = Integer.parseInt(staffArray[1].replaceAll("[^0-9]", "").trim());

        Staff staff = new Staff(name, skill);

        staffMarket.add(staff);
      }
    } catch (IOException | NumberFormatException e) {
      System.err.println("Error reading staff configuration file: " + e.getMessage() + "\n");
    }
  }

  /*
  The method below fills three arraylists with names of facilities.

  The arraylists are then used by the builder methods (buildHall, buildLab and buildTheatre) to create buildings with distinct names
   */
  private void filNames(){
    for (int i = 1; i <= 200; i++) {
      hallNames.add("Hall " + i);
    }

    for (int i = 1; i <= 200; i++) {
      labNames.add("Lab " + i);
    }

    for (int i = 1; i <= 200; i++) {
      theatreNames.add("Theatre " + i);
    }
  }

  /*
  This method handles the replenishment of staff stamina.   */
  private void replenishStamina() {
    Iterator<Staff> itr = university.getStaff();
    int i = 0;

    while (itr.hasNext()) { // iterate over all the staff and replenish their stamina
      Staff staff = itr.next();
      staff.replenishStamina();
      i++;
    }

    System.out.println("Stamina replenished for " + i + " staff.\n");
  }

  /*
  This method handles the leaving of staff from the university.

  If the staff's years of teaching is equal to 30 they will leave the university.
  Additionally, if a random number between 0 and 100 is greater than the staff's stamina they will leave the university
   */
  private void staffLeaving() {
    Iterator<Staff> itr = university.getStaff();
    Random random = new Random();
    int i = 0;
    int j = 0;

    while (itr.hasNext()) {
      j++;
      Staff staff = itr.next();
      if (staff.getYearsOfTeaching() >= 30 ||
          random.nextInt(100)>staff.getStamina()){
        itr.remove();
        i++;
      }
    }
    if (i == 1) System.out.println(i + " staff has left the university. New number of staff is: " + (j-i) + "\n");
    else System.out.println(i + " staff have left the university. New number of staff is: " + (j-i) +"\n");
  }

  /*
  This method handles the deduction of the universities reputation based on the number of uninstructed students.

  Due to the effectiveness of the instruct students method, no students are uninstructed.
   */
  private void deductReputation() {
    int studentsPerStaff = 0;

    studentsPerStaff = (int) Math.ceil( (double) university.getNumberOfStudents() / university.getNumberOfStaff());

    int instructedStudents = university.getNumberOfStaff() * studentsPerStaff;

    int uninstructedStudents = university.getNumberOfStudents() - instructedStudents;

    if(university.getNumberOfStudents()>instructedStudents) {
      university.subReputation(uninstructedStudents);

      System.out.println(uninstructedStudents + " uninstructed students. Reputation has been deducted accordingly.\n");
    } else System.out.println("No uninstructed students. Reputation has not been deducted.\n");
  }

  /*
  This method handles the increment of the staff years of teaching
   */
  private void increaseYearsOfTeaching() {
    Iterator<Staff> itr = university.getStaff();
    while (itr.hasNext()) itr.next().increaseYearsOfTeaching();
  }

  /*
  This method handles the universities expenditure.

  It adds up the maintenance costs for the facilities and the staff salaries.
  It then subtracts that from the universities budget.
   */
  private void pay() {
    float netSpend = 0;
    netSpend += university.getEstate().getMaintenanceCost();
    netSpend += university.getHumanResource().getTotalSalary();
    if(university.getBudget()>netSpend) {
      university.subBudget(netSpend);

      System.out.println("A total of " + netSpend + " has been paid. Budget left: " + university.getBudget()+"\n");
    } else System.out.println("The university is unable to pay the maintenance costs and staff salaries.\n");
  }

  /*
  The following methode handles the assignment of students to staff to be instructed.

  The number of students is first divided by the number of teachers.
  The remainder is then appropriately assigned to the staff.
  This ensures that no staff has to instruct an unfair amount of students.
   */
  private void instructStudents() {
    int studentsPerStaff;
    int totalStudents = university.getNumberOfStudents();
    int totalStaff = university.getNumberOfStaff();

    studentsPerStaff = totalStudents / totalStaff;

    int remainingStudents = totalStudents % totalStaff;

    Iterator<Staff> itr = university.getStaff();

    while (itr.hasNext()) {
      Staff staff = itr.next();
      int assignedStudents = studentsPerStaff;

      if (remainingStudents > 0) {
        assignedStudents++;
        remainingStudents--;
      }

      staff.instruct(assignedStudents);
    }

    System.out.println("Staff assignment over. " + university.getNumberOfStudents() + " students have been assigned to "+university.getNumberOfStaff()+" staff.\n");
  }

  /*
  The method below handles the hiring of staff in the university.

  It aims to maintain a steady number of staff which is 5.
  It does this by compensating for the staff which have left the university and hiring the amount that have left.
  However, when the ration of students to staff in the university exceeds 20 it then aims to maintain at least 10 staff rather than 5.
   */
  private void hireStaff() {
    if (university.getNumberOfStaff() < 10) {
      if (university.getNumberOfStaff() == 0) {
        for (int i = 0; i < 5; i++) {
          university.getHumanResource().addStaff(staffMarket.get(0));
          staffMarket.remove(0);
        }
      } else if (university.getNumberOfStaff() < 5) {
        int j = 5 - university.getNumberOfStaff();

        for (int i = 0; i < j; i++) {
          university.getHumanResource().addStaff(staffMarket.get(0));
          staffMarket.remove(0);
        }
      } else if (university.getNumberOfStudents() / university.getNumberOfStaff() >= 20) {
        int j = 10 - university.getNumberOfStaff();
        for (int i = 0; i < j; i++) {
          university.getHumanResource().addStaff(staffMarket.get(0));
          staffMarket.remove(0);
        }
      }
    }else{
      if (university.getNumberOfStaff() >= 10) {
        int j = 10 - university.getNumberOfStaff();
        for (int i = 0; i < j; i++) {
          university.getHumanResource().addStaff(staffMarket.get(0));
          staffMarket.remove(0);
        }
      }
    }
  }

  /*
  The method below handles the collection of each students tribute towards the universities budget.
   */
  private void collectTuition() {
    int i = university.getNumberOfStudents()*10;
    university.addBudget(i);

    System.out.println("Tuition of " + university.getNumberOfStudents()*10 + " collected from " + university.getEstate().getNumberOfStudents() +  " students. Budget left: " + university.getBudget()+"\n");
  }

  /*
  The following 3 methods below each create their respective facility.

  The name of the facility to be created is randomly gotten from 3 arraylists which store multiple names.
   */
  private void buildHall(){
    Random random = new Random();
    if(!hallNames.isEmpty()){
      int index = random.nextInt(hallNames.size());
      university.build("hall" , hallNames.get(index));
      hallNames.remove(index);
    }else{
      System.out.println("Ran of of names for halls.\n");
    }
  }
  private void buildLab(){
    Random random = new Random();
    if(!labNames.isEmpty()){
      int index = random.nextInt(labNames.size());
      university.build("lab" , labNames.get(index));
      labNames.remove(index);
    }else{
      System.out.println("Ran out of names for labs.\n");
    }
  }
  private void buildTheatre(){
    Random random = new Random();
    if(!theatreNames.isEmpty()){
      int index = random.nextInt(theatreNames.size());
      university.build("theatre" , theatreNames.get(index));
      theatreNames.remove(index);
    }else{
      System.out.println("Ran out of names for theatres.\n");
    }
  }

  /*
  This method handles the building and upgrading of facilities within the EcsSim

  When the university has a sizable budget over greater than or equal to 1400, a hall, lab and theatre are constructed.
  Else the getNeededFacility method from the university class is used to determine which facility is needed the most.
  It is then constructed

  The for loop goes through the facilities in the EcsSim and systematically upgrades them if the university can afford to do so.
  It aims to achieve as many fully upgraded facilities as possible.
   */
  private void construction(){
      if(university.getBudget() >= 1400){
        buildHall();
        buildLab();
        buildTheatre();

        System.out.println("One of each building has been constructed. Budget left: " + university.getBudget()+"\n");
      }else if(university.getBudget()>400){
        if(university.getNeededFacility() == 1 && university.getBudget()>200) {
          buildHall();
          buildHall();
          System.out.println("Two halls constructed. Budget left: " + university.getBudget()+"\n");
        }
        else if(university.getNeededFacility() == 2 && university.getBudget()>600) {
          buildLab();
          buildLab();
          System.out.println("Two labs constructed. Budget left: " + university.getBudget()+"\n");
        }
        else if(university.getNeededFacility() == 3 && university.getBudget()>400) {
          buildTheatre();
          buildTheatre();
          System.out.println("Two theatres constructed. Budget left: " + university.getBudget()+"\n");
        }
      }else if(university.getBudget()>100){
        if(university.getNeededFacility() == 1 && university.getBudget()>100) {
          buildHall();
          System.out.println("Halls constructed. Budget left: " + university.getBudget()+"\n");
        }
        else if(university.getNeededFacility() == 2 && university.getBudget()>300) {
          buildLab();
          System.out.println("Labs constructed. Budget left: " + university.getBudget()+"\n");
      } else if (university.getNeededFacility() == 3 && university.getBudget() > 200) {
        buildTheatre();
        System.out.println("Theatres constructed. Budget left: " + university.getBudget() + "\n");
        }
      }else{
      System.out.println("Insufficient funds. No construction has been carried out.\n");
    }

    int i = 0;
    for (Building building : university.getBuildings()){
      if( building.getLevel()<building.getMaxLevel()&&
          building.getUpgradeCost()<university.getBudget()&&
          university.getBudget()>800){
        try {
          university.upgrade(building);
          i++;
        } catch (Exception e) {
          System.err.println("Unable to upgrade building." + e.getMessage() + "\n");
        }
      }
    }
    System.out.println(i + " upgrades done. Budget left: " + university.getBudget() + "\n");
  }


  public static void main(String[] args){
    if(args.length!=3){
      System.out.println("Incorrect configuration format. Expected format: Name of staff configuration file; Initial budget for the university; Years of simulation.\n");
      System.out.println("Recommended config 'staff.txt 20000 20'");
      return;
    }


    String staffConfigFile = args[0];
    int initialFunding = Integer.parseInt(args[1]);
    int simulationYears = Integer.parseInt(args[2]);

    EcsSim ecsSim = new EcsSim(staffConfigFile, initialFunding);
    ecsSim.simulate(simulationYears);
  }
}
