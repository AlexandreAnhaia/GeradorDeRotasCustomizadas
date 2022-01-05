package entidades;

import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.TreeSet;

public class AreaGeografica {

    public final PontoGeografico home;
    public final LinkedList<PontoGeografico> pre_mission;
    public final PontoGeografico point1;
    public final PontoGeografico point2;
    public final PontoGeografico point3;
    public final PontoGeografico point4;
    public final LinkedList<PontoGeografico> pos_mission;

    public AreaGeografica(PontoGeografico home, LinkedList<PontoGeografico> pre_mission, PontoGeografico point1, PontoGeografico point2, PontoGeografico point3, PontoGeografico point4, LinkedList<PontoGeografico> pos_mission) {
        this.home = home;
        this.pre_mission = pre_mission;
        this.point1 = point1;
        this.point2 = point2;
        this.point3 = point3;
        this.point4 = point4;
        this.pos_mission = pos_mission;
    }

    public static LinkedList<PontoGeografico> buildPreparationPoints(PontoGeografico... points) {
        return new LinkedList<>(Arrays.asList(points));
    }

    private static double toAngle(String angle) {
        //System.out.println(angle);
        String v[] = angle.split(",");
        return Double.parseDouble(v[0]) + Double.parseDouble(v[1]) / 60 + Double.parseDouble(v[2]) / (3600 * 10000);
    }

    private static PontoImagem readMetaDataImage(File image) throws ParseException {
        ImageMetadata metadata = null;
        try {
            metadata = Imaging.getMetadata(image);
            Thread.sleep(500);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
        String str = jpegMetadata.toString();
        //System.out.println(str);

        String date = str.substring(str.indexOf("DateTime: '") + "DateTime: '".length(), str.indexOf("YCbCrPositioning:"));
        date = date.substring(0, date.indexOf("'"));

        DateFormat format = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
        long time_stamp = format.parse(date).getTime();

        //System.out.println("("+date+")");
        String gps = str.substring(str.indexOf("Gps:"), str.indexOf("Sub:"));
        System.out.println(gps);
        String latRef = gps.substring(gps.indexOf("GPSLatitudeRef: '") + "GPSLatitudeRef: '".length(), gps.indexOf("GPSLatitude:"));
        String lngRef = gps.substring(gps.indexOf("GPSLongitudeRef: '") + "GPSLongitudeRef: '".length(), gps.indexOf("GPSLongitude:"));
        latRef = latRef.substring(0, latRef.indexOf("'"));
        lngRef = lngRef.substring(0, lngRef.indexOf("'"));

//        System.out.println("("+latRef+")");
//        System.out.println("("+lngRef+")");
        String lat = gps.substring(gps.indexOf("GPSLatitude:"), gps.indexOf("GPSLongitudeRef:"));
        String lng = gps.substring(gps.indexOf("GPSLongitude:"), gps.indexOf("GPSAltitudeRef:"));
        String alt = gps.substring(gps.indexOf("GPSAltitude:"));
        lat = lat.substring(13, lat.indexOf("/"));
        lng = lng.substring(14, lng.indexOf("/"));
        alt = alt.substring(13, alt.indexOf("/"));

        double lat_val = latRef.equals("S") ? -toAngle(lat) : +toAngle(lat);
        double lng_val = lngRef.equals("W") ? -toAngle(lng) : +toAngle(lng);
        double alt_val = Double.parseDouble(alt) / 1000.0;

//            System.out.println(f.getName());
//            System.out.println("lat:" + lat_val);
//            System.out.println("lng:" + lng_val);
//            System.out.println("alt:" + Double.parseDouble(alt)/1000.0);
        System.out.printf("%20s %20s %15.10f %15.10f %15.10f %15d\n", image.getName(), date, lat_val, lng_val, alt_val, time_stamp);
        PontoGeografico point = new PontoGeografico(lng_val, lat_val, alt_val);
        return new PontoImagem(point, time_stamp, image.getName());
    }

    public static AreaGeografica buildFromManualWaypoints(File directory) throws ParseException {
        LinkedList<PontoGeografico> home = new LinkedList<>();
        LinkedList<PontoGeografico> pre = new LinkedList<>();
        LinkedList<PontoGeografico> mission = new LinkedList<>();
        LinkedList<PontoGeografico> pos = new LinkedList<>();

        TreeSet<PontoImagem> tree = new TreeSet<>();
        for (File image : directory.listFiles()) {
            tree.add(readMetaDataImage(image));
        }
        PontoImagem ipHome = tree.first();
        for (PontoImagem ip : tree) {
            //System.out.println(ip);
            PontoGeografico geo = new PontoGeografico(ip.point.longitude, ip.point.latitude, ip.point.altura - ipHome.point.altura);
            switch (ip.mission_tag) {
                case "home":
                    home.addLast(geo);
                    break;
                case "pre":
                    pre.addLast(geo);
                    break;
                case "mission":
                    mission.addLast(geo);
                    break;
                case "pos":
                    pos.addLast(geo);
                    break;
                default:
                    throw new IllegalStateException("tag = " + ip.mission_tag + " is not valid, check file name in " + ip.file_name + " is wrong");
            }
        }

        if (home.size() != 1) {
            throw new IllegalStateException("home list size = " + home.size() + " is not valid, check file names in directory " + directory.getAbsolutePath());
        }
        if (mission.size() != 4) {
            throw new IllegalStateException("mission list size = " + mission.size() + " is not valid, check file names in directory " + directory.getAbsolutePath());
        }
        System.out.println("home: " + home);
        System.out.println("pre: " + pre);
        System.out.println("mission: " + mission);
        System.out.println("pos: " + pos);

        return new AreaGeografica(home.getFirst(),
                buildPreparationPoints(pre.stream().toArray(PontoGeografico[]::new)),
                mission.get(0), mission.get(1), mission.get(2), mission.get(3),
                buildPreparationPoints(pos.stream().toArray(PontoGeografico[]::new))
        );
    }

}
