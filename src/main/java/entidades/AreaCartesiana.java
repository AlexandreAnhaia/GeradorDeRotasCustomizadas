package entidades;

import static Controladores.Controlador.toCartesian;

public class AreaCartesiana {

    public final AreaGeografica geo;
    public final PontoCartesiano h;
    public final PontoCartesiano[] pre_mission;
    public final PontoCartesiano p1;
    public final PontoCartesiano p2;
    public final PontoCartesiano p3;
    public final PontoCartesiano p4;
    public final PontoCartesiano[] pos_mission;

    public final double hipotenusa;
    public final double comprimentoBase;

    public AreaCartesiana(AreaGeografica area) {
        this.geo = area;
        this.h = toCartesian(area.home, area.home);
        this.p1 = toCartesian(area.point1, area.home);
        this.p2 = toCartesian(area.point2, area.home);
        this.p3 = toCartesian(area.point3, area.home);
        this.p4 = toCartesian(area.point4, area.home);
        this.pre_mission = area.pre_mission.stream().map(gp -> toCartesian(gp, area.home)).toArray(PontoCartesiano[]::new);
        this.pos_mission = area.pos_mission.stream().map(gp -> toCartesian(gp, area.home)).toArray(PontoCartesiano[]::new);
        this.hipotenusa = calcHipotenusa(p1, p2, p3, p4);
        this.comprimentoBase = calcComprimentoBase(p1, p2, p3, p4);

    }

    private static double calcHipotenusa(PontoCartesiano p1, PontoCartesiano p2, PontoCartesiano p3, PontoCartesiano p4) {
        PontoCartesiano h1 = p4.minus(p1);
        PontoCartesiano h2 = p3.minus(p2);
        return Math.max(h1.norm(), h2.norm());
    }

    private double calcComprimentoBase(PontoCartesiano p1, PontoCartesiano p2, PontoCartesiano p3, PontoCartesiano p4) {
        PontoCartesiano h1 = p4.minus(p3);
        PontoCartesiano h2 = p1.minus(p2);
        return Math.max(h1.norm(), h2.norm());
    }
}
