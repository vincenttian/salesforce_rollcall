global class Member implements Comparable{
    
    public String id {get;set;}
    public String name {get;set;}
    public String company {get;set;}
    public String email {get;set;}
    public boolean status {get;set;}
    
   
    /** Main constructor for an event. */ 
    public Member (String recId, String memberName, String memberCompany, String memberEmail, boolean checkedIn) {
        id = recId;
        name = memberName;
        company = memberCompany;
        email = memberEmail;
        status = checkedIn;
    }

    global Integer compareTo(Object comp) 
    {
        Member m = (Member) comp;
        String n = m.name.toUpperCase();
        return name.toUpperCase().compareTo(n); 
    }

}