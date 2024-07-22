package university;

public class Staff {
  private final String name;
  private int skill;
  private int yearsOFTeaching = 0;
  private int stamina = 100;


  public Staff(String _name, int _skill){
    name = _name;
    skill = _skill;
  }


  public int instruct(int numberOfStudents){
    if(skill<100){
      skill++;
    }

    stamina = (int) (stamina - Math.ceil((double) numberOfStudents /(20+ skill)) * 20);

    return (100*skill)/(100+numberOfStudents);
  }

  public void replenishStamina(){
    if(stamina>80){
      stamina = 100;
    }else{
      stamina = stamina+20;
    }
  }

  public void increaseYearsOfTeaching(){
    yearsOFTeaching++;
  }


  public String getName(){
    return name;
  }

  public int getSkill(){
    return skill;
  }

  public int getYearsOfTeaching (){
    return yearsOFTeaching;
  }

  public int getStamina() {
    return stamina;
  }
}
