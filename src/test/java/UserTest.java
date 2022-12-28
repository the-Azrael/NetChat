import org.junit.Assert;
import org.junit.Test;

public class UserTest {
    @Test
    public void testUserId() {
        User user1 = new User();
        int user1Id = user1.getId();
        User user2 = new User();
        int user2Id = user2.getId();
        User user3 = new User();
        int user3Id = user3.getId();
        Assert.assertEquals(user1Id + 1, user2Id);
        Assert.assertEquals(user2Id + 1, user3Id);
    }

    @Test
    public void testUserConstruct() {
        User user1 = new User();
        User user2 = new User(String.valueOf(user1.getId()), user1.getLogin(), user1.getName(), user1.getPass());
        User user3 = new User();
        Assert.assertEquals(user1.equals(user2), Global.EQUALS);
        Assert.assertEquals(user1.equals(user3), Global.NOT_EQUALS);
        Assert.assertEquals(user2.equals(user3), Global.NOT_EQUALS);
    }

}
