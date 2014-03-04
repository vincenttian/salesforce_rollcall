/**
*
*Roll Call application basic functionality
*
*/


global class CheckInController{

    public Campaign event {get;set;}

    public CheckInController() {
        event = [SELECT Id, Name, Description, StartDate FROM Campaign WHERE Id=:ApexPages.currentPage().getParameters().get('event_id')];
    }


    // For debugging purposes
    // Tested.
    public static void inspect_event_table() {
        Campaign[] table = [SELECT Name, Description, StartDate FROM Campaign];
        System.Debug(table);
    }

    // Tested.
    public static void inspect_event_attendee_table() {
        CampaignMember[] table = [SELECT Lead.Email, Lead.FirstName, Lead.LastName, Contact.Email, Contact.FirstName, Contact.LastName, CampaignID, Status FROM CampaignMember];
        System.Debug(table);
    }

    public static void inspect_event_table(Campaign event) {
        CampaignMember[] table = [SELECT Lead.Email, Lead.FirstName, Lead.LastName, Contact.Email, Contact.FirstName, Contact.LastName, CampaignID, Status FROM CampaignMember];
        System.Debug(table);
    }


    // Case; Used when drop-ins come to an event
    public static void register_event_attendee(String email, String first_name, String last_name, String company, String campaign_id) { // Later switch to just contact and campaign ID
        // first create contact, then put it in
        Contact tmp_contact = new Contact(Email=email, FirstName=first_name, LastName=last_name, Company__c=company);
        insert tmp_contact;
        CampaignMember new_event_attendee = new CampaignMember(ContactId=tmp_contact.id, CampaignId=campaign_id, Status = 'Responded'); // NOT SETTING PICKLIST TO DEFAULT SPECIFIED VALUE
        insert new_event_attendee;
    }

    public static void update_event_attendee(String email, String first_name, String last_name, String company, String campaign_id) { // Later switch to just contact and campaign ID
        // first create contact, then put it in
        
        // NEEDS WORK FIX THIS 
        CampaignMember member = [SELECT ContactId FROM CampaignMember WHERE CampaignId=:campaign_id AND (Contact.Email=:email or Lead.Email=:email)];
        Contact updated_contact = [SELECT firstname, lastname, Company__c FROM Contact WHERE Id=:member.ContactId];
        updated_contact.firstname = first_name;
        updated_contact.lastname = last_name;
        updated_contact.Company__c = company;
        update updated_contact;
    }

    // Tested
    public static String[] check_in(String campaign_id, String email) {        
        CampaignMember event_attendee = [SELECT ContactID, Status, CampaignId FROM CampaignMember WHERE CampaignId=:campaign_id AND (Contact.email=:email or Lead.email=:email)];
        System.debug('in call');
        if (event_attendee == null) {
            System.debug('in call');
            // THROW ERROR
            // throw new Checkin_Exception('Attendee is not registered for the event');
            return new String[0];
        } else {
            System.debug(event_attendee);
            // System.debug(event_attendee[0].status);
            event_attendee.status = 'Responded'; // temp status to signify checked in  // NOT SETTING PICKLIST TO SPECIFIED VALUE
            System.debug(event_attendee);
        }
        update event_attendee;


        Contact contact_event_attendee = [SELECT FirstName, LastName, Company__c FROM Contact WHERE Id=:event_attendee.ContactId];
        String[] info = new String[3];
        // String[] info = new String{contact_event_attendee.FirstName, contact_event_attendee.LastName, contact_event_attendee.Company__c};
        info[0] = contact_event_attendee.FirstName;
        info[1] = contact_event_attendee.LastName;
        info[2] = contact_event_attendee.Company__c;
        return info;
    }

    // logic to check if a campaign member needs to register or just check in
    // first check if the person is registered or not
    public static String[] handle_parent_events(String campaign_id, String email) {
        Map<Id, Campaign> potential_children = new Map<Id, Campaign>([SELECT Name, Description, StartDate, Status, ParentId, Id FROM Campaign WHERE ParentId=:campaign_id OR Id=:campaign_id]);
        CampaignMember[] event_attendee = [SELECT Id, CampaignId FROM CampaignMember WHERE CampaignId in :potential_children.keySet() AND (Lead.Email=:email OR Contact.Email=:email)];
        if (event_attendee.size() == 0) {
            System.debug('No Attendees');
            // register the attendee
            // MUST RAISE AN ERROR
            return new String[0];
        } else {
            return CheckInController.check_in(string.valueof(campaign_id), email);
        }
    }

    // Checking in attendees for checkin page
    @RemoteAction
    global static String[] check_in_attendee(String event_id, String email) {
        Campaign event = [SELECT Id FROM Campaign WHERE isActive=True AND Id=:event_id];
        System.debug('Remote call');
        return CheckInController.handle_parent_events(string.valueof(event.Id), email);
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

// Debug script

/*

date myDate = date.newInstance(1960, 2, 17);EventController test = new EventController();
Campaign x = test.create_event('party', mydate, 'description', 'Open');
test.register_event_attendee('test@test.com', 'blah', 'tian', x, 'Open');
Rollcall.inspect_event_attendee_table();
CampaignMember event_attendee = [SELECT Contact.Email, Lead.Email FROM CampaignMember WHERE Contact.Email=:email];

*/