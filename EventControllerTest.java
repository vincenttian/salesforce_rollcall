@isTest
private class EventControllerTest {

    static testMethod void test_attendee_search() {
        Campaign c = RollCallTestUtility.createEventCampaign();
        Event[] events = EventController.attendee_search(c.ID, 'Lee', 0);
        System.assertNotEquals(events, null);
    }
}