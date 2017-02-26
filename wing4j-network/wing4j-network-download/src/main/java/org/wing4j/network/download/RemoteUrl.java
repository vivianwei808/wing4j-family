package org.wing4j.network.download;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 远程对象URL表示，包括Host地址 和 URL
 */
@Slf4j
@Data
@ToString
public class RemoteUrl {
    String url;
    List<String> hosts;
    List<URL> urls = new ArrayList<URL>();

    public RemoteUrl(String url, List<String> hosts) {
        this.url = url;
        this.hosts = hosts;
        for (String host : hosts) {
            try {
                if (!host.contains("http://")) {
                    host = "http://" + host;
                }
                urls.add(new URL(host + url));
            } catch (MalformedURLException e) {
                log.error(e.toString());
            }
        }
    }
}