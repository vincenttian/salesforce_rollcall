/** A wrapper class for a campaign. */

global class Event{
    
    /** Getters and Setters. */
    public String name {get;set;}
    public String start {get;set;}
    public String description {get;set;}
    public Integer checkedIn {get;set;}
    public Integer registered {get;set;}
    public Decimal maxCapacity {get;set;}
    public String titleColor {get;set;}
    public String textColor {get;set;}
    public List<String> checkedInTimes {get;set;}
    public ID cid {get;set;}
    
   
    /* Main constructor for an event that represents campaign C. */ 
    public Event (Campaign c) {
        name = c.Name;
        start = c.StartDate.format();
        description = c.description;
        Map<Id, Campaign> potential_children =
            new Map<Id, Campaign>([SELECT Name, Description, StartDate, Status, ParentId,
            Id FROM Campaign WHERE ParentId=:c.Id OR Id=:c.Id]);
        registered = [SELECT Count() FROM CampaignMember
                      WHERE CampaignId in :potential_children.keySet()];
        checkedIn = [SELECT Count() FROM CampaignMember
                     WHERE CampaignId in :potential_children.keySet() AND
                     (Status='Responded')];
        maxCapacity = c.MaxCapacity__c;
        titleColor = 'title blue';
        textColor = 'blue_text';
        cid = c.id;
        CampaignMember[] checked_in = [SELECT LastModifiedDate, Lead.Firstname,
            Lead.Lastname, Lead.Email, Contact.Firstname, Contact.Lastname,
            Contact.Email, Contact.Company__c FROM CampaignMember WHERE
            Status='Responded' AND CampaignId in :potential_children.keySet()];
        List<String> data = new List<String>();
        for (CampaignMember check_in: checked_in) {
            data.add('\'' + check_in.LastModifiedDate.format('HH:mm:ss') + '\'');
        }
        String[] arr = String[data.size()];
        checkedInTimes = data;
    }

    /** 
    *  Special constructor for an event that takes in the campaign C,
    *  to add a color that is represented by I.
    */
    public Event (Campaign c, Integer i) {
        name = c.Name;
        start = c.StartDate.format();
        description = c.description;
        Map<Id, Campaign> potential_children =
            new Map<Id, Campaign>([SELECT Name, Description, StartDate, Status, ParentId,
            Id FROM Campaign WHERE ParentId=:c.Id OR Id=:c.Id]);
        registered = [SELECT Count() FROM CampaignMember
                      WHERE CampaignId in :potential_children.keySet()];
        checkedIn = [SELECT Count() FROM CampaignMember
                     WHERE CampaignId in :potential_children.keySet() AND
                     (Status='Responded')];
        maxCapacity = c.MaxCapacity__c;
        cid = c.id;
        if (Math.mod(i, 5) == 0) {
            titleColor = 'title blue';
            textColor = 'blue_text';
        }
        if (Math.mod(i, 5) == 1) {
            titleColor = 'title magenta';
            textColor = 'magenta_text';
        }
        if (Math.mod(i, 5) == 2) {
            titleColor = 'title green';
            textColor = 'green_text';
        }
        if (Math.mod(i, 5) == 3) {
            titleColor = 'title red';
            textColor = 'red_text';
        }
        if (Math.mod(i, 5) == 4) {
            titleColor = 'title orange';
            textColor = 'orange_text';
        }
    }

}