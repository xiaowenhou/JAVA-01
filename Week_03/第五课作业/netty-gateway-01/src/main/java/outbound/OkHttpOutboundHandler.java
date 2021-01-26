package outbound;

import io.netty.handler.codec.http.FullHttpRequest;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * 使用OkHttp客户端发送请求
 */
public class OkHttpOutboundHandler extends AbstractHttpOutboundHandler {

    @Override
    protected String sendRequest(FullHttpRequest fullHttpRequest, String webUrl) {
        System.out.println("OkHttpClient execute...");
        OkHttpClient httpClient = new OkHttpClient();
        Request request = new Request.Builder().get().url(webUrl).build();
        try {
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String resValue = response.body().string();
                System.out.println("response value is :" + resValue);
                return resValue;
            }
        } catch (IOException e) {
            System.out.println("OkHttp execute Exception。。。");
            e.printStackTrace();
        }

        return "";
    }
}
