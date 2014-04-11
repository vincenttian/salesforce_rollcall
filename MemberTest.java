@isTest
private class MemberTest {

    static testMethod void test_member() {
       Member one = new Member ('1','A', '', '', false);
       Member two = new Member ('2','B', '', '', false);
       boolean t = one.compareTo(two) < 0;
       System.assertEquals(t, true);
    }

}