@isTest 
private class RollCallTest {
    
    static testMethod void test_create_event() {
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

    static testMethod void test_create_child_event() {
        x b = new x(Name='Behind the Cloud', Price__c=100);
        System.debug('Price before inserting new book: ' + b.Price__c);
        // Insert 
        insert b;
        // Retrieve 
        b = [SELECT a FROM  WHERE Id =:b.Id];
        System.debug('Price after trigger fired: ' + b.Price__c);
        // Test that the trigger correctly updated the price
   System.assertEquals(90, b.Price__c);
    }

    static testMethod void test_delete_event() {
        x b = new x(Name='Behind the Cloud', Price__c=100);
        System.debug('Price before inserting new book: ' + b.Price__c);
        // Insert 
        insert b;
        // Retrieve 
        b = [SELECT a FROM  WHERE Id =:b.Id];
        System.debug('Price after trigger fired: ' + b.Price__c);
        // Test that the trigger correctly updated the price
        System.assertEquals(90, b.Price__c);
    }

    static testMethod void test_end_event() {
        x b = new x(Name='Behind the Cloud', Price__c=100);
        System.debug('Price before inserting new book: ' + b.Price__c);
        // Insert 
        insert b;
        // Retrieve 
        b = [SELECT a FROM  WHERE Id =:b.Id];
        System.debug('Price after trigger fired: ' + b.Price__c);
        // Test that the trigger correctly updated the price
        System.assertEquals(90, b.Price__c);
    }

}