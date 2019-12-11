package com.gmall;


public class GmallPassportWebApplicationTests {

    public static void main(String[] args) {
        String getCode = "https://api.weibo.com/oauth2/authorize?client_id=1831898356&response_type=code&redirect_uri=http://gmall.com/vlogin";
        String code = "c04ce6bedcb1cc5405a24931d7542cc4";
        String getAccessToken = "https://api.weibo.com/oauth2/access_token?client_id=1831898356&client_secret=9f29c688c517cb91814a9fd50a7bc48c&grant_type=authorization_code&redirect_uri=http://gmall.com/vlogin&code=c04ce6bedcb1cc5405a24931d7542cc4";
        String accessToken = "access_token : 2.00RpWvvGg89yzBfb66f35d1aNtUpIC"+
                            "remind_in : 157679999" +
                            "expires_in : 157679999" +
                            "uid : 6352759027" +
                            "isRealName : true";
         String getUserInfo = "https://api.weibo.com/2/users/show.json?uid=6352759027&access_token=2.00RpWvvGg89yzBfb66f35d1aNtUpIC";
    }

}
