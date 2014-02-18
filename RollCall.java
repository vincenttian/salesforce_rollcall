public class RollCall{

    // For debugging purposes
    public void inspect_tables() {
        Event__c[] s = [SELECT Name__c, Description__c, Time__c FROM Event__c WHERE Name__c=:event_name];
        System.Debug(s);
    }

    public void create_event(String name, Datetime start_time, String description, Boolean open) {
        Event__c new_event = new Event__c(Name__c=name, Time__c=start_time, Description__c=description, Open__c=open);
        insert new_event;
    }


    public void edit_event(String event_name, String field_name, Object value) {
        Event__c s = [SELECT Name__c, Description__c, Time__c FROM Event__c WHERE Name__c=:event_name];
        if (field_name == 'description') {
            s.Description__c = (String) value;
        }
        update s;
    }

    public void delete_event(String event_name) {
        Event__c s = [SELECT Description__c FROM Event__c WHERE Name__c=:event_name];
        delete s;
    }

}

// Debug script
Rollcall test = new Rollcall();
test.create_event('party', datetime.now(), 'crazy party', True);
// test.edit_event('party', 'description', 'changed description');
