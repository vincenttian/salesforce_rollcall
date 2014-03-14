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
        Date test_date = Date.newInstance(2014, 2, 17);
        Campaign parent_campaign = create_event('test_event2', test_date, 'test_description', 'Open');
        System.debug('Inserting parent campaign into database: ' + parent_campaign);
        // Insert parent campaign
        insert parent_campaign;

        Date test_date = Date.newInstance(2014, 2, 17);
        Campaign child_campaign = create_child_event('test_event2', test_date, 'test_description', 'Open', parent_campaign.Id);
        System.debug('Inserting parent campaign into database: ' + child_campaign);
        // Insert child campaign
        insert child_campaign;
        // Retrieve child
        child = [SELECT ParentId FROM Campaign WHERE Id =:child_campaign.Id];
        // Test that the child event's parentId matches
       System.assertEquals(child_campaign.Id, parent_campaign.Id);
    }

    static testMethod void test_delete_event() {
        Date test_date = Date.newInstance(2014, 2, 17);
        Campaign campaign = create_event('test_event1', test_date, 'test_description', 'Open');
        System.debug('Inserting campaign into database: ' + c);
        // Insert
        insert campaign;
        // Retrieve
        Campaign[] c = [SELECT Name, StartDate, Description, Status, isActive FROM Campaign WHERE Id =:campaign.Id];
        System.assertEquals(1, c.size());
        // Deleting event
        delete_event(campaign.Id);
        Campaign[] c = [SELECT Name, StartDate, Description, Status, isActive FROM Campaign WHERE Id =:campaign.Id];
        // Test that campaign no longer exists in the database
        System.assertEquals(0, c.size());
    }

    static testMethod void test_end_event() {
        Date test_date = Date.newInstance(2014, 2, 17);
        Campaign campaign = create_event('test_event1', test_date, 'test_description', 'Open');
        System.debug('Inserting campaign into database: ' + c);
        // Insert
        insert campaign;
        // Retrieve
        campaign = [SELECT Campaign FROM Campaign WHERE Id =:campaign.Id];
        // Check that the event is active
        System.assertEquals(True, campaign.isActive);
        // End event
        end_event(campaign.Id);
        // Test that the event is no longer active
        System.assertEquals(False, campaign.isActive);
    }

    static testMethod void test_get_events() {
        Date test_date = Date.newInstance(2014, 2, 17);
        Campaign campaign = create_event('test_event1', test_date, 'test_description', 'Open');
        System.debug('Inserting campaign into database: ' + c);
        // Insert
        insert campaign;
        Event[] e = getEvents();
        System.assertNotEquals(null, e);
    }
}
