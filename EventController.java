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
    public void create_event(String name, Date start_date, String description, String open_status) {
        Campaign new_event = new Campaign(Name=name, StartDate=start_date, Description=description, Status=open_status, isActive=True);
        insert new_event;
    }

    public void create_child_event(String name, Date start_date, String description, String open_status, Id parent_id) {
        Campaign new_event = new Campaign(Name=name, StartDate=start_date, Description=description, Status=open_status, isActive=True, ParentId=parent_id);
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

    public void end_event(String event_name) {
        Campaign event = [SELECT Name, isActive FROM Campaign WHERE Name=:event_name];
        event.isActive = False;
        update event;
    }


    // METHODS FOR JAVASCRIPT REMOTING
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


// Debug script

/*

date myDate = date.newInstance(1960, 2, 17);EventController test = new EventController();
Campaign x = test.create_event('party', mydate, 'description', 'Open');
test.register_event_attendee('test@test.com', 'blah', 'tian', x, 'Open');
Rollcall.inspect_event_attendee_table();
CampaignMember event_attendee = [SELECT Contact.Email, Lead.Email FROM CampaignMember WHERE Contact.Email=:email];

*/