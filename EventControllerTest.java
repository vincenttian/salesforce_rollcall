@isTest
private class EventControllerTest {

    static testMethod void test_attendee_search() {
    }

    static testMethod void test_get_attendees() {
        RollCall r = new RollCall();
        Date test_date = Date.newInstance(2014, 2, 17);
        r.create_event('test_event7', test_date, 'test_description', 'Open');
        Campaign c = [SELECT Id, isActive FROM Campaign WHERE Name = 'test_event7'];
        EventController.attendee_search(c.ID, 'test', 0);
    }

    static testMethod void test_get_checkedin_times() {
        RollCall r = new RollCall();
        Date test_date = Date.newInstance(2014, 2, 17);
        r.create_event('test_event7', test_date, 'test_description', 'Open');
        Campaign c = [SELECT Id, isActive FROM Campaign WHERE Name = 'test_event7'];
        EventController.get_checkedin_times(c.ID);
    }

}
