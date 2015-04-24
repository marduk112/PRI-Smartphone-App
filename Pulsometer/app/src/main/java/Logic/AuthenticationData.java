package Logic;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.security.Timestamp;
import java.util.Date;

/**
 * Created by Szymon Wójcik on 2015-04-23.
 */
public class AuthenticationData {
    @JsonProperty("access_token")
    public String access_token;
    @JsonProperty("token_type")
    public String token_type;
    @JsonProperty("expires_in")
    public int expires_in;
    @JsonProperty("userName")
    public String userName;
    @JsonProperty(".issued")
    public Date issued;
    @JsonProperty(".expires")
    public Date expires;
}
