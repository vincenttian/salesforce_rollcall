@isTest 
private class RollCallTest {
    
    static testMethod void test_check_in() {
        Date test_date = Date.newInstance(2014, 2, 17);
        Campaign c = create_event('test_event1', test_date, 'test_description', 'Open');
        System.debug('Inserting campaign into database: ' + c);
        // Insert 
        insert c;
        // Retrieve 
        c = [SELECT Name, StartDate, Description, Status, isActive FROM Campaign WHERE Id =:c.Id];
        // Test that the trigger correctly updated the price
        System.assertEquals('test_event1', c.Name);
        System.assertEquals(test_date, c.StartDate);
        System.assertEquals('test_description', c.Description);
        System.assertEquals('Open', c.Status);
        System.assertEquals(True, c.isActive);
    }

    static testMethod void test_handle_parent_events() {
        Date test_date = Date.newInstance(2014, 2, 17);
        Campaign c = create_event('test_event1', test_date, 'test_description', 'Open');
        System.debug('Inserting campaign into database: ' + c);
        // Insert 
        insert c;
        // Retrieve 
        c = [SELECT Name, StartDate, Description, Status, isActive FROM Campaign WHERE Id =:c.Id];
        // Test that the trigger correctly updated the price
        System.assertEquals('test_event1', c.Name);
        System.assertEquals(test_date, c.StartDate);
        System.assertEquals('test_description', c.Description);
        System.assertEquals('Open', c.Status);
        System.assertEquals(True, c.isActive);
    }

}