/**
*
*Roll Call application 
*Controller for Checkin page
*@authors Howard Chen and Vincent Tian
*
*/

global class CheckInController{

    public CheckInController() {
    }

    public static void register_event_attendee(String email, String first_name, String last_name, String company, String campaign_id) { 
        Lead tmp_contact = new Lead(Email=email, FirstName=first_name, LastName=last_name, Company=company);
        insert tmp_contact;
        CampaignMember new_event_attendee = new CampaignMember(LeadId=tmp_contact.id, CampaignId=campaign_id, Status = 'Responded'); 
        insert new_event_attendee;
    }

    public static void update_event_attendee(String email, String first_name, String last_name, String company, String campaign_id) { 
        CampaignMember member = [SELECT ContactId FROM CampaignMember WHERE CampaignId=:campaign_id AND (Contact.Email=:email or Lead.Email=:email)];
        Contact updated_contact = [SELECT firstname, lastname, Company__c FROM Contact WHERE Id=:member.ContactId];
        if (updated_contact == null) {
            Lead updated_lead = [SELECT firstname, lastname, Company FROM Lead WHERE Id=:member.LeadId];
            updated_lead.company = company;
            updated_lead.firstname = first_name;
            updated_lead.lastname = last_name;
            update updated_lead;
        } else {
            updated_contact.Company__c = company;
            updated_contact.firstname = first_name;
            updated_contact.lastname = last_name;
            update updated_contact;
        }
    }

    public static String[] check_in(String campaign_id, String email) {        
        CampaignMember event_attendee = [SELECT ContactID, LeadID, Status, CampaignId FROM CampaignMember WHERE CampaignId=:campaign_id AND (Contact.email=:email or Lead.email=:email)];
        if (event_attendee == null) {
            return new String[0];
        } else {
            event_attendee.status = 'Responded'; 
        }
        update event_attendee;
        String[] info = new String[3];
        Contact contact_event_attendee = [SELECT FirstName, LastName, Company__c FROM Contact WHERE Id=:event_attendee.ContactId];
        if (contact_event_attendee == null) {
            Lead lead_event_attendee = [SELECT FirstName, LastName, Company FROM Lead WHERE Id=:event_attendee.LeadId];
            info[0] = lead_event_attendee.FirstName;
            info[1] = lead_event_attendee.LastName;
            info[2] = lead_event_attendee.Company;
        } else {
            info[0] = contact_event_attendee.FirstName;
            info[1] = contact_event_attendee.LastName;
            info[2] = contact_event_attendee.Company__c;
        }
        return info;
    }

    // logic to check if a campaign member needs to register or just check in
    public static String[] handle_parent_events(String campaign_id, String email) {
        Map<Id, Campaign> potential_children = new Map<Id, Campaign>([SELECT Name, Description, StartDate, Status, ParentId, Id FROM Campaign WHERE ParentId=:campaign_id OR Id=:campaign_id]);
        CampaignMember[] event_attendee = [SELECT Id, CampaignId, ContactId, LeadId FROM CampaignMember WHERE CampaignId in :potential_children.keySet() AND (Lead.Email=:email OR Contact.Email=:email)];
        if (event_attendee.size() == 0) {
            return new String[0];
        } else if (event_attendee.size() != 1) {
            String[] info = new String[6];
            Contact[] contact_event_attendee = [SELECT FirstName, LastName, Company__c FROM Contact WHERE (Id=:event_attendee[0].ContactId or Id=:event_attendee[1].ContactId)];
            if (contact_event_attendee.size() == 0) {
                Lead[] lead_event_attendee = [SELECT FirstName, LastName, Company FROM Lead WHERE (Id=:event_attendee[0].LeadId or Id=:event_attendee[1].LeadId)];
                info[0] = lead_event_attendee[0].FirstName;
                info[1] = lead_event_attendee[0].LastName;
                info[2] = lead_event_attendee[0].Company;
                info[3] = lead_event_attendee[1].FirstName;
                info[4] = lead_event_attendee[1].LastName;
                info[5] = lead_event_attendee[1].Company;
            } else {
                info[0] = contact_event_attendee[0].FirstName;
                info[1] = contact_event_attendee[0].LastName;
                info[2] = contact_event_attendee[0].Company__c;
                info[3] = contact_event_attendee[1].FirstName;
                info[4] = contact_event_attendee[1].LastName;
                info[5] = contact_event_attendee[1].Company__c;
            }
            return info;
        } else {
            return CheckInController.check_in(string.valueof(campaign_id), email);
        }
    }

    // Checking in attendees for checkin page
    @RemoteAction
    global static String[] check_in_attendee(String event_id, String email) {
        System.debug(event_id);
        System.debug('asdlfkjsad;lfjka please go here');
        Campaign event = [SELECT Id FROM Campaign WHERE isActive=True AND Id=:event_id];
        return handle_parent_events(string.valueof(event.Id), email);
    }

    // Checking in attendees for checkin page
    @RemoteAction
    global static void multiple_login(String event_id, String company, String email) {
        // login person that has specific attributes above 
        CampaignMember event_attendee = [SELECT ContactId, LeadId FROM CampaignMember WHERE CampaignId=:event_id AND ((Contact.Company__c=:company AND Contact.email=:email) or (Lead.Company=:company AND Lead.email=:email))];
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