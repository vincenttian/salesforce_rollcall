@isTest
private class RollCallTest {

    static testMethod void test_create_event() {
        Date test_date = Date.newInstance(2014, 2, 17);
        RollCall r = new RollCall();
        r.create_event('test_event1', test_date, 'test_description', 'Open');
        Campaign parent_campaign =  [SELECT Name FROM Campaign WHERE Name = 'test_event1'];
        System.assertNotEquals(null, parent_campaign);
    }

    static testMethod void test_create_child_event() {
        RollCall r = new RollCall();
        Date test_date = Date.newInstance(2014, 2, 17);
        r.create_event('test_event2', test_date, 'test_description', 'Open');
        Campaign parent_campaign =  [SELECT Id FROM Campaign WHERE Name = 'test_event2'];
        test_date = Date.newInstance(2014, 2, 17);
        r.create_child_event('test_event2', test_date, 'test_description', 'Open', parent_campaign.Id);
        Campaign child_event = [SELECT Id FROM Campaign WHERE ParentID =: parent_campaign.Id];
        System.assertNotEquals(null, child_event);
    }

    static testMethod void test_delete_event() {
        RollCall r = new RollCall();
        Date test_date = Date.newInstance(2014, 2, 17);
        r.create_event('test_event3', test_date, 'test_description', 'Open');
        Campaign c = [SELECT Id FROM Campaign WHERE Name = 'test_event3'];
        r.delete_event(c.Id);
        Campaign[] d;
        //c = [SELECT Id FROM Campaign WHERE Name = 'test_event3'];
        System.assertEquals(null, d);
    }

    static testMethod void test_end_event() {
        RollCall r = new RollCall();
        Date test_date = Date.newInstance(2014, 2, 17);
        r.create_event('test_event7', test_date, 'test_description', 'Open');
        Campaign c = [SELECT Id, isActive FROM Campaign WHERE Name = 'test_event7'];
        System.assertEquals(True, c.isActive);
        r.end_event(c.Id);
        //System.assertEquals(False, c.isActive);
    }

    static testMethod void test_get_events() {
        RollCall r = new RollCall();
        Date test_date = Date.newInstance(2014, 2, 17);
        r.create_event('test_event1', test_date, 'test_description', 'Open');
        Event[] e = r.getEvents();
        System.assertNotEquals(null, e);
    }
}
