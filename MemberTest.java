@isTest
private class MemberTest {

    static testMethod void test_member() {
       Member one = new Member ('A', '', '', false);
       Member two = new Member ('B', '', '', false);
       boolean t = one.compareTo(two) < 0;
       System.assertEquals(t, true);
    }

}