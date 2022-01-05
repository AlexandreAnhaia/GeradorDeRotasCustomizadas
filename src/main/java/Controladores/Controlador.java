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

    private Rota calculaRotaCompleta() {
        AreaCartesiana area = missao.area;

        final PontoCartesiano cartesianHome = area.h;
        final PontoCartesiano cartesianPoint1 = area.p1;
        final PontoCartesiano cartesianPoint2 = area.p2;
        final PontoCartesiano cartesianPoint3 = area.p3;
        final PontoCartesiano cartesianPoint4 = area.p4;

        LinkedList<PontoCartesiano> rota = new LinkedList<>();

        rota.add(area.h);
        //rota.addAll(Arrays.asList(area.pre_mission));
        PontoCartesiano rBarra;
        PontoCartesiano lBarra;
        if (missao.direction == HORIZONTAL_DIRECTION) {
            if (missao.moviment == FORWARD_MOVIMENT) {
                rBarra = cartesianPoint3.minus(cartesianPoint2).divide(missao.nVoltas - 1);
                lBarra = cartesianPoint4.minus(cartesianPoint1).divide(missao.nVoltas - 1);
                rota.add(cartesianPoint1);
                rota.add(cartesianPoint2);
                for (int i = 1; i < missao.nVoltas; i++) {
                    if (i % 2 == 0) {
                        rota.add(cartesianPoint1.sumProd(i, lBarra));
                        rota.add(cartesianPoint2.sumProd(i, rBarra));
                    } else {
                        rota.add(cartesianPoint2.sumProd(i, rBarra));
                        rota.add(cartesianPoint1.sumProd(i, lBarra));
                    }
                }
            } else {
                rBarra = cartesianPoint2.minus(cartesianPoint3).divide(missao.nVoltas - 1);
                lBarra = cartesianPoint1.minus(cartesianPoint4).divide(missao.nVoltas - 1);
                rota.add(cartesianPoint4);
                rota.add(cartesianPoint3);
                for (int i = 1; i < missao.nVoltas; i++) {
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
            if (missao.moviment == FORWARD_MOVIMENT) {
                rBarra = cartesianPoint3.minus(cartesianPoint4).divide(missao.nVoltas - 1);
                lBarra = cartesianPoint2.minus(cartesianPoint1).divide(missao.nVoltas - 1);
                rota.add(cartesianPoint1);
                rota.add(cartesianPoint4);
                for (int i = 1; i < missao.nVoltas; i++) {
                    if (i % 2 == 0) {
                        rota.add(cartesianPoint1.sumProd(i, lBarra));
                        rota.add(cartesianPoint4.sumProd(i, rBarra));
                    } else {
                        rota.add(cartesianPoint4.sumProd(i, rBarra));
                        rota.add(cartesianPoint1.sumProd(i, lBarra));
                    }
                }
            } else {
                rBarra = cartesianPoint1.minus(cartesianPoint2).divide(missao.nVoltas - 1);
                lBarra = cartesianPoint4.minus(cartesianPoint3).divide(missao.nVoltas - 1);
                rota.add(cartesianPoint3);
                rota.add(cartesianPoint2);
                for (int i = 1; i < missao.nVoltas; i++) {
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

    private LinkedList<PontoCartesiano> adicionaPrePontosExtras(LinkedList<PontoCartesiano> pre, PontoCartesiano start_mission){
        LinkedList<PontoCartesiano> result = new LinkedList<>();
        PontoCartesiano a = null;
        pre.addLast(start_mission);
        missao.extra_pre = 0;
        for(PontoCartesiano p : pre){
            if(a!=null){
                double distance = p.minus(a).norm();
                if(distance>2.0){
                    double lambda = (distance-1)/distance;
                    PontoCartesiano extra = new PontoCartesiano(a.x*(1-lambda)+p.x*lambda, a.y*(1-lambda)+p.y*lambda, a.z*(1-lambda)+p.z*lambda);
                    result.add(extra);
                    missao.extra_pre++;
                    result.add(p);
                }else{
                    result.add(p);
                }
            }else{
                result.add(p);
            }
            a = p;
        }
        result.removeLast();
        return result;
    }

    private LinkedList<PontoCartesiano> adicionaPosPontosExtras(PontoCartesiano end_mission, LinkedList<PontoCartesiano> pos){
        LinkedList<PontoCartesiano> result = new LinkedList<>();
        PontoCartesiano a = null;
        pos.addFirst(end_mission);
        missao.extra_pos = 0;
        for(PontoCartesiano p : pos){
            if(a!=null){
                double distance = p.minus(a).norm();
                if(distance>2.0){
                    double lambda = (distance-1)/distance;
                    PontoCartesiano extra = new PontoCartesiano(a.x*(1-lambda)+p.x*lambda, a.y*(1-lambda)+p.y*lambda, a.z*(1-lambda)+p.z*lambda);
                    result.add(extra);
                    missao.extra_pos++;
                    result.add(p);
                }else{
                    result.add(p);
                }
            }else{
                result.add(p);
            }
            a = p;
        }
        result.removeFirst();
        return result;
    }

    public void calculaRota() throws FileNotFoundException, IOException {
        Rota rota = calculaRotaCompleta();

        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("Número de voltas: " + missao.nVoltas);
        System.out.println("Número de WayPoints: " + rota.route.size());
        System.out.println("Comprimento da volta: " + missao.comprimentoVolta);
        System.out.println("Largura da volta: " + missao.larguraVolta);
        System.out.println("Width da foto: " + missao.width);
        System.out.println("Height da foto: " + missao.height);
        System.out.println("Hipotenusa: " + missao.area.hipotenusa);
        System.out.println("Comprimento da base: " + missao.area.comprimentoBase);
        System.out.println("Comprimento total da rota em metros: " + rota.routeLenght);
        System.out.println("Ângulo camPitch: " + missao.camPitch);
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("P1: " + missao.area.geo.point1);
        System.out.println("P2: " + missao.area.geo.point2);
        System.out.println("P3: " + missao.area.geo.point3);
        System.out.println("P4: " + missao.area.geo.point4);
        System.out.println("Número de Fotos " + rota.numberPictures);
        System.out.println("Velocity Eficient " + missao.drone.velocityEficiente / 3.6);
        System.out.println("Velocity Shutter " + missao.velocityShutter);
        System.out.println("Velocity Pictures " + rota.velocityPictures);
        System.out.println("Velocity Cruiser " + ((int) (rota.velocityCruiser * 36)) / 10.0 + " km/h");
        System.out.println("Flight Time " + rota.flightTime / 60 + " minutes");
        System.out.println("Pictures Per Second " + rota.picturesPerSecond);
        System.out.println("Interval Per Picture " + (int) (1.0 / rota.picturesPerSecond + 0.01) + " Segundos");
        System.out.println("Pictures Precision " + missao.picturePrecision + " mm/px");
        System.out.println("Precisão em milimetros: " + calcGSD(missao));
        System.out.println("-------------------------------------------------------------------------------");

        int numeroDeMissoes;

        LinkedList<Rota> rotas = new LinkedList<Rota>();

        if (missao.drone.tempoUtilBateria < rota.flightTime) {
            numeroDeMissoes = (int) Math.ceil((rota.flightTime / 60) / missao.drone.tempoUtilBateria);
            LinkedList<Integer> index = splitIndex(rota.route, numeroDeMissoes);
            index.addFirst(1);
            index.addLast(rota.route.size() - 2);

            for (int m = 0; m < numeroDeMissoes; m++) {
                LinkedList<PontoCartesiano> missionPre = new LinkedList<>();
                missionPre.add(missao.area.h);
                missionPre.addAll(Arrays.asList(missao.area.pre_mission));

                LinkedList<PontoCartesiano> missionRoute = buildSubRoute(rota.route, index.get(m), index.get(m + 1));

                LinkedList<PontoCartesiano> missionPos = new LinkedList<>();
                missionPos.addAll(Arrays.asList(missao.area.pos_mission));
                missionPos.add(missao.area.h);

                missionPre = adicionaPrePontosExtras(missionPre, missionRoute.getFirst());
                missionPos = adicionaPosPontosExtras(missionRoute.getLast(), missionPos);

                LinkedList<PontoCartesiano> subRoute = new LinkedList<>();
                subRoute.addAll(missionPre);
                subRoute.addAll(missionRoute);
                subRoute.addAll(missionPos);

                Rota sub = new Rota(subRoute, missao);
                System.out.println("----------------------------------------------(SubRoute " + (m + 1) + ")---------------------------------------------------");
                System.out.println("Número de WayPoints: " + subRoute.size());
                System.out.println("Comprimento da rota: " + sub.routeLenght);
                System.out.println("Número de Fotos " + sub.numberPictures);
                System.out.println("Velocity Cruiser " + ((int) (sub.velocityCruiser * 36)) / 10.0 + " km/h");
                System.out.println("Flight Time " + sub.flightTime / 60 + " minutes");
                System.out.println("Pictures Per Second " + sub.picturesPerSecond);
                System.out.println("Interval Per Picture " + (1.0 / sub.picturesPerSecond) + " Segundos");
                System.out.println(sub.geoRoute.stream().map(e -> e.toString()).reduce(String::concat).get());

                rotas.add(sub);
            }
        } else {
            rotas.add(rota);
        }
        saveKml(rotas, missao);
        saveLitchi(rotas, missao);
    }

    private static void addRowLitchCsv(PrintStream out, double lat, double lng, double alt, double heading, double speed, double photho_interval) {
        out.print(lat + "," + lng + "," + alt + "," + heading + "," + "0, 0, 0, -1");
        //altitudemode,speed(m/s),poi_latitude,poi_longitude,poi_altitude(m),poi_altitudemode,photo_timeinterval,photo_distinterval
        //0,0,0,0,0,0,1.9,0
        out.print(", 0, "+speed+", 0, 0, 0, 0, "+photho_interval+", 0");
        out.println();
    }

    public static void saveLitchi(LinkedList<Rota> rotas, Missao missao) throws FileNotFoundException {
        int contador = 1;
        final double headingMission = calcHeadingMission(missao.area.p1, missao.area.p2, missao.area.p3);

        for (Rota rota : rotas) {
            double speed = ((int) (rota.velocityCruiser * 10)) / 10.0;
            double photho_interval = 1.0/rota.picturesPerSecond;
            PrintStream out = new PrintStream("./route" + (contador++) + ".csv");
            out.println("latitude,longitude,altitude(m),heading(deg),curvesize(m),rotationdir,gimbalmode,gimbalpitchangle,"
                    + "actiontype1,actionparam1,actiontype2,actionparam2,actiontype3,actionparam3,actiontype4,actionparam4,actiontype5,actionparam5,"
                    + "actiontype6,actionparam6,actiontype7,actionparam7,actiontype8,actionparam8,actiontype9,actionparam9,actiontype10,actionparam10,"
                    + "actiontype11,actionparam11,actiontype12,actionparam12,actiontype13,actionparam13,actiontype14,actionparam14,actiontype15,actionparam15,"
                    + "altitudemode,speed(m/s),poi_latitude,poi_longitude,poi_altitude(m),poi_altitudemode,photo_timeinterval,photo_distinterval");
            //,altitudemode,speed(m/s),poi_latitude,poi_longitude,poi_altitude(m),poi_altitudemode,photo_timeinterval,photo_distinterval
            for (int i = 0; i < rota.geoRoute.size(); i++) {
                PontoGeografico pontoGeografico = rota.geoRoute.get(i);
                double headingRoute = i < rota.geoRoute.size()-1 ? calcHeadingDroneDirection(rota.route.get(i), rota.route.get(i + 1)) : 0;
                if (i == 0) {
                    //primeiro ponto da rota (home), deve mudar o angulo da camera para -15 graus e direcionar o drone para o proximo ponto da rota 
                    addRowLitchCsv(out, pontoGeografico.latitude, pontoGeografico.longitude, 2, headingRoute, 1.0, 0);
                } else if (i <= missao.area.pre_mission.length+missao.extra_pre) {
                    //todos os pontos do pre mission devem manter o drone direcionado para a rota
                    addRowLitchCsv(out, pontoGeografico.latitude, pontoGeografico.longitude, pontoGeografico.altura, headingRoute, 10.0, 0);
                } else if (i == missao.area.pre_mission.length+missao.extra_pre + 1) {
                    //no primeiro ponto da missão deve-se mudar o angulo da camera e a direção do drone no ideal para as fotos da missao, 
                    addRowLitchCsv(out, pontoGeografico.latitude, pontoGeografico.longitude, pontoGeografico.altura, headingMission, speed, photho_interval);

                } else if (i > missao.area.pre_mission.length+missao.extra_pre + 1 && i < rota.geoRoute.size() - 2 - missao.area.pos_mission.length-missao.extra_pos) {
                    //em todos os pontos seguintes da missão, o drone deve manter-se na drireção ideal de fotos continuando a navegacao
                    addRowLitchCsv(out, pontoGeografico.latitude, pontoGeografico.longitude, pontoGeografico.altura, headingMission, speed, photho_interval);
                } else if (i == rota.geoRoute.size() - 2 - missao.area.pos_mission.length-missao.extra_pos) {
                    //no ultimo ponto da missão o drone deve colocar a camera em -15 graus  e direcionar o drone para a rota de volta 
                    addRowLitchCsv(out, pontoGeografico.latitude, pontoGeografico.longitude, pontoGeografico.altura, headingMission, speed, photho_interval);
                    addRowLitchCsv(out, pontoGeografico.latitude, pontoGeografico.longitude, pontoGeografico.altura+1, headingRoute, 10.0, 0);
                } else if (i < rota.geoRoute.size() - 3) {
                    //em todos os pontos do pos mission o drone deve estar direcionado para a rota
                    addRowLitchCsv(out, pontoGeografico.latitude, pontoGeografico.longitude, pontoGeografico.altura, headingRoute, 10.0, 0);
                } else if (i < rota.geoRoute.size() - 1) {
                    //no ultimo ponto do pos mission o drone deve estar direcionado para a rota e movimentar-se devagar
                    addRowLitchCsv(out, pontoGeografico.latitude, pontoGeografico.longitude, pontoGeografico.altura, headingRoute, 1.0, 0);
                } else if (i == rota.geoRoute.size() - 1) {
                    //quando o drone chegar no home, este der girar e em seguida executar o landing
                    addRowLitchCsv(out, pontoGeografico.latitude, pontoGeografico.longitude, 2, 0, 1.0, 0);
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

    private static void saveKmlPoint(PrintStream out, PontoGeografico pontoGeografico, String nome) {
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

    public static void saveKml(LinkedList<Rota> rotas, Missao missao) throws FileNotFoundException {
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
        saveKmlPoint(out, missao.area.geo.home, "H");
        missao.area.geo.pre_mission.stream().forEach(gp -> saveKmlPoint(out, gp, "pre"));
        saveKmlPoint(out, missao.area.geo.point1, "m1");
        saveKmlPoint(out, missao.area.geo.point2, "m2");
        saveKmlPoint(out, missao.area.geo.point3, "m3");
        saveKmlPoint(out, missao.area.geo.point4, "m4");
        missao.area.geo.pos_mission.stream().forEach(gp -> saveKmlPoint(out, gp, "pos"));

        out.println("</Folder>");
        out.println("</Document>");
        out.println("</kml>");
        out.close();
    }

    public static LinkedList<PontoGeografico> transformGeoPoint(LinkedList<PontoCartesiano> route, PontoGeografico home) {
        LinkedList<PontoGeografico> rotaFinal = new LinkedList<>();
        for (PontoCartesiano elemento : route) {
            rotaFinal.add(toGeoPoint(elemento, home));
        }
        return rotaFinal;
    }

    public static double calcHeadingMission(PontoCartesiano c1, PontoCartesiano c2, PontoCartesiano c3) {
        PontoCartesiano va = c1.minus(c2);
        PontoCartesiano vb = c3.minus(c2);

        double dz = Math.max(Math.abs(va.z)/va.norm(),Math.abs(vb.z)/vb.norm());
        double plane_angle = Math.asin(dz)*180/Math.PI;
        System.out.println("dz = " + dz);
        System.out.println("angle = " + plane_angle);

        double heading;
        if(plane_angle>5){
            PontoCartesiano pv = va.vectorialProd(vb);
            if(pv.z>0){
                heading = 180+Math.atan2(pv.x, pv.y) * 180 / Math.PI;
            }else{
                heading = Math.atan2(pv.x, pv.y) * 180 / Math.PI;
            }
            //System.out.println("z = "+pv.z + ",   heading = "+heading);

            //double heading = 360 - Math.atan2((c2.y - c1.y), (c2.x - c1.x)) * 180 / Math.PI;
            //double heading = Math.atan2((c2.y - c1.y), (c2.x - c1.x)) * 180 / Math.PI;
        }else{
            double atam = Math.atan2(vb.y, vb.x)* 180 / Math.PI;
            System.out.println("atam = "+atam);
            heading = 270 - atam;
            heading = (heading < 0 ? heading + 360 : heading);
        }
        heading = (heading > 360 ? heading % 360 : heading);

        return heading;
    }

    public static double calcHeadingDroneDirection(PontoCartesiano c1, PontoCartesiano c2) {
        double heading = 360 - (-90 + Math.atan2((c2.y - c1.y), (c2.x - c1.x)) * 180 / Math.PI);
        heading = (heading > 360 ? heading % 360 : heading);
        return heading;
    }

    public static double calcComprimentoRota(LinkedList<PontoCartesiano> route) {
        double comprimentoTotal = 0;
        PontoCartesiano corrente = route.getFirst();
        for (PontoCartesiano elemento : route) {
            comprimentoTotal += elemento.minus(corrente).norm();
            corrente = elemento;
        }
        return comprimentoTotal;
    }

    public static LinkedList<PontoCartesiano> buildSubRoute(LinkedList<PontoCartesiano> route, int beguinIndex, int endIndex) {
        LinkedList<PontoCartesiano> resultado = new LinkedList<>();

        for (int i = beguinIndex; i <= endIndex; i++) {
            resultado.add(route.get(i));
        }
        return resultado;
    }

    public static LinkedList<Integer> splitIndex(LinkedList<PontoCartesiano> route, int numeroMissoes) {
        int size = (int) Math.floor(route.size() / numeroMissoes);
        LinkedList<Integer> resultado = new LinkedList<>();

        for (int m = 1; m < numeroMissoes; m++) {
            resultado.add(size * m);
        }
        return resultado;
    }

    public static PontoCartesiano toCartesian(PontoGeografico geoPoint, PontoGeografico home) {
        PontoCartesiano cartesianPointResultado = new PontoCartesiano(calcX(geoPoint.longitude, home.longitude, home.latitude),
                calcY(geoPoint.latitude, home.latitude), geoPoint.altura);
        return cartesianPointResultado;
    }

    public static PontoGeografico toGeoPoint(PontoCartesiano cartesianPoint, PontoGeografico home) {
        PontoGeografico geoPoint = new PontoGeografico(
                calcLongitudeX(home.latitude, home.longitude, cartesianPoint.x),
                calcLatitudeY(home.latitude, cartesianPoint.y),
                cartesianPoint.z
        );
        return geoPoint;
    }

    private static double calcY(double latitude, double latitudeBarra) {
        double resultado = (latitude - latitudeBarra) * (10000000.0 / 90);
        return resultado;
    }

    private static double calcX(double longitude, double longitudeBarra, double latitudeBarra) {
        double resultado = (longitude - longitudeBarra) * (6400000.0 * (Math.cos(latitudeBarra * PI / 180) * 2 * PI / 360));
        return resultado;
    }

    private static double calcLatitudeY(double latitudeBarra, double y) {
        double resultado = ((y * 90) / 10000000.0) + latitudeBarra;
        return resultado;
    }

    private static double calcLongitudeX(double latitudeBarra, double longitudeBarra, double x) {
        double resultado = ((x * 90) / (10008000 * Math.cos(latitudeBarra * PI / 180))) + longitudeBarra;
        return resultado;
    }

    public static double photoLengthOnGround(double distanciaFoto, double aberturaCamera) {
        double resultado = 2 * distanciaFoto * Math.tan((aberturaCamera / 2) * PI / 180);
        return resultado;
    }

    public static double calcDenEscala(Missao missao) {
        double resultado = missao.distancePicture / (missao.cam.distanceFocus * Math.pow(10, -3));
        return resultado;
    }

    public static double calcSensorX(Missao missao) {
        double resultado = missao.cam.sensorX / missao.cam.resolutionW;
        return resultado;
    }

    public static double calcSensorY(Missao missao) {
        double resultado = missao.cam.sensorY / missao.cam.resolutionH;
        return resultado;
    }

    public static double calcTam(Missao missao) {
        double numberOne = calcSensorX(missao);
        double numberTwo = calcSensorY(missao);
        double resultado = Math.min(numberOne, numberTwo);
        return resultado;
    }

    public static double calcGSD(Missao missao) {
        double denEscala = calcDenEscala(missao);
        double tam = calcTam(missao);
        //System.out.println(denEscala);  ?????????????????
        double resultado = denEscala * tam;
        return resultado;
    }

}
