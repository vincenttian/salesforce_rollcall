@isTest
private class CheckInControllerTest {

    static testMethod void test_register_event_attendee() {
        Campaign c = RollCallTestUtility.createEventCampaign();
        String cID = c.ID + '';
        Contact testContact = new Contact(firstName = 'TestContact', lastName = 'Empty');
        CheckInController.update_attendee(cID, testContact);
        Member[] events = EventController.attendee_search(c.ID, 'Empty', 0);
        System.assertEquals(events.size(), 1);
    }

    static testMethod void test_register_event_attendee2() {
        SObject[] arr = RollCallTestUtility.createEventCampaign2();
        Campaign c = (Campaign) arr[0];
        Lead l = (Lead) arr[1];
        String oldName = l.firstName;
        l.firstName = 'RareFirstName';
        Member[] events = EventController.attendee_search(c.ID, 'RareFirstName', 0);
        System.assertEquals(events.size(), 0);
        events = EventController.attendee_search(c.ID, oldName, 0);
        System.assertEquals(events.size(), 1);
        CheckInController.update_attendee(c.ID + '', l);
        events = EventController.attendee_search(c.ID, 'RareFirstName', 0);
        System.assertEquals(events.size(), 1);
        events = EventController.attendee_search(c.ID, oldName, 0);
        System.assertEquals(events.size(), 0);
    }

    static testMethod void test_register_event_attendee3() {
        SObject[] arr = RollCallTestUtility.createEventCampaign3();
        Campaign c = (Campaign) arr[0];
        Contact l = (Contact) arr[1];
        String oldName = l.firstName;
        l.firstName = 'RareFirstName';
        Member[] events = EventController.attendee_search(c.ID, 'RareFirstName', 0);
        System.assertEquals(events.size(), 0);
        events = EventController.attendee_search(c.ID, oldName, 0);
        System.assertEquals(events.size(), 2);
        CheckInController.update_attendee(c.ID + '', l);
        events = EventController.attendee_search(c.ID, 'RareFirstName', 0);
        System.assertEquals(events.size(), 1);
        events = EventController.attendee_search(c.ID, oldName, 0);
        System.assertEquals(events.size(), 1);
    }

    static testMethod void test_check_in_lead() {
        SObject[] arr = RollCallTestUtility.createEventCampaign2();
        Campaign c = (Campaign) arr[0];
        Lead l = (Lead) arr[1];
        Member[] events = EventController.attendee_search(c.ID, l.FirstName, 0);
        System.assertEquals(events[0].status, false);
        CheckInController.check_in_attendee(c.ID + '', l.email);
        events = EventController.attendee_search(c.ID, l.FirstName, 0);
        System.assertEquals(events[0].status, true);
    }

    static testMethod void test_check_in_contact() {
        SObject[] arr = RollCallTestUtility.createEventCampaign3();
        Campaign c = (Campaign) arr[0];
        Contact l = (Contact) arr[1];
        Member[] events = EventController.attendee_search(c.ID, l.FirstName, 0);
        System.assertEquals(events[0].status, false);
        CheckInController.check_in_attendee(c.ID + '', l.email);
        events = EventController.attendee_search(c.ID, l.FirstName, 0);
        System.assertEquals(events[0].status, true);
    }

    static testMethod void test_multiple_checkin() {
        SObject[] arr = RollCallTestUtility.createEventCampaign3();
        Campaign c = (Campaign) arr[0];
        Lead l = (Lead) arr[1];
        Contact cn = (Contact) arr[2];
        Member[] events = EventController.attendee_search(c.ID, l.FirstName, 0);
        System.assertEquals(events[0].status, false);
        events = EventController.attendee_search(c.ID, cn.FirstName, 0);
        System.assertEquals(events[0].status, false);

    }
}