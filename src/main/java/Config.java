import entidades.Camera;
import entidades.Drone;

public class Config {

    private int pontosPreMissao = -1;
    private int direcao = 0;
    private int movimento = 0;
    private String droneString;
    private Drone droneObjeto;
    private String cameraString;
    private Camera cameraObjeto;
    private double blurFactor = 0;
    private double tamanhoCartaoSD = 0;
    private double distanciaFotos = 0;
    private double zoom = 0;
    private double sobreposicao = 0;

    public int getPontosPreMissao() {
        return pontosPreMissao;
    }

    public void setPontosPreMissao(int pontosPreMissao) {
        this.pontosPreMissao = pontosPreMissao;
    }

    public int getDirecao() {
        return direcao;
    }

    public void setDirecao(int direcao) {
        this.direcao = direcao;
    }

    public int getMovimento() {
        return movimento;
    }

    public void setMovimento(int movimento) {
        this.movimento = movimento;
    }

    public String getDroneString() {
        return droneString;
    }

    public void setDroneString(String droneString) {
        this.droneString = droneString;
    }

    public Drone getDroneObjeto() {
        return droneObjeto;
    }

    public void setDroneObjeto(Drone droneObjeto) {
        this.droneObjeto = droneObjeto;
    }

    public String getCameraString() {
        return cameraString;
    }

    public void setCameraString(String cameraString) {
        this.cameraString = cameraString;
    }

    public Camera getCameraObjeto() {
        return cameraObjeto;
    }

    public void setCameraObjeto(Camera cameraObjeto) {
        this.cameraObjeto = cameraObjeto;
    }

    public double getBlurFactor() {
        return blurFactor;
    }

    public void setBlurFactor(double blurFactor) {
        this.blurFactor = blurFactor;
    }

    public double getTamanhoCartaoSD() {
        return tamanhoCartaoSD;
    }

    public void setTamanhoCartaoSD(double tamanhoCartaoSD) {
        this.tamanhoCartaoSD = tamanhoCartaoSD;
    }

    public double getDistanciaFotos() {
        return distanciaFotos;
    }

    public void setDistanciaFotos(double distanciaFotos) {
        this.distanciaFotos = distanciaFotos;
    }

    public double getZoom() {
        return zoom;
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
    }

    public double getSobreposicao() {
        return sobreposicao;
    }

    public void setSobreposicao(double sobreposicao) {
        this.sobreposicao = sobreposicao;
    }
}
