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
        Campaign new_event = new Campaign(Name = name, StartDate=start_date, Description=description, Status=open_status, isActive=True);
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



    // METHODS THAT INTERACT WITH EVENT_ATTENDEE TABLE
    // Tested
    public void register_event_attendee(String email, String first_name, String last_name, Campaign event, String status) {
        // first create contact, then put it in
        Contact tmp_contact = new Contact(Email=email, FirstName=first_name, LastName=last_name);
        insert tmp_contact;
        CampaignMember new_event_attendee = new CampaignMember(ContactId=tmp_contact.id, CampaignId=event.id, Campaign = event, Status = status);
        insert new_event_attendee;
    }

    // Tested
    public void check_in(Campaign event, String email) {        
        CampaignMember event_attendee = [SELECT ContactID, Status FROM CampaignMember WHERE Contact.email=:email or Lead.email=:email];
        if (event_attendee == null) {
            // THROW ERROR
            // throw new Checkin_Exception('Attendee is not registered for the event');
        } else {
            event_attendee.status = 'Received';
        }
        update event_attendee; 
    }

    public void delete_event_attendee(String email, Campaign event) {
        CampaignMember event_attendee = [SELECT Contact.Email, Lead.Email FROM CampaignMember WHERE Contact.Email=:email];
        delete event_attendee;
    }



    // METHODS FOR JAVASCRIPT REMOTING
    @RemoteAction
    global static Campaign[] get_events() {
        Campaign[] events = [SELECT Name, Description, StartDate FROM Campaign WHERE isActive=True]; 
        // AND Status in ('Open', 'In Progress') ];
        return events;
    }

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


