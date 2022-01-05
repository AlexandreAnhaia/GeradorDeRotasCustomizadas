package entidades;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class PontoCSV {

    private ArrayList<PontoGeografico> points = new ArrayList<>();

    public PontoCSV() {
    }

    private final int LATITUDE = 0;
    private final int LONGITUDE = 1;
    private final int ALTITUDE = 2;
    private final int DATETIMELOCAL = 12;
    private final int ISTAKINGFOTO = 27;

    public void loader(String arquivoCSV) {


        //String arquivoCSV = "./2019-10-29_11-09-04_v2.csv";
        //arquivoCSV = "./2019-10-30_10-20-06_v2.csv";


        BufferedReader bufferedReader;
        String linha;
        int contador = 0;
        try {
            bufferedReader = new BufferedReader(new FileReader(arquivoCSV));
            ArrayList<String> array = new ArrayList<>();
            bufferedReader.readLine();

            int anterior = 0;
            System.out.println("LATITUDE,       LONGITUDE,  ALTITUDE,   DATETIMELOCAL,          ISTAKINGFOTO");
            while ((linha = bufferedReader.readLine()) != null) {
                linha = linha + " ,";

                String[] colunas = linha.contains(";")? linha.split(";") : linha.split(",");


                if (anterior == 0 && Integer.parseInt(colunas[ISTAKINGFOTO]) == 1) {
                    PontoGeografico geoPoint = new PontoGeografico(Double.valueOf(colunas[LONGITUDE]), Double.valueOf(colunas[LATITUDE]), Double.valueOf(colunas[ALTITUDE]));
                    points.add(geoPoint);
                    contador++;
                }
                anterior = Integer.parseInt(colunas[ISTAKINGFOTO]);
            }
            System.out.println("==================================================================================");
            System.out.println("LONGITUDE,              LATITUDE,       ALTITUDE");
            for (PontoGeografico point : points) {
                System.out.println(point.toString());
            }
            System.out.println(contador);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public ArrayList<PontoGeografico> getPoints() {
        return points;
    }
}
