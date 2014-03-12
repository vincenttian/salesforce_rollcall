/**
*
*Roll Call application 
*Controller for Index page
*@authors Howard Chen and Vincent Tian
*
*/

global class RollCall{

    public void create_event(String name, Date start_date, String description, String open_status) {
        Campaign new_event = new Campaign(Name=name, StartDate=start_date, Description=description, Status=open_status, isActive=True);
        insert new_event;
    }

    public void create_child_event(String name, Date start_date, String description, String open_status, Id parent_id) {
        Campaign new_event = new Campaign(Name=name, StartDate=start_date, Description=description, Status=open_status, isActive=True, ParentId=parent_id);
        insert new_event;
    }

    public void delete_event(String id) {
        Campaign event = [SELECT Description FROM Campaign WHERE Id=:id];
        delete event;
    }

    public void end_event(String id) {
        Campaign event = [SELECT Name, isActive FROM Campaign WHERE Id=:id];
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

    // Method for javascript remoting
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
