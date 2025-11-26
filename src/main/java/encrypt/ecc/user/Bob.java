package encrypt.ecc.user;

public class Bob extends BaseUser {
    private static final String NAME = "Bob";

    public Bob() {
    }

    @Override
    public String getName() {
        return NAME;
    }
}
