/**
*
*Roll Call application 
*Controller for Index page
*@authors Howard Chen and Vincent Tian
*
*/

global class RollCall{

    // For apex: repeat    
    public Event[] getEvents() {
        Campaign[] campaigns = [SELECT Name, Description, StartDate, MaxCapacity__c FROM Campaign WHERE IsActive = True AND ParentId = null ORDER BY StartDate ASC NULLS FIRST];
        Event[] events = new Event[]{};
            
        Integer i = 0;    
        for (Campaign c : campaigns) {
            events.add(new Event(c, i));
            i++;
        }
        return events;
    }

}