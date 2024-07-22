package university;

import java.util.HashMap;
import java.util.Iterator;

public class HumanResource {
  private final HashMap<Staff, Float> staffSalary = new HashMap<>();

  public void addStaff(Staff staff) {
    float salary =
        (float)
            (staff.getSkill() * 0.095
                + Math.random() * (staff.getSkill() * 0.105 - staff.getSkill() * 0.095));
    staffSalary.put(staff, salary);
  }

  public Iterator<Staff> getStaff() {
    return staffSalary.keySet().iterator();
  }

  public float getTotalSalary() {
    float totalSalary = 0;
    Iterator<Staff> itr = getStaff();

    while (itr.hasNext()) {
      totalSalary += staffSalary.get(itr.next());
    }

    return totalSalary;
  }


  public int getStaffSize(){
    return staffSalary.size();
  }
 }
