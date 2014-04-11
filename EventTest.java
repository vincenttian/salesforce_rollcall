@isTest
private class EventTest {

    static testMethod void test_event() {
        Campaign c = RollCallTestUtility.createEventCampaign();
        Event e = new Event(c);
        Campaign d;
        for (Integer i = 0; i < 12; i++) {
            d = new Campaign(name='TestEvent', StartDate=Date.today().AddMonths(i));
            e = new Event(d, i);
        }
        d = new Campaign(name='TestEvent', StartDate=Date.today().AddDays(2));
        e = new Event(d, 1);
        System.assertEquals(e.name, d.name);
    }
    
    static testMethod void test_event2() {
        Campaign c = new Campaign(name='Parent', isActive=true, MaxCapacity__c = 1);
        insert c;
        Event e = new Event(c, 0);
        c = new Campaign(name='Parent', isActive=true, MaxCapacity__c = 1, StartDate=Date.today().AddMonths(1));
        new Event(c);
        c = new Campaign(name='Parent', isActive=true, MaxCapacity__c = 1);
        new Event(c);
    }

}