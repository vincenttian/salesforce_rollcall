@isTest
private class EventControllerTest {

    static testMethod void contstructor() {
        EventController c = new EventController();
    }

    static testMethod void test_attendee_search() {
        Campaign c = RollCallTestUtility.createEventCampaign();
        SObject[] events = EventController.attendee_search(c.ID, 'Lee', 0);
        System.assertNotEquals(events, null);
    }
    
    static testMethod void test_get_checkedin_times() {
        Campaign c = RollCallTestUtility.createEventCampaign();
        String cid = c.ID + '';
        List<DateTime> dates = EventController.get_checkedin_times(cid);
        System.assertNotEquals(dates, null);
    }
    
    static testMethod void test_update_stats() {
        Campaign c = RollCallTestUtility.createEventCampaign();
        String cid = c.ID + '';
        Event e = EventController.update_stats(cid);
        System.assertNotEquals(e, null);
    }
}