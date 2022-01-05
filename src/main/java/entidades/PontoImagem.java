package entidades;

public class PontoImagem {

    public final PontoGeografico point;
    public final long time_stamp;
    public final String file_name;
    public final String mission_tag;

    public PontoImagem(PontoGeografico point, long time_stamp, String file_name) {
        this.point = point;
        this.time_stamp = time_stamp;
        this.file_name = file_name;
        this.mission_tag = file_name.substring(file_name.indexOf("_") + 1, file_name.indexOf("."));
    }


    public int compareTo(PontoImagem o) {
        return Long.compare(this.time_stamp, o.time_stamp);
    }

    @Override
    public String toString() {
        return "ImagePoint{" + "point=" + point + ", time_stamp=" + time_stamp + ", file_name=" + file_name + ", mission_tag=" + mission_tag + '}';
    }
}
