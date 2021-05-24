package classes.database;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class Token {

    private UUID token;
    private Date expiration;

    private Token() {}

    //Construct a token from a UUID string 
    public Token(String token) {
        this.token = UUID.fromString(token);
    }

    /**
    @return randomly generated token with a default of 3 hour expiration period.
    **/
    public static Token randomToken() {

        Token token = new Token();

        Calendar exp = Calendar.getInstance();
        exp.add(Calendar.HOUR, 3);

        token.expiration = exp.getTime();
        token.token = UUID.randomUUID();

        return token;
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
