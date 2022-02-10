package entidades;

public class Missao {

    public static final int HORIZONTAL_DIRECTION = 1;
    public static final int VERTICAL_DIRECTION = 2;
    public static final int FORWARD_MOVIMENT = 3;
    public static final int BACKWARD_MOVIMENT = 4;

    public final int direcao;
    public final int movimento;
    public final Drone drone;
    public final Camera camera;
    public final AreaCartesiana area;
    public final double cartaoSD;
    public final double distanciaFoto;
    public final double zoom;
    public final double sobrePosicao;
    public final double blurFactor;
    public final double largura;
    public final double altura;
    public final double velocidadeObturador;
    public final double precisaoImagem;
    public final int numeroVoltas;
    public final double larguraVolta;
    public final double comprimentoVolta;
    public final double camPitch;

    public int extra_pre;
    public int extra_pos;

    public Missao(int direction, int moviment, Drone drone, Camera cam, AreaCartesiana area, double blurFactor, double cardSD, double distancePicture, double zoom, double sobrePosicao) {
        this.direcao = direction;
        this.movimento = moviment;
        this.drone = drone;
        this.camera = cam;
        this.area = area;
        this.cartaoSD = cardSD;
        this.distanciaFoto = distancePicture;
        this.zoom = zoom;
        this.sobrePosicao = sobrePosicao;
        this.blurFactor = blurFactor;
        this.largura = photoLengthOnGround(distancePicture, cam.openAngleW, zoom);
        this.altura = photoLengthOnGround(distancePicture, cam.openAngleH, zoom);

        velocidadeObturador = this.largura * this.blurFactor / (cam.resolutionW * cam.timeShutter);

        if (direction == HORIZONTAL_DIRECTION) {
            precisaoImagem = this.largura * 1000 / cam.resolutionW;
            numeroVoltas = calculaNumeroVoltas(area.hipotenusa, this.altura, this.sobrePosicao);
            larguraVolta = area.hipotenusa / (numeroVoltas - 1);
            comprimentoVolta = area.comprimentoBase;
        } else {
            precisaoImagem = this.altura * 1000 / cam.resolutionH;
            numeroVoltas = calculaNumeroVoltas(area.comprimentoBase, this.largura, this.sobrePosicao);
            larguraVolta = area.comprimentoBase / (numeroVoltas - 1);
            comprimentoVolta = area.hipotenusa;
        }
        double dVertical = Math.abs(area.ponto3.z - area.ponto2.z);
        double dProfundidade = Math.sqrt(Math.pow(area.ponto3.x - area.ponto2.x, 2) + Math.pow(area.ponto3.y - area.ponto2.y, 2));
        this.camPitch = -90 + Math.atan2(dVertical, dProfundidade) * 180 / Math.PI;
        if (this.camPitch < -90 || this.camPitch > 0) {
            throw new IllegalStateException("Invalid cam Pitch = " + this.camPitch);
        }
    }

    /**
     * Calcula o número total de voltas com base nas informações passadas como parâmetro
     * @param hipotenusa
     * @param largura
     * @param sobrePosicao
     * @return
     */
    private static int calculaNumeroVoltas(double hipotenusa, double largura, double sobrePosicao) {
        int resultado = (int) (0.8 + hipotenusa / (largura * (1 - sobrePosicao)));
        return resultado + 1;
    }

    /**
     * Calcula a largura da foto, com base na distância e na abertura da câmera
     * @param distanciaFoto
     * @param aberturaCamera
     * @param zoom
     * @return
     */
    public static double photoLengthOnGround(double distanciaFoto, double aberturaCamera, double zoom) {
        double resultado = 2 * distanciaFoto * Math.tan((aberturaCamera / (2 * zoom)) * Math.PI / 180);
        return resultado;
    }
}
