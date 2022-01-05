import Controladores.Controlador;
import entidades.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Locale;

import static entidades.Missao.FORWARD_MOVIMENT;
import static entidades.Missao.HORIZONTAL_DIRECTION;

public class Application {

    public static ArrayList<PontoGeografico> points = new ArrayList<>();

    public static void main(String[] args) throws IOException, ParseException {
        Locale.setDefault(Locale.ENGLISH);

        String arquivoCSV = "2019-12-12_16-43-51_v2-barragem-montante.csv";

        PontoCSV pointCSV = new PontoCSV();
        pointCSV.loader(arquivoCSV);
        points = pointCSV.getPoints();
        AreaCartesiana area = new AreaCartesiana(returnNewGeoArea(4));

        Missao misao = new Missao(HORIZONTAL_DIRECTION, FORWARD_MOVIMENT, Drone.buildMavicIIzoom(), Camera.buildMavicIIzoom(), area, 0.50, 16, 17, 1.0, 0.70);

        Controlador controladorGeral = new Controlador(misao);
        controladorGeral.calculaRota();
    }

    public static AreaGeografica returnNewGeoArea(int pre) {
        PontoGeografico home = null;
        LinkedList<PontoGeografico> pre_mission = new LinkedList<>();
        LinkedList<PontoGeografico> mission = new LinkedList<>();
        LinkedList<PontoGeografico> pos_mission = new LinkedList<>();
        for (int i = 0; i < points.size(); i++) {
            if (i == 0) {
                home = points.get(0);
            } else if (i <= pre) {
                pre_mission.add(points.get(i));
            } else if (i <= pre + 4) {
                mission.add(points.get(i));
            } else {
                pos_mission.add(points.get(i));
            }
        }
        AreaGeografica area = new AreaGeografica(home, pre_mission, mission.get(0), mission.get(1), mission.get(2), mission.get(3), pos_mission);
        return area;
    }
}
