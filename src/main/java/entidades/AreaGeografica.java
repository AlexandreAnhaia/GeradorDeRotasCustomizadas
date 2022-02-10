package entidades;

import java.util.LinkedList;

public class AreaGeografica {

    public final PontoGeografico pontoInicial;
    public final LinkedList<PontoGeografico> preMissao;
    public final PontoGeografico ponto1;
    public final PontoGeografico ponto2;
    public final PontoGeografico ponto3;
    public final PontoGeografico ponto4;
    public final LinkedList<PontoGeografico> posMissao;

    public AreaGeografica(PontoGeografico home, LinkedList<PontoGeografico> pre_mission, PontoGeografico point1, PontoGeografico point2, PontoGeografico point3, PontoGeografico point4, LinkedList<PontoGeografico> pos_mission) {
        this.pontoInicial = home;
        this.preMissao = pre_mission;
        this.ponto1 = point1;
        this.ponto2 = point2;
        this.ponto3 = point3;
        this.ponto4 = point4;
        this.posMissao = pos_mission;
    }

//    public static LinkedList<PontoGeografico> criaPontosDePreparacao(PontoGeografico... points) {
//        return new LinkedList<>(Arrays.asList(points));
//    }
//
//    private static double toAngle(String angle) {
//        //System.out.println(angle);
//        String v[] = angle.split(",");
//        return Double.parseDouble(v[0]) + Double.parseDouble(v[1]) / 60 + Double.parseDouble(v[2]) / (3600 * 10000);
//    }

//    private static PontoImagem leMetaDataImagem(File image) throws ParseException {
//        ImageMetadata metadata = null;
//        try {
//            metadata = Imaging.getMetadata(image);
//            Thread.sleep(500);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//        final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
//        String str = jpegMetadata.toString();
//        //System.out.println(str);
//
//        String data = str.substring(str.indexOf("DateTime: '") + "DateTime: '".length(), str.indexOf("YCbCrPositioning:"));
//        data = data.substring(0, data.indexOf("'"));
//
//        DateFormat format = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
//        long time_stamp = format.parse(data).getTime();
//
//        //System.out.println("("+date+")");
//        String gps = str.substring(str.indexOf("Gps:"), str.indexOf("Sub:"));
//        System.out.println(gps);
//        String latitudeRef = gps.substring(gps.indexOf("GPSLatitudeRef: '") + "GPSLatitudeRef: '".length(), gps.indexOf("GPSLatitude:"));
//        String longitudeRef = gps.substring(gps.indexOf("GPSLongitudeRef: '") + "GPSLongitudeRef: '".length(), gps.indexOf("GPSLongitude:"));
//        latitudeRef = latitudeRef.substring(0, latitudeRef.indexOf("'"));
//        longitudeRef = longitudeRef.substring(0, longitudeRef.indexOf("'"));
//
////        System.out.println("("+latRef+")");
////        System.out.println("("+lngRef+")");
//        String latitude = gps.substring(gps.indexOf("GPSLatitude:"), gps.indexOf("GPSLongitudeRef:"));
//        String longitude = gps.substring(gps.indexOf("GPSLongitude:"), gps.indexOf("GPSAltitudeRef:"));
//        String altitude = gps.substring(gps.indexOf("GPSAltitude:"));
//        latitude = latitude.substring(13, latitude.indexOf("/"));
//        longitude = longitude.substring(14, longitude.indexOf("/"));
//        altitude = altitude.substring(13, altitude.indexOf("/"));
//
//        double lat_val = longitudeRef.equals("S") ? -toAngle(latitude) : +toAngle(latitude);
//        double lng_val = longitudeRef.equals("W") ? -toAngle(longitude) : +toAngle(longitude);
//        double alt_val = Double.parseDouble(altitude) / 1000.0;
//
////            System.out.println(f.getName());
////            System.out.println("lat:" + lat_val);
////            System.out.println("lng:" + lng_val);
////            System.out.println("alt:" + Double.parseDouble(alt)/1000.0);
//        System.out.printf("%20s %20s %15.10f %15.10f %15.10f %15d\n", image.getName(), data, lat_val, lng_val, alt_val, time_stamp);
//        PontoGeografico point = new PontoGeografico(lng_val, lat_val, alt_val);
//        return new PontoImagem(point, time_stamp, image.getName());
//    }

//    public static AreaGeografica buildFromManualWaypoints(File directory) throws ParseException {
//        LinkedList<PontoGeografico> pontosIniciais = new LinkedList<>();
//        LinkedList<PontoGeografico> pontosPreMissao = new LinkedList<>();
//        LinkedList<PontoGeografico> missao = new LinkedList<>();
//        LinkedList<PontoGeografico> pontosPosMissao = new LinkedList<>();
//
//        TreeSet<PontoImagem> tree = new TreeSet<>();
//        for (File image : directory.listFiles()) {
//            tree.add(leMetaDataImagem(image));
//        }
//        PontoImagem ipHome = tree.first();
//        for (PontoImagem ip : tree) {
//            //System.out.println(ip);
//            PontoGeografico geo = new PontoGeografico(ip.ponto.longitude, ip.ponto.latitude, ip.ponto.altura - ipHome.ponto.altura);
//            switch (ip.mission_tag) {
//                case "home":
//                    pontosIniciais.addLast(geo);
//                    break;
//                case "pre":
//                    pontosPreMissao.addLast(geo);
//                    break;
//                case "mission":
//                    missao.addLast(geo);
//                    break;
//                case "pos":
//                    pontosPosMissao.addLast(geo);
//                    break;
//                default:
//                    throw new IllegalStateException("tag = " + ip.mission_tag + " is not valid, check file name in " + ip.nomeArquivo + " is wrong");
//            }
//        }
//
//        if (pontosIniciais.size() != 1) {
//            throw new IllegalStateException("home list size = " + pontosIniciais.size() + " is not valid, check file names in directory " + directory.getAbsolutePath());
//        }
//        if (missao.size() != 4) {
//            throw new IllegalStateException("mission list size = " + missao.size() + " is not valid, check file names in directory " + directory.getAbsolutePath());
//        }
//        System.out.println("home: " + pontosIniciais);
//        System.out.println("pre: " + pontosPreMissao);
//        System.out.println("mission: " + missao);
//        System.out.println("pos: " + pontosPosMissao);
//
//        return new AreaGeografica(pontosIniciais.getFirst(),
//                criaPontosDePreparacao(pontosPreMissao.stream().toArray(PontoGeografico[]::new)),
//                missao.get(0), missao.get(1), missao.get(2), missao.get(3),
//                criaPontosDePreparacao(pontosPosMissao.stream().toArray(PontoGeografico[]::new))
//        );
//    }

}
