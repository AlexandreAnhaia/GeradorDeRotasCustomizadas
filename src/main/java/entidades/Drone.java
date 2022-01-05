package entidades;

public class Drone {

    public final double peso;           //gramas
    public final double tempoUtilBateria;  //min
    public final double velocityEficiente;//km/h

    public Drone(double peso, double batteryNormalFlight, double batterySafe, double efficientVelocity) {
        this.peso = peso;
        this.tempoUtilBateria = batteryNormalFlight*(1-batterySafe);
        this.velocityEficiente = efficientVelocity;
    }

    public static Drone buildMavicProI() {
        return new Drone(743.0, 24.0, 0.3, 32.5);
    }

    public static Drone buildMavicIIzoom() {
        return new Drone(905.0, 30.0, 0.3, 32.0);
    }

    public static Drone buildPhatom4ProV2() {
        return new Drone(1375.0, 21.0, 0.3, 36.0);
    }

    public static Drone buildPhatom4Pro() {
        return new Drone(1388.0, 21.0, 0.3, 36.0);
    }

    public static Drone buildPhatom4Advanced() {
        return new Drone(1368.0, 21.0, 0.3, 36.0);
    }
}
