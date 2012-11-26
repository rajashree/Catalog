import java.util.HashMap;
import java.util.Map;

public class User {
  private String name;
  private int age;
  private boolean male;
  private Map<String, Address> addresses = new HashMap<String, Address>();
  private CustomModelFreeMarkerServlet.StringSequence stringSequence;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public boolean isMale() {
    return male;
  }

  public void setMale(boolean male) {
    this.male = male;
  }

  public Map<String, Address> getAddresses() {
    return addresses;
  }

  public void setAddresses(Map<String, Address> addresses) {
    this.addresses = addresses;
  }

  public CustomModelFreeMarkerServlet.StringSequence getStringSequence() {
    return stringSequence;
  }

  public void setStringSequence(CustomModelFreeMarkerServlet.StringSequence stringSequence) {
    this.stringSequence = stringSequence;
  }
}
