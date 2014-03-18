/**
*
*Roll Call application 
*Controller for Event page
*@authors Howard Chen and Vincent Tian
*
*/

global class EventController {

    public Event event{get; set;} 

    public EventController() {
        Campaign c = [SELECT Id, Name, Description, StartDate, MaxCapacity__c FROM Campaign WHERE Id=:ApexPages.currentPage().getParameters().get('event_id')];
        event = new Event(c);
    }

    @RemoteAction
    global static sObject[] attendee_search(ID cid, String name, Integer offset) {
        String search_name = '%' + String.escapeSingleQuotes(name) + '%';
        Campaign c = [SELECT Id FROM Campaign WHERE Id=:cid];
        Map<Id, Campaign> potential_children = new Map<Id, Campaign>([SELECT Name, Description, StartDate, Status, ParentId, Id FROM Campaign WHERE ParentId=:c.id OR Id=:c.id]);
        CampaignMember[] registered = [SELECT Lead.Name, Lead.Email, 
        Contact.Name, Contact.Email, Contact.Company__c, LeadID, ContactID
        FROM CampaignMember WHERE Status=:Event.checkedInStatus AND
             CampaignId in :potential_children.keySet() AND 
             (Contact.Name LIKE :search_name OR Lead.Name LIKE :search_name) ORDER BY  LastModifiedDate DESC LIMIT 50 OFFSET :offset];
        sObject[] registered2 = new sObject[]{};
        for (CampaignMember cm : registered) {
            if (cm.ContactID != null) { 
                registered2.add(cm.Contact);
            } else {
                registered2.add(cm.Lead);
            }
        }
        return registered2;
    }

    // For d3    
    @RemoteAction
    global static List<DateTime> get_checkedin_times(String event_id) {
        Campaign c = [SELECT Id FROM Campaign WHERE Id=:event_id];
        Map<Id, Campaign> potential_children = new Map<Id, Campaign>([SELECT Name, Description, StartDate, Status, ParentId, Id FROM Campaign WHERE ParentId=:c.id OR Id=:c.id]);
        CampaignMember[] checked_in = [SELECT LastModifiedDate, Lead.Firstname, Lead.Lastname, Lead.Email, Contact.Firstname, Contact.Lastname, Contact.Email, Contact.Company__c FROM CampaignMember WHERE Status='Responded' AND CampaignId in :potential_children.keySet() ORDER BY LastModifiedDate ASC];
        List<DateTime> data = new List<DateTime>();
        for (CampaignMember check_in: checked_in) {
            data.add(check_in.LastModifiedDate);
        }
        return data;
    }

    // Updates info for charts
    @RemoteAction
    global static Event update_stats(String event_id) {
        Campaign c = [SELECT Name, StartDate, Description, Id, MaxCapacity__c, LastModifiedDate FROM Campaign WHERE Id=:event_id];
        Event e = new Event(c);
        return e;
    }

}