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

    public static void check_in(String campaign_id, String email) {        
        CampaignMember[] event_attendee = [SELECT ContactID, Status, CampaignId FROM CampaignMember WHERE CampaignId=:campaign_id];// AND (Contact.email=:email or Lead.email=:email)];
        if (event_attendee.size() == 0) {
            // Should not get here
        } else {
            event_attendee[0].status = 'Responded'; // temp status to signify checked in  // NOT SETTING PICKLIST TO SPECIFIED VALUE
        }
        update event_attendee[0];
    }

    // logic to check if a campaign member needs to register or just check in
    public static void handle_parent_events(String campaign_id, String email) {
        Map<Id, Campaign> potential_children = new Map<Id, Campaign>([SELECT Name, Description, StartDate, Status, ParentId, Id FROM Campaign WHERE ParentId=:campaign_id OR Id=:campaign_id]);
        CampaignMember[] event_attendee = [SELECT Id, CampaignId FROM CampaignMember WHERE CampaignId in :potential_children.keySet() AND (Lead.Email=:email OR Contact.Email=:email)];
        if (event_attendee.size() == 0) {
            // register the attendee
            // MUST RAISE AN ERROR
        } else {
            EventController.check_in(string.valueof(campaign_id), email);
        }
    }

    // For apex: repeat    
    public CampaignMember[] getAttendees() {
        Campaign c = [SELECT Id FROM Campaign WHERE isActive=True AND Id=:event.cid];
        Map<Id, Campaign> potential_children = new Map<Id, Campaign>([SELECT Name, Description, StartDate, Status, ParentId, Id FROM Campaign WHERE ParentId=:c.id OR Id=:c.id]);
        CampaignMember[] registered = [SELECT Lead.Firstname, Lead.Lastname, Lead.Email, Contact.Firstname, Contact.Lastname, Contact.Email, Contact.Company__c FROM CampaignMember WHERE Status='Responded' AND CampaignId in :potential_children.keySet()];
        return registered;
    }

    @RemoteAction
    global static sObject[] attendee_search(ID cid, String name, Integer offset) {
        String search_name = '%' + String.escapeSingleQuotes(name) + '%';
        Campaign c = [SELECT Id FROM Campaign WHERE isActive=True AND Id=:cid];
        Map<Id, Campaign> potential_children = new Map<Id, Campaign>([SELECT Name, Description, StartDate, Status, ParentId, Id FROM Campaign WHERE ParentId=:c.id OR Id=:c.id]);
        CampaignMember[] registered = [SELECT Lead.Firstname, Lead.Lastname, Lead.Email, 
        Contact.Firstname, Contact.Lastname, Contact.Email, Contact.Company__c 
        FROM CampaignMember WHERE Status='Responded' AND
         CampaignId in :potential_children.keySet() AND (Contact.Name LIKE :search_name OR Lead.Name LIKE :search_name) ORDER BY  Id  LIMIT 50 OFFSET :offset];
        sObject[] registered2 = new sObject[]{};
        for (CampaignMember cm : registered) {
            registered2.add(cm.Contact);
            registered2.add(cm.Lead);
        }
        return registered2;
    }

    // Returns a single campaign with parameter for detail view
    @RemoteAction
    global static Campaign get_event(String event_id) {
        Campaign event = [SELECT Name, Description, StartDate FROM Campaign WHERE isActive=True AND Id=:event_id];
        return event;         
    }

    // Returns the date of a single campaign 
    @RemoteAction
    global static String get_event_date(String event_id) {
        Campaign event = [SELECT Name, Description, StartDate FROM Campaign WHERE isActive=True AND Id=:event_id];
        return event.StartDate.format();         
    }

    // Edits event info
    @RemoteAction
    global static void edit_info(String event_id, String new_description) {
        Campaign event = [SELECT Name, Description, StartDate FROM Campaign WHERE isActive=True AND Id=:event_id];
        event.Description = new_description;
        update event;
    }

    // Gives statistics on the number of attendees
    @RemoteAction
    global static Integer[] event_stats(String event_id) {
        Campaign event = [SELECT Id FROM Campaign WHERE isActive=True AND Id=:event_id];
        Map<Id, Campaign> potential_children = new Map<Id, Campaign>([SELECT Name, Description, StartDate, Status, ParentId, Id FROM Campaign WHERE ParentId=:event_id OR Id=:event_id]);
        Integer registered = [SELECT Count() FROM CampaignMember WHERE CampaignId in :potential_children.keySet()];
        Integer checked_in = [SELECT Count() FROM CampaignMember WHERE CampaignId in :potential_children.keySet() AND (Status='Responded')];
        Integer[] data = new Integer[]{registered, checked_in};
        return data;   
    } 

    // For d3    
    @RemoteAction
    global static List<DateTime> getCheckedInTimes(String event_id) {
        Campaign c = [SELECT Id FROM Campaign WHERE isActive=True AND Id=:event_id];
        Map<Id, Campaign> potential_children = new Map<Id, Campaign>([SELECT Name, Description, StartDate, Status, ParentId, Id FROM Campaign WHERE ParentId=:c.id OR Id=:c.id]);
        CampaignMember[] checked_in = [SELECT LastModifiedDate, Lead.Firstname, Lead.Lastname, Lead.Email, Contact.Firstname, Contact.Lastname, Contact.Email, Contact.Company__c FROM CampaignMember WHERE Status='Responded' AND CampaignId in :potential_children.keySet() ORDER BY LastModifiedDate ASC];
        List<DateTime> data = new List<DateTime>();
        for (CampaignMember check_in: checked_in) {
            data.add(check_in.LastModifiedDate);
        }
        return data;
    }

}
