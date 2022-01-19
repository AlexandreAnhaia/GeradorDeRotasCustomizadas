import Controladores.Controlador;
import entidades.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;

import static entidades.Missao.FORWARD_MOVIMENT;
import static entidades.Missao.HORIZONTAL_DIRECTION;

public class Main {

    public static ArrayList<PontoGeografico> points = new ArrayList<>();

    public static void main(String[] args) throws IOException, ParseException {
            Application application = new Application();
            application.criaMissao();
    }

}
