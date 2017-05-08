package io.smartcat.ranger.data.generator;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import io.smartcat.ranger.data.generator.model.User;

public class SubSetRuleTest {

    @Test
    public void should_set_set_property() {
        ObjectGenerator<User> userGenerator = new ObjectGenerator.Builder<User>(User.class)
                .randomFrom("username", "Melkor").randomSubsetFrom("nicknames", "Belegurth", "Morgoth")
                .toBeGenerated(1000).build();

        List<User> result = userGenerator.generateAll();

        Assert.assertEquals(1000, result.size());

        boolean atLeastOneEmptySet = false;
        boolean atLeastOneWithSetOfSizeOne = false;
        boolean atLeastOneWithSetOfSizeTwo = false;

        for (User u : result) {
            Assert.assertEquals("Melkor", u.getUsername());
            if (u.getNicknames().isEmpty()) {
                atLeastOneEmptySet = true;
            } else if (u.getNicknames().size() == 1) {
                atLeastOneWithSetOfSizeOne = true;

                String nickname = u.getNicknames().stream().findFirst().get();
                boolean nicknameIsBelegurth = nickname.equals("Belegurth");
                boolean nicknameIsMorgoth = nickname.equals("Morgoth");
                Assert.assertTrue("Melkor must have nickname either Belegurth or Morgoth, but was" + nickname,
                        nicknameIsBelegurth || nicknameIsMorgoth);
            } else {
                atLeastOneWithSetOfSizeTwo = true;
                Assert.assertEquals(u.getNicknames().size(), 2);
                Assert.assertTrue("both nicknames must be present in the set",
                        u.getNicknames().contains("Belegurth") && u.getNicknames().contains("Morgoth"));
            }
        }

        Assert.assertTrue("should be at least one with empty list.", atLeastOneEmptySet);
        Assert.assertTrue("should be at least one with list of size one.", atLeastOneWithSetOfSizeOne);
        Assert.assertTrue("should be at least one with list of size two.", atLeastOneWithSetOfSizeTwo);
    }
}
