package at.htl.srsguard.model;

import java.util.Objects;

public class AppStream {
    private String app;
    private String stream;

    public AppStream(String app, String stream) {
        this.app = app;
        this.stream = stream;
    }

    public AppStream() {
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppStream)) return false;
        AppStream appStream = (AppStream) o;
        return app.equals(appStream.app) &&
                stream.equals(appStream.stream);
    }

    @Override
    public int hashCode() {
        return Objects.hash(app, stream);
    }
}
