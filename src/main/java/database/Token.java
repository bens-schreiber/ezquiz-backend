package database;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class Token {

    private final UUID token;
    private Date expiration;

    public Token() {

        this.token = UUID.randomUUID();

        Calendar exp = Calendar.getInstance();
        exp.add(Calendar.HOUR, 3);
        this.expiration = exp.getTime();
    }

    //For getting token from a request only
    public Token(String token) {
        this.token = UUID.fromString(token);
    }

    public void resetExpiration() {
        Calendar exp = Calendar.getInstance();
        exp.add(Calendar.HOUR, 3);
        this.expiration = exp.getTime();

    }

    public boolean isExpired() {
        return this.getExpiration().before(Calendar.getInstance().getTime());
    }

    public Date getExpiration() {
        return expiration;
    }

    public UUID getToken() {
        return token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token1 = (Token) o;
        if (token.equals(token1.token)) {
            if (!token1.isExpired()) {
                token1.resetExpiration();
                return true;
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(token);
    }
}
