package com.example.pulsometer.Logic;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Szymon Wójcik on 2015-04-23.
 */
public class AuthenticationData implements Serializable {
    public String access_token;
    public String token_type;
    public int expires_in;
    public String userName;
    public Date issued;
    public Date expires;
}
