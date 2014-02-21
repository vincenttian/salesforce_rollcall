/**
*
*Roll Call application basic functionality
*@author Vincent Tian
*Version1
*
*/


public class RollCall{


    // For debugging purposes
    public void inspect_event_table() {
        Event[] table = [SELECT Name, Description, StartDate FROM Event];
        System.Debug(table);
    }

    public void inspect_event_attendee_table() {
        CampaignMember[] table = [SELECT Email, FirstName, LastName, Campaign, Status FROM CampaignMember];
        System.Debug(table);
    }



    // METHODS THAT INTERACT WITH EVENT TABLE
    public void create_event(String name, Datetime start_time, String description, Status open_status) {
        Campaign new_event = new Campaign(Name = name, StartDate=start_date, Description=description, Status=open_status);
        insert new_event;
    }


    public void edit_event(String event_name, String field_name, String value) {
        Campaign event = [SELECT Name, Description, StartDate, Status FROM Campaign WHERE Name=:event_name];
        if (field_name == 'description') {
            event.Description = value;
        }
        update event;
    }

    public void delete_event(String event_name) {
        Campaign event = [SELECT Description FROM Campaign WHERE Name=:event_name];
        delete event;
    }



    // METHODS THAT INTERACT WITH EVENT_ATTENDEE TABLE
    public void register_event_attendee(Email email, String first_name, String last_name, Campaign event, Status status) {
        CampaignMember new_event_attendee = new CampaignMember(Email = email, FirstName = first_name, LastName = last_name, Campaign = event, Status = status);
        insert new_event_attendee;
    }

    public void check_in(Campaign event, String email) {        
        CampaignMember event_attendee = [SELECT Email, FirstName, LastName, Campaign, Status FROM CampaignMember WHERE Campaign:= event AND Email=:email];
        if (event_attendee == null) {
            // THROW ERROR
            throw new Checkin_Exception('Attendee is not registered for the event');
        }
        Status new_status = new Status(Status.Received); // Change after creating new status in backend
        event_attendee.Status = new_status;
        update new_status; 
    }

    public void delete_event_attendee(String email, Campaign event) {
        CampaignMember event_attendee = [SELECT Email FROM CampaignMember WHERE Email=:email AND Campaign=:event];
        delete event_attendee;
    }

}

// custom exception for handling Event_Attendee checkins
public class Checkin_Exception extends Exception {

}

// Debug script
Rollcall test = new Rollcall();
test.create_event('party', datetime.now(), 'crazy party', True);
// test.edit_event('party', 'description', 'changed description');



