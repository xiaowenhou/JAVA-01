package outbound;

import io.netty.handler.codec.http.FullHttpRequest;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * 使用HttpClient发送请求
 */
public class HttpClientOutboundHandler extends AbstractHttpOutboundHandler{

    @Override
    protected String sendRequest(FullHttpRequest fullHttpRequest, String webUrl) {
        System.out.println("HttpClient execute...");
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(webUrl);
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
