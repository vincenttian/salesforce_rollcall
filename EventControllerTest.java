@isTest
private class EventControllerTest {

    static testMethod void test_attendee_search() {
        EventController e = new EventController();
        e.getAttendees();
    }

    static testMethod void test_get_attendees() {
        EventController e = new EventController();
        Campaign c =  [SELECT Id FROM Campaign WHERE Name = 'test_event1'];
        EventController.attendee_search(c.ID, 'test', 0);
    }

    static testMethod void test_get_checkedin_times() {
        EventController e = new EventController();
        Campaign c =  [SELECT Id FROM Campaign WHERE Name = 'test_event1'];
        EventController.get_checkedin_times(c.ID);
    }

}
