public class RollCall{


    // For debugging purposes
    public void inspect_event_table() {
        Event__c[] table = [SELECT Name__c, Description__c, Time__c FROM Event__c];
        System.Debug(table);
    }

    public void inspect_event_attendee_table() {
        Event_Attendee__c[] table = [SELECT Attendee__c, Event__c, Status__c FROM Event_Attendee__c];
        System.Debug(table);
    }

    public void inspect_attendee_table() {
        Attendee__c[] table = [SELECT First_Name__c, Last_Name__c, Company__c, email__c FROM Attendee__c];
        System.Debug(table);
    }




    // METHODS THAT INTERACT WITH EVENT TABLE
    public void create_event(String name, Datetime start_time, String description, Boolean open) {
        Event__c new_event = new Event__c(Name__c=name, Time__c=start_time, Description__c=description, Open__c=open);
        insert new_event;
    }


    public void edit_event(String event_name, String field_name, String value) {
        Event__c event = [SELECT Name__c, Description__c, Time__c FROM Event__c WHERE Name__c=:event_name];
        if (field_name == 'description') {
            event.Description__c = value;
        }
        update event;
    }

    public void delete_event(String event_name) {
        Event__c event = [SELECT Description__c FROM Event__c WHERE Name__c=:event_name];
        delete event;
    }



    // METHODS THAT INTERACT WITH EVENT_ATTENDEE TABLE
    public void register_event_attendee(String email, String event) {
        Event_Attendee__c new_event_attendee = new Event_Attendee__c(Attendee__c=email, Event__c=event, Status__c='registered');
        insert new_event_attendee;
    }

    public void check_in(String event_name, String email) {        
        Event__c event = [SELECT Name__c FROM Event__c WHERE Name__c=:event_name];
        String temp_event_name = event.Name__c;
        Attendee__c name = [SELECT Email__c FROM Attendee__c WHERE Email__c=:email];
        if (name == null) { // considers if person is a drop-in that is not in the master Attendee table
            // redirect to add_attendee() so that user enters first name, last name, email, company
        } else { // the user is already in the master Attendee table
            String temp_email = name.Email__c;
            Event_Attendee__c event_attendee = [SELECT Status__c FROM Event_Attendee__c WHERE Attendee__c=:email AND Event__c=:temp_event_name];            
            event_attendee.Status__c = 'checked in';
            update event_attendee;
        }
    }

    public void delete_event_attendee(String email, String event) {
        // needs testing
        Event_Attendee__c event_attendee = [SELECT Attendee__c, Event__c FROM Event_Attendee__c WHERE Attendee__c=:email AND Event__c=:event];
        delete event_attendee;
    }



    // METHODS THAT INTERACT WITH ATTENDEE TABLE
    public void add_attendee(String first_name, String last_name, String email, String company) {
        Attendee__c new_attendee = new Attendee__c(First_Name__c=first_name, Last_Name__c=last_name, Email__c=email, Company__c=company);
        insert new_attendee;
    }

    public void update_attendee_info(String email, String first_name, String last_name, String company) {
        // TODO
        Attendee__c attendee = [SELECT First_Name__c, Last_Name__c, Company__c FROM Attendee__c WHERE Email__c=:email];
        attendee.First_Name__c = first_name;
        attendee.Last_Name__c = last_name;
        attendee.Company__c = company;
        update attendee;
    }

    public void delete_attendee(String email) {
        Attendee__c attendee = [SELECT Email__c FROM Attendee__c WHERE Email__c=:email];
        delete attendee;
    }

}

// Debug script
Rollcall test = new Rollcall();
test.create_event('party', datetime.now(), 'crazy party', True);
// test.edit_event('party', 'description', 'changed description');



