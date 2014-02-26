public class RollCall{

    // For debugging purposes
    public void inspect_tables() {
        Event__c[] table = [SELECT Name__c, Description__c, Time__c FROM Event__c WHERE Name__c=:event_name];
        System.Debug(table);
    }


    // METHODS THAT INTERACT WITH EVENT TABLE
    public void create_event(String name, Datetime start_time, String description, Boolean open) {
        Event__c new_event = new Event__c(Name__c=name, Time__c=start_time, Description__c=description, Open__c=open);
        insert new_event;
    }


    public void edit_event(String event_name, String field_name, Object value) {
        Event__c event = [SELECT Name__c, Description__c, Time__c FROM Event__c WHERE Name__c=:event_name];
        if (field_name == 'description') {
            event.Description__c = (String) value;
        }
        update event;
    }

    public void delete_event(String event_name) {
        Event__c event = [SELECT Description__c FROM Event__c WHERE Name__c=:event_name];
        delete event;
    }

    // METHODS THAT INTERACT WITH EVENT_ATTENDEE TABLE
    public void check_in(String event_name, String email) {        
        Event__c event = [SELECT Email__c FROM Attendee__c WHERE Email__c=:email];
        Attendee__c name = [SELECT Email__c FROM Attendee__c WHERE Email__c=:email];
        // considers if person is a drop-in that is not in the master Atendee table
        if (name == null) {
            // redirect them to drop in page to enter additional information
            return 'not checked in'
        // the user is already 
        } else {

        }
        name.Status__c = 'checked in';
        update name;
    }

    // METHODS THAT INTERACT WITH ATTENDEE TABLE
    public void update_attendee_info(String first_name, String last_name, String company) {
        // TODO
    }

    public void add_attendee(String first_name, String last_name, String email, String company) {
        // TODO
    }

    public void delete_attendee(String email) {
        // TODO
    }



}

// Debug script
Rollcall test = new Rollcall();
test.create_event('party', datetime.now(), 'crazy party', True);
// test.edit_event('party', 'description', 'changed description');



