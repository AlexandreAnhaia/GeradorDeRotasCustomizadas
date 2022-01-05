package entidades;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.LinkedList;

import static Controladores.Controlador.calcComprimentoRota;
import static Controladores.Controlador.transformGeoPoint;
import static entidades.Missao.HORIZONTAL_DIRECTION;

public class Rota {

    public final LinkedList<PontoCartesiano> route;
    public final LinkedList<PontoGeografico> geoRoute;
    public final double routeLenght;
    public final long numberPictures;
    public final double flightTime;
    public final double picturesPerSecond;
    public final double velocityPictures;
    public final double velocityCruiser;

    public Rota(LinkedList<PontoCartesiano> route, Missao missao) {
        this.route = route;
        this.geoRoute = transformGeoPoint(route, missao.area.geo.home);
        this.routeLenght = calcComprimentoRota(route);

        if (missao.direction == HORIZONTAL_DIRECTION) {
            numberPictures = Math.round(routeLenght / (missao.width * (1 - missao.sobrePosicao)));

            velocityPictures = this.routeLenght / (this.numberPictures * missao.cam.min_interval_pictures);
            //velocityCruiser = ((int)min(mission.drone.velocityEficiente / 3.6, mission.velocityShutter, this.velocityPictures)*36)/36.0;
            velocityCruiser = ((int) (min(missao.drone.velocityEficiente / 3.6, missao.velocityShutter, this.velocityPictures) * 10)) / 10.0;

            flightTime = (routeLenght / velocityCruiser);
            picturesPerSecond = numberPictures / flightTime;
        } else {
            numberPictures = Math.round(routeLenght / (missao.height * (1 - missao.sobrePosicao)));

            velocityPictures = this.routeLenght / (this.numberPictures * missao.cam.min_interval_pictures);
            //velocityCruiser = ((int)min(mission.drone.velocityEficiente / 3.6, mission.velocityShutter, this.velocityPictures)*36)/36.0;
            velocityCruiser = ((int) (min(missao.drone.velocityEficiente / 3.6, missao.velocityShutter, this.velocityPictures) * 10)) / 10.0;

            flightTime = (routeLenght / velocityCruiser);
            picturesPerSecond = numberPictures / flightTime;
        }
    }

    private static double min(double... args) {
        return Arrays.stream(args).min().getAsDouble();
    }

    public void saveKml(PrintStream out, String name) {
        out.println("<Placemark>");
        out.println("<name>" + name + "</name>");
        out.println("<styleUrl>#m_ylw-pushpin0000</styleUrl>");
        out.println("<LineString>");
        out.println("<tessellate>1</tessellate>");
        out.println("<altitudeMode>relativeToGround</altitudeMode>");
        out.println("<coordinates>");
        out.println(this.geoRoute.stream().map(e -> e.toString()).reduce(String::concat).get());
        out.println("</coordinates>");
        out.println("</LineString>");
        out.println("</Placemark>");
    }
}
