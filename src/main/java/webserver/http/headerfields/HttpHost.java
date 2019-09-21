package webserver.http.headerfields;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class HttpHost implements HttpHeaderField {
    private static final Map<String, HttpHost> CACHE = new HashMap<>();

    private final String name;
    private final HttpPort port;

    public static Optional<HttpHost> of(String input) {
        final String escaped = input.trim();
        if (CACHE.containsKey(escaped)) {
            return Optional.of(CACHE.get(escaped));
        }
        if (escaped.contains(" ")) {
            return Optional.empty();
        }
        final String[] hostnameAndPort = escaped.split(":");
        if (hostnameAndPort.length == 1) {
            final HttpHost host = new HttpHost(escaped, HttpPort.PORT_80);
            CACHE.put(escaped, host);
            return Optional.of(host);
        }
        return HttpPort.of(hostnameAndPort[1]).map(port -> {
            final HttpHost host = new HttpHost(hostnameAndPort[0], port);
            CACHE.put(escaped, host);
            return host;
        });
    }

    private HttpHost(String name, HttpPort port) {
        this.name = name;
        this.port = port;
    }

    @Override
    public String toString() {
        return this.name + ":" + this.port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HttpHost)) {
            return false;
        }
        final HttpHost rhs = (HttpHost) o;
        return Objects.equals(this.name, rhs.name) &&
                Objects.equals(this.port, rhs.port);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.port);
    }
}