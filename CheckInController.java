/**
*
*Roll Call application
*Controller for Checkin page
*@authors Howard Chen and Vincent Tian
*
*/

global with sharing class CheckInController{


    public Event event{get; set;}

    public CheckInController() {
        Campaign c = [SELECT Id, Name, Description, StartDate, MaxCapacity__c FROM Campaign 
                      WHERE Id=:ApexPages.currentPage().getParameters().get('event_id')];
        if (c != null) {
            event = new Event(c);
        } else {
            event = null;
        }
    }

    public static void register_event_attendee(String campaign_id, Contact attendee ) {
        insert attendee;
        CampaignMember new_event_attendee = new CampaignMember(ContactId=attendee.id, 
                                                               CampaignId=campaign_id, 
                                                               Status = Event.checkedInStatus);
        insert new_event_attendee;
    }

    public static void check_in(CampaignMember attendee) {
        attendee.status = Event.checkedInStatus;
        update attendee;
    }

    // logic to check if a campaign member needs to register or just check in
    public static CampaignMember[] handle_parent_events(String campaign_id, String email) {
        Map<Id, Campaign> potential_children = new Map<Id, Campaign>([SELECT Name, Description, StartDate, Status,
                                                                     ParentId, Id, MaxCapacity__c FROM Campaign 
                                                                     WHERE ParentId=:campaign_id OR Id=:campaign_id]);
        Campaign pcampaign = potential_children.get(campaign_id);
        Decimal capacity = pcampaign.MaxCapacity__c;
        if (capacity != null) { // There is a max capacity
            Integer checkedIn = [SELECT Count() FROM CampaignMember
                     WHERE CampaignId in :potential_children.keySet() AND
                     (Status=:Event.checkedInStatus)];
            Integer capacity_int = pcampaign.MaxCapacity__c.intValue();
            if (checkedIn >= capacity_int) {
                throw new CapacityException('Event already at max capacity');
            } 
        }

        String soql = 'SELECT CampaignId, ContactId, LeadId, Lead.Name, Contact.Name, Lead.Email, Contact.Email, ';
        for(Schema.FieldSetMember f : SObjectType.Contact.FieldSets.RollCall.getFields()) {
            if (f.getFieldPath() != 'Name' && f.getFieldPath() != 'Email')
            soql += 'Contact.'+f.getFieldPath() + ', ';
        }

        for(Schema.FieldSetMember f : SObjectType.Lead.FieldSets.RollCall.getFields()) {
            if (f.getFieldPath() != 'Name' && f.getFieldPath() != 'Email')
            soql += 'Lead.'+f.getFieldPath() + ', ';
        } 

        String campaignIds = '';
        for (String id : potential_children.keySet()){
            campaignIds += ',\'' +id + '\'';
        }

        soql+= ' Id FROM CampaignMember WHERE CampaignId in ('+campaignIds.substring(1)+ 
               ') AND (Lead.Email=\'' +email+'\' OR Contact.Email=\''+email+'\')';    

        CampaignMember[] event_attendee = Database.query(soql);   

        if (event_attendee.size() == 1) {            
            check_in(event_attendee[0]);
        }
        return event_attendee;
             
    }

    // Checking in attendees for checkin page
    @RemoteAction
    global static CampaignMember[] check_in_attendee(String event_id, String email) {
        return handle_parent_events(event_id, email);
    }

    // Checking in attendees for checkin page
    @RemoteAction
    global static void check_in_multiple(String CampaignMemberId) {
        // login person that has specific attributes above
        CampaignMember event_attendee = [SELECT status FROM CampaignMember WHERE id = :CampaignMemberId];
        event_attendee.status = Event.checkedInStatus;
        update event_attendee;
    }

    // Checking in attendees for checkin page
    @RemoteAction
    global static void update_attendee(String event_id, SObject attendee) {
        if (attendee.Id == null){
            register_event_attendee(event_id, (Contact)attendee);
        }else{
            CampaignMember[] cm = [ SELECT Status, ContactId, LeadId FROM CampaignMember 
                                    WHERE CampaignId=:event_Id AND (ContactId=:attendee.Id or 
                                    LeadId=:attendee.Id)];
            update attendee;
            check_in(cm[0]);
        }
    }

    public class CapacityException extends Exception {}

}