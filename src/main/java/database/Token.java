package database;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Token {

    private final UUID token;
    private Date expiration;

    public Token(UUID token) {

        this.token = token;

        Calendar exp = Calendar.getInstance();
        exp.add(Calendar.MINUTE, 3);
        this.expiration = exp.getTime();
    }

    public Token() {

        this.token = UUID.randomUUID();

        Calendar exp = Calendar.getInstance();
        exp.add(Calendar.HOUR, 3);
        this.expiration = exp.getTime();
    }

    public Token resetExpiration() {
        Calendar exp = Calendar.getInstance();
        exp.add(Calendar.HOUR, 3);
        this.expiration = exp.getTime();

        return this;
    }

    public UUID getToken() {
        return token;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Token))
            return false;
        if (obj == this)
            return true;
        return this.token.equals(((Token) obj).getToken());
    }
}
