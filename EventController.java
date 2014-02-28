/**
*
*Roll Call application basic functionality
*@author Vincent Tian
*Version1
*
*/


global class EventController{

    public Campaign event {get;set;}

    public EventController() {
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



    // METHODS THAT INTERACT WITH EVENT TABLE
    // Tested
    public void create_event(String name, Date start_date, String description, String open_status) {
        Campaign new_event = new Campaign(Name=name, StartDate=start_date, Description=description, Status=open_status, isActive=True);
        insert new_event;
    }

    public void create_child_event(String name, Date start_date, String description, String open_status, Id parent_id) {
        Campaign new_event = new Campaign(Name=name, StartDate=start_date, Description=description, Status=open_status, isActive=True, ParentId=parent_id);
        insert new_event;
    }

    // Tested
    public void edit_event(String event_name, String field_name, String value) {
        Campaign event = [SELECT Name, Description, StartDate, Status FROM Campaign WHERE Name=:event_name];
        if (field_name == 'description') {
            event.Description = value;
        }
        update event;
    }

    // Tested
    public void delete_event(String event_name) {
        Campaign event = [SELECT Description FROM Campaign WHERE Name=:event_name];
        delete event;
    }

    public void end_event(String event_name) {
        Campaign event = [SELECT Name, isActive FROM Campaign WHERE Name=:event_name];
        event.isActive = False;
        update event;
    }



    // METHODS THAT INTERACT WITH EVENT_ATTENDEE TABLE
    // Tested
    // Case; Used when drop-ins come to an event
    public void register_event_attendee(String email, String first_name, String last_name, String campaign_id) { // Later switch to just contact and campaign ID
        // first create contact, then put it in
        Contact tmp_contact = new Contact(Email=email, FirstName=first_name, LastName=last_name);
        insert tmp_contact;
        CampaignMember new_event_attendee = new CampaignMember(ContactId=tmp_contact.id, CampaignId=campaign_id, Status = 'Planned'); // NOT SETTING PICKLIST TO DEFAULT SPECIFIED VALUE
        insert new_event_attendee;
    }

    // Tested
    public static void check_in(String campaign_id, String email) {        
        CampaignMember[] event_attendee = [SELECT ContactID, Status, CampaignId FROM CampaignMember WHERE CampaignId=:campaign_id];// AND (Contact.email=:email or Lead.email=:email)];
        System.debug('in call');
        if (event_attendee.size() == 0) {
            System.debug('in call');
            // THROW ERROR
            // throw new Checkin_Exception('Attendee is not registered for the event');
        } else {
            System.debug(event_attendee[0]);
            // System.debug(event_attendee[0].status);
            event_attendee[0].status = 'Responded'; // temp status to signify checked in  // NOT SETTING PICKLIST TO SPECIFIED VALUE
            System.debug(event_attendee[0]);
        }
        update event_attendee[0];
    }

    public void delete_event_attendee(String email, String campaign_id) {
        CampaignMember event_attendee = [SELECT Contact.Email, Lead.Email FROM CampaignMember WHERE CampaignId=:campaign_id AND Contact.Email=:email];
        delete event_attendee;
    }

    // logic to check if a campaign member needs to register or just check in
    // first check if the person is registered or not
    public static void handle_parent_events(String campaign_id, String email) {

        Map<Id, Campaign> potential_children = new Map<Id, Campaign>([SELECT Name, Description, StartDate, Status, ParentId, Id FROM Campaign WHERE ParentId=:campaign_id OR Id=:campaign_id]);
        CampaignMember[] event_attendee = [SELECT Id, CampaignId FROM CampaignMember WHERE CampaignId in :potential_children.keySet() AND (Lead.Email=:email OR Contact.Email=:email)];
        if (event_attendee.size() == 0) {
            System.debug('No Attendees');
            // register the attendee
            // MUST RAISE AN ERROR
        } else {
            EventController.check_in(string.valueof(campaign_id), email);
        }
        
    }



    // For apex: repeat    
    public Campaign[] getEvents() {
        Campaign[] events = [SELECT Name, Description, StartDate FROM Campaign WHERE IsActive = True AND ParentId = null ORDER BY StartDate ASC NULLS FIRST]; 
        return events;
    }




    // METHODS FOR JAVASCRIPT REMOTING
    // Returns a list of all events for main page
    @RemoteAction
    global static Campaign[] get_events() {
        Campaign[] events = [SELECT Name, Description, StartDate FROM Campaign WHERE IsActive = True AND ParentId = null ORDER BY StartDate ASC NULLS FIRST]; 
        return events;
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

    // Checking in attendees for checkin page
    @RemoteAction
    global static void check_in_attendee(String event_id, String email) {
        Campaign event = [SELECT Id FROM Campaign WHERE isActive=True AND Id=:event_id];
        System.debug('Remote call');
        EventController.handle_parent_events(string.valueof(event.Id), email);
    }

    // // Gives statistics on the number of attendees
    @RemoteAction
    global static Integer[] event_stats(String event_id) {
        Campaign event = [SELECT Id FROM Campaign WHERE isActive=True AND Id=:event_id];
        Integer registered = [SELECT count() FROM CampaignMember WHERE CampaignId=:event.Id];
        Integer checked_in = [SELECT count() FROM CampaignMember WHERE Status = 'Responded' AND CampaignId=:event.Id];
        Integer[] data = new Integer[]{registered, checked_in};
        System.debug(data);
        return data;   
    } 


}

// custom exception for handling Event_Attendee checkins
// public class Checkin_Exception extends Exception {}

// Debug script
/*
date myDate = date.newInstance(1960, 2, 17);EventController test = new EventController();
Campaign x = test.create_event('party', mydate, 'description', 'Open');
test.register_event_attendee('test@test.com', 'blah', 'tian', x, 'Open');
Rollcall.inspect_event_attendee_table();

CampaignMember event_attendee = [SELECT Contact.Email, Lead.Email FROM CampaignMember WHERE Contact.Email=:email];

*/