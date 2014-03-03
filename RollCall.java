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
    public void delete_event(String event_name) {
        Campaign event = [SELECT Description FROM Campaign WHERE Name=:event_name];
        delete event;
    }

    public void end_event(String event_name) {
        Campaign event = [SELECT Name, isActive FROM Campaign WHERE Name=:event_name];
        event.isActive = False;
        update event;
    }

    // For apex: repeat    
    public Event[] getEvents() {
        Campaign[] campaigns = [SELECT Name, Description, StartDate, MaxCapacity__c FROM Campaign WHERE IsActive = True AND ParentId = null ORDER BY StartDate ASC NULLS FIRST];
        Integer count = [SELECT count() FROM Campaign WHERE IsActive = True AND ParentId = null];
        Event[] events = new Event[count];
        for (Integer i = 0; i < count; i ++) {
            events[i] = new Event(campaigns[i], i);
        }
        return events;
    }


    // METHODS FOR JAVASCRIPT REMOTING
    // Returns a list of all events for main page
    @RemoteAction
    global static Event[] get_events() {
        Campaign[] campaigns = [SELECT Name, Description, StartDate, MaxCapacity__c FROM Campaign WHERE IsActive = True AND ParentId = null ORDER BY StartDate ASC NULLS FIRST];
        Integer count = [SELECT count() FROM Campaign WHERE IsActive = True AND ParentId = null];
        Event[] events = new Event[count];
        for (Integer i = 0; i < count; i ++) {
            events[i] = new Event(campaigns[i]);
        }
        return events;
    }

}

// Debug script

/*

date myDate = date.newInstance(1960, 2, 17);Rollcall test = new Rollcall();
Campaign x = test.create_event('party', mydate, 'description', 'Open');
test.register_event_attendee('test@test.com', 'blah', 'tian', x, 'Open');
Rollcall.inspect_event_attendee_table();
CampaignMember event_attendee = [SELECT Contact.Email, Lead.Email FROM CampaignMember WHERE Contact.Email=:email];

*/