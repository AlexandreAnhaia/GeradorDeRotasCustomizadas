import Controladores.Controlador;
import com.google.gson.Gson;
import entidades.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Locale;

public class Application {

    public static ArrayList<PontoGeografico> points = new ArrayList<>();

    public void criaMissao() throws IOException {
        Locale.setDefault(Locale.ENGLISH);
        Config configuracao = null;

        try {
            String json = String.join(" ", Files.readAllLines(Paths.get("./configuracao.json"), StandardCharsets.UTF_8));
            configuracao = new Gson().fromJson(json, Config.class);
        }
        //Trata as exceptions que podem ser lançadas no decorrer do processo
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String arquivoCSV = configuracao.getArquivo() + ".csv";
        configuracao.setCameraObjeto(Camera.retornaCamera(configuracao.getCamera()));
        configuracao.setDroneObjeto(Drone.retornaDrone(configuracao.getDrone()));

        PontoCSV pointCSV = new PontoCSV();
        pointCSV.loader(arquivoCSV);
        points = pointCSV.getPoints();
        AreaCartesiana area = new AreaCartesiana(returnNewGeoArea(configuracao.getPontosPreMissao()));

        Missao missao = new Missao(
                configuracao.getDirecao(),
                configuracao.getMovimento(),
                configuracao.getDroneObjeto(),
                configuracao.getCameraObjeto(),
                area,
                configuracao.getBlurFactor(),
                configuracao.getTamanhoCartaoSD(),
                configuracao.getDistanciaFotos(),
                configuracao.getZoom(),
                configuracao.getSobreposicao());

        Controlador controladorGeral = new Controlador(missao);
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
