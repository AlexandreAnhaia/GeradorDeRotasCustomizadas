package entidades;

public class Missao {

    public static final int HORIZONTAL_DIRECTION = 1;
    public static final int VERTICAL_DIRECTION = 2;
    public static final int FORWARD_MOVIMENT = 3;
    public static final int BACKWARD_MOVIMENT = 4;

    public final int direction;
    public final int moviment;
    public final Drone drone;
    public final Camera cam;
    public final AreaCartesiana area;
    public final double cardSD;
    public final double distancePicture;
    public final double zoom;
    public final double sobrePosicao;
    public final double blurFactor;
    public final double width;
    public final double height;
    public final double velocityShutter;
    public final double picturePrecision;
    public final int nVoltas;
    public final double larguraVolta;
    public final double comprimentoVolta;
    public final double camPitch;

    public int extra_pre;
    public int extra_pos;

    public Missao(int direction, int moviment, Drone drone, Camera cam, AreaCartesiana area, double blurFactor, double cardSD, double distancePicture, double zoom, double sobrePosicao) {
        this.direction = direction;
        this.moviment = moviment;
        this.drone = drone;
        this.cam = cam;
        this.area = area;
        this.cardSD = cardSD;
        this.distancePicture = distancePicture;
        this.zoom = zoom;
        this.sobrePosicao = sobrePosicao;
        this.blurFactor = blurFactor;

        this.width = photoLengthOnGround(distancePicture, cam.openAngleW, zoom);
        this.height = photoLengthOnGround(distancePicture, cam.openAngleH, zoom);

        velocityShutter = this.width * this.blurFactor / (cam.resolutionW * cam.timeShutter);

        if (direction == HORIZONTAL_DIRECTION) {
            picturePrecision = this.width * 1000 / cam.resolutionW;
            nVoltas = calcNumeroVoltas(area.hipotenusa, this.height, this.sobrePosicao);
            larguraVolta = area.hipotenusa / (nVoltas - 1);
            comprimentoVolta = area.comprimentoBase;
        } else {
            picturePrecision = this.height * 1000 / cam.resolutionH;
            nVoltas = calcNumeroVoltas(area.comprimentoBase, this.width, this.sobrePosicao);
            larguraVolta = area.comprimentoBase / (nVoltas - 1);
            comprimentoVolta = area.hipotenusa;
        }
        double dVertical = Math.abs(area.p3.z - area.p2.z);
        double dProfundidade = Math.sqrt(Math.pow(area.p3.x - area.p2.x, 2) + Math.pow(area.p3.y - area.p2.y, 2));
        this.camPitch = -90 + Math.atan2(dVertical, dProfundidade) * 180 / Math.PI;
        if(this.camPitch<-90 || this.camPitch>0){
            throw new IllegalStateException("Invalid cam Pitch = "+this.camPitch);
        }
    }

    private static int calcNumeroVoltas(double hipoenusa, double largura, double sobrePosicao) {
        int resultado = (int) (0.8 + hipoenusa / (largura * (1 - sobrePosicao)));
        return resultado + 1;
    }

    public static double photoLengthOnGround(double distanciaFoto, double aberturaCamera, double zoom) {
        double resultado = 2 * distanciaFoto * Math.tan((aberturaCamera / (2*zoom)) * Math.PI / 180);
        return resultado;
    }
}
