public class Complaint {
	//instance variables
    private String id;
    private String description;
    private String cookID;
   
    public Complaint(){
    	id = "";
        description = "";
        cookID = "";
    }
  
    public Complaint(String idNew, String descriptionNew, String cookIDNew) {
        id = idNew;
        description = descriptionNew;
        cookID = cookIDNew;
    }
  
    public String getId() {
        return id;
    }

    public void setId(String idNew) {
        id = idNew;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String descriptionNew) {
        description = descriptionNew;
    }

    public String getCookID() {
        return cookID;
    }

    public void setCookID(String cookIDNew) {
        cookID = cookIDNew;
    }

    public String toString(){
        return "Cook ID: " + cookID + "\n" + "Description: " + description+"\nID: "+id;
    }
}
