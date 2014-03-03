global class Event{
    
    public String name {get;set;}
    public String start {get;set;}
    public String description {get;set;}
    public Integer checkedIn {get;set;}
    public Integer registered {get;set;}
    public Decimal maxCapacity {get;set;}
    
    
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
        
    }

}