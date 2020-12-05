package appgateway;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

/**
 * AzureにApplicationGatewayを作成しBackendにサーバーを用意<br>
 * 初回接続時にレスポンスからCookieを取り出し次回以降の接続にCookieを詰め、同一のBackendサーバーへリクエストを送る<br>
 * Azureのログで接続されたサーバーを確認<br>
 * CookieのNameはApplicationGatewayAffinityとして設定されてくる<br>
 */
public class AppGateCookieVerification {

	private static Cookie appGateCookie = null;
	private static final String APP_GATE_COOKIE_NAME = "ApplicationGatewayAffinity";

	public static void main(String[] args) {

		// First request(Get Cookie)
		postRequest("http://xx.xx.xx.xx:80/", null);
		// Request with Cookie
		for (int i = 0; i < 5; i++) {
			postRequest("http://xx.xx.xx.xx:80/", appGateCookie);
		}
	}

	private static void postRequest(String url, Cookie ck) {
		HttpClient client = new HttpClient();
		HttpMethod method = new GetMethod(url);

		if (ck != null) {
			System.out.println("Cookie " + appGateCookie.getName() + "=" + appGateCookie.getValue());
			method.setRequestHeader("Cookie", appGateCookie.getName() + "=" + appGateCookie.getValue());
		}
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler(3, false));
		Cookie[] cookies = null;
		try {
			client.executeMethod(method);
			cookies = client.getState().getCookies();
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(APP_GATE_COOKIE_NAME)) {
					appGateCookie = cookie;
					System.out.println(cookie.toString());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
