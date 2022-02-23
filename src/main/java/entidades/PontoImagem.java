//package entidades;
//
//public class PontoImagem {
//
//    public final PontoGeografico ponto;
//    public final long time_stamp;
//    public final String nomeArquivo;
//    public final String mission_tag;
//
//    public PontoImagem(PontoGeografico point, long time_stamp, String file_name) {
//        this.ponto = point;
//        this.time_stamp = time_stamp;
//        this.nomeArquivo = file_name;
//        this.mission_tag = file_name.substring(file_name.indexOf("_") + 1, file_name.indexOf("."));
//    }
//
//
//    public int compareTo(PontoImagem o) {
//        return Long.compare(this.time_stamp, o.time_stamp);
//    }
//
//    @Override
//    public String toString() {
//        return "ImagePoint{" + "point=" + ponto + ", time_stamp=" + time_stamp + ", file_name=" + nomeArquivo + ", mission_tag=" + mission_tag + '}';
//    }
//}
