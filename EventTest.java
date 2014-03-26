@isTest
private class EventTest {

    static testMethod void test_event() {
        Campaign c = RollCallTestUtility.createEventCampaign();
        for (int i = 0; i < 12; i++) {
            Event e = new Event(c.ID, i);
        }
    }

}