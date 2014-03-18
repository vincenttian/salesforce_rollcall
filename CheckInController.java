/**
*
*Roll Call application 
*Controller for Checkin page
*@authors Howard Chen and Vincent Tian
*
*/

global class CheckInController{

    public Event event{get; set;} 

    public CheckInController() {
        Campaign c = [SELECT Id, Name, Description, StartDate, MaxCapacity__c FROM Campaign WHERE Id=:ApexPages.currentPage().getParameters().get('event_id')];
        event = new Event(c);
    }

    public static void register_event_attendee(String email, String first_name, String last_name, String company, String campaign_id) { 
        Contact tmp_contact = new Contact(Email=email, FirstName=first_name, LastName=last_name, Company__c=company);
        insert tmp_contact;
        CampaignMember new_event_attendee = new CampaignMember(ContactId=tmp_contact.id, CampaignId=campaign_id, Status = Event.checkedInStatus); 
        insert new_event_attendee;
    }

    public static void update_event_attendee(CampaignMember member, String email, String first_name, String last_name, String company) { 

        if (member.ContactId == null) {
            Lead updated_lead = [SELECT firstname, lastname, Company FROM Lead WHERE Id=:member.LeadId];
            updated_lead.company = company;
            updated_lead.firstname = first_name;
            updated_lead.lastname = last_name;
            update updated_lead;
        } else {
            Contact updated_contact = [SELECT firstname, lastname, Company__c FROM Contact WHERE Id=:member.ContactId];
            updated_contact.Company__c = company;
            updated_contact.firstname = first_name;
            updated_contact.lastname = last_name;
            update updated_contact;
        }
        check_in(member);
    }


    public static void check_in(CampaignMember attendee) {        
        attendee.status = Event.checkedInStatus; 
        update attendee;
    }

    // logic to check if a campaign member needs to register or just check in
    public static CampaignMember[] handle_parent_events(String campaign_id, String email) {
        Map<Id, Campaign> potential_children = new Map<Id, Campaign>([SELECT Name, Description, StartDate, Status, ParentId, Id FROM Campaign WHERE ParentId=:campaign_id OR Id=:campaign_id]);
        CampaignMember[] event_attendee = [SELECT Id, CampaignId, ContactId, LeadId, Lead.Name, Lead.FirstName, Lead.LastName, Contact.Name, Contact.FirstName, Contact.LastName, Lead.Email, Contact.Email, Lead.Company, Contact.Company__c 
                                           FROM CampaignMember WHERE CampaignId in :potential_children.keySet() AND 
                                           (Lead.Email=:email OR Contact.Email=:email)];
        
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
        event_attendee.status = 'Responded'; 
        update event_attendee;
    }

    // Checking in attendees for checkin page
    @RemoteAction
    global static void update_attendee(String event_id, String first_name, String last_name, String email, String company) {
        CampaignMember[] cm = [SELECT Status, ContactId, LeadId FROM CampaignMember WHERE CampaignId=:event_Id AND (Contact.Email=:email or Lead.Email=:email)];
        if (cm.size() == 0) {
            // register attendee
            register_event_attendee(email, first_name, last_name, company, event_id);
        } else {
            update_event_attendee(cm[0], email, first_name, last_name, company);
        }
    }

}