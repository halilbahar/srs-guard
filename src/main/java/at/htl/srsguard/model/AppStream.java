package at.htl.srsguard.model;

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
}
