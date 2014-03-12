/**
*
*Roll Call application 
*Controller for Checkin page
*@authors Howard Chen and Vincent Tian
*
*/

global class CheckInController{

    public Campaign event {get;set;}

    public CheckInController() {
        event = [SELECT Id, Name, Description, StartDate FROM Campaign WHERE Id=:ApexPages.currentPage().getParameters().get('event_id')];
    }

    public static void register_event_attendee(String email, String first_name, String last_name, String company, String campaign_id) { 
        Contact tmp_contact = new Contact(Email=email, FirstName=first_name, LastName=last_name, Company__c=company);
        insert tmp_contact;
        CampaignMember new_event_attendee = new CampaignMember(ContactId=tmp_contact.id, CampaignId=campaign_id, Status = 'Responded'); 
        insert new_event_attendee;
    }

    public static void update_event_attendee(String email, String first_name, String last_name, String company, String campaign_id) { 
        CampaignMember member = [SELECT ContactId FROM CampaignMember WHERE CampaignId=:campaign_id AND (Contact.Email=:email or Lead.Email=:email)];
        Contact updated_contact = [SELECT firstname, lastname, Company__c FROM Contact WHERE Id=:member.ContactId];
        updated_contact.firstname = first_name;
        updated_contact.lastname = last_name;
        updated_contact.Company__c = company;
        update updated_contact;
    }

    public static String[] check_in(String campaign_id, String email) {        
        CampaignMember event_attendee = [SELECT ContactID, Status, CampaignId FROM CampaignMember WHERE CampaignId=:campaign_id AND (Contact.email=:email or Lead.email=:email)];
        if (event_attendee == null) {
            return new String[0];
        } else {
            event_attendee.status = 'Responded'; 
        }
        update event_attendee;
        Contact contact_event_attendee = [SELECT FirstName, LastName, Company__c FROM Contact WHERE Id=:event_attendee.ContactId];
        String[] info = new String[3];
        info[0] = contact_event_attendee.FirstName;
        info[1] = contact_event_attendee.LastName;
        info[2] = contact_event_attendee.Company__c;
        return info;
    }

    // logic to check if a campaign member needs to register or just check in
    public static String[] handle_parent_events(String campaign_id, String email) {
        Map<Id, Campaign> potential_children = new Map<Id, Campaign>([SELECT Name, Description, StartDate, Status, ParentId, Id FROM Campaign WHERE ParentId=:campaign_id OR Id=:campaign_id]);
        CampaignMember[] event_attendee = [SELECT Id, CampaignId, ContactId FROM CampaignMember WHERE CampaignId in :potential_children.keySet() AND (Lead.Email=:email OR Contact.Email=:email)];
        if (event_attendee.size() == 0) {
            return new String[0];
        } else if (event_attendee.size() != 1) {
            String[] info = new String[6];
            Contact[] contact_event_attendee = [SELECT FirstName, LastName, Company__c FROM Contact WHERE (Id=:event_attendee[0].ContactId or Id=:event_attendee[1].ContactId)];
            info[0] = contact_event_attendee[0].FirstName;
            info[1] = contact_event_attendee[0].LastName;
            info[2] = contact_event_attendee[0].Company__c;
            info[3] = contact_event_attendee[1].FirstName;
            info[4] = contact_event_attendee[1].LastName;
            info[5] = contact_event_attendee[1].Company__c;
            return info;
        } else {
            return CheckInController.check_in(string.valueof(campaign_id), email);
        }
    }

    // Checking in attendees for checkin page
    @RemoteAction
    global static String[] check_in_attendee(String event_id, String email) {
        Campaign event = [SELECT Id FROM Campaign WHERE isActive=True AND Id=:event_id];
        return CheckInController.handle_parent_events(string.valueof(event.Id), email);
    }

    // Checking in attendees for checkin page
    @RemoteAction
    global static void multiple_login(String event_id, String company, String email) {
        // login person that has specific attributes above 
        CampaignMember event_attendee = [SELECT ContactId FROM CampaignMember WHERE Contact.Company__c=:company AND Contact.email=:email AND CampaignId=:event_id];
        event_attendee.status = 'Responded'; 
        update event_attendee;
    }

    // Checking in attendees for checkin page
    @RemoteAction
    global static void update_attendee(String event_id, String first_name, String last_name, String email, String company) {
        Integer attendee_size = [SELECT count() FROM CampaignMember WHERE CampaignId=:event_Id AND (Contact.Email=:email or Lead.Email=:email)];
        if (attendee_size == 0) {
            // register attendee
            CheckInController.register_event_attendee(email, first_name, last_name, company, event_id);
        } else {
            // update attendee
            CheckInController.update_event_attendee(email, first_name, last_name, company, event_id);
        }
    }

}
