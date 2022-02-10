package Controladores;

import entidades.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.LinkedList;

import static entidades.Missao.FORWARD_MOVIMENT;
import static entidades.Missao.HORIZONTAL_DIRECTION;

public class Controlador {

    private final static double PI = Math.PI;

    public final Missao missao;

    public Controlador(Missao missao) {
        this.missao = missao;
    }

    /**
     * Transforma os pontos geográficos da missão em pontos cartesianos, em metros como unidade de medida, para poder ser calculada a rota
     * Após isso, é calculado os pontos da rota com base no número de voltas necessárias para mapear a área completa. Por fim, os pontos cartesianos que estão na unidade de medida
     * em metros é retornado para pontos geográficos e adicionados na lista final (Rota completa).
     * @return Objeto Rota
     */
    private Rota calculaRotaCompleta() {
        AreaCartesiana area = missao.area;

        final PontoCartesiano cartesianHome = area.pontoInicial;
        final PontoCartesiano cartesianPoint1 = area.ponto1;
        final PontoCartesiano cartesianPoint2 = area.ponto2;
        final PontoCartesiano cartesianPoint3 = area.ponto3;
        final PontoCartesiano cartesianPoint4 = area.ponto4;

        LinkedList<PontoCartesiano> rota = new LinkedList<>();

        rota.add(area.pontoInicial);
        //rota.addAll(Arrays.asList(area.pre_mission));
        PontoCartesiano rBarra;
        PontoCartesiano lBarra;
        if (missao.direcao == HORIZONTAL_DIRECTION) {
            if (missao.movimento == FORWARD_MOVIMENT) {
                rBarra = cartesianPoint3.minus(cartesianPoint2).divide(missao.numeroVoltas - 1);
                lBarra = cartesianPoint4.minus(cartesianPoint1).divide(missao.numeroVoltas - 1);
                rota.add(cartesianPoint1);
                rota.add(cartesianPoint2);

                for (int i = 1; i < missao.numeroVoltas; i++) {
                    if (i % 2 == 0) {
                        rota.add(cartesianPoint1.sumProd(i, lBarra));
                        rota.add(cartesianPoint2.sumProd(i, rBarra));
                    } else {
                        rota.add(cartesianPoint2.sumProd(i, rBarra));
                        rota.add(cartesianPoint1.sumProd(i, lBarra));
                    }
                }
            } else {
                rBarra = cartesianPoint2.minus(cartesianPoint3).divide(missao.numeroVoltas - 1);
                lBarra = cartesianPoint1.minus(cartesianPoint4).divide(missao.numeroVoltas - 1);
                rota.add(cartesianPoint4);
                rota.add(cartesianPoint3);

                for (int i = 1; i < missao.numeroVoltas; i++) {
                    if (i % 2 == 0) {
                        rota.add(cartesianPoint4.sumProd(i, lBarra));
                        rota.add(cartesianPoint3.sumProd(i, rBarra));
                    } else {
                        rota.add(cartesianPoint3.sumProd(i, rBarra));
                        rota.add(cartesianPoint4.sumProd(i, lBarra));
                    }
                }
            }
        } else {
            if (missao.movimento == FORWARD_MOVIMENT) {
                rBarra = cartesianPoint3.minus(cartesianPoint4).divide(missao.numeroVoltas - 1);
                lBarra = cartesianPoint2.minus(cartesianPoint1).divide(missao.numeroVoltas - 1);
                rota.add(cartesianPoint1);
                rota.add(cartesianPoint4);

                for (int i = 1; i < missao.numeroVoltas; i++) {
                    if (i % 2 == 0) {
                        rota.add(cartesianPoint1.sumProd(i, lBarra));
                        rota.add(cartesianPoint4.sumProd(i, rBarra));
                    } else {
                        rota.add(cartesianPoint4.sumProd(i, rBarra));
                        rota.add(cartesianPoint1.sumProd(i, lBarra));
                    }
                }
            } else {
                rBarra = cartesianPoint1.minus(cartesianPoint2).divide(missao.numeroVoltas - 1);
                lBarra = cartesianPoint4.minus(cartesianPoint3).divide(missao.numeroVoltas - 1);
                rota.add(cartesianPoint3);
                rota.add(cartesianPoint2);

                for (int i = 1; i < missao.numeroVoltas; i++) {
                    if (i % 2 == 0) {
                        rota.add(cartesianPoint3.sumProd(i, lBarra));
                        rota.add(cartesianPoint2.sumProd(i, rBarra));
                    } else {
                        rota.add(cartesianPoint2.sumProd(i, rBarra));
                        rota.add(cartesianPoint3.sumProd(i, lBarra));
                    }
                }
            }
        }
        //rota.addAll(Arrays.asList(area.pos_mission));
        rota.add(cartesianHome);
        return new Rota(rota, missao);
    }

    private LinkedList<PontoCartesiano> adicionaPrePontosExtras(LinkedList<PontoCartesiano> pre, PontoCartesiano start_mission) {
        LinkedList<PontoCartesiano> result = new LinkedList<>();
        PontoCartesiano pontoCartesiano = null;
        pre.addLast(start_mission);
        missao.extra_pre = 0;
        for (PontoCartesiano p : pre) {
            if (pontoCartesiano != null) {
                double distance = p.minus(pontoCartesiano).norm();
                if (distance > 2.0) {
                    double lambda = (distance - 1) / distance;
                    PontoCartesiano extra = new PontoCartesiano(pontoCartesiano.x * (1 - lambda) + p.x * lambda, pontoCartesiano.y * (1 - lambda) + p.y * lambda, pontoCartesiano.z * (1 - lambda) + p.z * lambda);
                    result.add(extra);
                    missao.extra_pre++;
                    result.add(p);
                } else {
                    result.add(p);
                }
            } else {
                result.add(p);
            }
            pontoCartesiano = p;
        }
        result.removeLast();
        return result;
    }

    private LinkedList<PontoCartesiano> adicionaPosPontosExtras(PontoCartesiano end_mission, LinkedList<PontoCartesiano> pos) {
        LinkedList<PontoCartesiano> result = new LinkedList<>();
        PontoCartesiano pontoCartesiano = null;
        pos.addFirst(end_mission);
        missao.extra_pos = 0;
        for (PontoCartesiano p : pos) {
            if (pontoCartesiano != null) {
                double distance = p.minus(pontoCartesiano).norm();
                if (distance > 2.0) {
                    double lambda = (distance - 1) / distance;
                    PontoCartesiano extra = new PontoCartesiano(pontoCartesiano.x * (1 - lambda) + p.x * lambda, pontoCartesiano.y * (1 - lambda) + p.y * lambda, pontoCartesiano.z * (1 - lambda) + p.z * lambda);
                    result.add(extra);
                    missao.extra_pos++;
                    result.add(p);
                } else {
                    result.add(p);
                }
            } else {
                result.add(p);
            }
            pontoCartesiano = p;
        }
        result.removeFirst();
        return result;
    }

    /**
     * Transforma os pontos geográficos da missão em pontos cartesianos, em metros como unidade de medida, com o objetivo de calcular a Rota ou uma parte da rota Total. Esse método
     * pode calcula se a rota total vai precisar ser dividida em mais de uma rota. Por fim, após os cálculos realizados, os pontos cartesianos que estão em metros é retornado para pontos
     * geográficos e adionados na(s) lista(s) (Rota(s)).
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void calculaRota() throws FileNotFoundException, IOException {
        Rota rota = calculaRotaCompleta();

        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("Número de voltas: " + missao.numeroVoltas);
        System.out.println("Número de WayPoints: " + rota.rota.size());
        System.out.println("Comprimento da volta: " + missao.comprimentoVolta);
        System.out.println("Largura da volta: " + missao.larguraVolta);
        System.out.println("Width da foto: " + missao.largura);
        System.out.println("Height da foto: " + missao.altura);
        System.out.println("Hipotenusa: " + missao.area.hipotenusa);
        System.out.println("Comprimento da base: " + missao.area.comprimentoBase);
        System.out.println("Comprimento total da rota em metros: " + rota.comprimentoRota);
        System.out.println("Ângulo camPitch: " + missao.camPitch);
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("P1: " + missao.area.areaGeografica.ponto1);
        System.out.println("P2: " + missao.area.areaGeografica.ponto2);
        System.out.println("P3: " + missao.area.areaGeografica.ponto3);
        System.out.println("P4: " + missao.area.areaGeografica.ponto4);
        System.out.println("Número de Fotos " + rota.numeroFotos);
        System.out.println("Velocity Eficient " + missao.drone.velocidadeEficiente / 3.6);
        System.out.println("Velocity Shutter " + missao.velocidadeObturador);
        System.out.println("Velocity Pictures " + rota.velocidadeFotos);
        System.out.println("Velocity Cruiser " + ((int) (rota.velocidadeCruzeiro * 36)) / 10.0 + " km/h");
        System.out.println("Flight Time " + rota.tempoVoo / 60 + " minutes");
        System.out.println("Pictures Per Second " + rota.fotosPorSegundo);
        System.out.println("Interval Per Picture " + (int) (1.0 / rota.fotosPorSegundo + 0.01) + " Segundos");
        System.out.println("Pictures Precision " + missao.precisaoImagem + " mm/px");
        System.out.println("Precisão em milimetros: " + calcGSD(missao));
        System.out.println("-------------------------------------------------------------------------------");

        int numeroDeMissoes;

        LinkedList<Rota> rotas = new LinkedList<Rota>();

        if (missao.drone.tempoUtilBateria < rota.tempoVoo) {
            numeroDeMissoes = (int) Math.ceil((rota.tempoVoo / 60) / missao.drone.tempoUtilBateria);
            LinkedList<Integer> index = splitIndex(rota.rota, numeroDeMissoes);
            index.addFirst(1);
            index.addLast(rota.rota.size() - 2);

            for (int m = 0; m < numeroDeMissoes; m++) {
                LinkedList<PontoCartesiano> missaoPre = new LinkedList<>();
                missaoPre.add(missao.area.pontoInicial);
                missaoPre.addAll(Arrays.asList(missao.area.preMissao));

                LinkedList<PontoCartesiano> rotaMissao = criaSubRota(rota.rota, index.get(m), index.get(m + 1));

                LinkedList<PontoCartesiano> missaoPos = new LinkedList<>();
                missaoPos.addAll(Arrays.asList(missao.area.posMissao));
                missaoPos.add(missao.area.pontoInicial);

                missaoPre = adicionaPrePontosExtras(missaoPre, rotaMissao.getFirst());
                missaoPos = adicionaPosPontosExtras(rotaMissao.getLast(), missaoPos);

                LinkedList<PontoCartesiano> subRoute = new LinkedList<>();
                subRoute.addAll(missaoPre);
                subRoute.addAll(rotaMissao);
                subRoute.addAll(missaoPos);

                Rota sub = new Rota(subRoute, missao);
                System.out.println("----------------------------------------------(SubRoute " + (m + 1) + ")---------------------------------------------------");
                System.out.println("Número de WayPoints: " + subRoute.size());
                System.out.println("Comprimento da rota: " + sub.comprimentoRota);
                System.out.println("Número de Fotos " + sub.numeroFotos);
                System.out.println("Velocity Cruiser " + ((int) (sub.velocidadeCruzeiro * 36)) / 10.0 + " km/h");
                System.out.println("Flight Time " + sub.tempoVoo / 60 + " minutes");
                System.out.println("Pictures Per Second " + sub.fotosPorSegundo);
                System.out.println("Interval Per Picture " + (1.0 / sub.fotosPorSegundo) + " Segundos");
                System.out.println(sub.rotaGeografica.stream().map(e -> e.toString()).reduce(String::concat).get());

                rotas.add(sub);
            }
        } else {
            rotas.add(rota);
        }
        salvaKml(rotas, missao);
        salvaLitchi(rotas, missao);
    }

    private static void adicionaLinhaLitchiCSV(PrintStream out, double latitude, double longitude, double altitude, double heading, double velocidade, double intervaloEntreFotos) {
        out.print(latitude + "," + longitude + "," + altitude + "," + heading + "," + "0, 0, 0, -1");
        //altitudemode,speed(m/s),poi_latitude,poi_longitude,poi_altitude(m),poi_altitudemode,photo_timeinterval,photo_distinterval
        //0,0,0,0,0,0,1.9,0
        out.print(", 0, " + velocidade + ", 0, 0, 0, 0, " + intervaloEntreFotos + ", 0");
        out.println();
    }

    public static void salvaLitchi(LinkedList<Rota> rotas, Missao missao) throws FileNotFoundException {
        int contador = 1;
        final double headingMission = calculaHeadingDaMissao(missao.area.ponto1, missao.area.ponto2, missao.area.ponto3);

        for (Rota rota : rotas) {
            double velocidade = ((int) (rota.velocidadeCruzeiro * 10)) / 10.0;
            double intervaloEntreFotos = 1.0 / rota.fotosPorSegundo;

            PrintStream out = new PrintStream("./route" + (contador++) + ".csv");

            out.println("latitude,longitude,altitude(m),heading(deg),curvesize(m),rotationdir,gimbalmode,gimbalpitchangle,"
                    + "actiontype1,actionparam1,actiontype2,actionparam2,actiontype3,actionparam3,actiontype4,actionparam4,actiontype5,actionparam5,"
                    + "actiontype6,actionparam6,actiontype7,actionparam7,actiontype8,actionparam8,actiontype9,actionparam9,actiontype10,actionparam10,"
                    + "actiontype11,actionparam11,actiontype12,actionparam12,actiontype13,actionparam13,actiontype14,actionparam14,actiontype15,actionparam15,"
                    + "altitudemode,speed(m/s),poi_latitude,poi_longitude,poi_altitude(m),poi_altitudemode,photo_timeinterval,photo_distinterval");
            //,altitudemode,speed(m/s),poi_latitude,poi_longitude,poi_altitude(m),poi_altitudemode,photo_timeinterval,photo_distinterval
            for (int i = 0; i < rota.rotaGeografica.size(); i++) {
                PontoGeografico pontoGeografico = rota.rotaGeografica.get(i);
                double headingRoute = i < rota.rotaGeografica.size() - 1 ? calculaHeadingDaDirecaoDrone(rota.rota.get(i), rota.rota.get(i + 1)) : 0;
                if (i == 0) {
                    //primeiro ponto da rota (home), deve mudar o angulo da camera para -15 graus e direcionar o drone para o proximo ponto da rota 
                    adicionaLinhaLitchiCSV(out, pontoGeografico.latitude, pontoGeografico.longitude, 2, headingRoute, 1.0, 0);
                } else if (i <= missao.area.preMissao.length + missao.extra_pre) {
                    //todos os pontos do pre mission devem manter o drone direcionado para a rota
                    adicionaLinhaLitchiCSV(out, pontoGeografico.latitude, pontoGeografico.longitude, pontoGeografico.altura, headingRoute, 10.0, 0);
                } else if (i == missao.area.preMissao.length + missao.extra_pre + 1) {
                    //no primeiro ponto da missão deve-se mudar o angulo da camera e a direção do drone no ideal para as fotos da missao, 
                    adicionaLinhaLitchiCSV(out, pontoGeografico.latitude, pontoGeografico.longitude, pontoGeografico.altura, headingMission, velocidade, intervaloEntreFotos);

                } else if (i > missao.area.preMissao.length + missao.extra_pre + 1 && i < rota.rotaGeografica.size() - 2 - missao.area.posMissao.length - missao.extra_pos) {
                    //em todos os pontos seguintes da missão, o drone deve manter-se na drireção ideal de fotos continuando a navegacao
                    adicionaLinhaLitchiCSV(out, pontoGeografico.latitude, pontoGeografico.longitude, pontoGeografico.altura, headingMission, velocidade, intervaloEntreFotos);
                } else if (i == rota.rotaGeografica.size() - 2 - missao.area.posMissao.length - missao.extra_pos) {
                    //no ultimo ponto da missão o drone deve colocar a camera em -15 graus  e direcionar o drone para a rota de volta 
                    adicionaLinhaLitchiCSV(out, pontoGeografico.latitude, pontoGeografico.longitude, pontoGeografico.altura, headingMission, velocidade, intervaloEntreFotos);
                    adicionaLinhaLitchiCSV(out, pontoGeografico.latitude, pontoGeografico.longitude, pontoGeografico.altura + 1, headingRoute, 10.0, 0);
                } else if (i < rota.rotaGeografica.size() - 3) {
                    //em todos os pontos do pos mission o drone deve estar direcionado para a rota
                    adicionaLinhaLitchiCSV(out, pontoGeografico.latitude, pontoGeografico.longitude, pontoGeografico.altura, headingRoute, 10.0, 0);
                } else if (i < rota.rotaGeografica.size() - 1) {
                    //no ultimo ponto do pos mission o drone deve estar direcionado para a rota e movimentar-se devagar
                    adicionaLinhaLitchiCSV(out, pontoGeografico.latitude, pontoGeografico.longitude, pontoGeografico.altura, headingRoute, 1.0, 0);
                } else if (i == rota.rotaGeografica.size() - 1) {
                    //quando o drone chegar no home, este der girar e em seguida executar o landing
                    adicionaLinhaLitchiCSV(out, pontoGeografico.latitude, pontoGeografico.longitude, 2, 0, 1.0, 0);
                } else {
                    throw new IllegalStateException("Isso nao deveria ser possivel, reveja as codiçoes nos if's acima");
                }
                /*if (i == 0) {
                    //primeiro ponto da rota (home), deve mudar o angulo da camera para -15 graus e direcionar o drone para o proximo ponto da rota 
                    addRowLitchCsv(out, pontoGeografico.latitude, pontoGeografico.longitude, 2, headingRoute, 1.0, 0, new Action(ACTION_TILT_CAM, -15));
                } else if (i <= mission.area.pre_mission.length) {
                    //todos os pontos do pre mission devem manter o drone direcionado para a rota
                    addRowLitchCsv(out, pontoGeografico.latitude, pontoGeografico.longitude, pontoGeografico.altura, headingRoute, 5.0, 0);
                } else if (i == mission.area.pre_mission.length + 1) {
                    //no primeiro ponto da missão deve-se mudar o angulo da camera e a direção do drone no ideal para as fotos da missao, 
                    addRowLitchCsv(out, pontoGeografico.latitude, pontoGeografico.longitude, pontoGeografico.altura, headingMission, speed, photho_interval,
                            new Action(ACTION_TILT_CAM, mission.camPitch), new Action(ACTION_STAY_FOR, 5 * 1000), new Action(ACTION_TAKE_PHOTO, -1));

                } else if (i > mission.area.pre_mission.length + 1 && i < route.geoRoute.size() - 2 - mission.area.pos_mission.length) {
                    //em todos os pontos seguintes da missão, o drone deve manter-se na drireção ideal de fotos continuando a navegacao
                    addRowLitchCsv(out, pontoGeografico.latitude, pontoGeografico.longitude, pontoGeografico.altura, headingMission, speed, photho_interval);
                } else if (i == route.geoRoute.size() - 2 - mission.area.pos_mission.length) {
                    //no ultimo ponto da missão o drone deve colocar a camera em -15 graus  e direcionar o drone para a rota de volta 
                    addRowLitchCsv(out, pontoGeografico.latitude, pontoGeografico.longitude, pontoGeografico.altura, headingMission, speed, photho_interval);
                    addRowLitchCsv(out, pontoGeografico.latitude, pontoGeografico.longitude, pontoGeografico.altura+1, headingRoute, 5.0, 0, new Action(ACTION_TILT_CAM, -15));
                } else if (i < route.geoRoute.size() - 2) {
                    //em todos os pontos do pos mission o drone deve estar direcionado para a rota
                    addRowLitchCsv(out, pontoGeografico.latitude, pontoGeografico.longitude, pontoGeografico.altura, headingRoute, 5.0, 0);
                } else if (i == route.geoRoute.size() - 2) {
                    //no ultimo ponto do pos mission o drone deve estar direcionado para a rota e movimentar-se devagar
                    addRowLitchCsv(out, pontoGeografico.latitude, pontoGeografico.longitude, pontoGeografico.altura, headingRoute, 1.0, 0);
                } else if (i == route.geoRoute.size() - 1) {
                    //quando o drone chegar no home, este der girar e em seguida executar o landing
                    addRowLitchCsv(out, pontoGeografico.latitude, pontoGeografico.longitude, 2, 0, 1.0, 0, new Action(ACTION_ROTATE_DRONE, 180));
                } else {
                    throw new IllegalStateException("Isso nao deveria ser possivel, reveja as codiçoes nos if's acima");
                }*/
            }
            out.close();
        }
    }

    private static void salvaPontosKml(PrintStream out, PontoGeografico pontoGeografico, String nome) {
        out.println("<Placemark>");
        out.printf("<name>%s</name>\n", nome);
        out.println("<styleUrl>#m_ylw-pushpin</styleUrl>");
        out.println("<Point>");
        out.println("<gx:drawOrder>1</gx:drawOrder>");
        out.println("<altitudeMode>relativeToGround</altitudeMode>");
        out.printf("<coordinates>%1.15f,%1.15f,%1.15f</coordinates>\n", pontoGeografico.longitude, pontoGeografico.latitude, pontoGeografico.altura);
        out.println("</Point>");
        out.println("</Placemark>");
    }

    /**
     * Faz a criação do arquivo do tipo KML, para poder visualizara  rota completa através do Google Earth.
     * @param rotas
     * @param missao
     * @throws FileNotFoundException
     */
    public static void salvaKml(LinkedList<Rota> rotas, Missao missao) throws FileNotFoundException {
        PrintStream out = new PrintStream("./novo.kml");
        out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        out.println("<kml>");
        out.println("<Document>");
        out.println("<name>Barragem.kml</name>");
        out.println("<name>Barragem.kml</name>");
        out.println("<Folder>");
        out.println("<name>Barragem</name>");
        out.println("<open>1</open>");
        int contador = 1;
        for (Rota rota : rotas) {
            rota.saveKml(out, "route" + contador++);
        }
        salvaPontosKml(out, missao.area.areaGeografica.pontoInicial, "H");
        missao.area.areaGeografica.preMissao.stream().forEach(gp -> salvaPontosKml(out, gp, "pre"));
        salvaPontosKml(out, missao.area.areaGeografica.ponto1, "m1");
        salvaPontosKml(out, missao.area.areaGeografica.ponto2, "m2");
        salvaPontosKml(out, missao.area.areaGeografica.ponto3, "m3");
        salvaPontosKml(out, missao.area.areaGeografica.ponto4, "m4");
        missao.area.areaGeografica.posMissao.stream().forEach(gp -> salvaPontosKml(out, gp, "pos"));

        out.println("</Folder>");
        out.println("</Document>");
        out.println("</kml>");
        out.close();
    }

    /**
     * Transforma os pontos cartesianos em pontos geográficos.
     * @param route
     * @param home
     * @return
     */
    public static LinkedList<PontoGeografico> convertePontosGeograficos(LinkedList<PontoCartesiano> route, PontoGeografico home) {
        LinkedList<PontoGeografico> rotaFinal = new LinkedList<>();
        for (PontoCartesiano elemento : route) {
            rotaFinal.add(converteParaPontoGeografico(elemento, home));
        }
        return rotaFinal;
    }

    public static double calculaHeadingDaMissao(PontoCartesiano c1, PontoCartesiano c2, PontoCartesiano c3) {
        PontoCartesiano va = c1.minus(c2);
        PontoCartesiano vb = c3.minus(c2);

        double dz = Math.max(Math.abs(va.z) / va.norm(), Math.abs(vb.z) / vb.norm());
        double plane_angle = Math.asin(dz) * 180 / Math.PI;
        System.out.println("dz = " + dz);
        System.out.println("angle = " + plane_angle);

        double heading;
        if (plane_angle > 5) {
            PontoCartesiano pv = va.vectorialProd(vb);
            if (pv.z > 0) {
                heading = 180 + Math.atan2(pv.x, pv.y) * 180 / Math.PI;
            } else {
                heading = Math.atan2(pv.x, pv.y) * 180 / Math.PI;
            }
            //System.out.println("z = "+pv.z + ",   heading = "+heading);

            //double heading = 360 - Math.atan2((c2.y - c1.y), (c2.x - c1.x)) * 180 / Math.PI;
            //double heading = Math.atan2((c2.y - c1.y), (c2.x - c1.x)) * 180 / Math.PI;
        } else {
            double atam = Math.atan2(vb.y, vb.x) * 180 / Math.PI;
            System.out.println("atam = " + atam);
            heading = 270 - atam;
            heading = (heading < 0 ? heading + 360 : heading);
        }
        heading = (heading > 360 ? heading % 360 : heading);

        return heading;
    }

    public static double calculaHeadingDaDirecaoDrone(PontoCartesiano c1, PontoCartesiano c2) {
        double heading = 360 - (-90 + Math.atan2((c2.y - c1.y), (c2.x - c1.x)) * 180 / Math.PI);
        heading = (heading > 360 ? heading % 360 : heading);
        return heading;
    }

    /**
     * Calcula o comprimento total da rota ou o comprimento total de cada sub-rota.
     * @param route
     * @return
     */
    public static double calculaComprimentoRota(LinkedList<PontoCartesiano> route) {
        double comprimentoTotal = 0;
        PontoCartesiano corrente = route.getFirst();
        for (PontoCartesiano elemento : route) {
            comprimentoTotal += elemento.minus(corrente).norm();
            corrente = elemento;
        }
        return comprimentoTotal;
    }

    /**
     * Cria as sub-rotas, caso seja necessário dividir a rota completa em mais de uma rota.
     * @param route
     * @param beguinIndex
     * @param endIndex
     * @return
     */
    public static LinkedList<PontoCartesiano> criaSubRota(LinkedList<PontoCartesiano> route, int beguinIndex, int endIndex) {
        LinkedList<PontoCartesiano> resultado = new LinkedList<>();

        for (int i = beguinIndex; i <= endIndex; i++) {
            resultado.add(route.get(i));
        }
        return resultado;
    }

    /**
     * Verifica o tamanho de cada sub-rota e cálcula o número de pontos de cada sub-rota.
     * @param route
     * @param numeroMissoes
     * @return
     */
    public static LinkedList<Integer> splitIndex(LinkedList<PontoCartesiano> route, int numeroMissoes) {
        int size = (int) Math.floor(route.size() / numeroMissoes);
        LinkedList<Integer> resultado = new LinkedList<>();

        for (int m = 1; m < numeroMissoes; m++) {
            resultado.add(size * m);
        }
        return resultado;
    }

    /**
     * Transforma pontos geográficos em pontos cartesianos.
     * @param geoPoint
     * @param home
     * @return
     */
    public static PontoCartesiano converteParaCartesiano(PontoGeografico geoPoint, PontoGeografico home) {
        PontoCartesiano cartesianPointResultado = new PontoCartesiano(calculaX(geoPoint.longitude, home.longitude, home.latitude),
                calculaY(geoPoint.latitude, home.latitude), geoPoint.altura);
        return cartesianPointResultado;
    }

    /**
     * Transforma pontos cartesianos em pontos geográficos.
     * @param cartesianPoint
     * @param home
     * @return
     */
    public static PontoGeografico converteParaPontoGeografico(PontoCartesiano cartesianPoint, PontoGeografico home) {
        PontoGeografico geoPoint = new PontoGeografico(
                calculaLongitudeX(home.latitude, home.longitude, cartesianPoint.x),
                calculaLatitudeY(home.latitude, cartesianPoint.y),
                cartesianPoint.z
        );
        return geoPoint;
    }

    /**
     * Calcula a coordenada Y, em metros, de um ponto qualquer do planeta Terra com base a uma coordenada geográfica
     * @param latitude é o ponto do qual se deseja calcular para cartesiano
     * @param latitudeBarra é o ponto de referência
     * @return
     */
    private static double calculaY(double latitude, double latitudeBarra) {
        double resultado = (latitude - latitudeBarra) * (10000000.0 / 90);
        return resultado;
    }

    /**
     * Calcula a coordenada X, em metros, de um ponto qualquer do planeta Terra com base a uma coordenada geográfica.
     * @param longitude é o ponto do qual se deseja calcular para cartesiano
     * @param longitudeBarra é a longitude do ponto de referência
     * @param latitudeBarra é a latitude do ponto de referêcia
     * @return
     */
    private static double calculaX(double longitude, double longitudeBarra, double latitudeBarra) {
        double resultado = (longitude - longitudeBarra) * (6400000.0 * (Math.cos(latitudeBarra * PI / 180) * 2 * PI / 360));
        return resultado;
    }

    /**
     * Calcula a latitude geográfica de um ponto
     * @param latitudeBarra é a latitude do ponto de referência
     * @param y é a latitude em cartesiano
     * @return
     */
    private static double calculaLatitudeY(double latitudeBarra, double y) {
        double resultado = ((y * 90) / 10000000.0) + latitudeBarra;
        return resultado;
    }

    /**
     * Calcula a longitude geográfica de um ponto
     * @param latitudeBarra é a latitude do ponto de referência
     * @param longitudeBarra é a longitude do ponto de referência
     * @param x longitude em cartesiano
     * @return
     */
    private static double calculaLongitudeX(double latitudeBarra, double longitudeBarra, double x) {
        double resultado = ((x * 90) / (10008000 * Math.cos(latitudeBarra * PI / 180))) + longitudeBarra;
        return resultado;
    }

    /**
     * Calcula a largura da foto com base na distância da foto e da abertura da câmera
     * @param distanciaFoto
     * @param aberturaCamera
     * @return
     */

    /**
     *
     * @param distanciaFoto
     * @param aberturaCamera
     * @return
     */
    public static double photoLengthOnGround(double distanciaFoto, double aberturaCamera) {
        double resultado = 2 * distanciaFoto * Math.tan((aberturaCamera / 2) * PI / 180);
        return resultado;
    }

    /**
     * Calcula a distância da foto divido pela distância do foco(propriedade da câmera).
     * @param missao
     * @return
     */
    public static double calculaDenEscala(Missao missao) {
        double resultado = missao.distanciaFoto / (missao.camera.distanceFocus * Math.pow(10, -3));
        return resultado;
    }

    /**
     * Calcula a área X da imagem
     * @param missao
     * @return
     */
    public static double calculaSensorX(Missao missao) {
        double resultado = missao.camera.sensorX / missao.camera.resolutionW;
        return resultado;
    }

    /**
     * Calcula a área Y da imagem
     * @param missao
     * @return
     */
    public static double calculaSensorY(Missao missao) {
        double resultado = missao.camera.sensorY / missao.camera.resolutionH;
        return resultado;
    }

    /**
     * Faz a comparação do X e do Y da imagem e retorn o menor dos valores
     * @param missao
     * @return
     */
    public static double calculaTam(Missao missao) {
        double numberOne = calculaSensorX(missao);
        double numberTwo = calculaSensorY(missao);
        double resultado = Math.min(numberOne, numberTwo);
        return resultado;
    }

    /**
     * Calcula a precisão em milimetros por cada pixel da imagem, através do valor do DenEscala multiplicado pelo Tam.
     * @param missao
     * @return
     */
    public static double calcGSD(Missao missao) {
        double denEscala = calculaDenEscala(missao);
        double tam = calculaTam(missao);
        //System.out.println(denEscala);  ?????????????????
        double resultado = denEscala * tam;
        return resultado;
    }

}
