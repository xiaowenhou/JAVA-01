package outbound;

import cn.hutool.core.thread.NamedThreadFactory;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.util.internal.StringUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.*;

/**
 * 使用OkHttp客户端发送请求
 */
public class OkHttpOutboundHandler extends AbstractHttpOutboundHandler {

    private final ExecutorService proxyService;

    public OkHttpOutboundHandler(){
        //核心线程数设置为CPU核心数，因为虽然是IO密集，但是也有其他的应用在抢占资源
        int cores = Runtime.getRuntime().availableProcessors();
        //保持线程数时间
        long keepAliveTime = 5000;
        //阻塞队列的长度
        int queueSize = 2048;
        //定义线程池的拒绝策略为调用线程自己执行
        RejectedExecutionHandler rejectHandler = new ThreadPoolExecutor.CallerRunsPolicy();
        this.proxyService = new ThreadPoolExecutor(cores, cores,
                keepAliveTime, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(queueSize),
                //线程名称为proxyService， 不是守护线程
                new NamedThreadFactory("proxyService", false),
                rejectHandler);

    }


    @Override
    protected String sendRequest(FullHttpRequest fullHttpRequest, String webUrl) {
        if (StringUtil.isNullOrEmpty(webUrl)) {
            return "";
        }
        System.out.println("OkHttpClient execute...");

        OkHttpClient httpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();

        //将原请求的headers在去除Host参数之后全部放在当前请求上发送给下游服务,
        HttpHeaders headers = fullHttpRequest.headers();
        Set<String> headerNames = headers.names();
        headerNames.stream().filter(h -> !"Host".equals(h)).forEach(h -> builder.header(h, headers.get(h)));
        Request request = builder.get().url(webUrl).build();
        System.out.println(request.headers().toString());


        //通过线程池调用， 并且将结果从Future中获取到
        Future<String> future = proxyService.submit(() -> fetchGet(httpClient, request));
        String result;
        try {
            //只等待3秒钟
            result = future.get(3000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
            return "";
        }
        return result;
    }


    private String fetchGet(final OkHttpClient httpClient, final Request request) {
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