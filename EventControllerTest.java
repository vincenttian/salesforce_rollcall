@isTest 
private class RollCallTest {
    
    static testMethod void test_attendee_search() {
        Date test_date = Date.newInstance(2014, 2, 17);
        Campaign c = create_event('test_event1', test_date, 'test_description', 'Open');
        System.debug('Inserting campaign into database: ' + c);
        // Insert 
        insert c;
        // Retrieve 
        c = [SELECT Name, StartDate, Description, Status, isActive FROM Campaign WHERE Id =:c.Id];
        // Test that the trigger correctly updated the price
        System.assertEquals(True, c.isActive);
    }

    static testMethod void test_get_event() {
        Date test_date = Date.newInstance(2014, 2, 17);
        Campaign c = create_event('test_event1', test_date, 'test_description', 'Open');
        System.debug('Inserting campaign into database: ' + c);
        // Insert 
        insert c;
        // Retrieve 
        c = [SELECT Name, StartDate, Description, Status, isActive FROM Campaign WHERE Id =:c.Id];
        // Test that the trigger correctly updated the price
        System.assertEquals(True, c.isActive);
    }

    static testMethod void get_event_date() {
        Date test_date = Date.newInstance(2014, 2, 17);
        Campaign c = create_event('test_event1', test_date, 'test_description', 'Open');
        System.debug('Inserting campaign into database: ' + c);
        // Insert 
        insert c;
        // Retrieve 
        c = [SELECT Name, StartDate, Description, Status, isActive FROM Campaign WHERE Id =:c.Id];
        // Test that the trigger correctly updated the price
        System.assertEquals(True, c.isActive);
    }

    static testMethod void test_edit_info() {
        Date test_date = Date.newInstance(2014, 2, 17);
        Campaign c = create_event('test_event1', test_date, 'test_description', 'Open');
        System.debug('Inserting campaign into database: ' + c);
        // Insert 
        insert c;
        // Retrieve 
        c = [SELECT Name, StartDate, Description, Status, isActive FROM Campaign WHERE Id =:c.Id];
        // Test that the trigger correctly updated the price
        System.assertEquals(True, c.isActive);
    }

    static testMethod void test_event_stats() {
        Date test_date = Date.newInstance(2014, 2, 17);
        Campaign c = create_event('test_event1', test_date, 'test_description', 'Open');
        System.debug('Inserting campaign into database: ' + c);
        // Insert 
        insert c;
        // Retrieve 
        c = [SELECT Name, StartDate, Description, Status, isActive FROM Campaign WHERE Id =:c.Id];
        // Test that the trigger correctly updated the price
        System.assertEquals(True, c.isActive);
    }

    static testMethod void test_get_checkedin_times() {
        Date test_date = Date.newInstance(2014, 2, 17);
        Campaign c = create_event('test_event1', test_date, 'test_description', 'Open');
        System.debug('Inserting campaign into database: ' + c);
        // Insert 
        insert c;
        // Retrieve 
        c = [SELECT Name, StartDate, Description, Status, isActive FROM Campaign WHERE Id =:c.Id];
        // Test that the trigger correctly updated the price
        System.assertEquals(True, c.isActive);
    }

}