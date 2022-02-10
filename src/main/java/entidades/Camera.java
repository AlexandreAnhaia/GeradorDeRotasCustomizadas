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

    /**
     * Construtor do objeto Camera que possui características padrão da câmera do Mavic Pro modelo I
     * @return Objeto Camera
     */
    private static Camera criaMavicPro1() {
        return new Camera(78.8, 59.1, 4000.0, 3000.0, 1.0, 1.0 / 8000.0, 6.17, 4.5, 28.0, 2.0);
    }

    /**
     * Construtor do objeto Camera que possui características padrão da câmera do Mavic II Zoom
     * @return Obeto Camera
     */
    private static Camera criaMavic2zoom() {
        return new Camera(78.8, 59.1, 4000.0, 3000.0, 2.0, 1.0 / 8000.0, 6.17, 4.5, 28.0, 2.0);
    }

    /**
     * Método auxiliar do arquivo de configuração JSON da missão
     * @param camera Modelo da câmera escolhida para a missão;
     * @return Objeto Camera
     */
    public static Camera retornaCamera(String camera) {
        switch (camera) {
            case "Mavic2Zoom":
                return criaMavic2zoom();
            case "MavicPro1":
                return criaMavicPro1();
            default:
                System.out.println("Necessário escolher uma camera para criar a rota customizada");
                return null;
        }
    }
}
