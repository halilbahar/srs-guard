package at.htl.srsguard.model;

import at.htl.srsguard.entity.Permission;

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
        if (o instanceof AppStream) {
            AppStream appStream = (AppStream) o;
            return app.equals(appStream.app) &&
                    stream.equals(appStream.stream);
        } else if (o instanceof Permission) {
            Permission permission = (Permission) o;
            return permission.getApp().getName().equals(this.app) &&
                    permission.getStream().getName().equals(this.stream);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(app, stream);
    }
}
