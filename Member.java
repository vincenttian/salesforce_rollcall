global class Member{
    
    public String name {get;set;}
    public String company {get;set;}
    public String email {get;set;}
    public boolean status {get;set;}
    
   
    /** Main constructor for an event. */ 
    public Member (String memberName, String memberCompany, String memberEmail, boolean checkedIn) {
        name = memberName;
        company = memberCompany;
        email = memberEmail;
        status = checkedIn;
    }

}