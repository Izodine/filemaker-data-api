package com.joselopezrosario.androidfm;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class LoginOAuth {
    private static final String ENDPOINT = BuildConfig.API_HOST;
    private static final String OAUTH_REQUEST_ID = BuildConfig.OAUTH_REQUEST_ID;
    private static final String OAUTH_IDENTIFIER = BuildConfig.OAUTH_IDENTIFIER;

    @Test
    public void loginOAuth() {
        System.out.println("-----------------------");
        System.out.println("Login via OAuth");
        System.out.println("-----------------------");
        String isRunningOnTravis = System.getenv("CI");
        if ( isRunningOnTravis != null && isRunningOnTravis.equals("true")){
            System.out.println("Running on Travis... skip test");
            assert true;
        }else{
            FmRequest request = new FmRequest()
                    .loginOAuth(ENDPOINT, OAUTH_REQUEST_ID, OAUTH_IDENTIFIER)
                    .build();
            FmResponse response = Fm.execute(request);
            String token = response.getToken();
            System.out.println("Token: " + token);
            assert response.isOk();
        }
    }
}