@isTest
private class RollCallTest {

    static testMethod void test() {
        RollCallTestUtility.createEventCampaign();
        Rollcall r = new RollCall();
        Event[] events = r.getEvents();
        boolean containsMember = false;
        for (Event e: events) {
            if (e.registered == 4 && e.name =='Parent') {
                 containsMember = true;
            }
        }
        System.assertEquals(containsMember, true);
    }
}