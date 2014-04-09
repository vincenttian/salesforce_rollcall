/**
*
*Roll Call application 
*Controller for Event page
*@authors Howard Chen and Vincent Tian
*
*/

global with sharing class EventController {

    public Event event{get; set;} 

    public EventController() {
        Campaign[] c = [SELECT Id, Name, Description, StartDate, MaxCapacity__c FROM Campaign 
                      WHERE Id=:ApexPages.currentPage().getParameters().get('event_id')];
        if (c.size() != 0) {
            event = new Event(c[0]);
        }
    }

    @RemoteAction
    global static Member[] attendee_search(ID cid, String name, Integer offset) {
        if (Schema.sObjectType.Contact.fields.Email.isAccessible()) {
            String search_name = '%' + String.escapeSingleQuotes(name) + '%';
            Map<Id, Campaign> potential_children = new Map<Id, Campaign>([SELECT Name, Description, StartDate, Status, 
                                                                          ParentId, Id FROM Campaign WHERE ParentId=:cid OR 
                                                                          Id=:cid]);
            CampaignMember[] registered = [SELECT Lead.Name, Lead.Email, 
                                            Contact.Name, Contact.Email, Contact.Company__c, Lead.Company, LeadID, ContactID, Status
                                            FROM CampaignMember WHERE 
                                            (Status=:Event.checkedInStatus OR Status=:Event.registeredStatus) AND
                                            CampaignId in :potential_children.keySet() AND 
                                            (Contact.Name LIKE :search_name OR Lead.Name LIKE :search_name 
                                            OR Contact.Company__c LIKE :search_name OR Lead.Company LIKE :search_name)
                                            ORDER BY Attendee_Name__c ASC LIMIT 50 OFFSET :offset];
            Member[] registered2 = new Member[]{};
            for (CampaignMember cm : registered) {
                if (cm.ContactID != null) { 
                    Member m = new Member(cm.Contact.name, cm.Contact.Company__c, cm.Contact.Email, false);
                    if (cm.Status == Event.checkedInStatus) {
                        m.Status = true;
                    }
                    registered2.add(m);
                } else {
                    Member m = new Member(cm.Lead.name, cm.Lead.Company, cm.Lead.Email, false);
                    if (cm.Status == Event.checkedInStatus) {
                        m.Status = true;
                    }
                    registered2.add(m);
                }
            }
            return registered2;
        } else {
            throw new CheckInController.ProfilePermissionException('Profile does not have read permission');
        }
    }

    // For d3    
    @RemoteAction
    global static List<DateTime> get_checkedin_times(String event_id) {
        Map<Id, Campaign> potential_children = new Map<Id, Campaign>([SELECT Name, Description, StartDate, Status, ParentId, Id FROM Campaign WHERE ParentId=:event_id OR Id=:event_id]);
        CampaignMember[] checked_in = [SELECT LastModifiedDate, Lead.Firstname, Lead.Lastname, Lead.Email, 
                                      Contact.Firstname, Contact.Lastname, Contact.Email, Contact.Company__c 
                                      FROM CampaignMember WHERE Status=:Event.checkedInStatus AND 
                                      CampaignId in :potential_children.keySet() ORDER BY LastModifiedDate ASC];
        List<DateTime> data = new List<DateTime>();
        for (CampaignMember check_in: checked_in) {
            data.add(check_in.LastModifiedDate);
        }
        return data;
    }

    // Updates info for charts
    @RemoteAction
    global static Event update_stats(String event_id) {
        Campaign c = [SELECT Name, StartDate, Description, Id, MaxCapacity__c, LastModifiedDate 
                      FROM Campaign WHERE Id=:event_id];
        Event e = new Event(c);
        return e;
    }

}