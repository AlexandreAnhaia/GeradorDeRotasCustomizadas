package entidades;

public class PontoGeografico {

    public final double longitude;
    public final double latitude;
    public final double altura;

    public PontoGeografico(double longitude, double latitude, double altura) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.altura = altura;
    }

    @Override
    public String toString() {
        return longitude + "," + latitude + "," + altura + " ";
    }
}
