package outbound;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.util.internal.StringUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

/**
 * 使用HttpClient发送请求
 */
public class HttpClientOutboundHandler extends AbstractHttpOutboundHandler{

    @Override
    protected String sendRequest(FullHttpRequest fullHttpRequest, String webUrl) {
        if (StringUtil.isNullOrEmpty(webUrl)) {
            return "";
        }
        System.out.println("HttpClient execute...");
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(webUrl);

        //将原请求的headers在去除Host参数之后全部放在当前请求上发送给下游服务,
        HttpHeaders headers = fullHttpRequest.headers();
        Set<String> headerNames = headers.names();
        headerNames.stream().filter(h -> !"Host".equals(h)).forEach(h -> httpGet.addHeader(h, headers.get(h)));
        System.out.println(Arrays.toString(httpGet.getAllHeaders()));


        CloseableHttpResponse response;
        try {
            response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                System.out.println(content);
                return content;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
