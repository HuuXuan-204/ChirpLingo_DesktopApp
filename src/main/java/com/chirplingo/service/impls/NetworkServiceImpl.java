package com.chirplingo.service.impls;

import com.chirplingo.service.interfaces.NetworkListener;
import com.chirplingo.service.interfaces.NetworkService;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetworkServiceImpl implements NetworkService {
    
    private interface Iphlpapi extends StdCallLibrary {
        Iphlpapi INSTANCE = Native.load("iphlpapi", Iphlpapi.class, W32APIOptions.DEFAULT_OPTIONS);
        int NotifyAddrChange(Pointer handle, Pointer overlapped);
    }

    private boolean isAvailable;
    private final List<NetworkListener> listeners;
    private final ExecutorService executor;
    private final HttpClient httpClient;
    private volatile boolean running = true;
    private final String CHECK_URL = "http://connectivitycheck.gstatic.com/generate_204";

    public NetworkServiceImpl(HttpClient httpClient) {
        this.listeners = new ArrayList<>();
        this.httpClient = httpClient;
        
        this.executor = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, "NativeNetworkMonitor");
            t.setDaemon(true);
            return t;
        });

        this.isAvailable = checkRealInternet();
        
        startNativeMonitor();
    }

    private void startNativeMonitor() {
        executor.submit(() -> {
            while (running) {
                Iphlpapi.INSTANCE.NotifyAddrChange(null, null);
                
                if (!running) break;

                try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
                
                boolean currentStatus = checkRealInternet();
                if (currentStatus != isAvailable) {
                    this.isAvailable = currentStatus;
                    notifyListeners();
                }
            }
        });
    }

    private boolean checkRealInternet() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(CHECK_URL))
                    .method("HEAD", HttpRequest.BodyPublishers.noBody())
                    .timeout(Duration.ofSeconds(3))
                    .build();

            HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
            return response.statusCode() == 204;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isNetworkAvailable() {
        return isAvailable;
    }

    @Override
    public void addNetworkListener(NetworkListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public void removeNetworkListener(NetworkListener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }

    private void notifyListeners() {
        List<NetworkListener> snapshot = new ArrayList<>(listeners);
        for (NetworkListener listener : snapshot) {
            listener.onNetworkChanged(isAvailable);
        }
    }

    public void shutdown() {
        this.running = false;
        executor.shutdownNow();
    }
}
