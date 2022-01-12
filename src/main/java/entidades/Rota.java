package entidades;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.LinkedList;

import static Controladores.Controlador.calculaComprimentoRota;
import static Controladores.Controlador.convertePontosGeograficos;
import static entidades.Missao.HORIZONTAL_DIRECTION;

public class Rota {

    public final LinkedList<PontoCartesiano> rota;
    public final LinkedList<PontoGeografico> rotaGeografica;
    public final double comprimentoRota;
    public final long numeroFotos;
    public final double tempoVoo;
    public final double fotosPorSegundo;
    public final double velocidadeFotos;
    public final double velocidadeCruzeiro;

    public Rota(LinkedList<PontoCartesiano> rota, Missao missao) {
        this.rota = rota;
        this.rotaGeografica = convertePontosGeograficos(rota, missao.area.areaGeografica.pontoInicial);
        this.comprimentoRota = calculaComprimentoRota(rota);

        if (missao.direcao == HORIZONTAL_DIRECTION) {
            numeroFotos = Math.round(comprimentoRota / (missao.largura * (1 - missao.sobrePosicao)));

            velocidadeFotos = this.comprimentoRota / (this.numeroFotos * missao.camera.min_interval_pictures);
            //velocityCruiser = ((int)min(mission.drone.velocityEficiente / 3.6, mission.velocityShutter, this.velocityPictures)*36)/36.0;
            velocidadeCruzeiro = ((int) (min(missao.drone.velocidadeEficiente / 3.6, missao.velocidadeObturador, this.velocidadeFotos) * 10)) / 10.0;

            tempoVoo = (comprimentoRota / velocidadeCruzeiro);
            fotosPorSegundo = numeroFotos / tempoVoo;
        } else {
            numeroFotos = Math.round(comprimentoRota / (missao.altura * (1 - missao.sobrePosicao)));

            velocidadeFotos = this.comprimentoRota / (this.numeroFotos * missao.camera.min_interval_pictures);
            //velocityCruiser = ((int)min(mission.drone.velocityEficiente / 3.6, mission.velocityShutter, this.velocityPictures)*36)/36.0;
            velocidadeCruzeiro = ((int) (min(missao.drone.velocidadeEficiente / 3.6, missao.velocidadeObturador, this.velocidadeFotos) * 10)) / 10.0;

            tempoVoo = (comprimentoRota / velocidadeCruzeiro);
            fotosPorSegundo = numeroFotos / tempoVoo;
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
        out.println(this.rotaGeografica.stream().map(e -> e.toString()).reduce(String::concat).get());
        out.println("</coordinates>");
        out.println("</LineString>");
        out.println("</Placemark>");
    }
}
