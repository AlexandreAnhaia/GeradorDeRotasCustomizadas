package entidades;

public class Camera {

    public final double openAngleW;
    public final double openAngleH;
    public final double resolutionW;
    public final double resolutionH;
    public final double zoomMax;
    public final double timeShutter;
    //public final double megaPixel;
    //public final double trigger;
    //public final double weight;
    public final double sensorX;
    public final double sensorY;
    public final double distanceFocus;
    public final double min_interval_pictures; // 2 para o Mavic DJI Pro, 0 indica desabilitado

    public Camera(double openAngleW, double openAngleH, double resolutionW, double resolutionH, double zoomMax, double timeShutter, double sensorX, double sensorY, double distanceFocus, double min_interval_pictures) {
        this.openAngleW = openAngleW;
        this.openAngleH = openAngleH;
        this.resolutionW = resolutionW;
        this.resolutionH = resolutionH;
        this.zoomMax = zoomMax;
        this.timeShutter = timeShutter;
        //this.megaPixel = megaPixel;
        //this.trigger = trigger;
        //this.weight = weight;
        this.sensorX = sensorX;
        this.sensorY = sensorY;
        this.distanceFocus = distanceFocus;
        this.min_interval_pictures = min_interval_pictures;
    }

    public static Camera buildMavicProI() {
        return new Camera(78.8, 59.1, 4000.0, 3000.0, 1.0, 1.0 / 8000.0, 6.17, 4.5, 28.0, 2.0);
    }

    public static Camera buildMavicIIzoom() {
        return new Camera(78.8, 59.1, 4000.0, 3000.0, 2.0, 1.0 / 8000.0, 6.17, 4.5, 28.0, 2.0);
    }
}
