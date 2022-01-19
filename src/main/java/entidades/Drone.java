package entidades;

public class Drone {

    public final double peso;           //gramas
    public final double tempoUtilBateria;  //min
    public final double velocidadeEficiente;//km/h

    public Drone(double peso, double batteryNormalFlight, double batterySafe, double efficientVelocity) {
        this.peso = peso;
        this.tempoUtilBateria = batteryNormalFlight * (1 - batterySafe);
        this.velocidadeEficiente = efficientVelocity;
    }

    public static Drone criaMavicProI() {
        return new Drone(743.0, 24.0, 0.3, 32.5);
    }

    public static Drone criaMavicIIzoom() {
        return new Drone(905.0, 30.0, 0.3, 32.0);
    }

    public static Drone criaPhantom4ProV2() {
        return new Drone(1375.0, 21.0, 0.3, 36.0);
    }

    public static Drone criaPhantom4Pro() {
        return new Drone(1388.0, 21.0, 0.3, 36.0);
    }

    public static Drone criaPhantom4Advanced() {
        return new Drone(1368.0, 21.0, 0.3, 36.0);
    }

    public static Drone retornaDrone(String drone) {
        switch (drone) {
            case "Mavic2Zoom":
                return criaMavicIIzoom();
            case "MavicPro1":
                return criaMavicProI();
            case "Phantom4ProV2":
                return criaPhantom4ProV2();
            case "Phantom4Pro":
                return criaPhantom4Pro();
            case "Phantom4Advanced":
                return criaPhantom4Advanced();
            default:
                System.out.println("Necessário escolher um drone para criar a rota customizada");
                return null;
        }
    }
}
