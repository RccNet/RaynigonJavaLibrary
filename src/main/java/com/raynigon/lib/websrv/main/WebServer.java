import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

import com.raynigon.lib.websrv.handlers.HttpHandler;
import com.raynigon.lib.websrv.handlers.HttpToWebSocketHandler;
import com.raynigon.lib.websrv.handlers.PathMatchHandler;
import com.raynigon.lib.websrv.handlers.WebServerHandler;
import com.raynigon.lib.websrv.handlers.WebSocketHandler;
import com.raynigon.lib.websrv.utils.NamingThreadFactory;
import com.raynigon.lib.websrv.utils.SslFactory;

import io.netty.bootstrap.ChannelFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class WebServer{

    private static final long               DEFAULT_STALE_CONNECTION_TIMEOUT = 5000;

    private final SocketAddress             socketAddress;
    private final URI                       publicUri;
    private final List<WebServerHandler>         handlers                         = new ArrayList<WebServerHandler>();
    private final List<ExecutorService>     executorServices                 = new ArrayList<ExecutorService>();
    private final Executor                  executor;

    private ServerBootstrap                 bootstrap;
    private Channel                         channel;
    private SSLContext                      sslContext;

    protected long                          nextId                           = 1;
    private Thread.UncaughtExceptionHandler exceptionHandler;
    private Thread.UncaughtExceptionHandler ioExceptionHandler;
    private long                            staleConnectionTimeout           = DEFAULT_STALE_CONNECTION_TIMEOUT;
    private int                             maxInitialLineLength             = 4096;
    private int                             maxHeaderSize                    = 8192;
    private int                             maxChunkSize                     = 8192;
    private int                             maxContentLength                 = 65536;

    public WebServer(int port){
        this(Executors.newSingleThreadScheduledExecutor(new NamingThreadFactory("WEBBIT-HANDLER-THREAD")), port);
    }

    private WebServer(ExecutorService executorService, int port){
        this((Executor) executorService, port);
        // If we created the executor, we have to be responsible for tearing it down.
        executorServices.add(executorService);
    }

    public WebServer(final Executor executor, int port){
        this(executor, new InetSocketAddress(port), localUri(port));
    }

    public WebServer(final Executor executor, SocketAddress socketAddress, URI publicUri){
        this.executor = executor;
        this.socketAddress = socketAddress;
        this.publicUri = publicUri;

        setupDefaultHandlers();
    }

    protected void setupDefaultHandlers(){
        /*add(new ServerHeaderHandler("Webbit"));
        add(new DateHeaderHandler());*/
    }

    public WebServer setupSsl(InputStream keyStore, String pass){
        return this.setupSsl(keyStore, pass, pass);
    }

    public WebServer setupSsl(InputStream keyStore, String storePass, String keyPass) {
        this.sslContext = new SslFactory(keyStore, storePass).getServerContext(keyPass);
        return this;
    }

    public URI getUri(){
        return publicUri;
    }

    public int getPort(){
        if(publicUri.getPort() == -1){
            return publicUri.getScheme().equalsIgnoreCase("https") ? 443 : 80;
        }
        return publicUri.getPort();
    }

    public Executor getExecutor(){
        return executor;
    }

    public WebServer staleConnectionTimeout(long millis){
        staleConnectionTimeout = millis;
        return this;
    }

    public WebServer add(WebServerHandler handler){
        handlers.add(handler);
        return this;
    }

    public WebServer add(String path, WebServerHandler handler){
        return add(new PathMatchHandler(path, handler));
    }

    public WebServer add(String path, WebSocketHandler handler){
        return add(path, new HttpToWebSocketHandler(handler));
    }

    public Future<WebServer> start(){
        FutureTask<WebServer> future = new FutureTask<WebServer>(new Callable<WebServer>(){
            @Override
            public WebServer call() throws Exception{
                if(isRunning()){
                    throw new IllegalStateException("Server already started.");
                }

                // Configure the server.
                bootstrap = new ServerBootstrap();

                bootstrap.channel(NioServerSocketChannel.class);
                
                bootstrap.childHandler(new ChannelInitializer<SocketChannel>(){

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception{
                        long timestamp = timestamp();
                        Object id = nextId();
                        if(sslContext != null){
                            SSLEngine sslEngine = sslContext.createSSLEngine();
                            sslEngine.setUseClientMode(false);
                            SslHandler ssl = new SslHandler(sslEngine);
                            ssl.setCloseOnSSLException(true);
                            pipeline.addLast("ssl", ssl);
                        }
                        ch.addLast("staleconnectiontracker", staleConnectionTrackingHandler);
                        ch.addLast("connectiontracker", connectionTrackingHandler);
                        ch.addLast("flashpolicydecoder", new FlashPolicyFileDecoder(executor, exceptionHandler, ioExceptionHandler, getPort()));
                        ch.addLast("decoder", new HttpRequestDecoder(maxInitialLineLength, maxHeaderSize, maxChunkSize));
                        ch.addLast("aggregator", new HttpChunkAggregator(maxContentLength));
                        ch.addLast("decompressor", new HttpContentDecompressor());
                        ch.addLast("encoder", new HttpResponseEncoder());
                        ch.addLast("compressor", new HttpContentCompressor());
                        ch.addLast("handler", new NettyHttpChannelHandler(executor, handlers, id, timestamp, exceptionHandler, ioExceptionHandler));
                    }
                });
                
        

                staleConnectionTrackingHandler = new StaleConnectionTrackingHandler(staleConnectionTimeout, executor);
                ScheduledExecutorService staleCheckExecutor = Executors.newSingleThreadScheduledExecutor(
                        new NamingThreadFactory("WEBBIT-STALE-CONNECTION-CHECK-THREAD"));
                staleCheckExecutor.scheduleWithFixedDelay(new Runnable(){
                    @Override
                    public void run(){
                        staleConnectionTrackingHandler.closeStaleConnections();
                    }
                }, staleConnectionTimeout / 2, staleConnectionTimeout / 2, TimeUnit.MILLISECONDS);
                executorServices.add(staleCheckExecutor);

                connectionTrackingHandler = new ConnectionTrackingHandler();
                ExecutorService bossExecutor = Executors
                        .newSingleThreadExecutor(new NamingThreadFactory("WEBBIT-BOSS-THREAD"));
                executorServices.add(bossExecutor);
                ExecutorService workerExecutor = Executors
                        .newSingleThreadExecutor(new NamingThreadFactory("WEBBIT-WORKER-THREAD"));
                executorServices.add(workerExecutor);
                bootstrap.setFactory(new NioServerSocketChannelFactory(bossExecutor, workerExecutor, 1));
                channel = bootstrap.bind(socketAddress);
                return WebServer.this;
            }
        });
        // don't use Executor here - it's just another resource we need to manage -
        // thread creation on startup should be fine
        final Thread thread = new Thread(future, "WEBBIT-STARTUP-THREAD");
        thread.start();
        return future;
    }

    public boolean isRunning(){
        return channel != null && channel.isBound();
    }


    public Future<WebServer> stop(){
        FutureTask<WebServer> future = new FutureTask<WebServer>(new Callable<WebServer>(){
            public WebServer call() throws Exception{
                if(channel != null){
                    channel.close();
                }
                if(connectionTrackingHandler != null){
                    connectionTrackingHandler.closeAllConnections();
                    connectionTrackingHandler = null;
                }
                if(bootstrap != null){
                    bootstrap.releaseExternalResources();
                }

                // shut down all services & give them a chance to terminate
                for(ExecutorService executorService : executorServices){
                    shutdownAndAwaitTermination(executorService);
                }

                bootstrap = null;

                if(channel != null){
                    channel.getCloseFuture().await();
                }

                return WebServer.this;
            }
        });
        // don't use Executor here - it's just another resource we need to manage -
        // thread creation on shutdown should be fine
        final Thread thread = new Thread(future, "WEBBIT-SHUTDOW-THREAD");
        thread.start();
        return future;
    }

    // See JavaDoc for ExecutorService
    private void shutdownAndAwaitTermination(ExecutorService executorService){
        executorService.shutdown(); // Disable new tasks from being submitted
        try{
            // Wait a while for existing tasks to terminate
            if(!executorService.awaitTermination(5, TimeUnit.SECONDS)){
                executorService.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if(!executorService.awaitTermination(5, TimeUnit.SECONDS)){
                    System.err.println("ExecutorService did not terminate");
                }
            }
        }catch(InterruptedException ie){
            // (Re-)Cancel if current thread also interrupted
            executorService.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }

    public WebServer uncaughtExceptionHandler(Thread.UncaughtExceptionHandler exceptionHandler){
        this.exceptionHandler = exceptionHandler;
        return this;
    }

    public WebServer connectionExceptionHandler(Thread.UncaughtExceptionHandler ioExceptionHandler){
        this.ioExceptionHandler = ioExceptionHandler;
        return this;
    }

    /**
     * @see HttpRequestDecoder
     */
    public WebServer maxChunkSize(int maxChunkSize){
        this.maxChunkSize = maxChunkSize;
        return this;
    }

    /**
     * @see HttpChunkAggregator
     */
    public WebServer maxContentLength(int maxContentLength){
        this.maxContentLength = maxContentLength;
        return this;
    }

    /**
     * @see HttpRequestDecoder
     */
    public WebServer maxHeaderSize(int maxHeaderSize){
        this.maxHeaderSize = maxHeaderSize;
        return this;
    }

    /**
     * @see HttpRequestDecoder
     */
    public WebServer maxInitialLineLength(int maxInitialLineLength){
        this.maxInitialLineLength = maxInitialLineLength;
        return this;
    }

    private static URI localUri(int port){
        try{
            return URI.create(
                    "http://" + InetAddress.getLocalHost().getHostName() + (port == 80 ? "" : (":" + port)) + "/");
        }catch(UnknownHostException e){
            throw new RuntimeException(
                    "can not create URI from localhost hostname - use constructor to pass an explicit URI", e);
        }
    }

    protected long timestamp(){
        return System.currentTimeMillis();
    }

    protected Object nextId(){
        return nextId++;
    }
}