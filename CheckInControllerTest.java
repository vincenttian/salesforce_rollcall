@isTest
private class CheckInControllerTest {

    static testMethod void test_update_event_attendee() {
      RollCall r = new RollCall();
      Date test_date = Date.newInstance(2014, 2, 17);
      r.create_event('test_event7', test_date, 'test_description', 'Open');
      Campaign c = [SELECT Id, isActive FROM Campaign WHERE Name = 'test_event7'];
      update_attendee(c.ID, String 'test', 'last_name', 'email', 'company')

    }

    static testMethod void test_register_event_attendee() {
      RollCall r = new RollCall();
      Date test_date = Date.newInstance(2014, 2, 17);
      r.create_event('test_event7', test_date, 'test_description', 'Open');
      Campaign c = [SELECT Id, isActive FROM Campaign WHERE Name = 'test_event7'];
      update_attendee(c.ID, String 'test', 'last_name', 'email', 'company')
    }

    static testMethod void test_check_in() {
      RollCall r = new RollCall();
      Date test_date = Date.newInstance(2014, 2, 17);
      r.create_event('test_event7', test_date, 'test_description', 'Open');
      Campaign c = [SELECT Id, isActive FROM Campaign WHERE Name = 'test_event7'];
      check_in_attendee(ID, 'test@test.com');
    }

}
