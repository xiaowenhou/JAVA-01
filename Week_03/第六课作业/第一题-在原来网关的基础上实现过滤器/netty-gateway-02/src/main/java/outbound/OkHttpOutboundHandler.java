package outbound;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Set;

/**
 * 使用OkHttp客户端发送请求
 */
public class OkHttpOutboundHandler extends AbstractHttpOutboundHandler {

    @Override
    protected String sendRequest(FullHttpRequest fullHttpRequest, String webUrl) {
        System.out.println("OkHttpClient execute...");

        OkHttpClient httpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();

        //将原请求的headers在去除Host参数之后全部放在当前请求上发送给下游服务,
        HttpHeaders headers = fullHttpRequest.headers();
        Set<String> headerNames = headers.names();
        headerNames.stream().filter(h -> !"Host".equals(h)).forEach(h -> builder.header(h, headers.get(h)));
        Request request = builder.get().url(webUrl).build();
        System.out.println(request.headers().toString());


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
