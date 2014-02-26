/**
*
*Roll Call application basic functionality
*@author Vincent Tian
*Version1
*
*/


global class RollCall{


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
    public void register_event_attendee(String email, String first_name, String last_name, String campaign_id, String status) {
        // first create contact, then put it in
        Contact tmp_contact = new Contact(Email=email, FirstName=first_name, LastName=last_name);
        insert tmp_contact;
        CampaignMember new_event_attendee = new CampaignMember(ContactId=tmp_contact.id, CampaignId=campaign_id, Status = status);
        insert new_event_attendee;
    }

    // Tested
    public void check_in(String campaign_id, String email) {        
        CampaignMember event_attendee = [SELECT ContactID, Status, CampaignId FROM CampaignMember WHERE CampaignId=:campaign_id AND (Contact.email=:email or Lead.email=:email)];
        if (event_attendee == null) {
            // THROW ERROR
            // throw new Checkin_Exception('Attendee is not registered for the event');
        } else {
            event_attendee.status = 'Received'; // temp status to signify checked in 
        }
        update event_attendee; 
    }

    public void delete_event_attendee(String email, String campaign_id) {
        CampaignMember event_attendee = [SELECT Contact.Email, Lead.Email FROM CampaignMember WHERE CampaignId=:campaign_id AND Contact.Email=:email];
        delete event_attendee;
    }

    // logic to check if a campaign member needs to register or just check in
    // first check if the person is registered or not
    public void handle_parent_events(String campaign_id, String email) {
        // Three scenarios:
        //      1. Event is a parent event that has no children; standalone event
        //      2. Event is child of a parent event
        //      3. Event is a parent that has children


        Map<Id, Campaign> potential_children = new Map<Id, Campaign>([SELECT Name, Description, StartDate, Status, ParentId, Id FROM Campaign WHERE ParentId=:campaign_id OR Id=:campaign_id]);
        CampaignMember[] event_attendee = [SELECT Id, CampaignId FROM CampaignMember WHERE CampaignId in :potential_children.keySet() AND (Lead.Email=:email OR Contact.Email=:email)];
        if (event_attendee.size() == 0) {
            // register the attendee
        } else {
            check_in(campaign_id, email);
        }
        
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
    global static Campaign get_event(String event_name) {
        Campaign event = [SELECT Name, Description, StartDate FROM Campaign WHERE isActive=True AND Name=:event_name];
        return event;         
    }

    // Returns the date of a single campaign 
    @RemoteAction
    global static String get_event_date(String event_name) {
        Campaign event = [SELECT Name, Description, StartDate FROM Campaign WHERE isActive=True AND Name=:event_name];
        return event.StartDate.format();         
    }

    // Edits event info
    @RemoteAction
    global static void edit_info(String event_name, String new_description) {
        Campaign event = [SELECT Name, Description, StartDate FROM Campaign WHERE isActive=True AND Name=:event_name];
        event.Description = new_description;
        update event;
    }

    // // write remoteaction for checking in
    // @RemoteAction
    // global static check_in(String event_name, String email) {
        
    // }

    // // Gives statistics on the number of attendees
    // @RemoteAction
    // global static Array event_stats(String event_name) {
        
    // }


}

// custom exception for handling Event_Attendee checkins
// public class Checkin_Exception extends Exception {}

// Debug script
/*
date myDate = date.newInstance(1960, 2, 17);Rollcall test = new Rollcall();
Campaign x = test.create_event('party', mydate, 'description', 'Open');
test.register_event_attendee('test@test.com', 'blah', 'tian', x, 'Open');
Rollcall.inspect_event_attendee_table();

CampaignMember event_attendee = [SELECT Contact.Email, Lead.Email FROM CampaignMember WHERE Contact.Email=:email];

*/


