/**
 *Unit tests for RollCall app. 
 *@author Howard Chen
 *Version 1
 */
@isTest
public class RollCallTest {

    /** Creates a simple test event. */
    public static Event__c create_one_event() {
        // Create one invoice statement
        Event__c test_event = create_event('TestEvent', DateTime.now(), 'Event used for testing', true);
        return testEvent;
    }

}

